package com.gmp.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SecurityConfig单元测试
 *
 * @author GMP系统开发团队
 */
@ActiveProfiles("test")
class SecurityConfigTest {

    @Test
    void testSecurityConfigBeanCreation() {
        // Given
        SecurityConfig config = new SecurityConfig();

        // When
        PasswordEncoder encoder = config.passwordEncoder();

        // Then
        assertThat(encoder).isNotNull();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    void testPasswordEncoderFunctionality() {
        // Given
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        String rawPassword = "testPassword123";

        // When
        String encodedPassword = encoder.encode(rawPassword);

        // Then
        assertThat(encodedPassword).isNotNull();
        assertThat(encodedPassword).isNotEmpty();
        assertThat(encodedPassword).doesNotContain(rawPassword); // Should be hashed
        assertThat(encoder.matches(rawPassword, encodedPassword)).isTrue();
        assertThat(encoder.matches("wrongPassword", encodedPassword)).isFalse();
    }

    @Test
    void testPasswordEncoderConsistency() {
        // Given
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder1 = config.passwordEncoder();
        PasswordEncoder encoder2 = config.passwordEncoder();

        String password = "consistenPassword";

        // When
        String encoded1 = encoder1.encode(password);
        String encoded2 = encoder2.encode(password);

        // Then - Same encoder should be able to verify different encodings of same password
        assertThat(encoder1.matches(password, encoded1)).isTrue();
        assertThat(encoder1.matches(password, encoded2)).isTrue();
        assertThat(encoder2.matches(password, encoded1)).isTrue();
        assertThat(encoder2.matches(password, encoded2)).isTrue();
    }

    @Test
    void testPasswordEncoderStrength() {
        // Given
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        String password = "weak";

        // When - encode multiple times
        String encoded1 = encoder.encode(password);
        String encoded2 = encoder.encode(password);

        // Then - Encodings should be different (salt)
        assertThat(encoded1).isNotEqualTo(encoded2);

        // But both should match the original password
        assertThat(encoder.matches(password, encoded1)).isTrue();
        assertThat(encoder.matches(password, encoded2)).isTrue();
    }
}
