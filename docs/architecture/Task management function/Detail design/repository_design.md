# Repository設計書

## 1. 本書の目的

本書は、タスク管理機能の初期MVPにおけるRepository設計を定義することを目的とする。

Repositoryは、Spring Data JPAを利用してDBアクセスを担当する層である。

本書では、Repository一覧、各Repositoryの責務、主な検索・保存方針、論理削除データの扱い、一意制約確認、履歴系データの取得方針を整理する。

## 2. 対象範囲

対象に含めるものは以下とする。

- Repository一覧
- Repositoryごとの責務
- 主な取得条件
- 論理削除データの除外方針
- 一意制約確認方針
- 固定マスタ取得方針
- 履歴系Entity取得方針

対象外とするものは以下とする。

- Serviceの業務処理詳細
- ControllerのAPI詳細
- DTO変換処理
- Entityの詳細項目
- SQLの具体実装
- Flywayマイグレーション定義

## 3. Repository設計方針

RepositoryはDBアクセスのみを担当する。

Repositoryでは業務ルールを判定しない。

認可判定はRepositoryでは行わず、Service層で行う。

通常取得では、論理削除済みデータを除外する。

論理削除対象Entityでは、`deleted_at is null` を基本条件とする。

Entityを保存・更新する処理はRepository経由で行う。

## 4. Repository一覧

| Repository | 対象Entity | 主な用途 |
|---|---|---|
| UserRepository | User | ユーザー取得、メールアドレス検索 |
| SystemRoleRepository | SystemRole | システムロール取得 |
| ProjectRepository | Project | プロジェクト取得、一覧取得 |
| ProjectMemberRepository | ProjectMember | プロジェクト参加状況、ロール取得 |
| ProjectRoleRepository | ProjectRole | プロジェクトロール取得 |
| TaskRepository | Task | タスク取得、カンバン一覧取得 |
| TaskStatusRepository | TaskStatus | Status固定マスタ取得 |
| TaskPriorityRepository | TaskPriority | Priority固定マスタ取得 |
| TaskCommentRepository | TaskComment | コメント取得 |
| AttachmentRepository | Attachment | 添付ファイル取得 |
| ProjectInvitationRepository | ProjectInvitation | 招待情報取得、token検索 |
| PasswordResetTokenRepository | PasswordResetToken | パスワードリセットtoken検索 |

## 5. 共通取得方針

論理削除対象Entityの通常取得では、削除済みデータを取得しない。

対象Entityは以下とする。

- User
- Project
- ProjectMember
- Task
- TaskComment
- Attachment

通常取得条件は以下を基本とする。

| 条件 | 内容 |
|---|---|
| deleted_at is null | 論理削除されていないこと |

履歴系Entityは物理削除せず、statusで状態を管理する。

対象Entityは以下とする。

- ProjectInvitation
- PasswordResetToken

## 6. UserRepository

## 6.1 責務

UserRepositoryは、ユーザー情報の取得と保存を担当する。

主な用途は以下とする。

- ログイン時のメールアドレス検索
- ユーザー登録時のメールアドレス重複確認
- ユーザーIDによる取得
- システム管理者数の確認
- ユーザー論理削除時の保存

## 6.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| メールアドレス検索 | email |
| メールアドレス重複確認 | email |
| ユーザーID取得 | id, deleted_at is null |
| システム管理者数取得 | systemRole = SYSTEM_ADMIN, deleted_at is null |

## 6.3 注意事項

パスワードはハッシュ化済みの値のみ保存する。

削除済みユーザーは通常ログイン対象外とする。

削除済みユーザーが作成したタスクやコメントは残す。

emailは一意制約の対象とする。

論理削除済みユーザーと同じemailで再登録を許可するかは後続工程で決定する。

初期MVPでは、論理削除済みユーザーも含めてemailの重複を許可しない方針を基本とする。

## 7. SystemRoleRepository

## 7.1 責務

SystemRoleRepositoryは、システムロール固定マスタの取得を担当する。

## 7.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| ロールコード検索 | code |
| システム管理者ロール取得 | code = SYSTEM_ADMIN |
| 一般ユーザーロール取得 | code = USER |

## 7.3 注意事項

初期MVPでは、画面やAPIから追加・変更・削除しない。

## 8. ProjectRepository

## 8.1 責務

ProjectRepositoryは、プロジェクト情報の取得と保存を担当する。

主な用途は以下とする。

- システム管理者向けの全プロジェクト一覧取得
- 一般ユーザー向けの参加済みプロジェクト一覧取得
- プロジェクト詳細取得
- プロジェクト作成
- プロジェクト更新
- プロジェクト論理削除

## 8.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| プロジェクトID取得 | id, deleted_at is null |
| 全プロジェクト一覧取得 | deleted_at is null |
| 作成者による取得 | created_by, deleted_at is null |

## 8.3 注意事項

参加済みプロジェクト一覧は、ProjectMemberRepositoryと組み合わせて取得する。

プロジェクト配下のデータを取得する場合は、対象Projectが論理削除されていないことをService層で確認する。

Projectが論理削除済みの場合、その配下のTask、TaskComment、Attachment、ProjectMemberは通常操作の対象外とする。

## 9. ProjectMemberRepository

## 9.1 責務

ProjectMemberRepositoryは、ユーザーとプロジェクトの参加関係およびプロジェクト内ロールの取得を担当する。

## 9.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| 対象プロジェクトのメンバー一覧 | project_id, deleted_at is null |
| ユーザーの参加済みプロジェクト取得 | user_id, deleted_at is null |
| 対象プロジェクト内のユーザーロール取得 | project_id, user_id, deleted_at is null |
| 対象プロジェクトのPROJECT_ADMIN数取得 | project_id, projectRole = PROJECT_ADMIN, deleted_at is null |
| メンバー重複確認 | project_id, user_id |

## 9.3 注意事項

project_idとuser_idの組み合わせは一意とする。

PROJECT_ADMINが0人になる操作は禁止するため、Service層で管理者数確認に利用する。

論理削除済みのProjectMemberが存在するユーザーを再招待・再参加させる場合は、新規作成ではなく既存のProjectMemberを復元する方針を基本とする。

復元時は、project_role_id、joined_at、deleted_at、deleted_byを適切に更新する。

## 10. ProjectRoleRepository

## 10.1 責務

ProjectRoleRepositoryは、プロジェクト内ロール固定マスタの取得を担当する。

## 10.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| ロールコード検索 | code |
| PROJECT_ADMIN取得 | code = PROJECT_ADMIN |
| MEMBER取得 | code = MEMBER |
| VIEWER取得 | code = VIEWER |

## 10.3 注意事項

初期MVPでは、画面やAPIから追加・変更・削除しない。

## 11. TaskRepository

## 11.1 責務

TaskRepositoryは、タスク情報の取得と保存を担当する。

主な用途は以下とする。

- カンバン画面用タスク一覧取得
- タスク詳細取得
- タスク作成
- タスク更新
- Status変更
- タスク論理削除
- プロジェクト削除時の関連タスク論理削除

## 11.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| プロジェクト内タスク一覧 | project_id, deleted_at is null |
| プロジェクト内Status別タスク一覧 | project_id, status_id, deleted_at is null |
| タスク詳細取得 | id, project_id, deleted_at is null |
| 担当者別タスク取得 | assignee_id, deleted_at is null |
| プロジェクト内タスク一括取得 | project_id, deleted_at is null |

## 11.3 注意事項

カンバン画面ではStatusごとにタスクを表示する。

削除済みタスクは通常表示しない。

タスク削除は物理削除ではなく論理削除とする。

## 12. TaskStatusRepository

## 12.1 責務

TaskStatusRepositoryは、TaskStatus固定マスタの取得を担当する。

## 12.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| Statusコード検索 | code |
| Status一覧取得 | sort_order順 |
| TODO取得 | code = TODO |

## 12.3 注意事項

初期MVPでは固定マスタとして扱う。

画面やAPIから追加・変更・削除しない。

## 13. TaskPriorityRepository

## 13.1 責務

TaskPriorityRepositoryは、TaskPriority固定マスタの取得を担当する。

## 13.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| Priorityコード検索 | code |
| Priority一覧取得 | sort_order順 |

## 13.3 注意事項

初期MVPでは固定マスタとして扱う。

画面やAPIから追加・変更・削除しない。

## 14. TaskCommentRepository

## 14.1 責務

TaskCommentRepositoryは、コメント情報の取得と保存を担当する。

## 14.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| タスク内コメント一覧取得 | task_id, deleted_at is null |
| コメント詳細取得 | id, task_id, deleted_at is null |
| 投稿者による取得 | user_id, deleted_at is null |
| プロジェクト削除時の関連コメント取得 | task_id群, deleted_at is null |

## 14.3 注意事項

コメント編集権限はService層で投稿者本人かどうかを確認する。

コメント削除は論理削除とする。

## 15. AttachmentRepository

## 15.1 責務

AttachmentRepositoryは、添付ファイルのメタ情報とファイル本体の取得・保存を担当する。

## 15.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| タスク内添付ファイル一覧取得 | task_id, deleted_at is null |
| 添付ファイル取得 | id, task_id, deleted_at is null |
| アップロード者による取得 | uploaded_by, deleted_at is null |
| プロジェクト削除時の関連添付ファイル取得 | task_id群, deleted_at is null |

## 15.3 注意事項

ファイル本体はDBに保存する。

添付ファイルの容量、種別、権限チェックはService層で行う。

添付ファイル削除は論理削除とする。

## 16. ProjectInvitationRepository

## 16.1 責務

ProjectInvitationRepositoryは、プロジェクト招待情報の取得と保存を担当する。

## 16.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| token検索 | token |
| 招待ID取得 | id |
| 招待先メールアドレス検索 | email |
| プロジェクト別招待履歴取得 | project_id |
| PENDING状態の招待取得 | status = PENDING |
| 期限切れ候補取得 | status = PENDING, expires_at < 現在日時 |
| 同一プロジェクト・同一メールのPENDING招待確認 | project_id, email, status = PENDING |

## 16.3 注意事項

ProjectInvitationは履歴として残す。

物理削除しない。

承認済み、期限切れ、無効化済みの招待は再利用できない。

論理削除済みプロジェクトに対する未承認の招待は、Service層で利用不可とする。

## 17. PasswordResetTokenRepository

## 17.1 責務

PasswordResetTokenRepositoryは、パスワードリセットトークン情報の取得と保存を担当する。

## 17.2 主な取得・確認

| 用途 | 条件 |
|---|---|
| token検索 | token |
| ユーザー別トークン取得 | user_id |
| ACTIVE状態のtoken取得 | token, status = ACTIVE |
| 期限切れ候補取得 | status = ACTIVE, expires_at < 現在日時 |
| ユーザーのACTIVEトークン取得 | user_id, status = ACTIVE |

## 17.3 注意事項

PasswordResetTokenは履歴として残す。

物理削除しない。

使用済み、期限切れのトークンは再利用できない。

## 18. 一意制約確認方針

Repositoryでは、以下の一意制約に関する確認で利用する。

| 対象 | 一意条件 |
|---|---|
| User | email |
| ProjectMember | project_id + user_id |
| ProjectInvitation | token |
| PasswordResetToken | token |

一意制約違反を最終的に防ぐため、DB側にも一意制約を設定する。

Repositoryによる事前確認だけに依存しない。

## 19. 論理削除方針

Repositoryでは、通常取得時に論理削除済みデータを除外する。

論理削除対象Entityは以下とする。

- User
- Project
- ProjectMember
- Task
- TaskComment
- Attachment

論理削除処理自体はService層で制御し、Repositoryは保存を担当する。

プロジェクト削除時の関連データ論理削除は、Service層で対象データを取得し、まとめて更新する。

## 20. 固定マスタ取得方針

固定マスタは、codeによる取得を基本とする。

対象は以下とする。

- SystemRole
- ProjectRole
- TaskStatus
- TaskPriority

固定マスタは初期MVPでは画面やAPIから変更しない。

## 21. RepositoryとServiceの責務分担

| 処理 | Repository | Service |
|---|---|---|
| DBからEntityを取得する | 行う | 呼び出す |
| Entityを保存する | 行う | 呼び出す |
| 論理削除済みを除外して取得する | 行う | 呼び出す |
| 権限を判定する | 行わない | 行う |
| 管理者0人防止を判定する | 行わない | 行う |
| 添付ファイル制約を判定する | 行わない | 行う |
| 招待URL有効期限を判定する | 行わない | 行う |
| パスワードリセットURL有効期限を判定する | 行わない | 行う |
| トランザクションを制御する | 行わない | 行う |

## 22. 決定事項

- RepositoryはDBアクセスのみを担当する
- 業務ルールはRepositoryでは判定しない
- 認可判定はService層で行う
- 通常取得では論理削除済みデータを除外する
- 固定マスタはcodeで取得する
- ProjectInvitationとPasswordResetTokenは履歴として残す
- 一意制約はRepositoryの事前確認だけでなくDB制約でも担保する

## 23. 未決事項

以下は後続工程で具体化する。

- 正式なRepositoryクラス名
- Repositoryメソッド名
- Spring Data JPAのクエリメソッドで対応する範囲
- JPQLを使用する範囲
- カスタムRepositoryを使用する範囲
- 論理削除条件を共通化する方法
- ページングを適用する一覧取得
- token検索時にハッシュ化tokenを使うか
- プロジェクト削除時の関連データ取得方法
- 論理削除済みユーザーと同じemailで再登録を許可するか