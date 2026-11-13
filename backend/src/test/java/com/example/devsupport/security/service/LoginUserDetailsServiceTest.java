package com.example.devsupport.security.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.devsupport.master.entity.SystemRole;
import com.example.devsupport.security.principal.LoginUserDetails;
import com.example.devsupport.user.entity.User;
import com.example.devsupport.user.repository.UserRepository;

public class LoginUserDetailsServiceTest {
    // UserRepositoryのモックと、テスト対象のLoginUserDetailsServiceを保持するフィールド
    private UserRepository userRepository;
    private LoginUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        // UserRepositoryのモックを作成し、LoginUserDetailsServiceのコンストラクタに渡して実物を生成する
        userRepository = mock(UserRepository.class);
        userDetailsService = new LoginUserDetailsService(userRepository);
    }

    @Test
    @DisplayName("UT-AUTH-LOOKUP-001: メール検索成功")
    void testLoadUserByUsernameSuccess() {
        Long userId = 101L;
        String email = "user101@example.com";
        String passwordHash = "dummy-hash-for-unit-test";
        String displayName = "テスト利用者";
        String systemRoleCode = "USER";
        String roleName = "一般ユーザー";

        // SystemRoleとUserのインスタンスを作成し、テスト用の値を設定する
        SystemRole systemRole = new SystemRole(systemRoleCode, roleName);
        User user = new User(email, passwordHash, displayName, systemRole);

        // ReflectionTestUtilsを使用して、Userオブジェクトのprivateフィールドにテスト用の値を設定する
        // ReflectionTestUtils.setField(対象オブジェクト, "Javaのフィールド名"(DBカラム名ではない), 設定する値);
        ReflectionTestUtils.setField(user, "id", userId);

        // モックのUserRepositoryがfindByEmailAndDeletedAtIsNullメソッドを呼び出されたときに、Optional.of(user)を返すように設定する
        when(userRepository.findByEmailAndDeletedAtIsNull(email))
                .thenReturn(java.util.Optional.of(user));

        // LoginUserDetailsServiceのloadUserByUsernameメソッドを呼び出し、戻り値を取得する
        LoginUserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails, "戻り値がnullでないこと");
        assertNotNull(userDetails.getAuthorities(), "権限がnullでないこと");
        assertEquals(1, userDetails.getAuthorities().size(), "権限が1つであること");
        assertAll(
                () -> assertEquals(userId, userDetails.getId(), "IDの取得が正しいこと"),
                () -> assertEquals(email, userDetails.getUsername(), "emailの取得が正しいこと"),
                () -> assertEquals(passwordHash, userDetails.getPassword(),
                        "passwordHashの取得が正しいこと"),
                () -> assertEquals(systemRoleCode,
                        userDetails.getAuthorities().iterator().next().getAuthority(), "権限の取得が正しいこと"));

        // モックのUserRepositoryがfindByEmailAndDeletedAtIsNullメソッドを1回だけ呼び出されたことを検証する
        verify(userRepository, times(1)).findByEmailAndDeletedAtIsNull(email);
    }

    @Test
    @DisplayName("UT-AUTH-LOOKUP-002: メール検索失敗")
    void testLoadUserByUsernameNotFound() {
        String email = "missing@example.com";

        // モックのUserRepositoryがfindByEmailAndDeletedAtIsNullメソッドを呼び出されたときに、Optional.empty()を返すように設定する
        when(userRepository.findByEmailAndDeletedAtIsNull(email))
                .thenReturn(Optional.empty());

         // LoginUserDetailsServiceのloadUserByUsernameメソッドを呼び出し、UsernameNotFoundExceptionがThrowされることを検証する
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email));

        assertEquals(
                "ユーザーが見つかりません。",
                exception.getMessage());

        verify(userRepository, times(1)).findByEmailAndDeletedAtIsNull(email);
    }
}