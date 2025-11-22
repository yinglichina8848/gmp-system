# Ubuntu开发环境部署指南

## 📋 系统要求

- Ubuntu 20.04 LTS 或更高版本
- 至少 8GB RAM（推荐 16GB）
- 至少 50GB 可用磁盘空间
- Internet 连接

## 🛠️ 开发工具安装

### 1. 系统更新
```bash
sudo apt update && sudo apt upgrade -y
```

### 2. 安装基础工具
```bash
sudo apt install -y curl wget git vim nano build-essential software-properties-common
```

### 3. 安装 Java Development Kit (JDK)
```bash
# 安装 OpenJDK 17
sudo apt install -y openjdk-17-jdk

# 验证安装
java -version
javac -version
```

### 4. 安装 Maven
```bash
# 安装 Maven
sudo apt install -y maven

# 验证安装
mvn -version
```

### 5. 安装 Node.js 和 npm
```bash
# 安装 NodeSource 仓库
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -

# 安装 Node.js
sudo apt-get install -y nodejs

# 验证安装
node -v
npm -v
```

### 6. 安装 Docker
```bash
# 卸载旧版本（如果存在）
sudo apt remove -y docker docker-engine docker.io containerd runc

# 安装必要工具
sudo apt update
sudo apt install -y ca-certificates curl gnupg lsb-release

# 添加 Docker 官方 GPG 密钥
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# 设置仓库
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# 更新 apt 包索引
sudo apt update

# 安装 Docker Engine
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# 验证安装
sudo docker --version
sudo docker compose version

# 将当前用户添加到 docker 组
sudo usermod -aG docker $USER

# 注意：需要重新登录或执行以下命令使组更改生效
newgrp docker
```

### 7. 安装 PostgreSQL
```bash
# 安装 PostgreSQL
sudo apt install -y postgresql postgresql-contrib

# 启动并启用 PostgreSQL 服务
sudo systemctl start postgresql
sudo systemctl enable postgresql

# 验证安装
sudo -u postgres psql -c "SELECT version();"
```

### 8. 安装 Redis
```bash
# 安装 Redis
sudo apt install -y redis-server

# 启动并启用 Redis 服务
sudo systemctl start redis-server
sudo systemctl enable redis-server

# 验证安装
redis-cli ping
```

## 🗃️ 项目配置

### 1. 克隆项目代码
```bash
git clone <项目仓库地址>
cd gmp-system
```

### 2. 配置数据库
```bash
# 切换到 postgres 用户
sudo -u postgres psql

# 创建数据库用户
CREATE USER gmp_user WITH PASSWORD 'gmp_password';

# 创建数据库
CREATE DATABASE gmp_db OWNER gmp_user;

# 授权
GRANT ALL PRIVILEGES ON DATABASE gmp_db TO gmp_user;

# 退出
\q
```

### 3. 配置环境变量
```bash
# 创建环境变量文件
cat > ~/.gmp_env << EOF
# 数据库配置
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=gmp_db
export DB_USER=gmp_user
export DB_PASSWORD=gmp_password

# Redis 配置
export REDIS_HOST=localhost
export REDIS_PORT=6379

# JWT 配置
export JWT_SECRET=your_jwt_secret_key_here

# 其他配置
export SERVER_PORT=8080
EOF

# 将环境变量添加到 shell 配置文件
echo "source ~/.gmp_env" >> ~/.bashrc
source ~/.bashrc
```

## 🚀 项目启动

### 1. 构建项目
```bash
# 进入项目根目录
cd gmp-system

# 清理并编译项目
mvn clean compile

# 运行测试
mvn test

# 打包项目
mvn package
```

### 2. 启动服务
```bash
# 使用 Docker Compose 启动所有服务（推荐）
docker-compose up -d

# 或者单独启动各个服务
# 启动认证服务
mvn spring-boot:run -pl services/auth-service

# 启动网关服务
mvn spring-boot:run -pl services/gateway
```

### 3. 验证服务
```bash
# 检查服务是否正常运行
curl http://localhost:8080/actuator/health

# 检查认证服务
curl http://localhost:8081/actuator/health
```

## 🔧 常见问题解决

### 1. 权限问题
如果遇到权限问题，请确保：
```bash
# 确保当前用户在 docker 组中
groups $USER

# 如果不在 docker 组中，添加用户到 docker 组
sudo usermod -aG docker $USER
```

### 2. 端口占用
如果端口被占用，可以更改配置：
```bash
# 查看端口占用情况
sudo netstat -tulpn | grep :8080

# 或者在 application.yml 中修改端口配置
```

### 3. 数据库连接问题
如果数据库连接失败，请检查：
```bash
# 检查 PostgreSQL 服务状态
sudo systemctl status postgresql

# 检查防火墙设置
sudo ufw status

# 验证数据库连接
psql -h localhost -p 5432 -U gmp_user -d gmp_db
```

### 4. 内存不足
如果遇到内存不足问题：
```bash
# 增加交换空间
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile

# 永久启用交换空间
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
```

## 📈 性能优化建议

### 1. JVM 调优
```bash
# 在启动应用时添加 JVM 参数
export JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC"
```

### 2. Docker 资源限制
在 docker-compose.yml 中配置资源限制：
```yaml
services:
  auth-service:
    # ...
    deploy:
      resources:
        limits:
          memory: 1G
          cpus: '0.5'
```

## 🛡️ 安全建议

### 1. 修改默认密码
```bash
# 修改数据库密码
sudo -u postgres psql
\password gmp_user
\q
```

### 2. 配置防火墙
```bash
# 启用 UFW 防火墙
sudo ufw enable

# 允许 SSH 连接
sudo ufw allow ssh

# 允许 Web 服务端口
sudo ufw allow 8080

# 查看防火墙状态
sudo ufw status
```

## 🔄 更新和维护

### 1. 系统更新
```bash
# 定期更新系统
sudo apt update && sudo apt upgrade -y

# 清理不需要的包
sudo apt autoremove -y
```

### 2. 项目更新
```bash
# 拉取最新代码
git pull origin main

# 重新构建项目
mvn clean package

# 重启服务
docker-compose down
docker-compose up -d
```

## 📞 技术支持

### 文档维护
- **最后更新**：2025年11月23日
- **维护团队**：GMP系统项目组

### 问题反馈
- **技术问题**：提交GitHub Issue或联系技术负责人
- **环境配置问题**：参考本文档或联系运维团队

---