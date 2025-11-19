#!/bin/bash

# 最终推送尝试脚本
echo "🚀 最终推送尝试..."

# 使用Gitee的noreply邮箱格式
echo "📧 配置Gitee noreply邮箱..."
git config user.email "yinglichina@users.noreply.gitee.com"
git config user.name "yinglichina"

# 重新提交所有更改
echo "🔄 重新提交更改..."
git commit --amend --reset-author --no-edit

# 尝试推送
echo "📤 推送到Gitee..."
git push -f origin main

if [ $? -eq 0 ]; then
    echo "✅ 推送成功!"
    echo "🎉 项目已成功同步到Gitee!"
    echo "仓库地址: https://gitee.com/yinglichina/gmpsystem"
else
    echo "❌ 推送仍然失败"
    echo ""
    echo "🔧 请手动尝试以下步骤："
    echo "1. 登录 https://gitee.com/yinglichina/gmpsystem"
    echo "2. 检查仓库设置和权限"
    echo "3. 确认邮箱设置是否正确"
    echo "4. 考虑重新创建仓库"
    echo ""
    echo "📋 或者创建新的仓库："
    echo "1. 访问 https://gitee.com/your-username/new"
    echo "2. 创建名为 'gmp-system' 的新仓库"
    echo "3. 不要初始化README（因为本地已有）"
    echo "4. 然后运行: git remote set-url origin https://gitee.com/您的用户名/gmp-system.git"
    echo "5. 最后推送: git push -u origin main"
fi