# Service設計書

## 1. 本書の目的

本書は、タスク管理機能の初期MVPにおけるService設計を定義することを目的とする。

Serviceは、業務ルール、認可判定、トランザクション制御、Repository呼び出し、DTO変換方針を扱う層である。

本書では、Service一覧、各Serviceの責務、主な処理、認可方針、トランザクション方針、論理削除方針を整理する。

## 2. 対象範囲

対象に含めるものは以下とする。

- Service一覧
- Serviceごとの責務
- Serviceごとの主な処理
- 認可判定方針
- トランザクション方針
- 論理削除方針
- 招待処理方針
- パスワードリセット処理方針
- 添付ファイル処理方針
- DTO変換方針

対象外とするものは以下とする。

- ControllerのAPI定義
- Repositoryメソッドの実装詳細
- Entityの項目定義
- DTOの項目定義
- 例外クラスの詳細
- Spring Security設定詳細

## 3. Service設計方針

Serviceは業務処理の中心とする。

ControllerはServiceを呼び出すのみとし、業務ルールを持たない。

RepositoryはDBアクセスのみを担当し、業務ルールを持たない。

認可判定はService層で必ず行う。

更新系処理ではトランザクションを使用する。

EntityをAPIレスポンスとして直接返却せず、Response DTOへ変換して返却する。

## 4. Service一覧

| Service | 主な責務 |
|---|---|
| AuthService | ログインユーザー情報取得、ログアウト補助 |
| PasswordResetService | パスワードリセット申請、再設定 |
| ProjectService | プロジェクト一覧、作成、詳細、更新、削除 |
| ProjectMemberService | メンバー一覧、ロール変更、メンバー削除 |
| ProjectInvitationService | 招待作成、招待情報取得、招待承認 |
| TaskService | タスク一覧、作成、詳細、更新、Status変更、削除 |
| TaskCommentService | コメント作成、更新、削除 |
| AttachmentService | 添付ファイル追加、取得、削除 |
| MasterDataService | 固定マスタ取得 |
| MailService | メール送信処理 |

## 5. 共通Service方針

Serviceでは以下を共通して行う。

- ログインユーザーの取得
- 対象データの存在確認
- 論理削除済みデータの除外確認
- 認可判定
- 業務ルール判定
- Repository呼び出し
- Entity更新
- DTO変換
- 必要に応じたメール送信

Serviceでは以下を行わない。

- HTTPリクエストの直接処理
- HTTPステータスの直接制御
- DBアクセスの直接実装
- 画面表示制御
- Spring Security設定

## 6. 認可判定方針

本システムでは、以下の2種類のロールを扱う。

| 種別 | ロール |
|---|---|
| システムロール | SYSTEM_ADMIN / USER |
| プロジェクトロール | PROJECT_ADMIN / MEMBER / VIEWER |

Serviceでは、操作ごとに必要なロールを確認する。

| 操作 | 許可ロール |
|---|---|
| プロジェクト作成 | SYSTEM_ADMIN |
| プロジェクト一覧取得 | SYSTEM_ADMINまたは参加済みユーザー |
| プロジェクト詳細取得 | SYSTEM_ADMINまたは対象プロジェクト参加者 |
| プロジェクト更新 | SYSTEM_ADMINまたはPROJECT_ADMIN |
| プロジェクト削除 | SYSTEM_ADMINまたはPROJECT_ADMIN |
| メンバー一覧取得 | SYSTEM_ADMINまたは対象プロジェクト参加者 |
| メンバー招待 | SYSTEM_ADMINまたはPROJECT_ADMIN |
| ロール変更 | SYSTEM_ADMINまたはPROJECT_ADMIN |
| メンバー削除 | SYSTEM_ADMINまたはPROJECT_ADMIN |
| タスク作成 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| タスク更新 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| タスクStatus変更 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| タスク削除 | SYSTEM_ADMINまたはPROJECT_ADMIN |
| コメント作成 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| コメント更新 | コメント投稿者本人 |
| コメント削除 | SYSTEM_ADMINまたはPROJECT_ADMIN |
| 添付ファイル追加 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| 添付ファイル削除 | SYSTEM_ADMINまたはPROJECT_ADMIN |

## 7. トランザクション方針

更新系処理ではトランザクションを使用する。

対象となる処理は以下とする。

- プロジェクト作成
- プロジェクト更新
- プロジェクト削除
- メンバー招待
- ロール変更
- メンバー削除
- 招待承認
- 未登録ユーザー登録・招待承認
- タスク作成
- タスク更新
- タスクStatus変更
- タスク削除
- コメント作成
- コメント更新
- コメント削除
- 添付ファイル追加
- 添付ファイル削除
- パスワードリセット申請
- パスワード再設定

参照系処理では、必要に応じて読み取り専用トランザクションを検討する。

## 8. DTO変換方針

Serviceは、Repositoryから取得したEntityをResponse DTOへ変換して返却する。

ControllerへEntityを直接返却しない。

削除済みユーザーを表示する場合、displayNameは `削除済ユーザー` に変換する。

論理削除済みデータは、通常のResponse DTOには含めない。

DTO変換処理をService内に置くか、専用Mapperに分けるかは後続工程で決定する。

## 9. AuthService

## 9.1 責務

AuthServiceは、認証済みユーザーに関する情報取得を担当する。

ログイン処理自体はSpring Securityを利用する。

## 9.2 主な処理

| 処理 | 概要 |
|---|---|
| ログインユーザー取得 | セッション上のユーザー情報からログインユーザーDTOを返す |
| ログアウト補助 | ログアウト後に必要な補助処理を行う |

## 9.3 業務ルール

- 未認証の場合は認証エラーとする
- 削除済みユーザーはログイン済みユーザーとして扱わない
- セッションIDはレスポンスJSONには含めない

## 10. PasswordResetService

## 10.1 責務

PasswordResetServiceは、パスワードリセット申請と再設定を担当する。

## 10.2 主な処理

| 処理 | 概要 |
|---|---|
| パスワードリセット申請 | メールアドレスを受け取り、リセットURLを送信する |
| パスワード再設定 | tokenを検証し、新しいパスワードを保存する |
| 期限切れ判定 | tokenの有効期限を確認する |
| 使用済み判定 | 使用済みtokenの再利用を防ぐ |

## 10.3 使用Repository

- UserRepository
- PasswordResetTokenRepository

## 10.4 業務ルール

- パスワードリセットURLの有効期限は30分
- tokenは推測困難な値とする
- 使用済みtokenは再利用できない
- 期限切れtokenは利用できない
- パスワードはハッシュ化して保存する
- 対象メールアドレスの存在有無に関わらず、申請APIは同じレスポンスを返す
- 同一ユーザーにACTIVEなtokenが存在する場合の扱いは後続工程で具体化する
- 論理削除済みユーザーはパスワードリセット対象外とする

## 10.5 レスポンス

| 処理 | Response DTO |
|---|---|
| パスワードリセット申請 | PasswordResetRequestResponse |
| パスワード再設定 | PasswordResetConfirmResponse |

## 11. ProjectService

## 11.1 責務

ProjectServiceは、プロジェクトの一覧取得、作成、詳細取得、更新、削除を担当する。

## 11.2 主な処理

| 処理 | 概要 |
|---|---|
| プロジェクト一覧取得 | ログインユーザーが閲覧可能なプロジェクト一覧を返す |
| プロジェクト作成 | SYSTEM_ADMINがプロジェクトを作成する |
| プロジェクト詳細取得 | 対象プロジェクトの詳細を返す |
| プロジェクト更新 | プロジェクト名、説明を更新する |
| プロジェクト削除 | プロジェクトを論理削除する |

## 11.3 使用Repository

- ProjectRepository
- ProjectMemberRepository
- ProjectRoleRepository
- TaskRepository
- TaskCommentRepository
- AttachmentRepository

## 11.4 業務ルール

- プロジェクト作成はSYSTEM_ADMINのみ可能
- プロジェクト作成直後、作成者をPROJECT_ADMINとしてProjectMemberに登録する
- SYSTEM_ADMINは全プロジェクトを閲覧できる
- 通常ユーザーは参加済みプロジェクトのみ閲覧できる
- プロジェクト更新はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- プロジェクト削除はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- プロジェクト削除時は、関連するProjectMember、Task、TaskComment、Attachmentも論理削除する
- ProjectInvitationは履歴として残す

## 11.5 トランザクション

| 処理 | トランザクション |
|---|---|
| プロジェクト作成 | 必要 |
| プロジェクト更新 | 必要 |
| プロジェクト削除 | 必要 |
| プロジェクト一覧取得 | 読み取り専用を検討 |
| プロジェクト詳細取得 | 読み取り専用を検討 |

## 11.6 レスポンス

| 処理 | Response DTO |
|---|---|
| プロジェクト一覧取得 | ProjectListResponse |
| プロジェクト詳細取得 | ProjectDetailResponse |
| プロジェクト作成 | ProjectCreateResponse |
| プロジェクト更新 | ProjectUpdateResponse |
| プロジェクト削除 | DeleteResponse |

## 12. ProjectMemberService

## 12.1 責務

ProjectMemberServiceは、プロジェクトメンバー一覧、ロール変更、メンバー削除を担当する。

## 12.2 主な処理

| 処理 | 概要 |
|---|---|
| メンバー一覧取得 | 対象プロジェクトの参加メンバー一覧を返す |
| ロール変更 | 対象メンバーのプロジェクトロールを変更する |
| メンバー削除 | 対象メンバーを論理削除する |

## 12.3 使用Repository

- ProjectRepository
- ProjectMemberRepository
- ProjectRoleRepository
- UserRepository

## 12.4 業務ルール

- メンバー一覧はSYSTEM_ADMINまたは対象プロジェクト参加者が閲覧できる
- ロール変更はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- メンバー削除はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- PROJECT_ADMINが0人になるロール変更は禁止する
- PROJECT_ADMINが0人になるメンバー削除は禁止する
- 論理削除済みProjectMemberは通常メンバーとして扱わない
- 論理削除済みProjectMemberを再招待・再参加させる場合は、既存レコードを復元する方針を基本とする

## 12.5 トランザクション

| 処理 | トランザクション |
|---|---|
| ロール変更 | 必要 |
| メンバー削除 | 必要 |
| メンバー一覧取得 | 読み取り専用を検討 |

## 12.6 レスポンス

| 処理 | Response DTO |
|---|---|
| メンバー一覧取得 | ProjectMemberListResponse |
| ロール変更 | ProjectMemberRoleUpdateResponse |
| メンバー削除 | DeleteResponse |

## 13. ProjectInvitationService

## 13.1 責務

ProjectInvitationServiceは、プロジェクト招待の作成、招待情報取得、招待承認を担当する。

## 13.2 主な処理

| 処理 | 概要 |
|---|---|
| 招待作成 | メールアドレスを指定してプロジェクトへ招待する |
| 招待情報取得 | tokenから招待情報を取得する |
| 未登録ユーザー登録・招待承認 | 招待URLからユーザー登録し、プロジェクトに参加する |
| 招待承認 | 登録済みユーザーが招待を承認し、プロジェクトに参加する |

## 13.3 使用Repository

- ProjectRepository
- ProjectMemberRepository
- ProjectRoleRepository
- ProjectInvitationRepository
- UserRepository

## 13.4 使用Service

- MailService

## 13.5 業務ルール

- 招待作成はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- 招待URLの有効期限は24時間
- 招待時の初期ロールはMEMBER固定
- 招待状態はPENDINGで作成する
- 同一プロジェクト・同一メールのPENDING招待がある場合、重複招待は行わない
- 承認済み、期限切れ、無効化済みの招待は利用できない
- 論理削除済みプロジェクトに対する招待は利用できない
- 未登録ユーザーは招待URLからユーザー登録後、対象プロジェクトに参加できる
- 登録済みユーザーの招待承認では、招待先メールアドレスとログインユーザーのメールアドレスが一致すること
- 招待承認後、ProjectMemberを作成または復元する
- 招待承認後、招待状態はACCEPTEDに変更する
- 未登録ユーザー登録・招待承認では、招待先emailのユーザーが既に存在する場合は新規登録せず、登録済みユーザー向けの招待承認を利用させる

## 13.6 トランザクション

| 処理 | トランザクション |
|---|---|
| 招待作成 | 必要 |
| 未登録ユーザー登録・招待承認 | 必要 |
| 招待承認 | 必要 |
| 招待情報取得 | 読み取り専用を検討 |

## 13.7 レスポンス

| 処理 | Response DTO |
|---|---|
| 招待作成 | ProjectInvitationCreateResponse |
| 招待情報取得 | ProjectInvitationDetailResponse |
| 未登録ユーザー登録・招待承認 | InvitationRegisterResponse |
| 招待承認 | InvitationAcceptResponse |

## 14. TaskService

## 14.1 責務

TaskServiceは、タスク一覧、作成、詳細、更新、Status変更、削除を担当する。

## 14.2 主な処理

| 処理 | 概要 |
|---|---|
| タスク一覧取得 | カンバン表示用にStatus別タスク一覧を返す |
| タスク作成 | 対象プロジェクトにタスクを作成する |
| タスク詳細取得 | タスク詳細、コメント、添付ファイルを返す |
| タスク更新 | タスク概要、説明、担当者、期限、Priorityを更新する |
| Status変更 | ドラッグアンドドロップ等によりStatusを変更する |
| タスク削除 | タスクを論理削除する |

## 14.3 使用Repository

- ProjectRepository
- ProjectMemberRepository
- TaskRepository
- TaskStatusRepository
- TaskPriorityRepository
- TaskCommentRepository
- AttachmentRepository
- UserRepository

## 14.4 業務ルール

- タスク一覧・詳細はSYSTEM_ADMINまたは対象プロジェクト参加者が閲覧できる
- タスク作成・更新・Status変更はSYSTEM_ADMIN、PROJECT_ADMIN、MEMBERが可能
- タスク削除はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- VIEWERは閲覧のみ可能
- タスク作成時の初期StatusはTODOとする
- StatusはTODO、IN_PROGRESS、REVIEW、DONEのいずれか
- PriorityはHIGHEST、HIGH、MEDIUM、LOW、LOWESTのいずれか
- 論理削除済みタスクは通常表示しない
- 論理削除済みプロジェクト配下のタスクは操作対象外とする

## 14.5 トランザクション

| 処理 | トランザクション |
|---|---|
| タスク作成 | 必要 |
| タスク更新 | 必要 |
| Status変更 | 必要 |
| タスク削除 | 必要 |
| タスク一覧取得 | 読み取り専用を検討 |
| タスク詳細取得 | 読み取り専用を検討 |

## 14.6 レスポンス

| 処理 | Response DTO |
|---|---|
| タスク一覧取得 | TaskBoardResponse |
| タスク詳細取得 | TaskDetailResponse |
| タスク作成 | TaskSaveResponse |
| タスク更新 | TaskSaveResponse |
| Status変更 | TaskSaveResponse |
| タスク削除 | DeleteResponse |

## 15. TaskCommentService

## 15.1 責務

TaskCommentServiceは、コメント作成、更新、削除を担当する。

## 15.2 主な処理

| 処理 | 概要 |
|---|---|
| コメント作成 | タスクにコメントを追加する |
| コメント更新 | 自分のコメントを更新する |
| コメント削除 | コメントを論理削除する |

## 15.3 使用Repository

- ProjectRepository
- ProjectMemberRepository
- TaskRepository
- TaskCommentRepository

## 15.4 業務ルール

- コメント作成はSYSTEM_ADMIN、PROJECT_ADMIN、MEMBERが可能
- コメント更新時は、対象Project、Task、TaskCommentが論理削除済みでないことを確認する
- コメント更新時は、投稿者本人であっても対象プロジェクトへアクセス可能であることを確認する
- コメント削除はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- VIEWERはコメント閲覧のみ可能
- 論理削除済みコメントは通常表示しない
- 論理削除済みタスクにはコメントを追加できない

## 15.5 トランザクション

| 処理 | トランザクション |
|---|---|
| コメント作成 | 必要 |
| コメント更新 | 必要 |
| コメント削除 | 必要 |

## 15.6 レスポンス

| 処理 | Response DTO |
|---|---|
| コメント作成 | CommentResponse |
| コメント更新 | CommentResponse |
| コメント削除 | DeleteResponse |

## 16. AttachmentService

## 16.1 責務

AttachmentServiceは、添付ファイル追加、取得、削除を担当する。

## 16.2 主な処理

| 処理 | 概要 |
|---|---|
| 添付ファイル追加 | タスクに添付ファイルを追加する |
| 添付ファイル取得 | 添付ファイル本体を返却する |
| 添付ファイル削除 | 添付ファイルを論理削除する |

## 16.3 使用Repository

- ProjectRepository
- ProjectMemberRepository
- TaskRepository
- AttachmentRepository

## 16.4 業務ルール

- 添付ファイル追加はSYSTEM_ADMIN、PROJECT_ADMIN、MEMBERが可能
- 添付ファイル取得はSYSTEM_ADMINまたは対象プロジェクト参加者が可能
- 添付ファイル削除はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- 1ファイルあたり5MBまで
- 添付可能なファイル種別は画像、PDF、テキスト、Office系ファイル
- ファイル本体はDBに保存する
- 論理削除済み添付ファイルは取得できない
- 論理削除済みタスクには添付できない
- 添付ファイル操作時は、対象ProjectとTaskが論理削除済みでないことを確認する

## 16.5 トランザクション

| 処理 | トランザクション |
|---|---|
| 添付ファイル追加 | 必要 |
| 添付ファイル削除 | 必要 |
| 添付ファイル取得 | 読み取り専用を検討 |

## 16.6 レスポンス

| 処理 | Response DTO |
|---|---|
| 添付ファイル追加 | AttachmentUploadResponse |
| 添付ファイル取得 | ファイルデータ |
| 添付ファイル削除 | DeleteResponse |

## 17. MasterDataService

## 17.1 責務

MasterDataServiceは、固定マスタの取得を担当する。

## 17.2 対象マスタ

- SystemRole
- ProjectRole
- TaskStatus
- TaskPriority

## 17.3 業務ルール

- 初期MVPでは固定マスタを画面やAPIから変更しない
- StatusとPriorityはcodeで判定する
- 表示順が必要なマスタはsort_order順で返却する

## 18. MailService

## 18.1 責務

MailServiceは、メール送信処理を担当する。

## 18.2 主な処理

| 処理 | 概要 |
|---|---|
| パスワードリセットメール送信 | リセットURLを含むメールを送信する |
| プロジェクト招待メール送信 | 招待URLを含むメールを送信する |

## 18.3 業務ルール

- 開発環境ではMailpitでメール内容を確認する
- 本番向けメール送信方式は後続工程で具体化する
- メール本文には必要なURLと有効期限を含める

## 19. 共通認可補助方針

Serviceでは、以下の認可補助処理を共通化することを検討する。

| 判定 | 概要 |
|---|---|
| isSystemAdmin | SYSTEM_ADMINか判定する |
| getProjectRole | 対象プロジェクト内ロールを取得する |
| canManageProject | プロジェクト管理可能か判定する |
| canEditTask | タスク作成・編集可能か判定する |
| canDeleteTask | タスク削除可能か判定する |
| canViewProject | プロジェクト閲覧可能か判定する |

具体的な共通化方法は後続工程で決定する。

## 20. 例外方針

Serviceでは、業務ルール違反や対象データなしを検出する。

例外の詳細は例外設計書で定義する。

主な例外候補は以下とする。

| 例外 | 主な発生箇所 |
|---|---|
| 認証エラー | 未ログイン |
| 認可エラー | 権限不足 |
| 入力不正 | Request DTOの入力値不正 |
| 対象データなし | 対象Entityが存在しない |
| 業務ルール違反 | 管理者0人禁止、期限切れtoken、重複招待など |
| ファイル制約違反 | 容量超過、許可されないファイル種別 |

## 21. 決定事項

- Serviceは業務ルールを担当する
- 認可判定はService層で必ず行う
- 更新系処理ではトランザクションを使用する
- RepositoryはServiceから呼び出す
- EntityはControllerへ直接返さず、Response DTOへ変換する
- 論理削除はService層で制御する
- メール送信はMailServiceに分離する
- パスワードリセットと招待はtokenとstatusで管理する

## 22. 未決事項

以下は後続工程で具体化する。

- 正式なServiceクラス名
- Serviceメソッド名
- DTO変換処理をService内に置くか、専用Mapperに分けるか
- 認可補助処理を共通Serviceに分けるか
- 例外クラスの詳細
- パスワードルール
- 同一ユーザーにACTIVEなパスワードリセットtokenが存在する場合の扱い
- 招待期限切れ、パスワードリセット期限切れを自動更新するか、参照時に判定するか
- 本番向けメール送信方式