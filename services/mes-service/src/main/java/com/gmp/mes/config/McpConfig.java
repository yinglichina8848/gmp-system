package com.gmp.mes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MCP配置类 - 配置MCP客户端和服务端功能
 * 
 * @author gmp-system
 */
@Configuration
@ConfigurationProperties(prefix = "mcp")
public class McpConfig {

    private boolean enabled = true;
    private Server server = new Server();
    private Client client = new Client();
    private Cache cache = new Cache();

    /**
     * 获取MCP启用状态
     * 
     * @return 是否启用MCP
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 设置MCP启用状态
     * 
     * @param enabled 是否启用MCP
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 获取MCP服务端配置
     * 
     * @return 服务端配置
     */
    public Server getServer() {
        return server;
    }

    /**
     * 设置MCP服务端配置
     * 
     * @param server 服务端配置
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * 获取MCP客户端配置
     * 
     * @return 客户端配置
     */
    public Client getClient() {
        return client;
    }

    /**
     * 设置MCP客户端配置
     * 
     * @param client 客户端配置
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * 获取MCP缓存配置
     * 
     * @return 缓存配置
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * 设置MCP缓存配置
     * 
     * @param cache 缓存配置
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * MCP服务端配置类
     */
    public static class Server {
        private String host = "localhost";
        private int port = 8085;

        /**
         * 获取服务端主机名
         * 
         * @return 主机名
         */
        public String getHost() {
            return host;
        }

        /**
         * 设置服务端主机名
         * 
         * @param host 主机名
         */
        public void setHost(String host) {
            this.host = host;
        }

        /**
         * 获取服务端端口
         * 
         * @return 端口号
         */
        public int getPort() {
            return port;
        }

        /**
         * 设置服务端端口
         * 
         * @param port 端口号
         */
        public void setPort(int port) {
            this.port = port;
        }
    }

    /**
     * MCP客户端配置类
     */
    public static class Client {
        private int timeout = 30000;

        /**
         * 获取客户端超时时间
         * 
         * @return 超时时间（毫秒）
         */
        public int getTimeout() {
            return timeout;
        }

        /**
         * 设置客户端超时时间
         * 
         * @param timeout 超时时间（毫秒）
         */
        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }

    /**
     * MCP缓存配置类
     */
    public static class Cache {
        private boolean enabled = true;
        private int ttl = 300000; // 5分钟

        /**
         * 获取缓存启用状态
         * 
         * @return 是否启用缓存
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * 设置缓存启用状态
         * 
         * @param enabled 是否启用缓存
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * 获取缓存过期时间
         * 
         * @return 过期时间（毫秒）
         */
        public int getTtl() {
            return ttl;
        }

        /**
         * 设置缓存过期时间
         * 
         * @param ttl 过期时间（毫秒）
         */
        public void setTtl(int ttl) {
            this.ttl = ttl;
        }
    }
}