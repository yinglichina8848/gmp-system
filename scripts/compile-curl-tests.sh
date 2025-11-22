#!/bin/bash

# GMP系统认证和权限管理页面功能CURL测试脚本
# 测试所有API端点和Web页面是否正常访问

set -e

echo "🚀 GMP系统认证和权限管理功能测试开始"

# 设置颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

# 基础配置
AUTH_BASE_URL="http://localhost:8081"
GATEWAY_BASE_URL="http://localhost:8080"
WEB_ROOT="."
PASS_COUNT=0
FAIL_COUNT=0
TOTAL_TESTS=0

# 统计函数
pass_test() {
    local test_name="$1"
    ((PASS_COUNT++))
    ((TOTAL_TESTS++))
    echo -e "${GREEN}✅ PASS${NC} - $test_name"
}

fail_test() {
    local test_name="$1"
    local error_msg="$2"
    ((FAIL_COUNT++))
    ((TOTAL_TESTS++))
    echo -e "${RED}❌ FAIL${NC} - $test_name"
    if [[ -n "$error_msg" ]]; then
        echo -e "${RED}   Error: $error_msg${NC}"
    fi
}

info() {
    echo -e "${BLUE}ℹ️ $1${NC}"
}

section_header() {
    echo
    echo -e "${PURPLE}🔍 $1${NC}"
    echo "================================================"
}

# 测试HTTP连接
test_connection() {
    section_header "测试基础连接"

    # 测试localhost连接
    info "测试localhost连接..."
    if curl -s --max-time 5 http://localhost:8080 > /dev/null 2>&1; then
        pass_test "Gateway服务连接"
    else
        fail_test "Gateway服务连接" "8080端口无响应"
    fi

    if curl -s --max-time 5 http://localhost:8081 > /dev/null 2>&1; then
        pass_test "Auth服务连接"
    else
        fail_test "Auth服务连接" "8081端口无响应"
    fi
}

# 测试认证API
test_auth_endpoints() {
    section_header "测试认证API端点"

    # 健康检查
    info "测试健康检查..."
    local health_response=$(curl -s -w "%{http_code}" http://localhost:8081/api/auth/health)
    local health_code="${health_response: -3}"
    if [[ "$health_code" == "200" ]]; then
        pass_test "健康检查 API"
    else
        fail_test "健康检查 API" "HTTP $health_code"
    fi

    # 测试用户登录
    info "测试用户登录..."
    local login_data='{"username":"admin@gmp.com","password":"Password123!"}'
    local login_response=$(curl -s -w "%{http_code}" -X POST \
        -H "Content-Type: application/json" \
        -d "$login_data" \
        http://localhost:8081/api/auth/login)
    local login_code="${login_response: -3}"

    if [[ "$login_code" == "200" ]]; then
        pass_test "用户登录 API"
        
        # 提取访问令牌
        ACCESS_TOKEN=$(echo "$login_response" | jq -r '.data.accessToken' 2>/dev/null || echo "")
        if [[ -n "$ACCESS_TOKEN" && "$ACCESS_TOKEN" != "null" ]]; then
            pass_test "JWT令牌提取"
            TEST_USER_TOKEN="$ACCESS_TOKEN"
        else
            fail_test "JWT令牌提取" "无法提取访问令牌"
        fi
    else
        fail_test "用户登录 API" "HTTP $login_code"
    fi

    # 测试错误的登录
    info "测试错误登录..."
    local wrong_login_data='{"username":"admin@gmp.com","password":"wrongpassword"}'
    local wrong_login_response=$(curl -s -w "%{http_code}" -X POST \
        -H "Content-Type: application/json" \
        -d "$wrong_login_data" \
        http://localhost:8081/api/auth/login)
    local wrong_login_code="${wrong_login_response: -3}"

    if [[ "$wrong_login_code" == "200" ]]; then
        local success_flag=$(echo "$wrong_login_response" | jq -r '.success' 2>/dev/null || echo "")
        if [[ "$success_flag" == "false" ]]; then
            pass_test "错误登录验证"
        else
            fail_test "错误登录验证" "应该返回失败但返回了成功"
        fi
    else
        fail_test "错误登录验证" "HTTP $wrong_login_code"
    fi
}

# 测试权限管理API
test_permission_endpoints() {
    section_header "测试权限管理API"

    if [[ -z "$TEST_USER_TOKEN" ]]; then
        info "跳过权限测试（无有效令牌）"
        return
    fi

    # 测试获取用户列表
    info "测试获取用户列表..."
    local users_response=$(curl -s -w "%{http_code}" \
        -H "Authorization: Bearer $TEST_USER_TOKEN" \
        http://localhost:8081/api/auth/users)
    local users_code="${users_response: -3}"

    if [[ "$users_code" == "200" ]]; then
        pass_test "获取用户列表 API"
    else
        fail_test "获取用户列表 API" "HTTP $users_code"
    fi

    # 测试权限检查
    info "测试权限检查..."
    local perm_response=$(curl -s -w "%{http_code}" \
        -H "Authorization: Bearer $TEST_USER_TOKEN" \
        "http://localhost:8081/api/auth/check/admin@gmp.com/permission?permission=READ_USER")
    local perm_code="${perm_response: -3}"

    if [[ "$perm_code" == "200" ]]; then
        pass_test "权限检查 API"
    else
        fail_test "权限检查 API" "HTTP $perm_code"
    fi

    # 测试角色检查
    info "测试角色检查..."
    local role_response=$(curl -s -w "%{http_code}" \
        -H "Authorization: Bearer $TEST_USER_TOKEN" \
        "http://localhost:8081/api/auth/check/admin@gmp.com/role?role=ADMIN")
    local role_code="${role_response: -3}"

    if [[ "$role_code" == "200" ]]; then
        pass_test "角色检查 API"
    else
        fail_test "角色检查 API" "HTTP $role_code"
    fi

    # 测试获取用户权限
    info "测试获取用户权限..."
    local user_perms_response=$(curl -s -w "%{http_code}" \
        -H "Authorization: Bearer $TEST_USER_TOKEN" \
        http://localhost:8081/api/auth/permissions/admin@gmp.com)
    local user_perms_code="${user_perms_response: -3}"

    if [[ "$user_perms_code" == "200" ]]; then
        pass_test "获取用户权限 API"
    else
        fail_test "获取用户权限 API" "HTTP $user_perms_code"
    fi

    # 测试用户登出
    info "测试用户登出..."
    local logout_response=$(curl -s -w "%{http_code}" -X POST \
        -H "Authorization: Bearer $TEST_USER_TOKEN" \
        http://localhost:8081/api/auth/logout)
    local logout_code="${logout_response: -3}"

    if [[ "$logout_code" == "200" ]]; then
        pass_test "用户登出 API"
    else
        fail_test "用户登出 API" "HTTP $logout_code"
    fi
}

# 测试Web页面访问
test_web_pages() {
    section_header "测试Web页面访问"

    # 测试主页
    info "测试主页..."
    local home_response=$(curl -s -w "%{http_code}" "file://$(pwd)/index.html")
    local home_code="${home_response: -3}"

    if [[ "$home_code" == "200" ]]; then
        pass_test "主页页面访问"
        
        # 检查主页是否包含必要元素
        local home_content=$(cat index.html 2>/dev/null)
        if [[ "$home_content" =~ "GMP信息管理系统" ]]; then
            pass_test "主页内容完整性"
        else
            fail_test "主页内容完整性" "缺少必要标题"
        fi
    else
        fail_test "主页页面访问" "HTTP $home_code"
    fi

    # 测试用户管理页面
    info "测试用户权限管理页面..."
    local user_mgmt_response=$(curl -s -w "%{http_code}" "file://$(pwd)/user-management.html")
    local user_mgmt_code="${user_mgmt_response: -3}"

    if [[ "$user_mgmt_code" == "200" ]]; then
        pass_test "用户管理页面访问"
        
        # 检查用户管理页面是否包含必要元素
        local user_mgmt_content=$(cat user-management.html 2>/dev/null)
        if [[ "$user_mgmt_content" =~ "用户权限管理" ]]; then
            pass_test "用户管理页面内容完整性"
        else
            fail_test "用户管理页面内容完整性" "缺少必要标题"
        fi
    else
        fail_test "用户管理页面访问" "HTTP $user_mgmt_code"
    fi

    # 测试文档页面
    info "测试测试使用指南..."
    local guide_response=$(curl -s -w "%{http_code}" "file://$(pwd)/docs/GMP系统测试和使用指南.md")
    local guide_code="${guide_response: -3}"

    if [[ "$guide_code" == "200" ]]; then
        pass_test "测试使用指南访问"
    else
        fail_test "测试使用指南访问" "HTTP $guide_code"
    fi
}

# 测试测试脚本
test_test_scripts() {
    section_header "测试脚本功能"

    # 检查集成测试脚本
    info "检查集成测试脚本..."
    if [[ -x "./scripts/run-integration-tests.sh" ]]; then
        pass_test "集成测试脚本存在且可执行"
        
        # 测试脚本帮助功能
        local help_output=$(./scripts/run-integration-tests.sh help 2>/dev/null || echo "")
        if [[ "$help_output" =~ "GMP系统认证集成测试脚本" ]]; then
            pass_test "集成测试脚本帮助功能"
        else
            fail_test "集成测试脚本帮助功能" "帮助输出异常"
        fi
    else
        fail_test "集成测试脚本存在且可执行" "脚本不存在或不可执行"
    fi

    # 检查覆盖率脚本
    info "检查覆盖率脚本..."
    if [[ -x "./scripts/generate-all-coverage.sh" ]]; then
        pass_test "覆盖率脚本存在且可执行"
    else
        fail_test "覆盖率脚本存在且可执行" "脚本不存在或不可执行"
    fi

    # 检查启动脚本
    info "检查启动脚本..."
    if [[ -x "./scripts/start-dev.sh" ]]; then
        pass_test "启动脚本存在且可执行"
    else
        fail_test "启动脚本存在且可执行" "脚本不存在或不可执行"
    fi
}

# 显示测试报告
show_test_report() {
    section_header "测试结果汇总"

    echo -e "${WHITE}总测试数: ${TOTAL_TESTS}${NC}"
    echo -e "${GREEN}通过: ${PASS_COUNT}${NC}"
    echo -e "${RED}失败: ${FAIL_COUNT}${NC}"
    
    if [[ $PASS_COUNT -gt 0 && $FAIL_COUNT -eq 0 ]]; then
        echo
        echo -e "${GREEN}🎉 所有测试通过！GMP认证和权限管理功能正常！${NC}"
        return 0
    elif [[ $PASS_COUNT -gt 0 && $FAIL_COUNT -gt 0 ]]; then
        echo
        echo -e "${YELLOW}⚠️ 部分测试失败，但核心功能基本可用${NC}"
        return 1
    else
        echo
        echo -e "${RED}❌ 多个测试失败，请检查系统配置${NC}"
        return 2
    fi
}

# 显示帮助信息
show_help() {
    cat << EOF
GMP系统认证和权限管理功能CURL测试脚本

用法:
    $0 [选项]

选项:
    all         运行所有测试 (默认)
    connection  只测试基础连接
    auth        只测试认证API
    permission  只测试权限管理API
    web         只测试Web页面
    scripts     只测试脚本功能
    help        显示此帮助信息

测试内容:
    🔗 基础服务连接
    🎫 用户登录验证
    🔐 权限管理功能
    🌐 Web页面访问
    🔧 脚本工具可用性

环境要求:
    ☕ 各服务运行在默认端口
    📝 jq工具 (用于JSON解析)
    🔗 curl工具

示例:
    # 运行完整测试套件
    $0

    # 只测试认证功能
    $0 auth

    # 只测试Web页面
    $0 web
EOF
}

# 主函数
main() {
    case "${1:-all}" in
        "all")
            test_connection
            test_auth_endpoints
            test_permission_endpoints
            test_web_pages
            test_test_scripts
            show_test_report
            ;;
        "connection")
            test_connection
            ;;
        "auth")
            test_auth_endpoints
            ;;
        "permission")
            test_permission_endpoints
            ;;
        "web")
            test_web_pages
            ;;
        "scripts")
            test_test_scripts
            ;;
        "help"|"-h"|"--help")
            show_help
            exit 0
            ;;
        *)
            echo -e "${RED}无效选项: $1${NC}"
            echo "使用 '$0 help' 查看帮助信息"
            exit 1
            ;;
    esac
}

# 检查jq是否安装
if ! command -v jq &> /dev/null; then
    echo -e "${YELLOW}⚠️ jq未安装，部分JSON解析功能可能受限${NC}"
fi

# 执行主函数
main "$@"
