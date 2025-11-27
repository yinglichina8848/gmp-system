/**
 * MCP监控控制台 JavaScript
 * 负责MCP监控控制台的动态交互和数据处理
 */

// MCP仪表盘主类
class McpDashboard {
    constructor() {
        // 应用状态
        this.state = {
            timeRange: '1h',         // 当前时间范围
            isLoading: false,        // 加载状态
            autoRefresh: false,      // 是否自动刷新
            refreshInterval: null,   // 自动刷新定时器
            refreshRate: 30000,      // 刷新频率(毫秒)
            selectedSystem: null,    // 选中的系统
            expandedError: null      // 展开的错误详情
        };
        
        // 图表实例
        this.charts = {};
        
        // 初始化
        this.init();
    }
    
    /**
     * 初始化仪表盘
     */
    init() {
        // 初始化事件监听
        this.initEventListeners();
        
        // 初始化图表
        this.initCharts();
        
        // 加载初始数据
        this.loadDashboardData();
    }
    
    /**
     * 初始化事件监听
     */
    initEventListeners() {
        // 侧边栏切换
        document.getElementById('toggle-sidebar').addEventListener('click', () => {
            this.toggleSidebar();
        });
        
        // 刷新按钮
        document.getElementById('refresh-data').addEventListener('click', () => {
            this.loadDashboardData();
        });
        
        // 时间范围选择器
        document.querySelectorAll('.time-range').forEach(button => {
            button.addEventListener('click', (e) => {
                this.setTimeRange(e.target.dataset.range);
            });
        });
        
        // 导航链接
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const targetId = link.getAttribute('href').substring(1);
                this.navigateToSection(targetId);
            });
        });
        
        // 系统状态表格行点击
        document.getElementById('system-status-table').addEventListener('click', (e) => {
            const row = e.target.closest('tr');
            if (row) {
                const systemName = row.cells[0].textContent;
                this.selectSystem(systemName);
            }
        });
        
        // 错误详情按钮点击
        document.addEventListener('click', (e) => {
            if (e.target.classList.contains('view-error-details')) {
                const callId = e.target.dataset.callId;
                this.viewErrorDetails(callId);
            }
        });
    }
    
    /**
     * 初始化图表
     */
    initCharts() {
        // 响应时间分布图
        this.initResponseTimeChart();
        
        // 性能趋势图
        this.initPerformanceTrendChart();
        
        // 系统调用分布图
        this.initSystemCallsChart();
    }
    
    /**
     * 初始化响应时间分布图
     */
    initResponseTimeChart() {
        const ctx = document.getElementById('response-time-chart')?.getContext('2d');
        if (!ctx) return;
        
        this.charts.responseTime = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['< 100ms', '100-500ms', '500-1000ms', '1000-2000ms', '> 2000ms'],
                datasets: [{
                    data: [0, 0, 0, 0, 0],
                    backgroundColor: [
                        '#28a745',  // 绿色
                        '#20c997',  // 浅绿色
                        '#ffc107',  // 黄色
                        '#fd7e14',  // 橙色
                        '#dc3545'   // 红色
                    ],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            boxWidth: 12,
                            padding: 15
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const label = context.label || '';
                                const value = context.raw || 0;
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = total > 0 ? Math.round((value / total) * 100) : 0;
                                return `${label}: ${percentage}%`;
                            }
                        }
                    }
                },
                cutout: '60%',
                animation: {
                    animateRotate: true,
                    animateScale: true
                }
            }
        });
    }
    
    /**
     * 初始化性能趋势图
     */
    initPerformanceTrendChart() {
        const ctx = document.getElementById('performance-trend-chart')?.getContext('2d');
        if (!ctx) return;
        
        this.charts.performanceTrend = new Chart(ctx, {
            type: 'line',
            data: {
                labels: [],
                datasets: [
                    {
                        label: '平均响应时间 (ms)',
                        data: [],
                        borderColor: '#3498db',
                        backgroundColor: 'rgba(52, 152, 219, 0.1)',
                        tension: 0.3,
                        fill: true,
                        yAxisID: 'y'
                    },
                    {
                        label: '调用次数',
                        data: [],
                        borderColor: '#28a745',
                        backgroundColor: 'transparent',
                        tension: 0.3,
                        yAxisID: 'y1'
                    },
                    {
                        label: '错误率 (%)',
                        data: [],
                        borderColor: '#e74c3c',
                        backgroundColor: 'transparent',
                        tension: 0.3,
                        yAxisID: 'y2'
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                interaction: {
                    mode: 'index',
                    intersect: false,
                },
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    tooltip: {
                        mode: 'index',
                        intersect: false,
                        callbacks: {
                            label: function(context) {
                                let label = context.dataset.label || '';
                                if (label) {
                                    label += ': ';
                                }
                                if (context.parsed.y !== null) {
                                    if (context.datasetIndex === 0) {
                                        label += context.parsed.y + ' ms';
                                    } else if (context.datasetIndex === 1) {
                                        label += context.parsed.y;
                                    } else if (context.datasetIndex === 2) {
                                        label += context.parsed.y.toFixed(2) + '%';
                                    }
                                }
                                return label;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        }
                    },
                    y: {
                        type: 'linear',
                        display: true,
                        position: 'left',
                        title: {
                            display: true,
                            text: '响应时间 (ms)'
                        },
                        min: 0,
                        grid: {
                            color: 'rgba(0, 0, 0, 0.05)'
                        }
                    },
                    y1: {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        title: {
                            display: true,
                            text: '调用次数'
                        },
                        min: 0,
                        grid: {
                            drawOnChartArea: false,
                        },
                    },
                    y2: {
                        type: 'linear',
                        display: false,
                        min: 0,
                        max: 100
                    }
                },
                animation: {
                    duration: 1000
                }
            }
        });
    }
    
    /**
     * 初始化系统调用分布图
     */
    initSystemCallsChart() {
        const ctx = document.getElementById('system-calls-chart')?.getContext('2d');
        if (!ctx) return;
        
        this.charts.systemCalls = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: [],
                datasets: [
                    {
                        label: '成功调用',
                        data: [],
                        backgroundColor: '#28a745'
                    },
                    {
                        label: '失败调用',
                        data: [],
                        backgroundColor: '#dc3545'
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top'
                    },
                    tooltip: {
                        callbacks: {
                            footer: function(tooltipItems) {
                                let total = 0;
                                tooltipItems.forEach(item => {
                                    total += item.raw;
                                });
                                return `总计: ${total}`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        }
                    },
                    y: {
                        beginAtZero: true,
                        ticks: {
                            precision: 0
                        },
                        grid: {
                            color: 'rgba(0, 0, 0, 0.05)'
                        }
                    }
                }
            }
        });
    }
    
    /**
     * 加载仪表盘数据
     */
    async loadDashboardData() {
        try {
            this.setLoadingState(true);
            
            // 并行加载各项数据
            const [metrics, systemStatus, circuitBreakers, recentErrors] = await Promise.all([
                this.fetchMetrics(),
                this.fetchSystemStatus(),
                this.fetchCircuitBreakers(),
                this.fetchRecentErrors()
            ]);
            
            // 更新UI
            this.updateMetrics(metrics);
            this.updateSystemStatus(systemStatus);
            this.updateCircuitBreakers(circuitBreakers);
            this.updateRecentErrors(recentErrors);
            
            // 加载性能趋势数据
            this.loadPerformanceTrendData(this.state.timeRange);
            
            // 更新最后刷新时间
            this.updateLastRefreshTime();
        } catch (error) {
            console.error('加载仪表盘数据失败:', error);
            this.showErrorMessage('加载数据失败，请稍后重试');
        } finally {
            this.setLoadingState(false);
        }
    }
    
    /**
     * 设置加载状态
     */
    setLoadingState(isLoading) {
        this.state.isLoading = isLoading;
        const refreshBtn = document.getElementById('refresh-data');
        
        if (refreshBtn) {
            if (isLoading) {
                refreshBtn.innerHTML = '<i class="fa fa-refresh fa-spin mr-1"></i> 加载中...';
                refreshBtn.disabled = true;
            } else {
                refreshBtn.innerHTML = '<i class="fa fa-refresh mr-1"></i> 刷新';
                refreshBtn.disabled = false;
            }
        }
    }
    
    /**
     * 切换侧边栏
     */
    toggleSidebar() {
        const sidebar = document.getElementById('sidebar');
        const content = document.getElementById('content');
        
        if (sidebar && content) {
            sidebar.classList.toggle('collapsed');
            content.classList.toggle('collapsed');
        }
    }
    
    /**
     * 设置时间范围
     */
    setTimeRange(timeRange) {
        if (this.state.timeRange === timeRange) return;
        
        this.state.timeRange = timeRange;
        
        // 更新按钮状态
        document.querySelectorAll('.time-range').forEach(btn => {
            btn.classList.remove('active');
        });
        
        const activeBtn = document.querySelector(`.time-range[data-range="${timeRange}"]`);
        if (activeBtn) {
            activeBtn.classList.add('active');
        }
        
        // 加载对应时间范围的数据
        this.loadPerformanceTrendData(timeRange);
    }
    
    /**
     * 导航到指定区域
     */
    navigateToSection(sectionId) {
        // 更新导航链接状态
        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${sectionId}`) {
                link.classList.add('active');
            }
        });
        
        // 这里可以实现内容区域的切换逻辑
        console.log(`导航到区域: ${sectionId}`);
        
        // 滚动到顶部
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
    
    /**
     * 选择系统
     */
    selectSystem(systemName) {
        this.state.selectedSystem = systemName;
        
        // 高亮选中的行
        document.querySelectorAll('#system-status-table tbody tr').forEach(row => {
            row.classList.toggle('bg-primary', row.cells[0].textContent === systemName);
            row.classList.toggle('text-white', row.cells[0].textContent === systemName);
        });
        
        // 可以加载该系统的详细信息
        this.loadSystemDetails(systemName);
    }
    
    /**
     * 更新最后刷新时间
     */
    updateLastRefreshTime() {
        const now = new Date();
        const lastUpdateEl = document.getElementById('last-update');
        if (lastUpdateEl) {
            lastUpdateEl.textContent = '最后更新: ' + now.toLocaleTimeString('zh-CN');
        }
    }
    
    /**
     * 获取指标数据
     */
    async fetchMetrics() {
        try {
            const response = await fetch('/api/mcp/dashboard/metrics');
            if (!response.ok) {
                throw new Error('获取指标数据失败');
            }
            return await response.json();
        } catch (error) {
            console.error('获取指标数据失败:', error);
            // 返回模拟数据
            return this.getMockMetricsData();
        }
    }
    
    /**
     * 获取系统状态数据
     */
    async fetchSystemStatus() {
        try {
            const response = await fetch('/api/mcp/dashboard/system-health');
            if (!response.ok) {
                throw new Error('获取系统状态失败');
            }
            return await response.json();
        } catch (error) {
            console.error('获取系统状态失败:', error);
            // 返回模拟数据
            return this.getMockSystemStatusData();
        }
    }
    
    /**
     * 获取断路器状态数据
     */
    async fetchCircuitBreakers() {
        try {
            const response = await fetch('/api/mcp/dashboard/circuit-breakers');
            if (!response.ok) {
                throw new Error('获取断路器状态失败');
            }
            return await response.json();
        } catch (error) {
            console.error('获取断路器状态失败:', error);
            // 返回模拟数据
            return this.getMockCircuitBreakerData();
        }
    }
    
    /**
     * 获取最近错误数据
     */
    async fetchRecentErrors() {
        try {
            const response = await fetch('/api/mcp/dashboard/errors');
            if (!response.ok) {
                throw new Error('获取最近错误失败');
            }
            return await response.json();
        } catch (error) {
            console.error('获取最近错误失败:', error);
            // 返回模拟数据
            return this.getMockErrorsData();
        }
    }
    
    /**
     * 加载性能趋势数据
     */
    async loadPerformanceTrendData(timeRange) {
        try {
            const response = await fetch(`/api/mcp/dashboard/performance-trend?range=${timeRange}`);
            if (!response.ok) {
                throw new Error('获取性能趋势数据失败');
            }
            const data = await response.json();
            this.updatePerformanceTrendChart(data.labels, data.responseTimes, data.callCounts, data.errorRates);
        } catch (error) {
            console.error('获取性能趋势数据失败:', error);
            // 使用模拟数据
            this.updatePerformanceTrendChartWithMockData(timeRange);
        }
    }
    
    /**
     * 加载系统详情
     */
    async loadSystemDetails(systemName) {
        try {
            const response = await fetch(`/api/mcp/dashboard/system-details/${encodeURIComponent(systemName)}`);
            if (!response.ok) {
                throw new Error('获取系统详情失败');
            }
            const data = await response.json();
            // 这里可以更新系统详情面板
            console.log('系统详情:', data);
        } catch (error) {
            console.error('获取系统详情失败:', error);
        }
    }
    
    /**
     * 查看错误详情
     */
    async viewErrorDetails(callId) {
        try {
            const response = await fetch(`/api/mcp/dashboard/error-details/${callId}`);
            if (!response.ok) {
                throw new Error('获取错误详情失败');
            }
            const errorDetails = await response.json();
            this.showErrorDetailsModal(errorDetails);
        } catch (error) {
            console.error('获取错误详情失败:', error);
            this.showErrorMessage('获取错误详情失败');
        }
    }
    
    /**
     * 更新指标显示
     */
    updateMetrics(metrics) {
        const updateMetric = (elementId, value) => {
            const element = document.getElementById(elementId);
            if (element) element.textContent = value;
        };
        
        const updateTrend = (elementId, trend) => {
            const element = document.getElementById(elementId);
            if (element) {
                element.innerHTML = `今日较昨日 <span class="text-white">${this.formatChange(trend)}</span>`;
            }
        };
        
        updateMetric('total-calls', this.formatNumber(metrics.totalCalls));
        updateTrend('calls-trend', metrics.callsTrend);
        
        updateMetric('success-rate', `${metrics.successRate}%`);
        updateTrend('success-trend', metrics.successTrend);
        
        updateMetric('avg-response-time', `${metrics.avgResponseTime}ms`);
        updateTrend('response-trend', metrics.responseTrend);
        
        updateMetric('error-rate', `${metrics.errorRate}%`);
        updateTrend('error-trend', metrics.errorTrend);
        
        // 更新响应时间分布图表
        if (this.charts.responseTime && metrics.responseTimeDistribution) {
            this.charts.responseTime.data.datasets[0].data = metrics.responseTimeDistribution;
            this.charts.responseTime.update();
        }
    }
    
    /**
     * 更新系统状态表格
     */
    updateSystemStatus(systems) {
        const tableBody = document.getElementById('system-status-table');
        if (!tableBody) return;
        
        tableBody.innerHTML = '';
        
        systems.forEach(system => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${system.name}</td>
                <td>
                    <span class="system-status status-${system.status.toLowerCase()}"></span>
                    ${system.status === 'UP' ? '已连接' : '未连接'}
                </td>
                <td>${this.getHealthStatusBadge(system.health)}</td>
                <td>${system.lastChecked}</td>
                <td>${this.formatNumber(system.calls)}</td>
                <td>${system.errors} (${((system.errors / system.calls) * 100).toFixed(2)}%)</td>
            `;
            tableBody.appendChild(row);
        });
    }
    
    /**
     * 更新断路器列表
     */
    updateCircuitBreakers(breakers) {
        const listGroup = document.getElementById('circuit-breaker-list');
        if (!listGroup) return;
        
        listGroup.innerHTML = '';
        
        breakers.forEach(breaker => {
            const item = document.createElement('div');
            item.className = 'list-group-item d-flex justify-content-between align-items-center';
            
            const statusClass = breaker.status === 'CLOSED' ? 'text-success' : 
                              (breaker.status === 'OPEN' ? 'text-danger' : 'text-warning');
            const statusText = breaker.status === 'CLOSED' ? '关闭' : 
                              (breaker.status === 'OPEN' ? '开启' : '半开');
            
            item.innerHTML = `
                <div>
                    <div><strong>${breaker.name}</strong></div>
                    <div class="text-sm">调用: ${this.formatNumber(breaker.calls)} | 失败: ${breaker.failures}</div>
                </div>
                <div class="text-right">
                    <div class="${statusClass}"><strong>${statusText}</strong></div>
                    <div class="text-sm">失败率: ${breaker.failureRate}%</div>
                </div>
            `;
            
            listGroup.appendChild(item);
        });
    }
    
    /**
     * 更新最近错误表格
     */
    updateRecentErrors(errors) {
        const tableBody = document.getElementById('recent-errors-table');
        if (!tableBody) return;
        
        tableBody.innerHTML = '';
        
        errors.forEach(error => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${error.time}</td>
                <td>${error.system}</td>
                <td>${error.errorType}</td>
                <td>${this.truncateText(error.message, 50)}</td>
                <td>${error.callId}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary view-error-details" data-call-id="${error.callId}">详情</button>
                    <button class="btn btn-sm btn-outline-secondary ml-1 retry-call" data-call-id="${error.callId}">重试</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }
    
    /**
     * 更新性能趋势图表
     */
    updatePerformanceTrendChart(labels, responseTimes, callCounts, errorRates) {
        if (!this.charts.performanceTrend) return;
        
        this.charts.performanceTrend.data.labels = labels;
        this.charts.performanceTrend.data.datasets[0].data = responseTimes;
        this.charts.performanceTrend.data.datasets[1].data = callCounts;
        this.charts.performanceTrend.data.datasets[2].data = errorRates;
        this.charts.performanceTrend.update();
    }
    
    /**
     * 更新性能趋势图表（使用模拟数据）
     */
    updatePerformanceTrendChartWithMockData(timeRange) {
        const { labels, responseTimes, callCounts, errorRates } = this.getMockPerformanceTrendData(timeRange);
        this.updatePerformanceTrendChart(labels, responseTimes, callCounts, errorRates);
    }
    
    /**
     * 显示错误详情模态框
     */
    showErrorDetailsModal(errorDetails) {
        // 这里可以实现错误详情模态框的显示逻辑
        console.log('显示错误详情:', errorDetails);
        alert(`错误详情\n时间: ${errorDetails.time}\n系统: ${errorDetails.system}\n错误类型: ${errorDetails.errorType}\n\n${errorDetails.message}`);
    }
    
    /**
     * 显示错误消息
     */
    showErrorMessage(message) {
        // 简单的错误消息显示
        alert(message);
    }
    
    /**
     * 获取健康状态徽章
     */
    getHealthStatusBadge(health) {
        switch(health) {
            case 'HEALTHY':
                return '<span class="badge bg-success">健康</span>';
            case 'DEGRADED':
                return '<span class="badge bg-warning">性能下降</span>';
            case 'UNHEALTHY':
                return '<span class="badge bg-danger">异常</span>';
            default:
                return '<span class="badge bg-secondary">未知</span>';
        }
    }
    
    /**
     * 格式化数字，添加千位分隔符
     */
    formatNumber(num) {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
    
    /**
     * 格式化变化百分比
     */
    formatChange(change) {
        if (change > 0) {
            return `<i class="fa fa-arrow-up text-success"></i> ${change.toFixed(1)}%`;
        } else if (change < 0) {
            return `<i class="fa fa-arrow-down text-danger"></i> ${Math.abs(change).toFixed(1)}%`;
        } else {
            return "<i class="fa fa-minus text-gray"></i> 0.0%";
        }
    }
    
    /**
     * 截断文本
     */
    truncateText(text, maxLength) {
        if (!text || text.length <= maxLength) return text;
        return text.substring(0, maxLength) + '...';
    }
    
    /**
     * 获取模拟指标数据
     */
    getMockMetricsData() {
        return {
            totalCalls: 12845,
            callsTrend: 15.2,
            successRate: 98.7,
            successTrend: 0.5,
            avgResponseTime: 283,
            responseTrend: -12.3,
            errorRate: 1.3,
            errorTrend: -0.4,
            responseTimeDistribution: [58, 32, 7, 2, 1]
        };
    }
    
    /**
     * 获取模拟系统状态数据
     */
    getMockSystemStatusData() {
        return [
            { name: 'EDMS', status: 'UP', health: 'HEALTHY', lastChecked: '2023-06-15 10:30:45', calls: 3560, errors: 12 },
            { name: 'MES', status: 'UP', health: 'HEALTHY', lastChecked: '2023-06-15 10:30:42', calls: 2890, errors: 8 },
            { name: 'LIMS', status: 'UP', health: 'HEALTHY', lastChecked: '2023-06-15 10:30:38', calls: 1980, errors: 5 },
            { name: 'ERP', status: 'UP', health: 'HEALTHY', lastChecked: '2023-06-15 10:30:40', calls: 4215, errors: 18 },
            { name: '培训系统', status: 'UP', health: 'HEALTHY', lastChecked: '2023-06-15 10:30:35', calls: 120, errors: 0 },
            { name: '设备系统', status: 'UP', health: 'HEALTHY', lastChecked: '2023-06-15 10:30:32', calls: 65, errors: 1 }
        ];
    }
    
    /**
     * 获取模拟断路器数据
     */
    getMockCircuitBreakerData() {
        return [
            { name: 'edmsClient', status: 'CLOSED', failureRate: 2.3, calls: 3560, failures: 12 },
            { name: 'mesClient', status: 'CLOSED', failureRate: 1.5, calls: 2890, failures: 8 },
            { name: 'limsClient', status: 'CLOSED', failureRate: 0.8, calls: 1980, failures: 5 },
            { name: 'erpClient', status: 'CLOSED', failureRate: 2.1, calls: 4215, failures: 18 },
            { name: 'trainingClient', status: 'CLOSED', failureRate: 0, calls: 120, failures: 0 },
            { name: 'equipmentClient', status: 'CLOSED', failureRate: 1.2, calls: 65, failures: 1 }
        ];
    }
    
    /**
     * 获取模拟错误数据
     */
    getMockErrorsData() {
        return [
            {
                time: '2023-06-15 10:28:15',
                system: 'ERP',
                errorType: 'ConnectionTimeoutException',
                message: '连接ERP系统超时，超过5000ms',
                callId: 'req-20230615-1028-43256'
            },
            {
                time: '2023-06-15 10:25:32',
                system: 'EDMS',
                errorType: 'ResourceNotFoundException',
                message: '无法找到文档ID: DOC-2023-0615-001',
                callId: 'req-20230615-1025-87342'
            },
            {
                time: '2023-06-15 10:20:48',
                system: 'MES',
                errorType: 'BadRequestException',
                message: '请求参数无效: batchNumber不能为空',
                callId: 'req-20230615-1020-12957'
            },
            {
                time: '2023-06-15 10:18:22',
                system: 'LIMS',
                errorType: 'UnauthorizedException',
                message: '认证失败: 令牌已过期',
                callId: 'req-20230615-1018-65421'
            },
            {
                time: '2023-06-15 10:15:05',
                system: 'ERP',
                errorType: 'ServiceUnavailableException',
                message: 'ERP系统暂时不可用，请稍后再试',
                callId: 'req-20230615-1015-29783'
            }
        ];
    }
    
    /**
     * 获取模拟性能趋势数据
     */
    getMockPerformanceTrendData(timeRange) {
        let labels, responseTimes, callCounts, errorRates;
        
        switch(timeRange) {
            case '1h':
                // 1小时数据 (每5分钟一个点)
                labels = Array.from({length: 12}, (_, i) => {
                    const now = new Date();
                    now.setMinutes(now.getMinutes() - 60 + i * 5);
                    return now.toLocaleTimeString('zh-CN', {hour: '2-digit', minute:'2-digit'});
                });
                responseTimes = [280, 275, 290, 310, 350, 320, 300, 295, 285, 270, 260, 255];
                callCounts = [120, 135, 145, 150, 165, 155, 140, 130, 125, 135, 140, 125];
                errorRates = [1.2, 1.1, 1.3, 1.5, 1.8, 1.4, 1.2, 1.1, 1.0, 0.9, 0.8, 0.7];
                break;
                
            case '24h':
                // 24小时数据 (每小时一个点)
                labels = Array.from({length: 24}, (_, i) => {
                    const now = new Date();
                    now.setHours(now.getHours() - 24 + i);
                    return now.toLocaleTimeString('zh-CN', {hour: '2-digit'});
                });
                // 生成模拟数据，包含工作时间和非工作时间的差异
                responseTimes = [];
                callCounts = [];
                errorRates = [];
                for (let i = 0; i < 24; i++) {
                    const hour = (new Date().getHours() - 24 + i + 24) % 24;
                    // 工作时间 (9:00-18:00)
                    if (hour >= 9 && hour <= 18) {
                        responseTimes.push(250 + Math.random() * 150);
                        callCounts.push(400 + Math.random() * 200);
                        errorRates.push(0.5 + Math.random() * 1.5);
                    } else {
                        // 非工作时间
                        responseTimes.push(200 + Math.random() * 100);
                        callCounts.push(50 + Math.random() * 100);
                        errorRates.push(0.3 + Math.random() * 0.7);
                    }
                }
                break;
                
            case '7d':
                // 7天数据 (每天一个点)
                labels = Array.from({length: 7}, (_, i) => {
                    const now = new Date();
                    now.setDate(now.getDate() - 7 + i);
                    return now.toLocaleDateString('zh-CN', {month: '2-digit', day: '2-digit'});
                });
                responseTimes = [320, 305, 290, 280, 295, 310, 285];
                callCounts = [3500, 3450, 3600, 3800, 3700, 3500, 2800];
                errorRates = [1.5, 1.4, 1.3, 1.2, 1.4, 1.6, 1.3];
                break;
                
            default:
                // 默认使用1小时数据
                labels = Array.from({length: 12}, (_, i) => {
                    const now = new Date();
                    now.setMinutes(now.getMinutes() - 60 + i * 5);
                    return now.toLocaleTimeString('zh-CN', {hour: '2-digit', minute:'2-digit'});
                });
                responseTimes = [280, 275, 290, 310, 350, 320, 300, 295, 285, 270, 260, 255];
                callCounts = [120, 135, 145, 150, 165, 155, 140, 130, 125, 135, 140, 125];
                errorRates = [1.2, 1.1, 1.3, 1.5, 1.8, 1.4, 1.2, 1.1, 1.0, 0.9, 0.8, 0.7];
        }
        
        return { labels, responseTimes, callCounts, errorRates };
    }
    
    /**
     * 启动自动刷新
     */
    startAutoRefresh() {
        if (this.state.autoRefresh) return;
        
        this.state.autoRefresh = true;
        this.state.refreshInterval = setInterval(() => {
            this.loadDashboardData();
        }, this.state.refreshRate);
    }
    
    /**
     * 停止自动刷新
     */
    stopAutoRefresh() {
        if (!this.state.autoRefresh) return;
        
        this.state.autoRefresh = false;
        if (this.state.refreshInterval) {
            clearInterval(this.state.refreshInterval);
            this.state.refreshInterval = null;
        }
    }
}

// DOM加载完成后初始化仪表盘
document.addEventListener('DOMContentLoaded', function() {
    // 确保文档完全加载
    setTimeout(() => {
        // 初始化MCP仪表盘
        window.mcpDashboard = new McpDashboard();
        
        // 可选：启动自动刷新
        // window.mcpDashboard.startAutoRefresh();
    }, 100);
});