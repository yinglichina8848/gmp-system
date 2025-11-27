#!/bin/bash

# EDMS服务File服务整合验证脚本
# 用于验证File服务功能是否已成功整合到EDMS服务中

set -e

echo "🚀 开始EDMS服务File服务整合验证..."
echo "=================================="

# 配置变量
EDMS_SERVICE_URL="http://localhost:8085/edms"
MINIO_URL="http://localhost:9000"
TEST_FILE_CONTENT="这是一个测试文件内容，用于验证File服务整合功能。"
TEST_FILE_NAME="integration-test.txt"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查服务是否运行
check_service() {
    local service_name=$1
    local service_url=$2
    
    log_info "检查 $service_name 服务是否运行..."
    
    if curl -s -f "$service_url/actuator/health" > /dev/null 2>&1; then
        log_info "$service_name 服务运行正常 ✅"
        return 0
    else
        log_error "$service_name 服务未运行或无法访问 ❌"
        return 1
    fi
}

# 检查MinIO连接
check_minio() {
    log_info "检查MinIO连接..."
    
    if curl -s -f "$MINIO_URL/minio/health/live" > /dev/null 2>&1; then
        log_info "MinIO服务连接正常 ✅"
        return 0
    else
        log_error "MinIO服务连接失败 ❌"
        return 1
    fi
}

# 测试文件上传功能
test_file_upload() {
    log_info "测试文件上传功能..."
    
    # 创建临时测试文件
    echo "$TEST_FILE_CONTENT" > /tmp/$TEST_FILE_NAME
    
    # 上传文件到EDMS服务
    local upload_response=$(curl -s -X POST \
        -F "file=@/tmp/$TEST_FILE_NAME" \
        -F "module=integration-test" \
        -F "metadata={\"test\":\"integration\"}" \
        "$EDMS_SERVICE_URL/api/v1/files/upload")
    
    if echo "$upload_response" | grep -q '"success":true\|id'; then
        local file_id=$(echo "$upload_response" | grep -o '"id":[0-9]*' | cut -d':' -f2)
        log_info "文件上传成功，文件ID: $file_id ✅"
        echo "$file_id" > /tmp/uploaded_file_id
        return 0
    else
        log_error "文件上传失败 ❌"
        echo "$upload_response"
        return 1
    fi
}

# 测试文件下载功能
test_file_download() {
    log_info "测试文件下载功能..."
    
    if [ ! -f /tmp/uploaded_file_id ]; then
        log_error "未找到上传的文件ID ❌"
        return 1
    fi
    
    local file_id=$(cat /tmp/uploaded_file_id)
    
    # 下载文件
    curl -s -o "/tmp/downloaded-$TEST_FILE_NAME" \
        "$EDMS_SERVICE_URL/api/v1/files/$file_id/download"
    
    if [ -f "/tmp/downloaded-$TEST_FILE_NAME" ]; then
        local downloaded_content=$(cat "/tmp/downloaded-$TEST_FILE_NAME")
        if [ "$downloaded_content" = "$TEST_FILE_CONTENT" ]; then
            log_info "文件下载成功，内容验证通过 ✅"
            return 0
        else
            log_error "下载的文件内容不匹配 ❌"
            return 1
        fi
    else
        log_error "文件下载失败 ❌"
        return 1
    fi
}

# 测试文件信息查询
test_file_info() {
    log_info "测试文件信息查询功能..."
    
    if [ ! -f /tmp/uploaded_file_id ]; then
        log_error "未找到上传的文件ID ❌"
        return 1
    fi
    
    local file_id=$(cat /tmp/uploaded_file_id)
    
    # 查询文件信息
    local info_response=$(curl -s "$EDMS_SERVICE_URL/api/v1/files/$file_id")
    
    if echo "$info_response" | grep -q '"fileName":"'$TEST_FILE_NAME'"'; then
        log_info "文件信息查询成功 ✅"
        return 0
    else
        log_error "文件信息查询失败 ❌"
        echo "$info_response"
        return 1
    fi
}

# 测试兼容层API
test_compatibility_api() {
    log_info "测试File服务兼容层API..."
    
    # 创建临时测试文件
    echo "$TEST_FILE_CONTENT" > /tmp/compatibility-test.txt
    
    # 使用兼容层API上传文件
    local compat_response=$(curl -s -X POST \
        -F "file=@/tmp/compatibility-test.txt" \
        -F "type=compatibility-test" \
        "$EDMS_SERVICE_URL/api/v1/file-service/files")
    
    if echo "$compat_response" | grep -q '"id"\|"fileName"'; then
        log_info "兼容层API测试成功 ✅"
        return 0
    else
        log_error "兼容层API测试失败 ❌"
        echo "$compat_response"
        return 1
    fi
}

# 测试文件统计功能
test_file_statistics() {
    log_info "测试文件统计功能..."
    
    local stats_response=$(curl -s "$EDMS_SERVICE_URL/api/v1/files/statistics")
    
    if echo "$stats_response" | grep -q '"countByModule"\|"sizeByModule"'; then
        log_info "文件统计功能正常 ✅"
        return 0
    else
        log_error "文件统计功能异常 ❌"
        echo "$stats_response"
        return 1
    fi
}

# 清理测试数据
cleanup() {
    log_info "清理测试数据..."
    
    # 删除临时文件
    rm -f /tmp/$TEST_FILE_NAME
    rm -f /tmp/downloaded-$TEST_FILE_NAME
    rm -f /tmp/compatibility-test.txt
    rm -f /tmp/uploaded_file_id
    
    log_info "测试数据清理完成 ✅"
}

# 主验证流程
main() {
    log_info "开始EDMS服务File服务整合验证流程..."
    
    local failed_tests=0
    local total_tests=0
    
    # 检查服务状态
    ((total_tests++))
    if ! check_service "EDMS" "$EDMS_SERVICE_URL"; then
        ((failed_tests++))
    fi
    
    ((total_tests++))
    if ! check_minio; then
        ((failed_tests++))
    fi
    
    # 功能测试
    ((total_tests++))
    if ! test_file_upload; then
        ((failed_tests++))
    fi
    
    ((total_tests++))
    if ! test_file_download; then
        ((failed_tests++))
    fi
    
    ((total_tests++))
    if ! test_file_info; then
        ((failed_tests++))
    fi
    
    ((total_tests++))
    if ! test_compatibility_api; then
        ((failed_tests++))
    fi
    
    ((total_tests++))
    if ! test_file_statistics; then
        ((failed_tests++))
    fi
    
    # 清理测试数据
    cleanup
    
    # 输出验证结果
    echo "=================================="
    log_info "验证流程完成！"
    echo "总测试数: $total_tests"
    echo "失败测试数: $failed_tests"
    echo "成功测试数: $((total_tests - failed_tests))"
    
    if [ $failed_tests -eq 0 ]; then
        log_info "🎉 所有测试通过！File服务已成功整合到EDMS服务中！"
        exit 0
    else
        log_error "❌ 有 $failed_tests 个测试失败，请检查相关配置和服务状态。"
        exit 1
    fi
}

# 脚本入口
if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    echo "EDMS服务File服务整合验证脚本"
    echo ""
    echo "用法: $0"
    echo ""
    echo "说明:"
    echo "  此脚本用于验证File服务功能是否已成功整合到EDMS服务中。"
    echo "  脚本会自动测试文件上传、下载、查询、兼容层API等功能。"
    echo ""
    echo "环境要求:"
    echo "  - EDMS服务运行在 http://localhost:8085/edms"
    echo "  - MinIO服务运行在 http://localhost:9000"
    echo "  - curl命令可用"
    echo ""
    exit 0
fi

# 捕获退出信号，确保清理
trap cleanup EXIT

# 执行主流程
main