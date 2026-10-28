package com.example.devsupport.master.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.devsupport.master.dto.response.MasterDataResponse;
import com.example.devsupport.master.dto.response.ProjectRoleResponse;
import com.example.devsupport.master.dto.response.TaskPriorityResponse;
import com.example.devsupport.master.dto.response.TaskStatusResponse;
import com.example.devsupport.master.entity.ProjectRole;
import com.example.devsupport.master.entity.TaskPriority;
import com.example.devsupport.master.entity.TaskStatus;
import com.example.devsupport.master.repository.ProjectRoleRepository;
import com.example.devsupport.master.repository.TaskStatusRepository;
import com.example.devsupport.master.repository.TaskPriorityRepository;

class MasterDataServiceTest {
    // 各Repositoryのモックを保持するフィールド
    private ProjectRoleRepository projectRoleRepository;
    private TaskStatusRepository taskStatusRepository;
    private TaskPriorityRepository taskPriorityRepository;

    // テスト対象となる実物のMasterDataServiceを保持するフィールド
    private MasterDataService masterDataService;

    @BeforeEach // 各テストメソッドの実行前に準備処理を実行する。別テストの戻り値設定や呼び出し履歴を引き継がない
    void setUp() {
        // 各Repositoryのモックを作成する
        projectRoleRepository = mock(ProjectRoleRepository.class);
        taskStatusRepository = mock(TaskStatusRepository.class);
        taskPriorityRepository = mock(TaskPriorityRepository.class);

        // 3つのRepositoryモックを渡し、実物のMasterDataServiceを作成する
        masterDataService = new MasterDataService(projectRoleRepository, taskStatusRepository, taskPriorityRepository);
    }

    @Test
    @DisplayName("UT-MASTER-001: 固定マスタ取得成功")
    void getMasterDataReturnsAllMasterData() {// 固定マスタ取得結果を検証するテストメソッド
        // 各Entityを作成する
        // 各種別ごとに2件用意することで、先頭の1件だけでなく複数件の変換や順序を確認する
        ProjectRole adminRole = new ProjectRole("PROJECT_ADMIN", "プロジェクト管理者");
        ProjectRole memberRole = new ProjectRole("MEMBER", "メンバー");

        TaskStatus todoStatus = new TaskStatus("TODO", "ToDo", 1);
        TaskStatus progressStatus = new TaskStatus("IN_PROGRESS", "In Progress", 2);

        TaskPriority highestPriority = new TaskPriority("HIGHEST", "Highest", 1);
        TaskPriority highPriority = new TaskPriority("HIGH", "High", 2);

        // 各ロール(ステータス)のprivateなidフィールドをそれぞれ設定する
        // DBによるID採番を使わないためここでIDを設定する
        ReflectionTestUtils.setField(adminRole, "id", 1L);
        ReflectionTestUtils.setField(memberRole, "id", 2L);
        ReflectionTestUtils.setField(todoStatus, "id", 11L);
        ReflectionTestUtils.setField(progressStatus, "id", 12L);
        ReflectionTestUtils.setField(highestPriority, "id", 21L);
        ReflectionTestUtils.setField(highPriority, "id", 22L);

        // List.ofはRepositoryが返した順で変更不可のリストを作成する
        // 自動的に並び替える処理は行わない
        when(projectRoleRepository.findAllByOrderByIdAsc()) // プロジェクトロールのID昇順検索を戻り値に設定し、ID順でリスト化する
                .thenReturn(List.of(adminRole, memberRole));

        when(taskStatusRepository.findAllByOrderBySortOrderAsc()) // ステータスのsortOrder昇順検索を戻り値に設定し、ID順でリスト化する
                .thenReturn(List.of(todoStatus, progressStatus));

        when(taskPriorityRepository.findAllByOrderBySortOrderAsc()) // 優先度のsortOrder昇順検索を戻り値に設定し、ID順でリスト化する
                .thenReturn(List.of(highestPriority, highPriority));

        // 固定マスタ取得処理を1回実行し、返却されたMasterDataResponseをactualへ格納する
        // この呼び出しで実物のServiceが3つのRepositoryモックを検索し、EntityをDTOへ変換する
        MasterDataResponse actual = masterDataService.getMasterData();

        // 各ロール(ステータス)の期待結果リストを作成する
        // 先頭に返されるべきDTOから作成する
        // List<ProjectRoleResponse>の<ProjectRoleResponse>は「このリストが持つ要素の型」を表す
        // 期待値をServiceの変換処理から作らずテスト側で独立して指定することで、変換結果の誤りを検出する
        List<ProjectRoleResponse> expectedRoles = List.of(
                new ProjectRoleResponse(1L, "PROJECT_ADMIN", "プロジェクト管理者"),
                new ProjectRoleResponse(2L, "MEMBER", "メンバー"));

        List<TaskStatusResponse> expectedStatuses = List.of(
                new TaskStatusResponse(11L, "TODO", "ToDo", 1),
                new TaskStatusResponse(12L, "IN_PROGRESS", "In Progress", 2));

        List<TaskPriorityResponse> expectedPriorities = List.of(
                new TaskPriorityResponse(21L, "HIGHEST", "Highest", 1),
                new TaskPriorityResponse(22L, "HIGH", "High", 2));

        // 3種類のリストに対する検証をまとめて実行する
        // 各ロール(ステータス)の件数・順序・DTO全項目が期待値と一致することを検証する
        assertAll(
                () -> assertEquals(expectedRoles, actual.projectRoles()),
                () -> assertEquals(expectedStatuses, actual.taskStatuses()),
                () -> assertEquals(expectedPriorities, actual.taskPriorities()));

        // 各ロール(ステータス)のID昇順検索(sortOrder昇順検索)が1回呼ばれたことを検証する
        // verifyは回数を省略すると1回を検証する
        verify(projectRoleRepository).findAllByOrderByIdAsc();
        verify(taskStatusRepository).findAllByOrderBySortOrderAsc();
        verify(taskPriorityRepository).findAllByOrderBySortOrderAsc();
    }
}
