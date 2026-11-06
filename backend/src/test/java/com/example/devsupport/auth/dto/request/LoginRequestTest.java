package com.example.devsupport.auth.dto.request;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

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
