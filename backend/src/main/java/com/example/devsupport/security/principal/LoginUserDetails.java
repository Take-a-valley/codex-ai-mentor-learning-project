package com.example.devsupport.security.principal;

import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// LoginUserDetailsは、Spring SecurityのUserDetailsインターフェースを実装し、ユーザー認証に必要な情報を提供するクラス
public class LoginUserDetails implements UserDetails {
    private final Long id;
    private final String email;
    private final String passwordHash;
    private final String systemRoleCode;

    public LoginUserDetails(Long id, String email, String passwordHash, String systemRoleCode) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.systemRoleCode = systemRoleCode;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.Collections.singletonList(new SimpleGrantedAuthority(systemRoleCode));
    }
}
