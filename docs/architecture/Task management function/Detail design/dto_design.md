# DTO設計書

## 1. 本書の目的

本書は、タスク管理機能の初期MVPにおけるDTO設計を定義することを目的とする。

DTOは、APIのリクエストおよびレスポンスで使用するデータ構造である。

EntityをAPI入出力に直接使用せず、画面やAPIの用途に合わせたDTOを定義することで、DB構造とAPI仕様を分離する。

## 2. 対象範囲

本書の対象範囲は、タスク管理機能の初期MVPで使用するDTOとする。

対象に含めるものは以下とする。

- 共通レスポンスDTO
- 共通エラーDTO
- ページングDTO
- 認証DTO
- パスワードリセットDTO
- プロジェクトDTO
- プロジェクトメンバーDTO
- 招待DTO
- タスクDTO
- コメントDTO
- 添付ファイルDTO

対象外とするものは以下とする。

- Entityの詳細定義
- Repositoryのメソッド定義
- Serviceの処理詳細
- Controllerのメソッド詳細
- フロントエンドの型定義

## 3. DTO設計方針

DTOは、APIの入出力専用のデータ構造とする。

EntityはAPIレスポンスとして直接返却しない。

リクエストDTOは、画面からバックエンドへ送信される入力値を表す。

レスポンスDTOは、バックエンドから画面へ返却する値を表す。

DTOの項目名は、JSONで扱いやすいようにcamelCaseを基本とする。

DTOにはDB内部管理項目を不用意に含めない。

## 4. DTO分類

DTOは以下に分類する。

| 分類 | 用途 |
|---|---|
| Request DTO | APIリクエストの入力値 |
| Response DTO | APIレスポンスの返却値 |
| Summary DTO | 一覧表示などで使う要約情報 |
| Detail DTO | 詳細画面などで使う詳細情報 |
| Common DTO | 共通レスポンス、ページング、エラーなど |
| Internal DTO | Service内部で必要に応じて使う補助的なデータ |

## 5. 共通レスポンス方針

APIレスポンスはJSON形式を基本とする。

成功時は、APIの用途に応じて必要なデータを返却する。

一覧系APIでは、必要に応じてページング情報を含める。

削除系APIでは、削除完了を示す結果を返却する。

添付ファイル取得APIのみ、JSONではなくファイルデータを返却する。

## 6. 共通エラーDTO

エラー発生時は、共通エラー形式で返却する。

### ErrorResponse

| 項目 | 型 | 概要 |
|---|---|---|
| code | string | エラーコード |
| message | string | エラーメッセージ |
| details | array | 入力項目ごとの詳細エラー |
| timestamp | string | エラー発生日時 |

### ErrorDetail

| 項目 | 型 | 概要 |
|---|---|---|
| field | string | エラー対象項目 |
| message | string | 項目別エラーメッセージ |

### 主な用途

- 入力チェックエラー
- 認証エラー
- 認可エラー
- 対象データなし
- 業務ルール違反
- CSRF不正

## 7. ページングDTO

一覧APIでページングが必要な場合に使用する。

### PageResponse

| 項目 | 型 | 概要 |
|---|---|---|
| pageNumber | number | 現在ページ |
| pageSize | number | 1ページあたりの件数 |
| totalItems | number | 全件数 |
| totalPages | number | 全ページ数 |

初期MVPでは、プロジェクト一覧やメンバー一覧などで利用を検討する。

カンバン画面のタスク一覧は、Statusごとの配列で返却する方針を基本とする。

## 8. 認証DTO

## 8.1 LoginRequest

ログインAPIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| email | string | 必須 | メールアドレス |
| password | string | 必須 | パスワード |

### 入力チェック

- emailは必須
- emailはメールアドレス形式
- passwordは必須

## 8.2 LoginResponse

ログイン成功時のレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| user | LoginUserResponse | ログインユーザー情報 |

セッション方式を採用するため、セッションIDはJSONでは返却しない。

セッションIDはCookieで管理する。

## 8.3 LoginUserResponse

ログインユーザー情報DTO。

| 項目 | 型 | 概要 |
|---|---|---|
| userId | number | ユーザーID |
| email | string | メールアドレス |
| displayName | string | 表示名 |
| systemRole | string | システムロール |

## 8.4 CurrentUserResponse

ログインユーザー取得APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| userId | number | ユーザーID |
| email | string | メールアドレス |
| displayName | string | 表示名 |
| systemRole | string | システムロール |

## 9. CSRF DTO

## 9.1 CsrfTokenResponse

CSRFトークン取得APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| token | string | CSRFトークン |
| headerName | string | リクエスト時に使用するヘッダー名 |

CSRFトークンのヘッダー名は、CsrfTokenResponseのheaderNameで返却する値を使用する。

## 10. パスワードリセットDTO

## 10.1 PasswordResetRequest

パスワードリセット申請APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| email | string | 必須 | メールアドレス |

### 入力チェック

- emailは必須
- emailはメールアドレス形式

## 10.2 PasswordResetRequestResponse

パスワードリセット申請APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| accepted | boolean | パスワードリセット申請を受け付けたか |

セキュリティ上、対象メールアドレスの存在有無に関わらず同じレスポンスを返す。

## 10.3 PasswordResetConfirmRequest

パスワード再設定APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| token | string | 必須 | パスワードリセットトークン |
| newPassword | string | 必須 | 新しいパスワード |

### 入力チェック

- tokenは必須
- newPasswordは必須
- newPasswordはパスワードルールを満たすこと

## 10.4 PasswordResetConfirmResponse

パスワード再設定APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| completed | boolean | パスワード再設定が完了したか |

## 11. プロジェクトDTO

## 11.1 ProjectCreateRequest

プロジェクト作成APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| name | string | 必須 | プロジェクト名 |
| description | string | 任意 | プロジェクト説明 |

### 入力チェック

- nameは必須
- nameは規定文字数以内
- descriptionは規定文字数以内

## 11.2 ProjectUpdateRequest

プロジェクト更新APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| name | string | 必須 | プロジェクト名 |
| description | string | 任意 | プロジェクト説明 |

### 入力チェック

- nameは必須
- nameは規定文字数以内
- descriptionは規定文字数以内

## 11.3 ProjectSummaryResponse

プロジェクト一覧用DTO。

| 項目 | 型 | 概要 |
|---|---|---|
| projectId | number | プロジェクトID |
| name | string | プロジェクト名 |
| description | string | プロジェクト説明 |
| myProjectRole | string | ログインユーザーの対象プロジェクト内ロール |

システム管理者が参加していないプロジェクトを閲覧する場合、myProjectRoleはnullを許可する。

## 11.4 ProjectListResponse

プロジェクト一覧APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| projects | array | プロジェクト一覧 |

## 11.5 ProjectDetailResponse

プロジェクト詳細APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| projectId | number | プロジェクトID |
| name | string | プロジェクト名 |
| description | string | プロジェクト説明 |
| myProjectRole | string | ログインユーザーの対象プロジェクト内ロール |
| createdAt | string | 作成日時 |
| updatedAt | string | 更新日時 |

## 11.6 ProjectCreateResponse

プロジェクト作成APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| projectId | number | 作成されたプロジェクトID |
| name | string | プロジェクト名 |
| description | string | プロジェクト説明 |

## 11.7 ProjectUpdateResponse

プロジェクト更新APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| projectId | number | プロジェクトID |
| name | string | プロジェクト名 |
| description | string | プロジェクト説明 |

## 11.8 DeleteResponse

削除系APIの共通レスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| deleted | boolean | 削除が完了したか |

削除は論理削除として扱う。

## 12. プロジェクトメンバーDTO

## 12.1 ProjectMemberSummaryResponse

メンバー一覧用DTO。

| 項目 | 型 | 概要 |
|---|---|---|
| memberId | number | プロジェクトメンバーID |
| userId | number | ユーザーID |
| email | string | メールアドレス |
| displayName | string | 表示名 |
| projectRole | string | プロジェクト内ロール |
| joinedAt | string | 参加日時 |

削除済みユーザーの場合、displayNameは `削除済ユーザー` とする。

## 12.2 ProjectMemberListResponse

メンバー一覧APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| items | array | メンバー一覧 |
| page | PageResponse | ページング情報 |

## 12.3 ProjectMemberRoleUpdateRequest

ロール変更APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| projectRole | string | 必須 | 変更後のプロジェクト内ロール |

### 入力チェック

- projectRoleは必須
- projectRoleはPROJECT_ADMIN、MEMBER、VIEWERのいずれか

## 12.4 ProjectMemberRoleUpdateResponse

ロール変更APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| memberId | number | プロジェクトメンバーID |
| userId | number | ユーザーID |
| projectRole | string | 変更後のプロジェクト内ロール |
| displayName | string | 表示名 |
| updatedAt | string | 更新日時 |

## 13. 招待DTO

## 13.1 ProjectInvitationCreateRequest

招待作成APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| email | string | 必須 | 招待先メールアドレス |

招待時の初期ロールはMEMBER固定のため、リクエストには含めない。

### 入力チェック

- emailは必須
- emailはメールアドレス形式

## 13.2 ProjectInvitationCreateResponse

招待作成APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| invitationId | number | 招待ID |
| projectId | number | プロジェクトID |
| email | string | 招待先メールアドレス |
| status | string | 招待状態 |
| expiresAt | string | 有効期限 |
| createdAt | string | 作成日時 |

## 13.3 ProjectInvitationDetailResponse

招待情報取得APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| invitationId | number | 招待ID |
| projectId | number | プロジェクトID |
| projectName | string | プロジェクト名 |
| email | string | 招待先メールアドレス |
| status | string | 招待状態 |
| expiresAt | string | 有効期限 |

## 13.4 InvitationRegisterRequest

未登録ユーザーの招待参加APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| displayName | string | 必須 | 表示名 |
| password | string | 必須 | パスワード |

招待先メールアドレスは招待情報から取得するため、リクエストには含めない。

## 13.5 InvitationAcceptResponse

登録済みユーザーの招待承認APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| accepted | boolean | 招待承認が完了したか |
| projectId | number | 参加したプロジェクトID |
| projectRole | string | 付与されたプロジェクトロール |

## 13.6 InvitationRegisterResponse

未登録ユーザー登録・招待承認APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| accepted | boolean | 招待承認が完了したか |
| user | LoginUserResponse | 登録されたユーザー情報 |
| projectId | number | 参加したプロジェクトID |
| projectRole | string | 付与されたプロジェクトロール |

## 14. タスクDTO

## 14.1 TaskCreateRequest

タスク作成APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| summary | string | 必須 | タスク概要 |
| description | string | 任意 | タスク説明 |
| assigneeId | number | 任意 | 担当者ユーザーID |
| dueDate | string | 任意 | 期限 |
| priority | string | 必須 | Priorityコード |

Statusは初期値としてTODOを設定するため、作成リクエストには含めない。

## 14.2 TaskUpdateRequest

タスク更新APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| summary | string | 必須 | タスク概要 |
| description | string | 任意 | タスク説明 |
| assigneeId | number | 任意 | 担当者ユーザーID |
| dueDate | string | 任意 | 期限 |
| priority | string | 必須 | Priorityコード |

## 14.3 TaskStatusUpdateRequest

タスクステータス変更APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| status | string | 必須 | 変更後Statusコード |

### 入力チェック

- statusはTODO、IN_PROGRESS、REVIEW、DONEのいずれか

## 14.4 UserSummaryResponse

ユーザー要約DTO。

| 項目 | 型 | 概要 |
|---|---|---|
| userId | number | ユーザーID |
| displayName | string | 表示名 |

削除済みユーザーの場合、displayNameは `削除済ユーザー` とする。

## 14.5 TaskSummaryResponse

カンバン表示用タスクDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| taskId | number | タスクID |
| summary | string | タスク概要 |
| assignee | UserSummaryResponse | 担当者 |
| priority | string | Priorityコード |
| dueDate | string | 期限 |
| createdAt | string | 作成日時 |
| updatedAt | string | 更新日時 |

## 14.6 TaskStatusGroupResponse

Status別タスク一覧DTO。

| 項目 | 型 | 概要 |
|---|---|---|
| status | string | Statusコード |
| label | string | Status表示名 |
| tasks | array | 対象Statusのタスク一覧 |

## 14.7 TaskBoardResponse

タスク一覧APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| statuses | array | Status別タスク一覧 |

## 14.8 TaskDetailResponse

タスク詳細APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| taskId | number | タスクID |
| projectId | number | プロジェクトID |
| summary | string | タスク概要 |
| description | string | タスク説明 |
| status | string | Statusコード |
| statusName | string | Status表示名 |
| priority | string | Priorityコード |
| priorityName | string | Priority表示名 |
| assignee | UserSummaryResponse | 担当者 |
| creator | UserSummaryResponse | 作成者 |
| dueDate | string | 期限 |
| comments | array | コメント一覧 |
| attachments | array | 添付ファイル一覧 |
| createdAt | string | 作成日時 |
| updatedAt | string | 更新日時 |

## 14.9 TaskSaveResponse

タスク作成・更新APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| projectId | number | プロジェクトID |
| taskId | number | タスクID |
| summary | string | タスク概要 |
| description | string | タスク説明 |
| status | string | Statusコード |
| priority | string | Priorityコード |
| assignee | UserSummaryResponse | 担当者 |
| creator | UserSummaryResponse | 作成者 |
| dueDate | string | 期限 |
| createdAt | string | 作成日時 |
| updatedAt | string | 更新日時 |

## 15. コメントDTO

## 15.1 CommentCreateRequest

コメント作成APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| body | string | 必須 | コメント本文 |

## 15.2 CommentUpdateRequest

コメント更新APIのリクエストDTO。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| body | string | 必須 | コメント本文 |

## 15.3 CommentResponse

コメントレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| commentId | number | コメントID |
| body | string | コメント本文 |
| author | UserSummaryResponse | 投稿者 |
| taskId | number | タスクID |
| createdAt | string | 作成日時 |
| updatedAt | string | 更新日時 |

## 16. 添付ファイルDTO

## 16.1 AttachmentUploadRequest

添付ファイル追加APIのリクエストDTO。

添付ファイル追加APIでは、JSONではなくmultipart/form-dataを使用する。

| 項目 | 型 | 必須 | 概要 |
|---|---|---|---|
| file | file | 必須 | 添付ファイル |

### 入力チェック

- fileは必須
- 1ファイルあたり5MBまで
- 画像、PDF、テキスト、Office系ファイルのみ許可

## 16.2 AttachmentResponse

添付ファイル情報DTO。

| 項目 | 型 | 概要 |
|---|---|---|
| attachmentId | number | 添付ファイルID |
| fileName | string | ファイル名 |
| fileSize | number | ファイルサイズ |
| contentType | string | ファイル種別 |
| uploadedBy | UserSummaryResponse | アップロード者 |
| createdAt | string | アップロード日時 |

## 16.3 AttachmentUploadResponse

添付ファイル追加APIのレスポンスDTO。

| 項目 | 型 | 概要 |
|---|---|---|
| attachmentId | number | 添付ファイルID |
| taskId | number | タスクID |
| fileName | string | ファイル名 |
| contentType | string | ファイル種別 |
| fileSize | number | ファイルサイズ |
| uploadedBy | UserSummaryResponse | アップロード者 |
| createdAt | string | アップロード日時 |

ファイル本体はJSONレスポンスには含めない。

## 16.4 AttachmentDownloadResponse

添付ファイル取得APIは、DTOではなくファイルデータを返却する。

レスポンスヘッダーで以下を返却する。

| 項目 | 概要 |
|---|---|
| Content-Type | ファイル種別 |
| Content-Disposition | ダウンロード用ファイル名 |
| Content-Length | ファイルサイズ |

## 17. DTOとEntityの対応

| DTO | 主な対応Entity |
|---|---|
| LoginUserResponse | User, SystemRole |
| ProjectSummaryResponse | Project, ProjectMember, ProjectRole |
| ProjectDetailResponse | Project, ProjectMember, ProjectRole |
| ProjectMemberSummaryResponse | ProjectMember, User, ProjectRole |
| ProjectInvitationDetailResponse | ProjectInvitation, Project |
| TaskSummaryResponse | Task, TaskStatus, TaskPriority, User |
| TaskDetailResponse | Task, TaskStatus, TaskPriority, User, TaskComment, Attachment |
| CommentResponse | TaskComment, User |
| AttachmentResponse | Attachment, User |
| PasswordReset系DTO | PasswordResetToken, User |
| ProjectInvitationCreateResponse | ProjectInvitation, Project |
| InvitationAcceptResponse | ProjectInvitation, ProjectMember |
| InvitationRegisterResponse | ProjectInvitation, ProjectMember, User |
| TaskBoardResponse | Task, TaskStatus, TaskPriority, User |
| TaskSaveResponse | Task, TaskStatus, TaskPriority, User |
| ProjectMemberRoleUpdateResponse | ProjectMember, User, ProjectRole |

## 18. DTO変換方針

EntityからResponse DTOへの変換は、Service層または変換専用の処理で行う。

Controllerでは、Entityを直接返却しない。

削除済みユーザーを表示する場合は、Response DTO作成時にdisplayNameを `削除済ユーザー` に変換する。

論理削除済みデータは、通常のResponse DTOには含めない。

## 19. 入力チェック方針

入力チェックはRequest DTOを起点として行う。

形式チェック、必須チェック、文字数チェックはRequest DTOで扱う。

業務ルールに関わるチェックはServiceで扱う。

| チェック内容 | 主な配置 |
|---|---|
| 必須入力 | Request DTO |
| メール形式 | Request DTO |
| 文字数 | Request DTO |
| パスワード形式 | Request DTO |
| Statusの存在確認 | Service |
| Priorityの存在確認 | Service |
| 権限チェック | Service |
| 管理者0人防止 | Service |
| 添付ファイル容量 | Service |
| 添付ファイル種別 | Service |
| 招待URL有効期限 | Service |
| パスワードリセットURL有効期限 | Service |

## 20. 決定事項

- API入出力にはDTOを使用する
- EntityはAPIレスポンスとして直接返却しない
- DTO項目名はcamelCaseを基本とする
- Request DTOは入力チェックの起点とする
- Response DTOには画面表示に必要な項目のみ含める
- 添付ファイル取得APIはJSON DTOではなくファイルデータを返却する
- 削除済みユーザーのdisplayNameは `削除済ユーザー` として返却する

## 21. 未決事項

以下は後続工程で具体化する。

- 正式なJavaクラス名
- DTOのパッケージ配置
- Java上の型
- 日時項目の形式
- 入力チェックの具体的な文字数
- パスワードルール
- DTO変換処理をService内に置くか、専用Mapperに分けるか
- ページングDTOを初期MVPでどの一覧APIに適用するか