#!/bin/bash

# GMP系统Gitee仓库配置脚本
# 这个脚本用于配置本地Git仓库与Gitee远程仓库的连接

echo "🚀 开始配置Gitee远程仓库..."

# 检查是否已经配置了远程仓库
if git remote get-url origin >/dev/null 2>&1; then
    echo "⚠️  已经配置了远程仓库: $(git remote get-url origin)"
    echo "是否要更新为新的Gitee仓库？ (y/n)"
    read -r response
    if [[ "$response" != "y" && "$response" != "Y" ]]; then
        echo "❌ 取消配置"
        exit 0
    fi
    # 删除现有远程仓库
    git remote remove origin
fi

# 提示用户输入Gitee仓库URL
echo "请输入您的Gitee仓库URL (例如: https://gitee.com/your-username/gmp-system.git):"
read -r gitee_url

# 验证URL格式
if [[ ! "$gitee_url" =~ ^https://gitee\.com/.*\.git$ ]]; then
    echo "❌ 无效的Gitee仓库URL格式"
    echo "正确的格式应该是: https://gitee.com/your-username/repository-name.git"
    exit 1
fi

# 添加远程仓库
echo "添加远程仓库: $gitee_url"
git remote add origin "$gitee_url"

# 验证远程仓库配置
if git remote get-url origin >/dev/null 2>&1; then
    echo "✅ 远程仓库配置成功!"
    echo "远程仓库URL: $(git remote get-url origin)"
else
    echo "❌ 远程仓库配置失败"
    exit 1
fi

# 推送代码到Gitee
echo "📤 推送代码到Gitee..."
git push -u origin main

if [ $? -eq 0 ]; then
    echo "✅ 代码推送成功!"
    echo "🎉 Gitee仓库配置完成!"
    echo "您可以在Gitee上查看您的项目: ${gitee_url%.git}"
else
    echo "❌ 代码推送失败，请检查:"
    echo "1. 网络连接是否正常"
    echo "2. Gitee仓库是否存在"
    echo "3. 是否有推送权限"
    echo "4. 用户名和密码是否正确"
fi

echo "🔧 常用Git命令:"
echo "  查看远程仓库: git remote -v"
echo "  推送代码: git push origin main"
echo "  拉取代码: git pull origin main"
echo "  查看状态: git status"