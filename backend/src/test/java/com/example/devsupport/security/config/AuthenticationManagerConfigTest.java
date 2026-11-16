package com.example.devsupport.security.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.devsupport.security.principal.LoginUserDetails;

public class AuthenticationManagerConfigTest {
    @Test
    @DisplayName("UT-AUTH-MANAGER-001: ユーザーあり・パスワード一致")
    public void testAuthenticationManagerWithValidUser() {
        Long userId = 101L;
        String email = "user101@example.com";
        String systemRoleCode = "USER";

        // UserDetailsServiceのモックを生成する
        UserDetailsService userDetailsService = mock(UserDetailsService.class);

        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        String rawPassword = "TestPass123!";
        // パスワードをエンコードして、テスト用のハッシュを生成する
        String passwordHash = passwordEncoder.encode(rawPassword);

        // AuthenticationManagerを生成するために、SecurityConfigのauthenticationManagerメソッドを呼び出す
        AuthenticationManager authenticationManager = securityConfig.authenticationManager(userDetailsService,
                passwordEncoder);

        // 準備したID・email・ハッシュ・ロールcodeで、LoginUserDetailsの実物を生成する
        LoginUserDetails userDetails = new LoginUserDetails(userId, email, passwordHash, systemRoleCode);

        // UserDetailsServiceのモックが、指定したemailで呼び出されたときに、準備したLoginUserDetailsを返すように設定する
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // emailと平文パスワードを渡し、認証前の要求を表すトークンを生成する
        // unauthenticated: 未認証の状態・ユーザー名(email)・パスワードを保持するトークンを生成する
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(email,
                rawPassword);

        // AuthenticationManagerのauthenticateメソッドを呼び出し、認証処理を実行する。認証に失敗した場合は例外が発生する
        Authentication authentication = authenticationManager.authenticate(authRequest);

        // 認証結果がnullでないこと、認証済みであることを確認する
        assertNotNull(authentication, "認証結果がnullでないこと");
        assertTrue(authentication.isAuthenticated(), "認証結果が認証済みであること");
        // getPrincipal()の戻り値がLoginUserDetailsのインスタンスであること、IDとusernameが一致することを確認する
        // ((LoginUserDetails)
        // authentication.getPrincipal())は、Object型で返るprincipalをLoginUserDetails型として扱うためのキャスト
        assertInstanceOf(LoginUserDetails.class, authentication.getPrincipal(), "認証結果のprincipalがLoginUserDetailsであること");
        assertEquals(userId, ((LoginUserDetails) authentication.getPrincipal()).getId(),
                "認証結果のprincipalのIDが一致すること");
        assertEquals(email, ((LoginUserDetails) authentication.getPrincipal()).getUsername(),
                "認証結果のprincipalのusernameが一致すること");
        // 認証結果の権限集合がnullではないことを確認する
        assertNotNull(authentication.getAuthorities(), "認証結果の権限がnullでないこと");

        // 認証結果の権限を取得し、Set<String>に変換する
        // grantedAuthority.getAuthority()は、GrantedAuthorityオブジェクトから権限名を取得するメソッド
        // Collectors.toSet()は、Streamの要素をSetに収集するためのCollectorを生成する
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toSet());

        // 認証結果の権限文字列にUSERとFACTOR_PASSWORDが含まれること、権限の数が2つであること、credentialsがnullであることを確認する
        assertTrue(authorities.contains("USER"), "認証結果の権限にUSERが含まれること");
        assertTrue(authorities.contains("FACTOR_PASSWORD"), "認証結果の権限にFACTOR_PASSWORDが含まれること");
        assertEquals(2, authentication.getAuthorities().size(), "認証結果の権限が2つであること");
        assertNull(authentication.getCredentials(), "認証結果のcredentialsがnullであること");

        // モックのUserDetailsServiceがloadUserByUsernameメソッドを1回だけ呼び出されたことを検証する
        verify(userDetailsService, times(1)).loadUserByUsername(email);
    }

    @Test
    @DisplayName("UT-AUTH-MANAGER-002: ユーザーあり・パスワード不一致")
    public void testAuthenticationManagerWithWrongPassword() {
        Long userId = 101L;
        String email = "user101@example.com";
        String systemRoleCode = "USER";

        // UserDetailsServiceのモックを生成する
        UserDetailsService userDetailsService = mock(UserDetailsService.class);

        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        String rawPassword = "TestPass123!";
        String passwordHash = passwordEncoder.encode(rawPassword);

        AuthenticationManager authenticationManager = securityConfig.authenticationManager(userDetailsService,
                passwordEncoder);

        // 準備したID・email・ハッシュ・ロールcodeで、LoginUserDetailsの実物を生成する
        LoginUserDetails userDetails = new LoginUserDetails(userId, email, passwordHash, systemRoleCode);

        // UserDetailsServiceのモックが、指定したemailで呼び出されたときに、準備したLoginUserDetailsを返すように設定する
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // 認証要求には、元のパスワードと異なる"WrongPass456!"を渡す
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(email,
                "WrongPass456!");

        // 認証処理を実行し、パスワード不一致の場合にBadCredentialsExceptionがThrowされることを検証する
        assertThrows(BadCredentialsException.class, () -> {
            authenticationManager.authenticate(authRequest);
        }, "パスワード不一致の場合、BadCredentialsExceptionがThrowされること");

        verify(userDetailsService, times(1)).loadUserByUsername(email);
    }

    @Test
    @DisplayName("UT-AUTH-MANAGER-003: ユーザーなし")
    public void testAuthenticationManagerWithNonExistentUser() {
        String missingEmail = "missing@example.com";
        String rawPassword = "TestPass123!";

        // UserDetailsServiceのモックを生成する
        UserDetailsService userDetailsService = mock(UserDetailsService.class);

        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        AuthenticationManager authenticationManager = securityConfig.authenticationManager(userDetailsService,
                passwordEncoder);

        // UserDetailsServiceのモックが、指定したemailで呼び出されたときに、UsernameNotFoundExceptionをThrowするように設定する
        when(userDetailsService.loadUserByUsername(missingEmail))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // 存在しないユーザーのemailを指定して、認証要求を生成する
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                missingEmail,
                rawPassword);

        // 認証処理を実行し、ユーザーが存在しない場合にBadCredentialsExceptionがThrowされることを検証する
        assertThrows(BadCredentialsException.class, () -> {
            authenticationManager.authenticate(authRequest);
        }, "ユーザーが存在しない場合、BadCredentialsExceptionがThrowされること");

        // 存在しないユーザーのemailでUserDetailsServiceのloadUserByUsernameメソッドが1回だけ呼び出されたことを検証する
        verify(userDetailsService, times(1)).loadUserByUsername(missingEmail);
    }
}
