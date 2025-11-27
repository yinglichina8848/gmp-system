package com.gmp.qms.service.async;

import com.gmp.qms.config.McpPerformanceConfig;
import com.gmp.qms.monitoring.McpMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * MCP异步执行服务，支持批量调用和并行处理，提高性能
 * 
 * @author GMP系统开发团队
 */
@Service
public class McpAsyncExecutionService {
    
    @Autowired
    private McpPerformanceConfig performanceConfig;
    
    @Autowired
    private McpMonitoringService monitoringService;
    
    @Autowired
    private Executor mcpTaskExecutor;
    
    /**
     * 异步执行单个MCP调用
     */
    @Async("mcpTaskExecutor")
    public <T> CompletableFuture<T> executeAsync(Supplier<T> task, String operationName) {
        McpMonitoringService.McpTimer timer = monitoringService.startTimer(operationName);
        try {
            T result = task.get();
            timer.complete(true);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            timer.complete(false);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * 批量异步执行MCP调用
     */
    public <T, R> List<R> executeBatchAsync(List<T> items, Function<T, R> processor, String operationName) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        
        int batchSize = performanceConfig.getBatch().getBatchSize();
        int maxConcurrentTasks = performanceConfig.getThreadPool().getMaxThreads();
        
        // 计算批次数
        int totalBatches = (int) Math.ceil((double) items.size() / batchSize);
        List<CompletableFuture<List<R>>> batchFutures = new ArrayList<>();
        
        // 创建线程池控制并发
        ExecutorService executorService = new ThreadPoolExecutor(
            Math.min(maxConcurrentTasks, totalBatches),
            Math.min(maxConcurrentTasks, totalBatches),
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(totalBatches)
        );
        
        try {
            // 分批处理
            for (int i = 0; i < totalBatches; i++) {
                final int batchIndex = i;
                CompletableFuture<List<R>> batchFuture = CompletableFuture.supplyAsync(() -> {
                    int start = batchIndex * batchSize;
                    int end = Math.min(start + batchSize, items.size());
                    List<T> batchItems = items.subList(start, end);
                    
                    return batchItems.stream()
                        .map(item -> {
                            McpMonitoringService.McpTimer timer = monitoringService.startTimer(operationName);
                            try {
                                R result = processor.apply(item);
                                timer.complete(true);
                                return result;
                            } catch (Exception e) {
                                timer.complete(false);
                                // 记录异常但继续处理其他项目
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                }, executorService);
                
                batchFutures.add(batchFuture);
            }
            
            // 等待所有批次完成
            CompletableFuture<Void> allOf = CompletableFuture.allOf(
                batchFutures.toArray(new CompletableFuture[0])
            );
            
            // 合并所有结果
            return allOf.thenApply(v -> 
                batchFutures.stream()
                    .flatMap(future -> future.join().stream())
                    .collect(Collectors.toList())
            ).join();
        } finally {
            executorService.shutdown();
        }
    }
    
    /**
     * 并行执行多个不同类型的MCP调用
     */
    public <T> Map<String, T> executeParallelAsync(Map<String, Supplier<T>> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyMap();
        }
        
        Map<String, CompletableFuture<T>> futures = new HashMap<>();
        
        // 为每个任务创建一个CompletableFuture
        for (Map.Entry<String, Supplier<T>> entry : tasks.entrySet()) {
            String taskName = entry.getKey();
            Supplier<T> task = entry.getValue();
            
            CompletableFuture<T> future = executeAsync(task, taskName);
            futures.put(taskName, future);
        }
        
        // 等待所有任务完成并收集结果
        Map<String, T> results = new HashMap<>();
        for (Map.Entry<String, CompletableFuture<T>> entry : futures.entrySet()) {
            try {
                T result = entry.getValue().get();
                results.put(entry.getKey(), result);
            } catch (Exception e) {
                // 记录异常但继续处理其他任务
                results.put(entry.getKey(), null);
            }
        }
        
        return results;
    }
    
    /**
     * 带超时的异步执行
     */
    @Async("mcpTaskExecutor")
    public <T> CompletableFuture<T> executeAsyncWithTimeout(Supplier<T> task, String operationName, long timeoutMs) {
        CompletableFuture<T> resultFuture = new CompletableFuture<>();
        
        // 执行实际任务
        CompletableFuture.runAsync(() -> {
            McpMonitoringService.McpTimer timer = monitoringService.startTimer(operationName);
            try {
                T result = task.get();
                timer.complete(true);
                resultFuture.complete(result);
            } catch (Exception e) {
                timer.complete(false);
                resultFuture.completeExceptionally(e);
            }
        }, mcpTaskExecutor);
        
        // 设置超时
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(timeoutMs);
                if (!resultFuture.isDone()) {
                    resultFuture.completeExceptionally(new TimeoutException("MCP call timed out after " + timeoutMs + "ms"));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, mcpTaskExecutor);
        
        return resultFuture;
    }
    
    /**
     * 重试机制的异步执行
     */
    @Async("mcpTaskExecutor")
    public <T> CompletableFuture<T> executeAsyncWithRetry(Supplier<T> task, String operationName, int maxRetries, long backoffMs) {
        return CompletableFuture.supplyAsync(() -> {
            int attempts = 0;
            Exception lastException = null;
            
            while (attempts <= maxRetries) {
                attempts++;
                McpMonitoringService.McpTimer timer = monitoringService.startTimer(operationName + "_attempt_" + attempts);
                
                try {
                    T result = task.get();
                    timer.complete(true);
                    return result;
                } catch (Exception e) {
                    timer.complete(false);
                    lastException = e;
                    
                    // 如果不是最后一次尝试，进行退避等待
                    if (attempts < maxRetries) {
                        try {
                            long waitTime = backoffMs * (long) Math.pow(2, attempts - 1); // 指数退避
                            Thread.sleep(waitTime);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException("Retry interrupted", ie);
                        }
                    }
                }
            }
            
            throw new RuntimeException("Failed after " + maxRetries + " retries", lastException);
        }, mcpTaskExecutor);
    }
}
