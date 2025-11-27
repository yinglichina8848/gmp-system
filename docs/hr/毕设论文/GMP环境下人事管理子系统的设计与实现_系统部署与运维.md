# GMP环境下人事管理子系统的设计与实现
## 系统部署与运维

## 📋 文档信息

| 属性 | 值 |
|------|---|
| 文档标题 | GMP环境下人事管理子系统的设计与实现_系统部署与运维 |
| 版本号 | v0.1.0-draft |
| 创建日期 | 2025年11月21日 |
| 更新日期 | 2025年11月21日 |
| 作者 | 毕业设计团队 |
| 状态 | 草稿 |

## 1️⃣ 部署架构概述

GMP环境下人事管理子系统采用了现代化的微服务架构，结合容器化技术实现灵活高效的部署。部署架构设计充分考虑了GMP合规性要求、系统安全性、高可用性和可扩展性，确保系统在生产环境中稳定运行并满足制药企业的特殊监管需求。

### 1.1 整体部署架构

系统的整体部署架构采用了分层设计，包括：

1. **前端接入层**：由Nginx反向代理和负载均衡器组成，负责用户请求的接收和分发
2. **API网关层**：处理请求路由、认证授权、限流熔断等横切关注点
3. **微服务层**：各个业务微服务独立部署，实现功能解耦
4. **数据持久层**：包括MySQL数据库和Redis缓存系统
5. **基础设施层**：提供容器编排、监控日志、备份恢复等基础服务

### 1.2 微服务部署架构

微服务层采用了多实例部署模式，每个微服务至少部署两个实例，确保高可用性。部署架构图如下所示：

```
                                          ┌─────────────────┐
                                          │  客户端浏览器   │
                                          └────────┬────────┘
                                                   │ HTTPS
                                                   ▼
┌───────────────────────────────────────────────────────────────────┐
│                        防火墙/安全组                                │
└───────────────────────────────────────────────────────────────────┘
                                   │
                                   ▼
┌───────────────────────────────────────────────────────────────────┐
│                        Nginx 负载均衡器                            │
└─────────────────┬─────────────────────────────────┬───────────────┘
                  │                                 │
         ┌────────▼───────────┐            ┌────────▼───────────┐
         │  API网关实例 1     │            │  API网关实例 2     │
         └────────┬───────────┘            └────────┬───────────┘
                  │                                 │
         ┌────────▼───────────┐            ┌────────▼───────────┐
         │   配置中心         │            │   服务注册发现     │
         └────────────────────┘            └────────────────────┘
                  │                                 │
┌─────────────────┼─────────────────────────────────┼───────────────┐
│                 │                                 │               │
▼                 ▼                                 ▼               ▼
┌─────────┐ ┌────────────────┐  ┌────────────────┐  ┌────────────────┐
│认证服务 │ │人事基础服务实例1│  │考勤服务实例1   │  │培训服务实例1   │
└─────────┘ └────────────────┘  └────────────────┘  └────────────────┘
                                    │                      │
                                    ▼                      ▼
                           ┌────────────────┐  ┌────────────────┐
                           │考勤服务实例2   │  │培训服务实例2   │
                           └────────────────┘  └────────────────┘
                                    │                      │
                                    ▼                      ▼
┌───────────────────────────────────────────────────────────────────┐
│                        共享存储/文件服务                            │
└───────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────┐  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐
│MySQL主数据库   │──│MySQL从数据库   │  │Redis主节点     │──│Redis从节点     │
└────────────────┘  └────────────────┘  └────────────────┘  └────────────────┘
                                    │
                                    ▼
┌───────────────────────────────────────────────────────────────────┐
│                        监控/日志/审计系统                           │
└───────────────────────────────────────────────────────────────────┘
```

### 1.3 高可用设计

系统的高可用设计体现在以下几个方面：

1. **服务冗余**：核心服务至少部署两个实例，避免单点故障
2. **负载均衡**：使用Nginx和API网关实现请求的负载均衡
3. **数据库主从复制**：MySQL采用主从复制架构，提高数据可用性
4. **Redis集群**：Redis采用主从架构，确保缓存服务的高可用性
5. **自动故障转移**：容器编排平台自动检测并替换故障实例
6. **数据备份与恢复**：定期备份关键数据，确保数据安全

### 1.4 安全性设计

系统的安全性设计符合GMP环境的严格要求：

1. **网络隔离**：通过VLAN和安全组实现网络隔离
2. **HTTPS传输**：所有通信均采用HTTPS加密
3. **访问控制**：基于RBAC的细粒度权限控制
4. **数据加密**：敏感数据存储加密
5. **审计日志**：完整记录所有关键操作
6. **入侵检测**：部署入侵检测系统，实时监控安全威胁

## 2️⃣ 部署环境准备

### 2.1 硬件环境要求

系统部署的硬件环境要求如下表所示：

| 服务器类型 | 最低配置 | 推荐配置 | 用途 |
|------------|----------|----------|------|
| 应用服务器 | 4核CPU、8GB内存、200GB存储 | 8核CPU、16GB内存、500GB SSD | 部署微服务应用 |
| 数据库服务器 | 8核CPU、16GB内存、500GB存储 | 16核CPU、32GB内存、1TB SSD | 运行MySQL数据库 |
| 缓存服务器 | 2核CPU、4GB内存、100GB存储 | 4核CPU、8GB内存、200GB SSD | 运行Redis缓存 |
| 监控服务器 | 4核CPU、8GB内存、200GB存储 | 8核CPU、16GB内存、500GB SSD | 运行监控和日志系统 |
| 负载均衡服务器 | 4核CPU、8GB内存、100GB存储 | 8核CPU、16GB内存、200GB SSD | 运行Nginx和API网关 |

### 2.2 软件环境要求

系统部署的软件环境要求如下表所示：

| 软件类型 | 版本 | 用途 |
|----------|------|------|
| 操作系统 | CentOS 7.9 / Ubuntu 20.04 LTS | 服务器操作系统 |
| Docker | 20.10+ | 容器化平台 |
| Kubernetes | 1.23+ | 容器编排平台 |
| MySQL | 8.0.30+ | 关系型数据库 |
| Redis | 7.0+ | 缓存系统 |
| Nginx | 1.20+ | Web服务器和负载均衡器 |
| Java | OpenJDK 17 | Java运行环境 |
| Node.js | 16.14+ | 前端构建环境 |
| Git | 2.30+ | 版本控制系统 |
| Jenkins | 2.361+ | CI/CD平台 |
| Prometheus | 2.37+ | 监控系统 |
| Grafana | 9.0+ | 可视化监控平台 |
| ELK Stack | 8.0+ | 日志收集和分析 |

### 2.3 网络环境配置

系统部署的网络环境配置要求如下：

1. **IP地址规划**：为每个服务器分配固定IP地址
2. **端口配置**：开放必要的服务端口，关闭不必要的端口
3. **网络安全**：配置防火墙规则，限制访问来源
4. **DNS配置**：设置域名解析，便于访问系统
5. **负载均衡**：配置负载均衡策略，分发用户请求

### 2.4 存储环境配置

系统部署的存储环境配置要求如下：

1. **数据存储**：配置高性能存储设备，用于数据库和文件存储
2. **备份存储**：配置独立的备份存储设备，用于数据备份
3. **共享存储**：配置NFS或Ceph等共享存储，用于文件共享
4. **存储分区**：合理规划磁盘分区，优化存储性能

## 3️⃣ 部署步骤

### 3.1 基础环境搭建

#### 3.1.1 操作系统安装与配置

1. **安装操作系统**：安装CentOS 7.9或Ubuntu 20.04 LTS
2. **系统更新**：更新系统到最新版本
3. **安全加固**：
   - 禁用不必要的服务
   - 配置防火墙
   - 设置SSH密钥认证
   - 禁用root远程登录

#### 3.1.2 容器化环境部署

1. **安装Docker**：
   ```bash
   # CentOS安装Docker
   sudo yum install -y yum-utils device-mapper-persistent-data lvm2
   sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
   sudo yum install docker-ce docker-ce-cli containerd.io
   sudo systemctl start docker
   sudo systemctl enable docker
   ```

2. **安装Kubernetes**：
   ```bash
   # 安装kubeadm, kubelet和kubectl
   sudo swapoff -a
   sudo yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes
   sudo systemctl enable --now kubelet
   ```

3. **初始化Kubernetes集群**：
   ```bash
   sudo kubeadm init --pod-network-cidr=10.244.0.0/16
   mkdir -p $HOME/.kube
   sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
   sudo chown $(id -u):$(id -g) $HOME/.kube/config
   ```

4. **安装网络插件**：
   ```bash
   kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
   ```

### 3.2 数据库部署

#### 3.2.1 MySQL主从架构部署

1. **创建MySQL配置文件**：
   ```yaml
   # mysql-master.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: mysql-master
   spec:
     replicas: 1
     selector:
       matchLabels:
         app: mysql
         role: master
     template:
       metadata:
         labels:
           app: mysql
           role: master
       spec:
         containers:
         - name: mysql
           image: mysql:8.0.30
           ports:
           - containerPort: 3306
           env:
           - name: MYSQL_ROOT_PASSWORD
             value: "root_password"
           - name: MYSQL_DATABASE
             value: "hr_system"
           volumeMounts:
           - name: mysql-data
             mountPath: /var/lib/mysql
         volumes:
         - name: mysql-data
           persistentVolumeClaim:
             claimName: mysql-pvc
   ```

2. **部署MySQL主节点**：
   ```bash
   kubectl apply -f mysql-master.yaml
   ```

3. **部署MySQL从节点**：
   ```bash
   kubectl apply -f mysql-slave.yaml
   ```

4. **配置主从复制**：
   ```bash
   # 在主节点执行
   mysql -u root -p
   CREATE USER 'repl'@'%' IDENTIFIED BY 'repl_password';
   GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
   FLUSH PRIVILEGES;
   SHOW MASTER STATUS;
   
   # 在从节点执行
   mysql -u root -p
   CHANGE MASTER TO MASTER_HOST='mysql-master', MASTER_USER='repl', MASTER_PASSWORD='repl_password', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=156;
   START SLAVE;
   SHOW SLAVE STATUS\G
   ```

#### 3.2.2 Redis集群部署

1. **创建Redis配置文件**：
   ```yaml
   # redis-master.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: redis-master
   spec:
     replicas: 1
     selector:
       matchLabels:
         app: redis
         role: master
     template:
       metadata:
         labels:
           app: redis
           role: master
       spec:
         containers:
         - name: redis
           image: redis:7.0
           ports:
           - containerPort: 6379
           command:
           - redis-server
           - --requirepass
           - "redis_password"
   ```

2. **部署Redis主从集群**：
   ```bash
   kubectl apply -f redis-master.yaml
   kubectl apply -f redis-slave.yaml
   ```

### 3.3 应用服务部署

#### 3.3.1 前端服务部署

1. **构建前端镜像**：
   ```bash
   cd /path/to/frontend
   docker build -t hr-frontend:latest .
   ```

2. **部署前端服务**：
   ```yaml
   # frontend.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: hr-frontend
   spec:
     replicas: 2
     selector:
       matchLabels:
         app: hr-frontend
     template:
       metadata:
         labels:
           app: hr-frontend
       spec:
         containers:
         - name: hr-frontend
           image: hr-frontend:latest
           ports:
           - containerPort: 80
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: hr-frontend
   spec:
     selector:
       app: hr-frontend
     ports:
     - port: 80
       targetPort: 80
     type: ClusterIP
   ```

3. **应用部署**：
   ```bash
   kubectl apply -f frontend.yaml
   ```

#### 3.3.2 微服务部署

以身份认证服务为例，其他微服务部署类似：

1. **构建微服务镜像**：
   ```bash
   cd /path/to/auth-service
   mvn clean package docker:build -Ddocker.image.prefix=hr-system
   ```

2. **部署微服务**：
   ```yaml
   # auth-service.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: auth-service
   spec:
     replicas: 2
     selector:
       matchLabels:
         app: auth-service
     template:
       metadata:
         labels:
           app: auth-service
       spec:
         containers:
         - name: auth-service
           image: hr-system/auth-service:latest
           ports:
           - containerPort: 8080
           env:
           - name: SPRING_PROFILES_ACTIVE
             value: "prod"
           - name: SPRING_DATASOURCE_URL
             value: "jdbc:mysql://mysql-master:3306/hr_system?useSSL=false"
           - name: SPRING_DATASOURCE_USERNAME
             value: "root"
           - name: SPRING_DATASOURCE_PASSWORD
             value: "root_password"
           - name: SPRING_REDIS_HOST
             value: "redis-master"
           - name: SPRING_REDIS_PASSWORD
             value: "redis_password"
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: auth-service
   spec:
     selector:
       app: auth-service
     ports:
     - port: 8080
       targetPort: 8080
     type: ClusterIP
   ```

3. **应用部署**：
   ```bash
   kubectl apply -f auth-service.yaml
   ```

4. **依次部署其他微服务**：
   ```bash
   kubectl apply -f hr-base-service.yaml
   kubectl apply -f attendance-service.yaml
   kubectl apply -f training-service.yaml
   kubectl apply -f certificate-service.yaml
   kubectl apply -f gmp-compliance-service.yaml
   ```

### 3.4 中间件部署

#### 3.4.1 API网关部署

1. **部署API网关**：
   ```yaml
   # api-gateway.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: api-gateway
   spec:
     replicas: 2
     selector:
       matchLabels:
         app: api-gateway
     template:
       metadata:
         labels:
           app: api-gateway
       spec:
         containers:
         - name: api-gateway
           image: hr-system/api-gateway:latest
           ports:
           - containerPort: 8080
           env:
           - name: SPRING_PROFILES_ACTIVE
             value: "prod"
           - name: SPRING_CLOUD_CONFIG_URI
             value: "http://config-server:8888"
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: api-gateway
   spec:
     selector:
       app: api-gateway
     ports:
     - port: 8080
       targetPort: 8080
     type: ClusterIP
   ```

2. **应用部署**：
   ```bash
   kubectl apply -f api-gateway.yaml
   ```

#### 3.4.2 配置中心部署

1. **部署配置中心**：
   ```yaml
   # config-server.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: config-server
   spec:
     replicas: 1
     selector:
       matchLabels:
         app: config-server
     template:
       metadata:
         labels:
           app: config-server
       spec:
         containers:
         - name: config-server
           image: hr-system/config-server:latest
           ports:
           - containerPort: 8888
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: config-server
   spec:
     selector:
       app: config-server
     ports:
     - port: 8888
       targetPort: 8888
     type: ClusterIP
   ```

2. **应用部署**：
   ```bash
   kubectl apply -f config-server.yaml
   ```

#### 3.4.3 服务注册发现部署

1. **部署服务注册发现**：
   ```yaml
   # eureka-server.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: eureka-server
   spec:
     replicas: 1
     selector:
       matchLabels:
         app: eureka-server
     template:
       metadata:
         labels:
           app: eureka-server
       spec:
         containers:
         - name: eureka-server
           image: hr-system/eureka-server:latest
           ports:
           - containerPort: 8761
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: eureka-server
   spec:
     selector:
       app: eureka-server
     ports:
     - port: 8761
       targetPort: 8761
     type: ClusterIP
   ```

2. **应用部署**：
   ```bash
   kubectl apply -f eureka-server.yaml
   ```

### 3.5 负载均衡配置

1. **部署Nginx负载均衡器**：
   ```yaml
   # nginx-ingress.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: nginx-ingress
   spec:
     replicas: 2
     selector:
       matchLabels:
         app: nginx-ingress
     template:
       metadata:
         labels:
           app: nginx-ingress
       spec:
         containers:
         - name: nginx
           image: nginx:1.20
           ports:
           - containerPort: 80
           - containerPort: 443
           volumeMounts:
           - name: nginx-conf
             mountPath: /etc/nginx/conf.d/
           - name: ssl-certs
             mountPath: /etc/nginx/ssl/
         volumes:
         - name: nginx-conf
           configMap:
             name: nginx-conf
         - name: ssl-certs
           secret:
             secretName: ssl-certs
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: nginx-ingress
   spec:
     selector:
       app: nginx-ingress
     ports:
     - port: 80
       targetPort: 80
       nodePort: 30080
     - port: 443
       targetPort: 443
       nodePort: 30443
     type: NodePort
   ```

2. **创建Nginx配置**：
   ```yaml
   # nginx-conf.yaml
   apiVersion: v1
   kind: ConfigMap
   metadata:
     name: nginx-conf
   data:
     default.conf: |
       upstream frontend {
         server hr-frontend:80;
       }
       
       upstream api {
         server api-gateway:8080;
       }
       
       server {
         listen 80;
         server_name hr-system.example.com;
         
         # 重定向到HTTPS
         return 301 https://$host$request_uri;
       }
       
       server {
         listen 443 ssl;
         server_name hr-system.example.com;
         
         ssl_certificate /etc/nginx/ssl/tls.crt;
         ssl_certificate_key /etc/nginx/ssl/tls.key;
         
         location / {
           proxy_pass http://frontend;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           proxy_set_header X-Forwarded-Proto $scheme;
         }
         
         location /api/ {
           proxy_pass http://api/;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           proxy_set_header X-Forwarded-Proto $scheme;
         }
       }
   ```

3. **应用配置**：
   ```bash
   kubectl apply -f nginx-conf.yaml
   kubectl apply -f nginx-ingress.yaml
   ```

## 4️⃣ 系统初始化与配置

### 4.1 数据库初始化

1. **执行数据库初始化脚本**：
   ```bash
   kubectl exec -it $(kubectl get pods -l app=mysql,role=master -o jsonpath="{.items[0].metadata.name}") -- mysql -u root -p hr_system < /path/to/init.sql
   ```

2. **初始化基础数据**：
   ```bash
   kubectl exec -it $(kubectl get pods -l app=hr-base-service -o jsonpath="{.items[0].metadata.name}") -- curl -X POST http://localhost:8080/api/v1/system/init
   ```

### 4.2 系统参数配置

1. **配置系统参数**：
   ```bash
   # 更新配置中心的系统参数
   kubectl exec -it $(kubectl get pods -l app=config-server -o jsonpath="{.items[0].metadata.name}") -- curl -X POST http://localhost:8888/actuator/refresh
   ```

2. **配置GMP合规参数**：
   - 证书有效期提醒天数
   - 培训周期要求
   - 审计日志保留期限
   - 敏感操作二次验证配置

### 4.3 用户与权限配置

1. **创建初始管理员用户**：
   ```bash
   kubectl exec -it $(kubectl get pods -l app=auth-service -o jsonpath="{.items[0].metadata.name}") -- curl -X POST http://localhost:8080/api/v1/auth/init-admin -H "Content-Type: application/json" -d '{"username":"admin","password":"Admin123!","email":"admin@example.com"}'
   ```

2. **配置系统角色和权限**：
   ```bash
   kubectl exec -it $(kubectl get pods -l app=auth-service -o jsonpath="{.items[0].metadata.name}") -- curl -X POST http://localhost:8080/api/v1/role/init-roles
   ```

## 5️⃣ 系统运维管理

### 5.1 监控系统

#### 5.1.1 Prometheus监控部署

1. **部署Prometheus**：
   ```yaml
   # prometheus.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: prometheus
   spec:
     replicas: 1
     selector:
       matchLabels:
         app: prometheus
     template:
       metadata:
         labels:
           app: prometheus
       spec:
         containers:
         - name: prometheus
           image: prom/prometheus:v2.37.0
           ports:
           - containerPort: 9090
           volumeMounts:
           - name: prometheus-config
             mountPath: /etc/prometheus/
           - name: prometheus-data
             mountPath: /prometheus/
         volumes:
         - name: prometheus-config
           configMap:
             name: prometheus-config
         - name: prometheus-data
           persistentVolumeClaim:
             claimName: prometheus-pvc
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: prometheus
   spec:
     selector:
       app: prometheus
     ports:
     - port: 9090
       targetPort: 9090
     type: ClusterIP
   ```

2. **创建Prometheus配置**：
   ```yaml
   # prometheus-config.yaml
   apiVersion: v1
   kind: ConfigMap
   metadata:
     name: prometheus-config
   data:
     prometheus.yml: |
       global:
         scrape_interval: 15s
       
       scrape_configs:
         - job_name: 'kubernetes-pods'
           kubernetes_sd_configs:
             - role: pod
           relabel_configs:
             - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_scrape]
               action: keep
               regex: true
             - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_path]
               action: replace
               target_label: __metrics_path__
               regex: (.+)
             - source_labels: [__address__, __meta_kubernetes_pod_annotation_prometheus_io_port]
               action: replace
               regex: ([^:]+)(?::\d+)?;(\d+)
               replacement: $1:$2
               target_label: __address__
   ```

3. **应用部署**：
   ```bash
   kubectl apply -f prometheus-config.yaml
   kubectl apply -f prometheus.yaml
   ```

#### 5.1.2 Grafana可视化部署

1. **部署Grafana**：
   ```yaml
   # grafana.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: grafana
   spec:
     replicas: 1
     selector:
       matchLabels:
         app: grafana
     template:
       metadata:
         labels:
           app: grafana
       spec:
         containers:
         - name: grafana
           image: grafana/grafana:9.0.0
           ports:
           - containerPort: 3000
           volumeMounts:
           - name: grafana-data
             mountPath: /var/lib/grafana/
         volumes:
         - name: grafana-data
           persistentVolumeClaim:
             claimName: grafana-pvc
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: grafana
   spec:
     selector:
       app: grafana
     ports:
     - port: 3000
       targetPort: 3000
     type: ClusterIP
   ```

2. **应用部署**：
   ```bash
   kubectl apply -f grafana.yaml
   ```

3. **配置监控面板**：
   - 访问Grafana界面（http://grafana:3000）
   - 添加Prometheus数据源
   - 导入系统监控面板
   - 配置告警规则

### 5.2 日志管理

#### 5.2.1 ELK Stack部署

1. **部署Elasticsearch**：
   ```yaml
   # elasticsearch.yaml
   apiVersion: apps/v1
   kind: StatefulSet
   metadata:
     name: elasticsearch
   spec:
     serviceName: elasticsearch
     replicas: 1
     selector:
       matchLabels:
         app: elasticsearch
     template:
       metadata:
         labels:
           app: elasticsearch
       spec:
         containers:
         - name: elasticsearch
           image: docker.elastic.co/elasticsearch/elasticsearch:8.0.0
           ports:
           - containerPort: 9200
           - containerPort: 9300
           env:
           - name: discovery.type
             value: single-node
           - name: ES_JAVA_OPTS
             value: -Xms1g -Xmx1g
           volumeMounts:
           - name: elasticsearch-data
             mountPath: /usr/share/elasticsearch/data
     volumeClaimTemplates:
     - metadata:
         name: elasticsearch-data
       spec:
         accessModes: ["ReadWriteOnce"]
         resources:
           requests:
             storage: 50Gi
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: elasticsearch
   spec:
     selector:
       app: elasticsearch
     ports:
     - port: 9200
       targetPort: 9200
     - port: 9300
       targetPort: 9300
     clusterIP: None
   ```

2. **部署Logstash**：
   ```yaml
   # logstash.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: logstash
   spec:
     replicas: 1
     selector:
       matchLabels:
         app: logstash
     template:
       metadata:
         labels:
           app: logstash
       spec:
         containers:
         - name: logstash
           image: docker.elastic.co/logstash/logstash:8.0.0
           ports:
           - containerPort: 5044
           volumeMounts:
           - name: logstash-config
             mountPath: /usr/share/logstash/pipeline/
         volumes:
         - name: logstash-config
           configMap:
             name: logstash-config
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: logstash
   spec:
     selector:
       app: logstash
     ports:
     - port: 5044
       targetPort: 5044
     type: ClusterIP
   ```

3. **部署Kibana**：
   ```yaml
   # kibana.yaml
   apiVersion: apps/v1
   kind: Deployment
   metadata:
     name: kibana
   spec:
     replicas: 1
     selector:
       matchLabels:
         app: kibana
     template:
       metadata:
         labels:
           app: kibana
       spec:
         containers:
         - name: kibana
           image: docker.elastic.co/kibana/kibana:8.0.0
           ports:
           - containerPort: 5601
           env:
           - name: ELASTICSEARCH_HOSTS
             value: http://elasticsearch:9200
   ---
   apiVersion: v1
   kind: Service
   metadata:
     name: kibana
   spec:
     selector:
       app: kibana
     ports:
     - port: 5601
       targetPort: 5601
     type: ClusterIP
   ```

4. **应用部署**：
   ```bash
   kubectl apply -f elasticsearch.yaml
   kubectl apply -f logstash-config.yaml
   kubectl apply -f logstash.yaml
   kubectl apply -f kibana.yaml
   ```

#### 5.2.2 日志收集配置

1. **部署Fluentd作为日志收集器**：
   ```yaml
   # fluentd.yaml
   apiVersion: apps/v1
   kind: DaemonSet
   metadata:
     name: fluentd
   spec:
     selector:
       matchLabels:
         app: fluentd
     template:
       metadata:
         labels:
           app: fluentd
       spec:
         containers:
         - name: fluentd
           image: fluent/fluentd-kubernetes-daemonset:v1-debian-elasticsearch
           volumeMounts:
           - name: varlog
             mountPath: /var/log
           - name: varlibdockercontainers
             mountPath: /var/lib/docker/containers
             readOnly: true
           env:
           - name: FLUENT_ELASTICSEARCH_HOST
             value: elasticsearch
           - name: FLUENT_ELASTICSEARCH_PORT
             value: "9200"
         volumes:
         - name: varlog
           hostPath:
             path: /var/log
         - name: varlibdockercontainers
           hostPath:
             path: /var/lib/docker/containers
   ```

2. **应用部署**：
   ```bash
   kubectl apply -f fluentd.yaml
   ```

3. **配置日志索引和可视化**：
   - 访问Kibana界面（http://kibana:5601）
   - 创建索引模式
   - 配置日志可视化面板
   - 设置日志告警

### 5.3 备份与恢复

#### 5.3.1 数据库备份策略

1. **配置定期备份**：
   ```yaml
   # mysql-backup-cronjob.yaml
   apiVersion: batch/v1
   kind: CronJob
   metadata:
     name: mysql-backup
   spec:
     schedule: "0 2 * * *"  # 每天凌晨2点执行
     jobTemplate:
       spec:
         template:
           spec:
             containers:
             - name: mysql-backup
               image: mysql:8.0.30
               command:
               - sh
               - -c
               - mysqldump -h mysql-master -u root -p${MYSQL_ROOT_PASSWORD} --all-databases | gzip > /backup/hr_system_$(date +%Y%m%d_%H%M%S).sql.gz
               env:
               - name: MYSQL_ROOT_PASSWORD
                 valueFrom:
                   secretKeyRef:
                     name: mysql-secrets
                     key: root-password
               volumeMounts:
               - name: backup-storage
                 mountPath: /backup
             restartPolicy: OnFailure
             volumes:
             - name: backup-storage
               persistentVolumeClaim:
                 claimName: backup-pvc
   ```

2. **应用部署**：
   ```bash
   kubectl apply -f mysql-backup-cronjob.yaml
   ```

#### 5.3.2 数据恢复流程

1. **查看备份文件**：
   ```bash
   kubectl exec -it $(kubectl get pods -l app=mysql-backup -o jsonpath="{.items[0].metadata.name}") -- ls -la /backup/
   ```

2. **执行数据恢复**：
   ```bash
   kubectl exec -it $(kubectl get pods -l app=mysql-backup -o jsonpath="{.items[0].metadata.name}") -- bash -c "gunzip -c /backup/hr_system_YYYYMMDD_HHMMSS.sql.gz | mysql -h mysql-master -u root -p${MYSQL_ROOT_PASSWORD}"
   ```

### 5.4 安全管理

#### 5.4.1 GMP合规性维护

1. **定期安全审计**：
   - 每月执行一次安全审计
   - 检查审计日志的完整性
   - 验证权限配置是否符合GMP要求

2. **定期安全更新**：
   - 及时更新系统补丁
   - 定期更新依赖包
   - 定期进行漏洞扫描

3. **GMP文档维护**：
   - 维护系统操作手册
   - 更新标准操作程序（SOP）
   - 记录系统变更历史

#### 5.4.2 应急响应

1. **制定应急响应计划**：
   - 系统故障应急响应
   - 安全事件应急响应
   - 数据泄露应急响应

2. **应急演练**：
   - 定期进行应急演练
   - 测试恢复流程的有效性
   - 持续改进应急响应计划

## 6️⃣ 系统升级与维护

### 6.1 升级流程

1. **升级准备**：
   - 备份当前系统数据和配置
   - 测试升级包在测试环境的兼容性
   - 制定详细的升级计划

2. **升级执行**：
   ```bash
   # 更新应用镜像
   kubectl set image deployment/auth-service auth-service=hr-system/auth-service:v1.1.0
   kubectl set image deployment/hr-base-service hr-base-service=hr-system/hr-base-service:v1.1.0
   # 其他服务类似更新
   ```

3. **升级验证**：
   - 验证系统功能是否正常
   - 检查性能指标是否符合要求
   - 确认数据完整性

### 6.2 日常维护任务

1. **定期检查**：
   - 检查系统运行状态
   - 监控资源使用情况
   - 查看错误日志

2. **性能优化**：
   - 优化数据库查询
   - 调整JVM参数
   - 优化缓存策略

3. **清理任务**：
   - 清理过期日志
   - 清理临时文件
   - 清理无效数据

## 7️⃣ 运维自动化

### 7.1 CI/CD流程

1. **Jenkins Pipeline配置**：
   ```groovy
   pipeline {
     agent any
     stages {
       stage('Build') {
         steps {
           sh 'mvn clean package'
         }
       }
       stage('Build Docker Image') {
         steps {
           sh 'docker build -t hr-system/${JOB_NAME}:${BUILD_NUMBER} .'
         }
       }
       stage('Push Docker Image') {
         steps {
           sh 'docker push hr-system/${JOB_NAME}:${BUILD_NUMBER}'
         }
       }
       stage('Deploy to Kubernetes') {
         steps {
           sh 'kubectl set image deployment/${JOB_NAME} ${JOB_NAME}=hr-system/${JOB_NAME}:${BUILD_NUMBER}'
         }
       }
       stage('Run Tests') {
         steps {
           sh 'mvn test'
         }
       }
     }
   }
   ```

2. **自动化部署流程**：
   - 代码提交触发构建
   - 自动构建、测试和打包
   - 自动部署到测试环境
   - 自动执行集成测试
   - 手动审核后部署到生产环境

### 7.2 监控自动化

1. **自动化告警**：
   - 配置基于阈值的告警规则
   - 配置趋势分析告警
   - 配置多渠道告警通知（邮件、短信、企业微信等）

2. **自动化扩缩容**：
   ```yaml
   # hpa.yaml
   apiVersion: autoscaling/v2
   kind: HorizontalPodAutoscaler
   metadata:
     name: api-gateway
   spec:
     scaleTargetRef:
       apiVersion: apps/v1
       kind: Deployment
       name: api-gateway
     minReplicas: 2
     maxReplicas: 10
     metrics:
     - type: Resource
       resource:
         name: cpu
         target:
           type: Utilization
           averageUtilization: 70
     - type: Resource
       resource:
         name: memory
         target:
           type: Utilization
           averageUtilization: 80
   ```

3. **应用部署**：
   ```bash
   kubectl apply -f hpa.yaml
   ```

## 8️⃣ 系统运维最佳实践

### 8.1 GMP环境下的特殊要求

1. **合规性文档管理**：
   - 维护完整的系统文档
   - 记录所有系统变更
   - 定期进行合规性审核

2. **权限管理最佳实践**：
   - 遵循最小权限原则
   - 定期审查用户权限
   - 实施职责分离原则

3. **数据完整性保障**：
   - 实施数据备份与恢复策略
   - 确保数据不可篡改性
   - 维护完整的审计跟踪

### 8.2 性能优化最佳实践

1. **数据库优化**：
   - 定期分析和优化查询
   - 合理使用索引
   - 定期清理和归档数据

2. **缓存优化**：
   - 合理设置缓存策略
   - 监控缓存命中率
   - 避免缓存雪崩和缓存穿透

3. **JVM优化**：
   - 调整GC策略
   - 监控内存使用情况
   - 避免内存泄漏

### 8.3 安全最佳实践

1. **密码策略**：
   - 强制复杂密码
   - 定期密码更新
   - 实施账户锁定机制

2. **访问控制**：
   - 实施网络隔离
   - 配置防火墙规则
   - 定期进行安全扫描

3. **数据安全**：
   - 加密敏感数据
   - 实施数据脱敏
   - 控制数据访问权限

---

*文档版本：v0.1.0-draft*
*审核状态：待审核*
*下次更新：根据实际部署情况调整*