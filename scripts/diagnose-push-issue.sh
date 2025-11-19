#!/bin/bash

# Gitee推送问题诊断脚本
echo "🔍 诊断Gitee推送问题..."

# 1. 检查Git配置
echo "📋 1. Git配置信息:"
echo "   用户名: $(git config user.name)"
echo "   邮箱: $(git config user.email)"
echo ""

# 2. 检查远程仓库
echo "🔗 2. 远程仓库配置:"
git remote -v
echo ""

# 3. 检查分支状态
echo "🌿 3. 分支状态:"
echo "   当前分支: $(git branch --show-current)"
echo "   所有分支:"
git branch -a
echo ""

# 4. 检查提交历史
echo "📜 4. 最近提交历史:"
git log --oneline -5
echo ""

# 5. 检查仓库状态
echo "📊 5. 仓库状态:"
git status
echo ""

# 6. 尝试测试推送（不实际推送）
echo "🧪 6. 测试推送（dry-run）:"
git push --dry-run origin main 2>&1 | head -10
echo ""

# 7. 检查Gitee仓库是否存在
echo "🌐 7. 检查Gitee仓库状态:"
repo_url="https://gitee.com/yinglichina/gmpsystem"
if curl -s -o /dev/null -w "%{http_code}" "$repo_url" | grep -q "200\|302"; then
    echo "   ✅ Gitee仓库存在并可访问"
    echo "   仓库地址: $repo_url"
else
    echo "   ❌ Gitee仓库可能不存在或无法访问"
    echo "   请检查: $repo_url"
fi
echo ""

# 8. 提供解决方案
echo "💡 8. 建议的解决方案:"
echo "   a) 检查Gitee账户邮箱设置:"
echo "      - 登录 https://gitee.com"
echo "      - 进入个人设置 -> 邮箱管理"
echo "      - 确保有公开的已验证邮箱"
echo ""
echo "   b) 检查仓库权限:"
echo "      - 确认您是仓库所有者"
echo "      - 检查是否有推送权限"
echo ""
echo "   c) 尝试重新创建仓库:"
echo "      - 删除当前Gitee仓库"
echo "      - 创建同名新仓库"
echo "      - 不要初始化README文件"
echo ""
echo "   d) 使用不同的推送方式:"
echo "      - HTTPS: https://gitee.com/username/repo.git"
echo "      - SSH: git@gitee.com:username/repo.git"
echo ""

echo "📞 如果问题仍然存在，请联系Gitee支持或尝试创建新的仓库。"