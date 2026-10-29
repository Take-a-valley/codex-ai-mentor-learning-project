package com.example.devsupport.user.service;

// static import: staticなメソッドを直接呼び出せるようにするためのimport文
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.devsupport.exception.ResourceNotFoundException;
import com.example.devsupport.master.entity.SystemRole;
import com.example.devsupport.user.dto.response.UserResponse;
import com.example.devsupport.user.entity.User;
import com.example.devsupport.user.repository.UserRepository;

class UserServiceTest {
    private UserRepository userRepository; // UserRepositoryのモックを保持するフィールド
    private UserService userService; // テスト対象となる実物のUserServiceを保持するフィールド

    @BeforeEach // 各テストメソッドの実行前に準備処理を実行する。別テストの戻り値設定や呼び出し履歴を引き継がない
    void setUp() {
        userRepository = mock(UserRepository.class); // 実DBへ接続しないUserRepositoryのモックを作成する
        userService = new UserService(userRepository); // モックのRepositoryを渡して、実物のUserServiceを作成する
    }

    @Test // このメソッドをJUnitのテスト対象として指定するためのアノテーション
    @DisplayName("UT-USER-001: ユーザーIDでユーザーを取得する") // テストケースの説明を付与するためのアノテーション、テストケースIDと内容を、実行結果の表示名として指定する
    void getUserByIdReturnsUserResponse() { // ID検索に成功した場合の返却値を検証するテストメソッド
        Long userId = 101L; // Long型のユーザーIDを設定
        String email = "user101@example.com";
        String displayName = "テスト利用者";
        String roleCode = "USER";
        String roleName = "一般ユーザー";

        // 検証結果を安定させるため、作成日時に固定値を設定する
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 1, 10, 0);
        // 作成日時との取り違えを検出できるよう、更新日時に異なる固定値を設定する
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 2, 15, 30);

        // SystemRoleとUserのインスタンスを作成
        SystemRole systemRole = new SystemRole(roleCode, roleName);
        User user = new User(
                email,
                "dummy-password-hash", // 今回は認証を検証しないため、ダミーのパスワードハッシュ文字列を渡す
                displayName,
                systemRole);

        // DBを使わないため、DB管理項目はテスト側で設定する
        // ReflectionTestUtilsを使用して、Userオブジェクトのprivateフィールドにテスト用の値を設定
        // ReflectionTestUtils.setField(対象オブジェクト, "Javaのフィールド名"(DBカラム名ではない), 設定する値);
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "createdAt", createdAt);
        ReflectionTestUtils.setField(user, "updatedAt", updatedAt);

        // Repositoryの検索結果を設定する
        // モックであるUserRepositoryがfindByIdAndDeletedAtIsNullメソッドを呼び出したときに、準備したUserを含むOptionalを返すように設定
        when(userRepository.findByIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.of(user));

        // UserServiceのgetUserByIdメソッドを呼び出し、結果をactualに格納
        // actual: 実際に実行した結果返ってきた値
        UserResponse actual = userService.getUserById(userId);

        assertAll( // assertAll: 複数検証の一括実行
                   // assertEquals: 値の一致検証
                   // assertEquals(期待値, 実際の値)
                   // ラムダ式を使って、各フィールドの値が期待通りであることを検証、失敗した検証があっても残りを実行し、複数の不一致をまとめて確認できる
                () -> assertEquals(userId, actual.id()),
                () -> assertEquals(email, actual.email()),
                () -> assertEquals(displayName, actual.displayName()),
                () -> assertEquals(roleCode, actual.systemRoleCode()),
                () -> assertEquals(roleName, actual.systemRoleName()),
                () -> assertEquals(createdAt, actual.createdAt()),
                () -> assertEquals(updatedAt, actual.updatedAt()));

        // モックであるUserRepositoryのfindByIdAndDeletedAtIsNullメソッドが1回だけ呼び出されたことを検証
        // verify(モックオブジェクト, times(呼び出し回数)).メソッド名(引数);
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(userId);
    }

    @Test
    @DisplayName("UT-USER-002：IDに該当するユーザーがいなければ例外")
    void getUserByIdThrowsWhenUserIsMissing() { // IDに該当するユーザーが存在しない場合の振る舞いを検証するテストメソッド
        Long userId = 999L; // UT-USER-001と同じ101Lでも@BeforeEachでテストごとにモックを作り直すため干渉しないが、単体テスト仕様書に準じて設定

        // モックであるUserRepositoryがfindByIdAndDeletedAtIsNullメソッドを呼び出したときに、検索結果なしを表す空のOptionalを返すように設定
        when(userRepository.findByIdAndDeletedAtIsNull(userId)).thenReturn(Optional.empty());

        // 例外の発生を検証し、発生した例外をメッセージ確認用の変数へ格納する
        // assertThrows(発生を確認したい例外の型, () -> 例外が発生することを確認したい処理)
        // ResourceNotFoundExceptionが発生する：検証成功。例外オブジェクトを返す
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class, // 発生を確認したい例外の型を指定
                () -> userService.getUserById(userId)); // ラムダ式で、例外が発生することを確認したいServiceの処理を指定

        // 例外のメッセージが期待通りであることを検証
        assertEquals(
                "ユーザーが見つかりません。",
                exception.getMessage());

        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(userId);
    }

    @Test
    @DisplayName("UT-USER-003: メールアドレスでユーザーを取得する")
    void getUserByEmailReturnsUserResponse() { // メールアドレス検索に成功した場合の返却値を検証するテストメソッド
        Long userId = 101L;
        String email = "user101@example.com";
        String displayName = "テスト利用者";
        String roleCode = "USER";
        String roleName = "一般ユーザー";

        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 2, 15, 30);

        SystemRole systemRole = new SystemRole(roleCode, roleName);
        User user = new User(
                email,
                "dummy-password-hash",
                displayName,
                systemRole);

        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "createdAt", createdAt);
        ReflectionTestUtils.setField(user, "updatedAt", updatedAt);

        // モックであるUserRepositoryがfindByEmailAndDeletedAtIsNullメソッドを呼び出したときに、準備したUserを含むOptionalを返すように設定
        when(userRepository.findByEmailAndDeletedAtIsNull(email))
                .thenReturn(Optional.of(user));

        // UserServiceのgetUserByEmailメソッドを呼び出し、結果をactualに格納
        UserResponse actual = userService.getUserByEmail(email);

        assertAll(
                () -> assertEquals(userId, actual.id()),
                () -> assertEquals(email, actual.email()),
                () -> assertEquals(displayName, actual.displayName()),
                () -> assertEquals(roleCode, actual.systemRoleCode()),
                () -> assertEquals(roleName, actual.systemRoleName()),
                () -> assertEquals(createdAt, actual.createdAt()),
                () -> assertEquals(updatedAt, actual.updatedAt()));

        // モックであるUserRepositoryのfindByEmailAndDeletedAtIsNullメソッドが1回だけ呼び出されたことを検証
        verify(userRepository, times(1)).findByEmailAndDeletedAtIsNull(email);
    }

    @Test
    @DisplayName("UT-USER-004：メールアドレスに該当するユーザーがいなければ例外")
    void getUserByEmailThrowsWhenUserIsMissing() { // メールアドレスに該当するユーザーが存在しない場合の振る舞いを検証するテストメソッド
        String email = "missing@example.com";

        when(userRepository.findByEmailAndDeletedAtIsNull(email)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserByEmail(email));
        assertEquals(
                "ユーザーが見つかりません。",
                exception.getMessage());

        verify(userRepository, times(1)).findByEmailAndDeletedAtIsNull(email);
    }
}
