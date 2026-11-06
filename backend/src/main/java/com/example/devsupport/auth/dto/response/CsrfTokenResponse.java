package com.example.devsupport.auth.dto.response;

public record CsrfTokenResponse(
        String token,
        String headerName) {

}
