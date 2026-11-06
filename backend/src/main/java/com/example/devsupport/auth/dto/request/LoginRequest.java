package com.example.devsupport.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank // null・空文字・空白だけの入力を拒否する
        @Email // メールアドレス形式の入力を要求する
        String email,
        @NotBlank String password) {

}
