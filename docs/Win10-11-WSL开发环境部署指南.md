# Win10/11 + WSL开发环境部署指南

## 📋 系统要求

- Windows 10 版本 2004 或更高版本（内部版本 19041 或更高）
- Windows 11（推荐）
- 至少 16GB RAM（推荐）
- 至少 50GB 可用磁盘空间
- Internet 连接

## 🛠️ WSL 安装和配置

### 1. 启用 WSL
以管理员身份打开 PowerShell 并运行：
```powershell
# 启用 WSL 功能
dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart

# 启用虚拟机功能
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
```

重启计算机。

### 2. 更新 WSL 内核
下载并安装最新的 Linux 内核更新包：
1. 访问 https://aka.ms/wsl2kernel
2. 下载并运行安装包

### 3. 设置 WSL 2 为默认版本
在 PowerShell 中运行：
```powershell
wsl --set-default-version 2
```

### 4. 安装 Ubuntu 发行版
```powershell
# 安装 Ubuntu
wsl --install -d Ubuntu

# 或者从 Microsoft Store 安装 Ubuntu
```

### 5. 初始化 Ubuntu
首次启动 Ubuntu 时，系统会要求设置用户名和密码：
```bash
# 设置用户名和密码（按提示操作）
# 例如：
# Enter new UNIX username: your_username
# Enter new UNIX password: your_password
# Retype new UNIX password: your_password
```

## 🛠️ 开发工具安装（在 WSL Ubuntu 中）

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

# 设置 JAVA_HOME 环境变量
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
source ~/.bashrc
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

### 6. 安装 Docker（WSL 2 中）
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
sudo service postgresql start

# 验证安装
sudo -u postgres psql -c "SELECT version();"
```

### 8. 安装 Redis
```bash
# 安装 Redis
sudo apt install -y redis-server

# 启动 Redis 服务
sudo service redis-server start

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

## 🖥️ Windows 端工具配置

### 1. 安装 Visual Studio Code
1. 访问 https://code.visualstudio.com/
2. 下载并安装适用于 Windows 的 VS Code

### 2. 安装 WSL 扩展
在 VS Code 中：
1. 打开扩展面板（Ctrl+Shift+X）
2. 搜索 "Remote - WSL"
3. 安装 "Remote - WSL" 扩展

### 3. 在 WSL 中使用 VS Code
```bash
# 在项目目录中运行
cd /path/to/gmp-system
code .
```

这将自动在 VS Code 中打开项目，并连接到 WSL 环境。

### 4. 安装 Windows Terminal（可选）
1. 从 Microsoft Store 搜索并安装 "Windows Terminal"
2. 可以更方便地管理 WSL 和其他命令行工具

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
sudo docker compose up -d

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

### 1. WSL 相关问题
```bash
# 检查 WSL 版本
wsl -l -v

# 如果需要将发行版设置为 WSL 2
wsl --set-version <distribution name> 2

# 重启 WSL
wsl --shutdown
```

### 2. 权限问题
如果遇到权限问题，请确保：
```bash
# 确保当前用户在 docker 组中
groups $USER

# 如果不在 docker 组中，添加用户到 docker 组
sudo usermod -aG docker $USER
```

### 3. 端口访问问题
在 Windows 上访问 WSL 中运行的服务：
```bash
# WSL 2 中的服务默认可以通过 localhost 访问
# 例如，如果在 WSL 中运行服务在 8080 端口
# 在 Windows 浏览器中访问 http://localhost:8080
```

### 4. 文件系统性能问题
为获得更好的性能，建议：
1. 将项目代码放在 WSL 文件系统中（/home/username/...）而不是 Windows 文件系统（/mnt/c/...）
2. 使用 VS Code 的 Remote - WSL 扩展进行开发

### 5. 内存不足
如果遇到内存不足问题：
```bash
# 在 WSL 中增加交换空间
sudo fallocate -l 2G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile

# 永久启用交换空间
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
```

## 📈 性能优化建议

### 1. WSL 配置优化
创建或编辑 `/etc/wsl.conf` 文件：
```bash
sudo nano /etc/wsl.conf
```

添加以下内容：
```ini
[boot]
command="sysctl -w vm.swappiness=1"

[automount]
enabled = true
options = "metadata,uid=1000,gid=1000,umask=022"
```

### 2. JVM 调优
```bash
# 在启动应用时添加 JVM 参数
export JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC"
```

## 🛡️ 安全建议

### 1. 修改默认密码
```bash
# 修改数据库密码
sudo -u postgres psql
\password gmp_user
\q
```

### 2. 防火墙配置
在 Windows 中配置防火墙：
1. 打开 Windows Defender 防火墙
2. 允许 WSL 通过防火墙
3. 根据需要配置端口规则

## 🔄 更新和维护

### 1. 系统更新
```bash
# 更新 WSL 内核
# 访问 https://aka.ms/wsl2kernel 并下载最新版本

# 更新 Ubuntu 系统
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
sudo docker compose down
sudo docker compose up -d
```

## 📞 技术支持

### 文档维护
- **最后更新**：2025年11月23日
- **维护团队**：GMP系统项目组

### 问题反馈
- **技术问题**：提交GitHub Issue或联系技术负责人
- **环境配置问题**：参考本文档或联系运维团队

---