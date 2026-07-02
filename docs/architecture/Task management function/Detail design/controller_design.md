# Controller設計書

## 1. 本書の目的

本書は、タスク管理機能の初期MVPにおけるController設計を定義することを目的とする。

Controllerは、HTTPリクエストを受け取り、Request DTOを受け取り、Serviceを呼び出し、Response DTOを返却する層である。

本書では、Controller一覧、各Controllerの責務、APIパス、HTTPメソッド、使用DTO、呼び出すService、認証要否、CSRF要否を整理する。

## 2. 対象範囲

対象に含めるものは以下とする。

- Controller一覧
- Controllerごとの責務
- APIパス
- HTTPメソッド
- 使用するRequest DTO
- 使用するResponse DTO
- 呼び出すService
- 認証要否
- CSRF要否
- Controllerの責務範囲

対象外とするものは以下とする。

- Serviceの業務処理詳細
- RepositoryのDBアクセス詳細
- Entityの項目定義
- DTOの項目定義
- Spring Security設定詳細
- 例外クラス詳細

## 3. Controller設計方針

ControllerはAPIの入口として、HTTPリクエストを受け付ける。

Controllerでは業務ルールを判定しない。

ControllerではDBアクセスを行わない。

Controllerでは認可の最終判断を行わず、Service層で判定する。

ControllerはRequest DTOを受け取り、Serviceを呼び出し、Response DTOを返却する。

入力チェックはRequest DTOを起点として行う。

## 4. Controller一覧

| Controller | 主な責務 |
|---|---|
| AuthController | ログイン、ログアウト、ログインユーザー取得 |
| CsrfController | CSRFトークン取得 |
| PasswordResetController | パスワードリセット申請、再設定 |
| ProjectController | プロジェクト一覧、作成、詳細、更新、削除 |
| ProjectMemberController | メンバー一覧、ロール変更、メンバー削除 |
| ProjectInvitationController | 招待作成、招待情報取得、招待承認 |
| TaskController | タスク一覧、作成、詳細、更新、Status変更、削除 |
| TaskCommentController | コメント作成、更新、削除 |
| AttachmentController | 添付ファイル追加、取得、削除 |

## 5. 共通Controller方針

Controllerで行うことは以下とする。

- APIパスの定義
- HTTPメソッドの定義
- Path Variableの受け取り
- Query Parameterの受け取り
- Request DTOの受け取り
- 入力チェックの起点
- 認証済みユーザー情報の受け取り
- Serviceの呼び出し
- Response DTOの返却

Controllerで行わないことは以下とする。

- 業務ルール判定
- DBアクセス
- Entityの直接返却
- 複雑な認可判定
- トランザクション制御
- DTO変換の詳細処理

## 6. 認証・CSRF方針

認証が必要なAPIでは、セッション情報からログインユーザーを取得する。

未認証の場合は401 Unauthorizedを返却する。

状態変更APIではCSRFトークンを必須とする。

CSRFトークンが不足または不正な場合は403 Forbiddenを返却する。

| HTTPメソッド | CSRF要否 |
|---|---|
| GET | 不要 |
| POST | 必要 |
| PUT | 必要 |
| PATCH | 必要 |
| DELETE | 必要 |

## 7. AuthController

## 7.1 責務

AuthControllerは、認証関連APIを担当する。

ログイン処理はSpring Securityを利用する。

## 7.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request DTO | Response DTO | Service |
|---|---|---|---|---|---|---|---|
| ログイン | POST | /api/auth/login | 不要 | 必要 | LoginRequest | LoginResponse | Spring Security / AuthService |
| ログアウト | POST | /api/auth/logout | 必要 | 必要 | なし | なし | Spring Security |
| ログインユーザー取得 | GET | /api/auth/me | 必要 | 不要 | なし | CurrentUserResponse | AuthService |

## 7.3 補足

ログイン成功時、セッションIDはJSONでは返却しない。

セッションIDはCookieで管理する。

ログアウト成功時はセッションを無効化する。

## 8. CsrfController

## 8.1 責務

CsrfControllerは、フロントエンドがCSRFトークンを取得するためのAPIを担当する。

## 8.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request DTO | Response DTO | Service |
|---|---|---|---|---|---|---|---|
| CSRFトークン取得 | GET | /api/csrf-token | 不要 | 不要 | なし | CsrfTokenResponse | Spring Security |

## 8.3 補足

CSRFトークンの生成と検証はSpring Securityで行う。

CSRFトークン取得APIのパスは `/api/csrf-token` とする。

## 9. PasswordResetController

## 9.1 責務

PasswordResetControllerは、パスワードリセット申請とパスワード再設定APIを担当する。

## 9.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request DTO | Response DTO | Service |
|---|---|---|---|---|---|---|---|
| パスワードリセット申請 | POST | /api/password-reset/request | 不要 | 必要 | PasswordResetRequest | PasswordResetRequestResponse | PasswordResetService |
| パスワード再設定 | POST | /api/password-reset/confirm | 不要 | 必要 | PasswordResetConfirmRequest | PasswordResetConfirmResponse | PasswordResetService |

## 9.3 補足

パスワードリセット申請では、対象メールアドレスの存在有無に関わらず同じレスポンスを返す。

業務ルールはPasswordResetServiceで判定する。

## 10. ProjectController

## 10.1 責務

ProjectControllerは、プロジェクトの一覧取得、作成、詳細取得、更新、削除APIを担当する。

## 10.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request DTO | Response DTO | Service |
|---|---|---|---|---|---|---|---|
| プロジェクト一覧取得 | GET | /api/projects | 必要 | 不要 | なし | ProjectListResponse | ProjectService |
| プロジェクト作成 | POST | /api/projects | 必要 | 必要 | ProjectCreateRequest | ProjectCreateResponse | ProjectService |
| プロジェクト詳細取得 | GET | /api/projects/{projectId} | 必要 | 不要 | なし | ProjectDetailResponse | ProjectService |
| プロジェクト更新 | PUT | /api/projects/{projectId} | 必要 | 必要 | ProjectUpdateRequest | ProjectUpdateResponse | ProjectService |
| プロジェクト削除 | DELETE | /api/projects/{projectId} | 必要 | 必要 | なし | DeleteResponse | ProjectService |

## 10.3 Path Variable

| 項目 | 概要 |
|---|---|
| projectId | プロジェクトID |

## 10.4 補足

プロジェクト作成権限はService層でSYSTEM_ADMINか確認する。

プロジェクト更新・削除権限はService層で判定する。

## 11. ProjectMemberController

## 11.1 責務

ProjectMemberControllerは、プロジェクトメンバー一覧、ロール変更、メンバー削除APIを担当する。

## 11.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request DTO | Response DTO | Service |
|---|---|---|---|---|---|---|---|
| メンバー一覧取得 | GET | /api/projects/{projectId}/members | 必要 | 不要 | なし | ProjectMemberListResponse | ProjectMemberService |
| ロール変更 | PUT | /api/projects/{projectId}/members/{memberId}/role | 必要 | 必要 | ProjectMemberRoleUpdateRequest | ProjectMemberRoleUpdateResponse | ProjectMemberService |
| メンバー削除 | DELETE | /api/projects/{projectId}/members/{memberId} | 必要 | 必要 | なし | DeleteResponse | ProjectMemberService |

## 11.3 Path Variable

| 項目 | 概要 |
|---|---|
| projectId | プロジェクトID |
| memberId | プロジェクトメンバーID |

## 11.4 補足

PROJECT_ADMINが0人になる操作は禁止する。

判定はProjectMemberServiceで行う。

## 12. ProjectInvitationController

## 12.1 責務

ProjectInvitationControllerは、プロジェクト招待の作成、招待情報取得、未登録ユーザー登録・招待承認、登録済みユーザー招待承認APIを担当する。

## 12.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request DTO | Response DTO | Service |
|---|---|---|---|---|---|---|---|
| 招待作成 | POST | /api/projects/{projectId}/invitations | 必要 | 必要 | ProjectInvitationCreateRequest | ProjectInvitationCreateResponse | ProjectInvitationService |
| 招待情報取得 | GET | /api/invitations/{token} | 不要 | 不要 | なし | ProjectInvitationDetailResponse | ProjectInvitationService |
| 未登録ユーザー登録・招待承認 | POST | /api/invitations/{token}/register | 不要 | 必要 | InvitationRegisterRequest | InvitationRegisterResponse | ProjectInvitationService |
| 招待承認 | POST | /api/invitations/{token}/accept | 必要 | 必要 | なし | InvitationAcceptResponse | ProjectInvitationService |

## 12.3 Path Variable

| 項目 | 概要 |
|---|---|
| projectId | プロジェクトID |
| token | 招待URL用トークン |

## 12.4 補足

招待作成時の初期ロールはMEMBER固定とする。

招待承認時の有効期限、status、メールアドレス一致確認はProjectInvitationServiceで行う。

## 13. TaskController

## 13.1 責務

TaskControllerは、タスク一覧、作成、詳細、更新、Status変更、削除APIを担当する。

## 13.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request DTO | Response DTO | Service |
|---|---|---|---|---|---|---|---|
| タスク一覧取得 | GET | /api/projects/{projectId}/tasks | 必要 | 不要 | なし | TaskBoardResponse | TaskService |
| タスク作成 | POST | /api/projects/{projectId}/tasks | 必要 | 必要 | TaskCreateRequest | TaskSaveResponse | TaskService |
| タスク詳細取得 | GET | /api/projects/{projectId}/tasks/{taskId} | 必要 | 不要 | なし | TaskDetailResponse | TaskService |
| タスク更新 | PUT | /api/projects/{projectId}/tasks/{taskId} | 必要 | 必要 | TaskUpdateRequest | TaskSaveResponse | TaskService |
| Status変更 | PATCH | /api/projects/{projectId}/tasks/{taskId}/status | 必要 | 必要 | TaskStatusUpdateRequest | TaskSaveResponse | TaskService |
| タスク削除 | DELETE | /api/projects/{projectId}/tasks/{taskId} | 必要 | 必要 | なし | DeleteResponse | TaskService |

## 13.3 Path Variable

| 項目 | 概要 |
|---|---|
| projectId | プロジェクトID |
| taskId | タスクID |

## 13.4 補足

タスク作成時の初期StatusはTODOとする。

Status変更はカンバン画面のドラッグアンドドロップで使用する。

## 14. TaskCommentController

## 14.1 責務

TaskCommentControllerは、コメント作成、更新、削除APIを担当する。

## 14.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request DTO | Response DTO | Service |
|---|---|---|---|---|---|---|---|
| コメント作成 | POST | /api/projects/{projectId}/tasks/{taskId}/comments | 必要 | 必要 | CommentCreateRequest | CommentResponse | TaskCommentService |
| コメント更新 | PUT | /api/projects/{projectId}/tasks/{taskId}/comments/{commentId} | 必要 | 必要 | CommentUpdateRequest | CommentResponse | TaskCommentService |
| コメント削除 | DELETE | /api/projects/{projectId}/tasks/{taskId}/comments/{commentId} | 必要 | 必要 | なし | DeleteResponse | TaskCommentService |

## 14.3 Path Variable

| 項目 | 概要 |
|---|---|
| projectId | プロジェクトID |
| taskId | タスクID |
| commentId | コメントID |

## 14.4 補足

コメント更新は投稿者本人のみ可能とする。

判定はTaskCommentServiceで行う。

## 15. AttachmentController

## 15.1 責務

AttachmentControllerは、添付ファイル追加、取得、削除APIを担当する。

## 15.2 API一覧

| 処理 | メソッド | パス | 認証 | CSRF | Request | Response | Service |
|---|---|---|---|---|---|---|---|
| 添付ファイル追加 | POST | /api/projects/{projectId}/tasks/{taskId}/attachments | 必要 | 必要 | multipart/form-data | AttachmentUploadResponse | AttachmentService |
| 添付ファイル取得 | GET | /api/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId} | 必要 | 不要 | なし | ファイルデータ | AttachmentService |
| 添付ファイル削除 | DELETE | /api/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId} | 必要 | 必要 | なし | DeleteResponse | AttachmentService |

## 15.3 Path Variable

| 項目 | 概要 |
|---|---|
| projectId | プロジェクトID |
| taskId | タスクID |
| attachmentId | 添付ファイルID |

## 15.4 補足

添付ファイル追加ではJSONではなくmultipart/form-dataを使用する。

添付ファイル取得ではJSONではなくファイルデータを返却する。

ファイル本体はDBから取得する。

## 16. 入力チェック方針

ControllerではRequest DTOを受け取り、入力チェックの起点とする。

必須チェック、形式チェック、文字数チェックはRequest DTOを中心に扱う。

業務ルールに関わるチェックはServiceで行う。

| チェック内容 | 主な担当 |
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

## 17. エラーレスポンス方針

Controllerで発生した例外は、共通例外ハンドリングでエラーレスポンスへ変換する。

Controllerごとに独自のエラーレスポンス形式を作らない。

主なHTTPステータスは以下とする。

| ステータス | 用途 |
|---|---|
| 400 | 入力不正 |
| 401 | 未認証 |
| 403 | 権限不足、CSRF不正 |
| 404 | 対象データなし |
| 409 | 業務ルール違反、競合 |
| 500 | サーバーエラー |

詳細は例外設計書で定義する。

## 18. ControllerとServiceの責務分担

| 処理 | Controller | Service |
|---|---|---|
| HTTPメソッド定義 | 行う | 行わない |
| APIパス定義 | 行う | 行わない |
| Path Variable受け取り | 行う | 行わない |
| Request DTO受け取り | 行う | 行わない |
| 業務ルール判定 | 行わない | 行う |
| 認可判定 | 行わない | 行う |
| トランザクション制御 | 行わない | 行う |
| Repository呼び出し | 行わない | 行う |
| Response DTO返却 | 行う | 作成する |

## 19. 決定事項

- ControllerはAPIの入口を担当する
- Controllerは業務ルールを持たない
- ControllerはRepositoryを直接呼び出さない
- ControllerはEntityを直接返却しない
- ControllerはServiceを呼び出す
- Request DTOを入力チェックの起点とする
- Response DTOを返却する
- 状態変更APIではCSRFトークンを必須とする
- 添付ファイル取得APIのみファイルデータを返却する
- Controllerクラス名は本書のController一覧に記載した名称を基本とする
- Controllerでは、Spring Securityで認証済みのユーザー情報を取得し、ServiceへログインユーザーIDまたはログインユーザー情報を渡す。具体的な取得方法はSpring Securityの標準的な仕組みを利用する。

## 20. 未決事項

以下は後続工程で具体化する。

- Controllerメソッド名
- 入力チェックエラーの詳細形式
- 例外ハンドリングの実装詳細
- 添付ファイル取得時のレスポンスヘッダー詳細