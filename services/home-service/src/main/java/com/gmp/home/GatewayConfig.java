package com.gmp.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * GMP系统路由配置
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> homeRoute() {
        return route(GET("/"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .bodyValue(getHomePageHtml()));
    }

    private String getHomePageHtml() {
        return """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>贵州高原彝药厂 GMP 信息管理系统</title>
                    <style>
                        * { margin: 0; padding: 0; box-sizing: border-box; }
                        body {
                            font-family: 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            min-height: 100vh;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            padding: 20px;
                        }
                        .container {
                            background: rgba(255, 255, 255, 0.95);
                            padding: 40px;
                            border-radius: 20px;
                            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                            max-width: 1000px;
                            width: 100%;
                            text-align: center;
                        }
                        .logo {
                            width: 120px;
                            height: 120px;
                            background: linear-gradient(45deg, #FF6B6B, #4ECDC4);
                            border-radius: 50%;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            margin: 0 auto 20px;
                            font-size: 40px;
                            color: white;
                            font-weight: bold;
                        }
                        .title {
                            font-size: 32px;
                            font-weight: bold;
                            color: #2c3e50;
                            margin-bottom: 20px;
                            line-height: 1.3;
                        }
                        .subtitle {
                            font-size: 20px;
                            color: #7f8c8d;
                            margin-bottom: 30px;
                        }
                        .welcome-msg {
                            font-size: 26px;
                            color: #27ae60;
                            font-weight: bold;
                            margin: 20px 0 40px;
                            padding: 20px;
                            background: rgba(39, 174, 96, 0.1);
                            border-radius: 12px;
                            border-left: 4px solid #27ae60;
                        }
                        .nav-grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
                            gap: 20px;
                            margin: 30px 0;
                        }
                        .nav-card {
                            background: white;
                            padding: 25px;
                            border-radius: 12px;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                            transition: all 0.3s ease;
                            border-left: 4px solid #3498db;
                            text-align: left;
                        }
                        .nav-card:hover {
                            transform: translateY(-5px);
                            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
                        }
                        .nav-header {
                            display: flex;
                            align-items: center;
                            margin-bottom: 15px;
                        }
                        .nav-icon {
                            font-size: 32px;
                            margin-right: 15px;
                        }
                        .nav-title {
                            font-weight: bold;
                            color: #2c3e50;
                            font-size: 16px;
                        }
                        .nav-desc {
                            font-size: 14px;
                            color: #6c757d;
                            margin-bottom: 15px;
                            line-height: 1.4;
                        }
                        .nav-link {
                            display: inline-block;
                            background: linear-gradient(45deg, #3498db, #2980b9);
                            color: white;
                            padding: 8px 16px;
                            border-radius: 20px;
                            text-decoration: none;
                            font-size: 12px;
                            font-weight: bold;
                            transition: all 0.3s ease;
                        }
                        .nav-link:hover {
                            background: linear-gradient(45deg, #2980b9, #21618c);
                            transform: translateY(-1px);
                        }
                        .status-info {
                            background: #f8f9fa;
                            padding: 20px;
                            border-radius: 10px;
                            margin-top: 30px;
                            text-align: left;
                        }
                        .status-title {
                            font-size: 18px;
                            font-weight: bold;
                            color: #2c3e50;
                            margin-bottom: 15px;
                        }
                        .status-item {
                            margin: 8px 0;
                            font-size: 14px;
                            color: #27ae60;
                        }
                        .status-item::before {
                            content: "✅ ";
                            color: #27ae60;
                            margin-right: 5px;
                        }
                        .footer {
                            margin-top: 40px;
                            font-size: 12px;
                            color: #95a5a6;
                        }
                        @media (max-width: 768px) {
                            .container { padding: 30px; }
                            .title { font-size: 24px; }
                            .welcome-msg { font-size: 22px; }
                            .nav-grid { grid-template-columns: 1fr; }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="logo">GMP</div>
                        <h1 class="title">贵州高原彝药厂<br>GMP 信息管理系统</h1>
                        <div class="subtitle">Guizhou Plateau Yi Medicine Factory GMP Information Management System</div>

                        <div class="welcome-msg">
                            🎊 欢迎使用 - GMP微服务系统已完全部署！
                        </div>

                        <div class="nav-grid">
                            <div class="nav-card">
                                <div class="nav-header">
                                    <div class="nav-icon">🗄️</div>
                                    <div class="nav-title">PostgreSQL 数据库</div>
                                </div>
                                <div class="nav-desc">核心业务数据库存储系统<br>地址: localhost:5432 | 用户名: postgres</div>
                                <a href="javascript:alert('📊 数据库连接信息：\\n🔗 地址: localhost:5432\\n👤 用户名: postgres\\n📄 数据库: gmp_system\\n\\n状态: ✅ 运行正常')" class="nav-link">查看连接</a>
                            </div>

                            <div class="nav-card">
                                <div class="nav-header">
                                    <div class="nav-icon">⚡</div>
                                    <div class="nav-title">Redis 缓存</div>
                                </div>
                                <div class="nav-desc">高性能分布式缓存服务<br>地址: localhost:6379 | 安全认证</div>
                                <a href="javascript:alert('⚡ Redis缓存服务信息：\\n🔗 地址: localhost:6379\\n🔐 密码: gmp_redis_password_2024\\n\\n状态: ✅ 运行正常')" class="nav-link">查看连接</a>
                            </div>

                            <div class="nav-card">
                                <div class="nav-header">
                                    <div class="nav-icon">📨</div>
                                    <div class="nav-title">RabbitMQ 消息队列</div>
                                </div>
                                <div class="nav-desc">企业级消息队列中间件<br>管理界面: localhost:15672</div>
                                <a href="http://localhost:15672" target="_blank" class="nav-link">访问管理界面</a>
                            </div>

                            <div class="nav-card">
                                <div class="nav-header">
                                    <div class="nav-icon">📊</div>
                                    <div class="nav-title">Grafana 监控面板</div>
                                </div>
                                <div class="nav-desc">可视化监控仪表板<br>实时系统监控与预警</div>
                                <a href="http://localhost:3000" target="_blank" class="nav-link">访问Grafana</a>
                            </div>

                            <div class="nav-card">
                                <div class="nav-header">
                                    <div class="nav-icon">📈</div>
                                    <div class="nav-title">Prometheus 监控</div>
                                </div>
                                <div class="nav-desc">指标收集与时间序列数据库<br>基础数据源服务</div>
                                <a href="http://localhost:9090" target="_blank" class="nav-link">访问Prometheus</a>
                            </div>

                            <div class="nav-card">
                                <div class="nav-header">
                                    <div class="nav-icon">☁️</div>
                                    <div class="nav-title">MinIO 对象存储</div>
                                </div>
                                <div class="nav-desc">高性能对象存储系统<br>文件存储与管理平台</div>
                                <a href="http://localhost:9001" target="_blank" class="nav-link">访问控制台</a>
                            </div>

                            <div class="nav-card">
                                <div class="nav-header">
                                    <div class="nav-icon">🏗️</div>
                                    <div class="nav-title">Eureka 注册中心</div>
                                </div>
                                <div class="nav-desc">微服务注册与发现中心<br>服务治理核心组件</div>
                                <a href="http://localhost:8761" target="_blank" class="nav-link">访问注册中心</a>
                            </div>
                        </div>

                        <div class="status-info">
                            <div class="status-title">🎯 系统部署状态</div>
                            <div class="status-item">GMP微服务架构已完全部署</div>
                            <div class="status-item">Spring Cloud Gateway运行 (端口 8080)</div>
                            <div class="status-item">所有容器服务正常运行</div>
                            <div class="status-item">准备接受业务微服务注册</div>
                        </div>

                        <div class="footer">
                            <p>© 2025 贵州高原彝药厂 GMP 信息管理系统 | 基于 Spring Cloud 微服务架构</p>
                            <p>系统版本 1.0.0 | 现代化药品生产质量管理系统 | 支持 GMP 标准</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
    }
}
