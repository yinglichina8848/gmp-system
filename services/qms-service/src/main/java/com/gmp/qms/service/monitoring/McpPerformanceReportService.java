package com.gmp.qms.service.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP性能报告服务，负责生成和管理MCP通信的性能报告
 */
@Slf4j
@Service
public class McpPerformanceReportService {

    /**
     * 获取最新的性能报告
     * @return 性能报告
     */
    public PerformanceReport getLatestPerformanceReport() {
        PerformanceReport report = new PerformanceReport();
        report.setTimestamp(new Date().toString());
        report.setPeriod("Last 5 minutes");
        report.setTotalCalls(1000);
        report.setSuccessRate(99.5);
        report.setAverageResponseTime(120);
        report.setP95ResponseTime(200);
        report.setP99ResponseTime(350);
        report.setErrorRate(0.5);
        report.setSystemDistribution(getSystemDistribution());
        return report;
    }

    /**
     * 按分钟获取性能趋势
     * @param minutes 分钟数
     * @return 性能趋势数据
     */
    public List<PerformanceTrendPoint> getPerformanceTrendsByMinute(int minutes) {
        List<PerformanceTrendPoint> trends = new ArrayList<>();
        // 生成模拟数据
        for (int i = minutes - 1; i >= 0; i--) {
            PerformanceTrendPoint point = new PerformanceTrendPoint();
            point.setTimestamp(getTimeAgo(i, "minute"));
            point.setCalls(100 + (int)(Math.random() * 50));
            point.setAverageResponseTime(100 + (int)(Math.random() * 50));
            point.setSuccessRate(99.0 + Math.random() * 0.9);
            trends.add(point);
        }
        return trends;
    }

    /**
     * 按小时获取性能趋势
     * @param hours 小时数
     * @return 性能趋势数据
     */
    public List<PerformanceTrendPoint> getPerformanceTrendsByHour(int hours) {
        List<PerformanceTrendPoint> trends = new ArrayList<>();
        // 生成模拟数据
        for (int i = hours - 1; i >= 0; i--) {
            PerformanceTrendPoint point = new PerformanceTrendPoint();
            point.setTimestamp(getTimeAgo(i, "hour"));
            point.setCalls(6000 + (int)(Math.random() * 3000));
            point.setAverageResponseTime(110 + (int)(Math.random() * 40));
            point.setSuccessRate(98.5 + Math.random() * 1.4);
            trends.add(point);
        }
        return trends;
    }

    /**
     * 按天获取性能趋势
     * @param days 天数
     * @return 性能趋势数据
     */
    public List<PerformanceTrendPoint> getPerformanceTrendsByDay(int days) {
        List<PerformanceTrendPoint> trends = new ArrayList<>();
        // 生成模拟数据
        for (int i = days - 1; i >= 0; i--) {
            PerformanceTrendPoint point = new PerformanceTrendPoint();
            point.setTimestamp(getTimeAgo(i, "day"));
            point.setCalls(144000 + (int)(Math.random() * 20000));
            point.setAverageResponseTime(105 + (int)(Math.random() * 35));
            point.setSuccessRate(99.0 + Math.random() * 0.9);
            trends.add(point);
        }
        return trends;
    }

    /**
     * 按周获取性能趋势
     * @param weeks 周数
     * @return 性能趋势数据
     */
    public List<PerformanceTrendPoint> getPerformanceTrendsByWeek(int weeks) {
        List<PerformanceTrendPoint> trends = new ArrayList<>();
        // 生成模拟数据
        for (int i = weeks - 1; i >= 0; i--) {
            PerformanceTrendPoint point = new PerformanceTrendPoint();
            point.setTimestamp(getTimeAgo(i, "week"));
            point.setCalls(1008000 + (int)(Math.random() * 100000));
            point.setAverageResponseTime(100 + (int)(Math.random() * 30));
            point.setSuccessRate(99.2 + Math.random() * 0.7);
            trends.add(point);
        }
        return trends;
    }

    // 辅助方法
    private String getTimeAgo(int value, String unit) {
        Calendar calendar = Calendar.getInstance();
        switch (unit) {
            case "minute":
                calendar.add(Calendar.MINUTE, -value);
                break;
            case "hour":
                calendar.add(Calendar.HOUR_OF_DAY, -value);
                break;
            case "day":
                calendar.add(Calendar.DAY_OF_MONTH, -value);
                break;
            case "week":
                calendar.add(Calendar.WEEK_OF_YEAR, -value);
                break;
        }
        return calendar.getTime().toString();
    }

    private Map<String, Long> getSystemDistribution() {
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("EDMS", 350L);
        distribution.put("ERP", 250L);
        distribution.put("LIMS", 200L);
        distribution.put("MES", 150L);
        distribution.put("Other", 50L);
        return distribution;
    }

    // 数据模型类
    public static class PerformanceReport {
        private String timestamp;
        private String period;
        private long totalCalls;
        private double successRate;
        private long averageResponseTime;
        private long p95ResponseTime;
        private long p99ResponseTime;
        private double errorRate;
        private Map<String, Long> systemDistribution;

        // Getters and Setters
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getPeriod() { return period; }
        public void setPeriod(String period) { this.period = period; }
        public long getTotalCalls() { return totalCalls; }
        public void setTotalCalls(long totalCalls) { this.totalCalls = totalCalls; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public long getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(long averageResponseTime) { this.averageResponseTime = averageResponseTime; }
        public long getP95ResponseTime() { return p95ResponseTime; }
        public void setP95ResponseTime(long p95ResponseTime) { this.p95ResponseTime = p95ResponseTime; }
        public long getP99ResponseTime() { return p99ResponseTime; }
        public void setP99ResponseTime(long p99ResponseTime) { this.p99ResponseTime = p99ResponseTime; }
        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
        public Map<String, Long> getSystemDistribution() { return systemDistribution; }
        public void setSystemDistribution(Map<String, Long> systemDistribution) { this.systemDistribution = systemDistribution; }
    }

    public static class PerformanceTrendPoint {
        private String timestamp;
        private long calls;
        private long averageResponseTime;
        private double successRate;

        // Getters and Setters
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public long getCalls() { return calls; }
        public void setCalls(long calls) { this.calls = calls; }
        public long getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(long averageResponseTime) { this.averageResponseTime = averageResponseTime; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
    }
}