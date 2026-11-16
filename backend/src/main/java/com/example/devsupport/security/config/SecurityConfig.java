package com.example.devsupport.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// SecurityConfigは、Spring Securityの設定を行うクラス
@Configuration
public class SecurityConfig {
    // PasswordEncoderをBeanとして定義することで、アプリケーション全体で使用できるようにする
    @Bean
    public PasswordEncoder passwordEncoder() {
        // DelegatingPasswordEncoderを生成する。新規エンコードにはBCryptを使用する
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // SecurityFilterChainをBeanとして定義することで、HTTPリクエストのセキュリティ設定を行う
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/master-data").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    // AuthenticationManagerをBeanとして定義することで、認証処理を行う
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }
}
