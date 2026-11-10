package com.example.devsupport.auth.dto.request;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class LoginRequestTest {
    private Validator validator; // 各テストで使用するValidatorを保持するフィールド
    private ValidatorFactory factory; // Validatorの取得元となるFactoryを、終了処理からも参照できるよう保持するフィールド

    // 各テストメソッドの実行前にsetUpを実行する
    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory(); // Validatorを取得するためのFactoryを作成する
        validator = factory.getValidator(); // FactoryからValidatorを取得する
    }

    @Test
    @DisplayName("UT-AUTH-DTO-001: LoginRequestのバリデーション成功")
    void testValidLoginRequest() {
        // 正常なメールアドレスとパスワードを持つLoginRequestを作成する
        LoginRequest request = new LoginRequest("user101@example.com", "TestPass123!");

        // LoginRequestのバリデーションを実行し、違反がある場合はviolationsに格納される
        // ConstraintViolation<LoginRequest>：LoginRequestに関する1件の制約違反
        // Set<ConstraintViolation<LoginRequest>>：その制約違反をまとめた集合
        // violations：検証で見つかった違反を保持する変数
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        // バリデーション違反がないことを確認し、失敗時には固定メッセージを表示する
        assertTrue(violations.isEmpty(), "正常入力では制約違反がないこと ");
    }

    @Test
    @DisplayName("UT-AUTH-DTO-002: emailがnull")
    void testEmailIsNull() {
        // emailにnull、passwordに正常値を設定したLoginRequestを作成する
        LoginRequest request = new LoginRequest(null, "TestPass123!");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // 違反集合からStreamを作成し、emailの確認結果をboolean変数に保持する
        boolean emailResult = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email") // anyMatchは条件に一致する違反が1件でもあればtrueを返す
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class)); // emailがnullの場合、NotBlank制約違反があることを確認する
        assertTrue(emailResult, "emailがnullの場合、NotBlank制約違反があること");

        // 違反集合からStreamを作成し、passwordの確認結果をboolean変数に保持する
        // anyMatch・noneMatchは終端操作なので、同じStreamを使い回さず新しいviolations.stream()を作成する
        boolean passwordResult = violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("password")); // noneMatchは一致する違反がなければtrueを返す
        assertTrue(passwordResult, "正常なpasswordには制約違反がないこと");
    }

    @Test
    @DisplayName("UT-AUTH-DTO-003: emailが空文字")
    void testEmailIsEmpty() {
        LoginRequest request = new LoginRequest("", "TestPass123!");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        boolean emailResult = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class)); // NotBlankはnullだけでなく空文字や空白だけの文字列も禁止する
        assertTrue(emailResult, "emailが空文字の場合、NotBlank制約違反があること");

        boolean passwordResult = violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertTrue(passwordResult, "正常なpasswordには制約違反がないこと");
    }

    @Test
    @DisplayName("UT-AUTH-DTO-004: emailが空白のみ")
    void testEmailIsBlank() {
        LoginRequest request = new LoginRequest("   ", "TestPass123!");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        boolean emailResult = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class));
        assertTrue(emailResult, "emailが空白のみの場合、NotBlank制約違反があること");

        boolean passwordResult = violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertTrue(passwordResult, "正常なpasswordには制約違反がないこと");
    }

    @Test
    @DisplayName("UT-AUTH-DTO-005: emailが不正形式")
    void testEmailIsInvalidFormat() {
        LoginRequest request = new LoginRequest("not-an-email", "TestPass123!");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        boolean emailFormatResult = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(Email.class)); // emailが不正形式の場合、Email制約違反があることを確認する
        assertTrue(emailFormatResult, "emailが不正形式の場合、Email制約違反があること");

        boolean emailBlankResult = violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class));
        assertTrue(emailBlankResult, "不正形式でも空白ではないemailには、NotBlank制約違反がないこと");

        boolean passwordResult = violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertTrue(passwordResult, "正常なpasswordには制約違反がないこと");
    }

    @Test
    @DisplayName("UT-AUTH-DTO-006: passwordがnull")
    void testPasswordIsNull() {
        LoginRequest request = new LoginRequest("user101@example.com", null);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        boolean emailResult = violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(emailResult, "正常なemailには制約違反がないこと");

        boolean passwordResult = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class));
        assertTrue(passwordResult, "passwordがnullの場合、NotBlank制約違反があること");
    }

    @Test
    @DisplayName("UT-AUTH-DTO-007: passwordが空文字")
    void testPasswordIsEmpty() {
        LoginRequest request = new LoginRequest("user101@example.com", "");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        boolean emailResult = violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(emailResult, "正常なemailには制約違反がないこと");

        boolean passwordResult = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class));
        assertTrue(passwordResult, "passwordが空文字の場合、NotBlank制約違反があること");
    }

    @Test
    @DisplayName("UT-AUTH-DTO-008: passwordが空白のみ")
    void testPasswordIsOnlyWhitespace() {
        LoginRequest request = new LoginRequest("user101@example.com", "   ");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        boolean emailResult = violations.stream()
                .noneMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(emailResult, "正常なemailには制約違反がないこと");

        boolean passwordResult = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")
                        && v.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class));
        assertTrue(passwordResult, "passwordが空白のみの場合、NotBlank制約違反があること");
    }

    // 各テストメソッドの実行後にtearDownを実行する
    @AfterEach
    // ValidatorFactoryを閉じてリソースを解放する終了処理を定義する
    void tearDown() {
        // factoryがnullでない場合にのみ閉じる
        if (factory != null) {
            factory.close();
        }
    }
}
