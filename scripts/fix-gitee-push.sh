#!/bin/bash

# Gitee推送问题修复脚本
echo "🔧 修复Gitee推送问题..."

# 检查当前Git配置
echo "📋 当前Git配置:"
echo "用户名: $(git config user.name)"
echo "邮箱: $(git config user.email)"

# 如果邮箱为空或包含noreply，建议设置公开邮箱
if [[ -z "$(git config user.email)" ]] || [[ "$(git config user.email)" == *"noreply"* ]]; then
    echo "⚠️  检测到邮箱设置问题"
    echo "请设置您的公开邮箱地址:"
    read -r email
    git config user.email "$email"
fi

# 重新配置提交信息，使用公开邮箱
echo "🔄 重新配置提交作者信息..."
git config user.name "${USER:-GMPSystem}"

# 修改最近的提交，使用新的作者信息
echo "📝 更新提交作者信息..."
git commit --amend --reset-author --no-edit

# 尝试推送到Gitee
echo "📤 重新推送到Gitee..."
git push -f origin main

if [ $? -eq 0 ]; then
    echo "✅ 推送成功!"
else
    echo "❌ 推送仍然失败，请尝试以下方法:"
    echo "1. 登录Gitee，进入个人设置 -> 邮箱管理"
    echo "2. 确保您有一个公开的邮箱地址"
    echo "3. 在Gitee仓库设置中检查是否有分支保护规则"
    echo "4. 检查是否有权限推送到此仓库"
    echo ""
    echo "或者尝试使用SSH方式推送:"
    echo "git remote set-url origin git@gitee.com:yinglichina/gmpsystem.git"
fi