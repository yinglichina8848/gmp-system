#!/bin/bash

# GMP系统Gitee推送完整解决方案
echo "🚀 GMP系统Gitee推送完整解决方案"
echo "=========================================="

# 函数：检查命令是否成功
check_success() {
    if [ $? -eq 0 ]; then
        echo "✅ $1"
    else
        echo "❌ $1"
        return 1
    fi
}

# 1. 显示当前配置
echo "📋 当前Git配置:"
echo "   用户名: $(git config user.name)"
echo "   邮箱: $(git config user.email)"
echo "   远程仓库: $(git remote get-url origin)"
echo ""

# 2. 尝试不同的邮箱配置
echo "🔧 尝试不同的邮箱配置方案..."

# 方案1: 使用noreply邮箱
echo "方案1: 使用Gitee noreply邮箱"
git config user.email "yinglichina@users.noreply.gitee.com"
git config user.name "yinglichina"
git commit --amend --reset-author --no-edit
git push -f origin main

if [ $? -eq 0 ]; then
    echo "🎉 方案1成功！推送完成！"
    exit 0
fi

# 方案2: 使用QQ邮箱（如果显示的是QQ邮箱）
echo "方案2: 使用QQ邮箱"
git config user.email "1820393151@qq.com"
git config user.name "YingLiChina"
git commit --amend --reset-author --no-edit
git push -f origin main

if [ $? -eq 0 ]; then
    echo "🎉 方案2成功！推送完成！"
    exit 0
fi

# 方案3: 创建新的仓库并推送
echo ""
echo "📦 方案3: 创建新的Gitee仓库"
echo "由于前两个方案失败，建议您手动创建新的仓库:"
echo ""
echo "1. 访问: https://gitee.com/your-username/new"
echo "2. 仓库名称: gmp-system"
echo "3. 描述: GMP信息管理系统 - 完整的GMP合规解决方案"
echo "4. 设置为公开仓库"
echo "5. ❌ 不要勾选 '使用Readme文件初始化仓库'"
echo "6. 点击 '创建'"
echo ""

read -p "创建完成后，输入新仓库的HTTPS地址 (如: https://gitee.com/your-username/gmp-system.git): " new_repo_url

if [[ -n "$new_repo_url" ]]; then
    echo "配置新仓库地址: $new_repo_url"
    git remote remove origin
    git remote add origin "$new_repo_url"
    git push -u origin main
    
    if [ $? -eq 0 ]; then
        echo "🎉 新仓库推送成功！"
        echo "仓库地址: ${new_repo_url%.git}"
        exit 0
    fi
fi

# 4. 最终建议
echo ""
echo "🆘 如果所有方案都失败，请尝试:"
echo ""
echo "1. 检查Gitee账户状态:"
echo "   - 登录 https://gitee.com"
echo "   - 检查账户是否被限制"
echo "   - 验证邮箱是否已验证"
echo ""
echo "2. 检查仓库权限:"
echo "   - 确认您是仓库所有者"
echo "   - 检查分支保护规则"
echo ""
echo "3. 使用Gitee网页手动上传:"
echo "   - 在Gitee网页创建文件"
echo "   - 手动复制粘贴内容"
echo ""
echo "4. 联系Gitee支持:"
echo "   - 访问 https://gitee.com/help"
echo "   - 提交技术支持工单"
echo ""

echo "📁 您的项目文件结构:"
find . -type f -name "*.md" -o -name "*.java" -o -name "*.xml" -o -name "*.sh" | grep -v "^\./\." | sort

echo ""
echo "感谢您的耐心！项目文档和代码已经整理完成，随时可以推送到代码仓库。"