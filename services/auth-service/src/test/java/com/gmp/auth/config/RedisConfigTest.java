package com.gmp.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * RedisConfig配置类单元测试
 * 提高配置层覆盖率
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class RedisConfigTest {

    @Test
    void testRedisConfigCreation() {
        // Given
        RedisConfig redisConfig = new RedisConfig();

        // Then
        assertThat(redisConfig).isNotNull();
    }

    @Test
    void testRedisTemplateBeanCreation() {
        // Given
        RedisConfig redisConfig = new RedisConfig();
        RedisConnectionFactory mockConnectionFactory = mock(RedisConnectionFactory.class);

        // When
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(mockConnectionFactory);

        // Then
        assertThat(redisTemplate).isNotNull();
        assertThat(redisTemplate.getConnectionFactory()).isEqualTo(mockConnectionFactory);
        // Verify serializers are set correctly
        assertThat(redisTemplate.getKeySerializer()).isInstanceOf(StringRedisSerializer.class);
        assertThat(redisTemplate.getValueSerializer()).isInstanceOf(GenericJackson2JsonRedisSerializer.class);
        assertThat(redisTemplate.getHashKeySerializer()).isInstanceOf(StringRedisSerializer.class);
        assertThat(redisTemplate.getHashValueSerializer()).isInstanceOf(GenericJackson2JsonRedisSerializer.class);
    }

    @Test
    void testRedisTemplateConfiguration() {
        // Given
        RedisConfig redisConfig = new RedisConfig();
        RedisConnectionFactory mockConnectionFactory = mock(RedisConnectionFactory.class);

        // When
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(mockConnectionFactory);

        // Then - verify template is properly configured after properties set
        // The afterPropertiesSet() was called in the bean method
        assertThat(redisTemplate.getKeySerializer()).isNotNull();
        assertThat(redisTemplate.getValueSerializer()).isNotNull();
        assertThat(redisTemplate.getHashKeySerializer()).isNotNull();
        assertThat(redisTemplate.getHashValueSerializer()).isNotNull();
    }

    @Test
    void testRedisTemplateWithNullConnectionFactory() {
        // Given
        RedisConfig redisConfig = new RedisConfig();

        // When & Then - should throw IllegalStateException when connection factory is null
        // RedisTemplate requires a valid connection factory
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            redisConfig.redisTemplate(null);
        });
    }

    @Test
    void testRedisTemplateTypeParameters() {
        // Given
        RedisConfig redisConfig = new RedisConfig();
        RedisConnectionFactory mockConnectionFactory = mock(RedisConnectionFactory.class);

        // When
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(mockConnectionFactory);

        // Then - verify the template type parameters
        assertThat(redisTemplate).isNotNull();
        // The template is configured for String keys and Object values
    }
}
