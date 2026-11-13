package com.example.devsupport.security.principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginUserDetailsTest {
    @Test
    @DisplayName("UT-AUTH-DETAILS-001: ID・email・passwordHashの取得検証")
    void getDetails() {
        Long id = 101L;
        String email = "user101@example.com";
        String passwordHash = "dummy-hash-for-unit-test";
        String systemRoleCode = "USER";

        // 準備した値をコンストラクタへ渡し、LoginUserDetailsの実物を生成する
        LoginUserDetails userDetails = new LoginUserDetails(id, email, passwordHash,
                systemRoleCode);

        // 各フィールドの値がの戻り値が準備した値と一致することを検証する
        assertEquals(id, userDetails.getId(), "IDの取得が正しいこと");
        assertEquals(email, userDetails.getUsername(), "emailの取得が正しいこと");
        assertEquals(passwordHash, userDetails.getPassword(), "passwordHashの取得が正しいこと");
    }

    @Test
    @DisplayName("UT-AUTH-DETAILS-002: USERの権限生成検証")
    void getAuthorities() {
        Long id = 101L;
        String email = "user101@example.com";
        String passwordHash = "dummy-hash-for-unit-test";
        String systemRoleCode = "USER";

        LoginUserDetails userDetails = new LoginUserDetails(id, email, passwordHash,
                systemRoleCode);

        assertNotNull(userDetails.getAuthorities(), "USERの権限がnullでないこと");
        assertEquals(1, userDetails.getAuthorities().size(), "USERの権限が1つであること");
        assertEquals("USER", userDetails.getAuthorities().iterator().next().getAuthority(), "USERの権限が正しいこと");
    }

    @Test
    @DisplayName("UT-AUTH-DETAILS-003: SYSTEM_ADMINの権限生成検証")
    void getAuthoritiesForSystemAdmin() {
        Long id = 102L;
        String email = "admin102@example.com";
        String passwordHash = "dummy-hash-for-unit-test";
        String systemRoleCode = "SYSTEM_ADMIN";

        LoginUserDetails userDetails = new LoginUserDetails(id, email, passwordHash,
                systemRoleCode);

        assertNotNull(userDetails.getAuthorities(), "SYSTEM_ADMINの権限がnullでないこと");
        assertEquals(1, userDetails.getAuthorities().size(), "SYSTEM_ADMINの権限が1つであること");
        assertEquals("SYSTEM_ADMIN", userDetails.getAuthorities().iterator().next().getAuthority(),
                "SYSTEM_ADMINの権限が正しいこと");
    }
}
