package com.example.devsupport.security.config;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderConfigTest {
    @Test
    @DisplayName("UT-AUTH-PASSWORD-001: 保存形式の確認")
    public void testPasswordEncoder() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder, "PasswordEncoderがnullでないこと");

        String actualEncodedPassword = passwordEncoder.encode("TestPass123!");

        assertNotNull(actualEncodedPassword, "エンコードされたパスワードがnullでないこと");
        assertTrue(actualEncodedPassword.startsWith("{bcrypt}"), "エンコード結果が`{bcrypt}`で始まること");
    }

    @Test
    @DisplayName("UT-AUTH-PASSWORD-002: 同じパスワードの照合成功")
    public void testPasswordMatching() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        String rawPassword = "TestPass123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "同じパスワードの照合が成功すること");
    }

    @Test
    @DisplayName("UT-AUTH-PASSWORD-003: 異なるパスワードの照合失敗")
    public void testDifferentPasswordMismatch() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        String rawPassword = "TestPass123!";
        String wrongPassword = "WrongPass456!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword), "異なるパスワードの照合が失敗すること");
    }
}
