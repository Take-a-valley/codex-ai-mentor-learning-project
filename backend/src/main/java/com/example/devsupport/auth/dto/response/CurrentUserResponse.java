package com.example.devsupport.auth.dto.response;

public record CurrentUserResponse(
        Long userId,
        String email,
        String displayName,
        String systemRole) {
}
