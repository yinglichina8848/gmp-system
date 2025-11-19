#!/bin/bash

# GMP系统 - 推送到新仓库脚本
# 解决Gitee邮箱隐私保护导致的推送失败问题

echo "🎯 GMP系统 - 推送到新仓库解决方案"
echo "=================================="

# 显示当前状态
echo "📊 当前Git状态："
git status
echo ""

# 显示提交历史
echo "📜 最近提交历史："
git log --oneline -5
echo ""

# 获取用户输入
echo "📝 请输入您的新Gitee仓库信息："
read -p "Gitee用户名: " gitee_username
read -p "新仓库名称 (建议使用: gmp-system-new): " repo_name

# 构建新仓库地址
new_url="https://gitee.com/${gitee_username}/${repo_name}.git"

echo ""
echo "🔗 新仓库地址: ${new_url}"
echo ""

# 确认操作
read -p "确认要推送到新仓库吗? (y/n): " confirm

if [[ $confirm == "y" || $confirm == "Y" ]]; then
    echo "🚀 开始推送操作..."
    
    # 删除旧的远程仓库
    echo "📤 删除旧远程仓库..."
    git remote remove origin
    
    # 添加新远程仓库
    echo "📥 添加新远程仓库..."
    git remote add origin "${new_url}"
    
    # 验证远程仓库
    echo "🔍 验证远程仓库配置："
    git remote -v
    echo ""
    
    # 尝试推送
    echo "📤 推送到新仓库..."
    if git push -u origin main; then
        echo ""
        echo "🎉 推送成功！"
        echo "✅ 您的项目已成功推送到: ${new_url}"
        echo "🌐 访问地址: https://gitee.com/${gitee_username}/${repo_name}"
    else
        echo ""
        echo "❌ 推送失败，请检查："
        echo "   1. 新仓库是否已在Gitee创建"
        echo "   2. 仓库地址是否正确"
        echo "   3. 您的Gitee账户状态"
        echo ""
        echo "💡 建议："
        echo "   1. 先在Gitee手动创建仓库"
        echo "   2. 确保仓库是空的（不要初始化）"
        echo "   3. 重新运行此脚本"
    fi
else
    echo "❌ 操作已取消"
fi

echo ""
echo "🔧 如果需要重新配置，请运行："
echo "   ./scripts/push-to-new-repo.sh"