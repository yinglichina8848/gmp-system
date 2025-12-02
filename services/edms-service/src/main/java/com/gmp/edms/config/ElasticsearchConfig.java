package com.gmp.edms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.gmp.edms.repository")
public class ElasticsearchConfig {
    // Spring Boot 3.x中，Spring Data Elasticsearch会自动配置Elasticsearch客户端
    // 不需要显式配置RestHighLevelClient
}

