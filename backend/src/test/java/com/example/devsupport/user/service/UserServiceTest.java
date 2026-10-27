package com.example.devsupport.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.flywaydb.core.internal.database.base.Database;

import com.example.devsupport.master.entity.SystemRole;
import com.example.devsupport.user.dto.response.UserResponse;
import com.example.devsupport.user.dto.response.UserSummaryResponse;
import com.example.devsupport.user.entity.User;
import com.example.devsupport.user.repository.UserRepository;
import com.example.devsupport.user.service.UserService;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach // 各テストメソッドの前に新しいRepositoryモックとServiceを準備する
    void setUp() {
        // UserRepositoryのモックworkを作成し、UserServiceのインスタンスを生成
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test // ユーザーIDでユーザーを取得するテスト
    @DisplayName("UT-USER-001: ユーザーIDでユーザーを取得する")
    void getUserByIdReturnsUserResponse() {
        // テスト用のユーザーIDと期待されるUserResponseを設定
        Long userId = 101L; // Long型のユーザーIDを設定
        String email = "user101@example.com";
        String displayName = "テスト利用者";
        String roleCode = "USER";
        String roleName = "一般ユーザー";

        // テスト用の作成日時と更新日時を設定
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 2, 15, 30);

        // SystemRoleとUserのインスタンスを作成
        SystemRole systemRole = new SystemRole(roleCode, roleName);
        User user = new User(
                email,
                "dummy-password-hash", // パスワードハッシュはテストでは無視
                displayName,
                systemRole);

        // DBを使わないため、DB管理項目はテスト側で設定する
        // ReflectionTestUtilsを使用して、Userオブジェクトのプライベートフィールドに値を設定
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "createAt", createdAt);
        ReflectionTestUtils.setField(user, "updatedAt", updatedAt);

        // Repositoryの検索結果を設定する
        // モックのUserRepositoryがfindByIdAndDeletedAtIsNullメソッドを呼び出したときに、Optional.of(user)を返すように設定
        when(userRepository.findByIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.of(user));

        // UserServiceのgetUserByIdメソッドを呼び出し、結果を取得
        UserResponse actual = userService.getUserById(userId);
    }
}
