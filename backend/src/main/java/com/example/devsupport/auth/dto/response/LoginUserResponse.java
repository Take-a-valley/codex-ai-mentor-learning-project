package com.example.devsupport.auth.dto.response;

public record LoginUserResponse(
        Long userId,
        String email,
        String displayName,
        String systemRole) {
}
