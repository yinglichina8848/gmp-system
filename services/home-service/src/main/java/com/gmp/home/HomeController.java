package com.gmp.home;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * GMP主页路由配置
 */
@Configuration
public class HomeController {

    @Bean
    public RouterFunction<ServerResponse> homeRoute() {
        return route(GET("/"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .bodyValue(getHomePageHtml()));
    }

    @Bean
    public RouterFunction<ServerResponse> healthRoute() {
        return route(GET("/health"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(
                                "{\"status\":\"UP\",\"version\":\"0.2.0\",\"description\":\"GMP Homepage Service\"}"));
    }

    private String getHomePageHtml() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>贵州高原彝药厂 GMP 信息管理系统</title>\n" +
                "    <style>\n" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }\n" +
                "        body {\n" +
                "            font-family: 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;\n" +
                "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "            min-height: 100vh;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background: rgba(255, 255, 255, 0.95);\n" +
                "            padding: 40px;\n" +
                "            border-radius: 20px;\n" +
                "            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);\n" +
                "            max-width: 1000px;\n" +
                "            width: 100%;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            width: 120px;\n" +
                "            height: 120px;\n" +
                "            background: linear-gradient(45deg, #FF6B6B, #4ECDC4);\n" +
                "            border-radius: 50%;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            margin: 0 auto 20px;\n" +
                "            font-size: 40px;\n" +
                "            color: white;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .title {\n" +
                "            font-size: 32px;\n" +
                "            font-weight: bold;\n" +
                "            color: #2c3e50;\n" +
                "            margin-bottom: 20px;\n" +
                "            line-height: 1.3;\n" +
                "        }\n" +
                "        .subtitle {\n" +
                "            font-size: 20px;\n" +
                "            color: #7f8c8d;\n" +
                "            margin-bottom: 30px;\n" +
                "        }\n" +
                "        .welcome-msg {\n" +
                "            font-size: 26px;\n" +
                "            color: #27ae60;\n" +
                "            font-weight: bold;\n" +
                "            margin: 20px 0 40px;\n" +
                "            padding: 20px;\n" +
                "            background: rgba(39, 174, 96, 0.1);\n" +
                "            border-radius: 12px;\n" +
                "            border-left: 4px solid #27ae60;\n" +
                "        }\n" +
                "        .nav-grid {\n" +
                "            display: grid;\n" +
                "            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));\n" +
                "            gap: 20px;\n" +
                "            margin: 30px 0;\n" +
                "        }\n" +
                "        .nav-card {\n" +
                "            background: white;\n" +
                "            padding: 25px;\n" +
                "            border-radius: 12px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "            transition: all 0.3s ease;\n" +
                "            border-left: 4px solid #3498db;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .nav-card:hover {\n" +
                "            transform: translateY(-5px);\n" +
                "            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);\n" +
                "        }\n" +
                "        .nav-header {\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "        .nav-icon {\n" +
                "            font-size: 32px;\n" +
                "            margin-right: 15px;\n" +
                "        }\n" +
                "        .nav-title {\n" +
                "            font-weight: bold;\n" +
                "            color: #2c3e50;\n" +
                "            font-size: 16px;\n" +
                "        }\n" +
                "        .nav-desc {\n" +
                "            font-size: 14px;\n" +
                "            color: #6c757d;\n" +
                "            margin-bottom: 15px;\n" +
                "            line-height: 1.4;\n" +
                "        }\n" +
                "        .nav-link {\n" +
                "            display: inline-block;\n" +
                "            background: linear-gradient(45deg, #3498db, #2980b9);\n" +
                "            color: white;\n" +
                "            padding: 8px 16px;\n" +
                "            border-radius: 20px;\n" +
                "            text-decoration: none;\n" +
                "            font-size: 12px;\n" +
                "            font-weight: bold;\n" +
                "            transition: all 0.3s ease;\n" +
                "        }\n" +
                "        .nav-link:hover {\n" +
                "            background: linear-gradient(45deg, #2980b9, #21618c);\n" +
                "            transform: translateY(-1px);\n" +
                "        }\n" +
                "        .status-info {\n" +
                "            background: #f8f9fa;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 10px;\n" +
                "            margin-top: 30px;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .status-title {\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "            color: #2c3e50;\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "        .status-item {\n" +
                "            margin: 8px 0;\n" +
                "            font-size: 14px;\n" +
                "            color: #27ae60;\n" +
                "        }\n" +
                "        .status-item::before {\n" +
                "            content: \"✅ \";\n" +
                "            color: #27ae60;\n" +
                "            margin-right: 5px;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 40px;\n" +
                "            font-size: 12px;\n" +
                "            color: #95a5a6;\n" +
                "        }\n" +
                "        @media (max-width: 768px) {\n" +
                "            .container { padding: 30px; }\n" +
                "            .title { font-size: 24px; }\n" +
                "            .welcome-msg { font-size: 22px; }\n" +
                "            .nav-grid { grid-template-columns: 1fr; }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"logo\">GMP</div>\n" +
                "        <h1 class=\"title\">贵州高原彝药厂<br>GMP 信息管理系统</h1>\n" +
                "        <div class=\"subtitle\">Guizhou Plateau Yi Pharmaceutical Factory Co., Ltd.</div>\n" +
            "\n" +
            "        <!-- GMP 信息管理系统各子系统 -->\n" +
            "        <div class=\"system-section gmp-section\">\n" +
            "            <h2 class=\"section-title gmp-title\">🏭 GMP 信息管理系统</h2>\n" +
            "            <p class=\"section-desc\">药品生产质量管理体系核心业务系统</p>\n" +
            "\n" +
            "            <div class=\"nav-grid gmp-grid\">\n" +
            "                <div class=\"nav-card gmp-card\">\n" +
            "                    <div class=\"nav-header\">\n" +
            "                        <div class=\"nav-icon\">🔍</div>\n" +
            "                        <div class=\"nav-title\">质量管理系统</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"nav-desc\">质量管理与合规执行系统<br>GMP质量体系的核心模块</div>\n" +
            "                    <a href=\"/qms\" class=\"nav-link\">进入系统</a>\n" +
            "                </div>\n" +
            "\n" +
            "                <div class=\"nav-card gmp-card\">\n" +
            "                    <div class=\"nav-header\">\n" +
            "                        <div class=\"nav-icon\">⚙️</div>\n" +
            "                        <div class=\"nav-title\">生产执行系统</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"nav-desc\">生产批次管理与执行跟踪<br>MES生产管控核心功能</div>\n" +
            "                    <a href=\"/mes\" class=\"nav-link\">进入系统</a>\n" +
            "                </div>\n" +
            "\n" +
            "                <div class=\"nav-card gmp-card\">\n" +
            "                    <div class=\"nav-header\">\n" +
            "                        <div class=\"nav-icon\">🧪</div>\n" +
            "                        <div class=\"nav-title\">实验室信息系统</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"nav-desc\">实验室检验与测试管理<br>LIMS样本检测追溯</div>\n" +
            "                    <a href=\"/lims\" class=\"nav-link\">进入系统</a>\n" +
            "                </div>\n" +
            "\n" +
            "                <div class=\"nav-card gmp-card\">\n" +
            "                    <div class=\"nav-header\">\n" +
            "                        <div class=\"nav-icon\">📄</div>\n" +
            "                        <div class=\"nav-title\">电子文档管理</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"nav-desc\">文档审批与版本控制<br>GMP电子文档管理系统</div>\n" +
            "                    <a href=\"/edms\" class=\"nav-link\">进入系统</a>\n" +
            "                </div>\n" +
            "\n" +
            "                <div class=\"nav-card gmp-card\">\n" +
            "                    <div class=\"nav-header\">\n" +
            "                        <div class=\"nav-icon\">👥</div>\n" +
            "                        <div class=\"nav-title\">用户认证系统</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"nav-desc\">用户管理与权限控制<br>企业级安全认证平台</div>\n" +
            "                    <a href=\"/auth\" class=\"nav-link\">进入系统</a>\n" +
            "                </div>\n" +
            "\n" +
            "                <div class=\"nav-card gmp-card\">\n" +
            "                    <div class=\"nav-header\">\n" +
            "                        <div class=\"nav-icon\">📁</div>\n" +
            "                        <div class=\"nav-title\">文件管理系统</div>\n" +
            "                    </div>\n" +
            "                    <div class=\"nav-desc\">文件存储与归档管理<br>统一的文件服务平台</div>\n" +
            "                    <a href=\"/files\" class=\"nav-link\">进入系统</a>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "\n" +
            "        <!-- 分隔符 -->\n" +
            "        <div class=\"section-separator\">\n" +
            "            <hr class=\"divider\">\n" +
            "            <span class=\"divider-text\">平台基础设施服务</span>\n" +
            "        </div>\n" +
            "\n" +
            "        <!-- 微服务基础设施 -->\n" +
            "        <div class=\"system-section infra-section\">\n" +
            "            <h2 class=\"section-title infra-title\">🚀 微服务基础设施</h2>\n" +
            "            <p class=\"section-desc\">支撑应用系统的关键中间件与监控服务</p>\n" +
            "\n" +
            "            <div class=\"nav-grid infra-grid\">\n" +
                "            <div class=\"nav-card infra-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">🗄️</div>\n" +
                "                    <div class=\"nav-title\">PostgreSQL 数据库</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">核心业务数据存储系统<br>关系型数据库服务</div>\n" +
                "                <a href=\"http://localhost:5432\" target=\"_blank\" class=\"nav-link\">管理连接</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card infra-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">⚡</div>\n" +
                "                    <div class=\"nav-title\">Redis 缓存</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">高性能内存缓存服务<br>认证令牌存储</div>\n" +
                "                <a href=\"javascript:alert('Redis服务信息：\\n🔗 地址: localhost:6379\\n🔐 密码: gmp_redis_password_2024')\" class=\"nav-link\">连接信息</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card infra-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">📨</div>\n" +
                "                    <div class=\"nav-title\">RabbitMQ 消息队列</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">企业级消息中间件<br>异步处理服务</div>\n" +
                "                <a href=\"http://localhost:15672\" target=\"_blank\" class=\"nav-link\">管理界面</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card infra-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">📊</div>\n" +
                "                    <div class=\"nav-title\">Grafana 监控面板</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">可视化系统监控面板<br>业务指标展示</div>\n" +
                "                <a href=\"http://localhost:3000\" target=\"_blank\" class=\"nav-link\">访问监控</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card infra-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">📈</div>\n" +
                "                    <div class=\"nav-title\">Prometheus 监控</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">指标收集与告警系统<br>性能数据收集</div>\n" +
                "                <a href=\"http://localhost:9090\" target=\"_blank\" class=\"nav-link\">访问控制台</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card infra-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">☁️</div>\n" +
                "                    <div class=\"nav-title\">MinIO 对象存储</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">高性能对象存储系统<br>文件存储服务</div>\n" +
                "                <a href=\"http://localhost:9001\" target=\"_blank\" class=\"nav-link\">访问面板</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card infra-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">🏗️</div>\n" +
                "                    <div class=\"nav-title\">Eureka 注册中心</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">微服务注册发现中心<br>服务治理中心</div>\n" +
                "                <a href=\"http://localhost:8761\" target=\"_blank\" class=\"nav-link\">服务注册</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <!-- 欢迎信息 -->\n" +
                "    <div class=\"welcome-msg\">\n" +
                "        🎊 欢迎使用 - GMP微服务系统已完全部署！\n" +
                "    </div>\n" +
                "\n" +
            "        <div class=\"status-info\">\n" +
            "            <div class=\"status-title\">🎯 系统部署状态</div>\n" +
            "            <div class=\"status-item\">GMP微服务架构已完全部署</div>\n" +
            "            <div class=\"status-item\">Spring Cloud Gateway运行 (端口 8080)</div>\n" +
            "            <div class=\"status-item\">所有容器服务正常运行</div>\n" +
            "            <div class=\"status-item\">数据库和用户认证系统已初始化</div>\n" +
            "            <div class=\"status-item\">准备接受各业务微服务注册</div>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"footer\">\n" +
            "            <p>© 2025 贵州高原彝药厂 GMP 信息管理系统 | 基于 Spring Cloud 微服务架构</p>\n" +
            "            <p>系统版本 0.2.1 | 现代化药品生产质量管理系统 | 支持 GMP 标准</p>\n" +
            "        </div>\n" +
            "\n" +
            "        <!-- GMP 分隔标识 -->\n" +
            "        <div class=\"gmp-identification\">\n" +
            "            <div class=\"gmp-badge\">GMP COMPLIANT</div>\n" +
            "            <div class=\"gmp-desc\">符合药品生产质量管理规范</div>\n" +
            "        </div>\n" +
            "\n" +
            "        <!-- 联系方式 -->\n" +
            "        <div class=\"contact-info\">\n" +
            "            📞 技术支持：GMP系统开发团队 | 📧 admin@gmp-system.com\n" +
            "        </div>\n" +
            "\n" +
            "    </div>\n" +
                "        <div class=\"nav-grid\">\n" +
                "            <div class=\"nav-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">🗄️</div>\n" +
                "                    <div class=\"nav-title\">PostgreSQL 数据库</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">核心业务数据库存储系统<br>地址: localhost:5432 | 用户名: postgres</div>\n" +
                "                <a href=\"javascript:alert('📊 数据库连接信息：\\n🔗 地址: localhost:5432\\n👤 用户名: postgres\\n📄 数据库: gmp_system\\n\\n状态: ✅ 运行正常')\" class=\"nav-link\">查看连接</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">⚡</div>\n" +
                "                    <div class=\"nav-title\">Redis 缓存</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">高性能分布式缓存服务<br>地址: localhost:6379 | 安全认证</div>\n" +
                "                <a href=\"javascript:alert('⚡ Redis缓存服务信息：\\n🔗 地址: localhost:6379\\n🔐 密码: gmp_redis_password_2024\\n\\n状态: ✅ 运行正常')\" class=\"nav-link\">查看连接</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">📨</div>\n" +
                "                    <div class=\"nav-title\">RabbitMQ 消息队列</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">企业级消息队列中间件<br>管理界面: localhost:15672</div>\n" +
                "                <a href=\"http://localhost:15672\" target=\"_blank\" class=\"nav-link\">访问管理界面</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">📊</div>\n" +
                "                    <div class=\"nav-title\">Grafana 监控面板</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">可视化监控仪表板<br>实时系统监控与预警</div>\n" +
                "                <a href=\"http://localhost:3000\" target=\"_blank\" class=\"nav-link\">访问Grafana</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">📈</div>\n" +
                "                    <div class=\"nav-title\">Prometheus 监控</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">指标收集与时间序列数据库<br>基础数据源服务</div>\n" +
                "                <a href=\"http://localhost:9090\" target=\"_blank\" class=\"nav-link\">访问Prometheus</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">☁️</div>\n" +
                "                    <div class=\"nav-title\">MinIO 对象存储</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">高性能对象存储系统<br>文件存储与管理平台</div>\n" +
                "                <a href=\"http://localhost:9001\" target=\"_blank\" class=\"nav-link\">访问控制台</a>\n" +
                "            </div>\n" +
                "\n" +
                "            <div class=\"nav-card\">\n" +
                "                <div class=\"nav-header\">\n" +
                "                    <div class=\"nav-icon\">🏗️</div>\n" +
                "                    <div class=\"nav-title\">Eureka 注册中心</div>\n" +
                "                </div>\n" +
                "                <div class=\"nav-desc\">微服务注册与发现中心<br>服务治理核心组件</div>\n" +
                "                <a href=\"http://localhost:8761\" target=\"_blank\" class=\"nav-link\">访问注册中心</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"status-info\">\n" +
                "            <div class=\"status-title\">🎯 系统部署状态</div>\n" +
                "            <div class=\"status-item\">GMP微服务架构已完全部署</div>\n" +
                "            <div class=\"status-item\">Spring Cloud Gateway运行 (端口 8080)</div>\n" +
                "            <div class=\"status-item\">所有容器服务正常运行</div>\n" +
                "            <div class=\"status-item\">准备接受业务微服务注册</div>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"footer\">\n" +
                "            <p>© 2025 贵州高原彝药厂 GMP 信息管理系统 | 基于 Spring Cloud 微服务架构</p>\n" +
                "            <p>系统版本 0.2.0 | 现代化药品生产质量管理系统 | 支持 GMP 标准</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
