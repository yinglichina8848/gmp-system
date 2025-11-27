package com.gmp.edms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 文件服务基础测试 - 验证Spring上下文能否正常启动
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FileServiceBasicTest {

    @Test
    public void contextLoads() {
        // 测试Spring上下文是否能正常加载
        // 如果能运行到这里，说明基本的配置和依赖是正确的
        System.out.println("EDMS服务上下文加载成功 - 文件服务基础架构正常");
    }
}