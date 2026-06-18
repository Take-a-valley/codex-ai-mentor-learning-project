# API設計書

## 1. 本書の目的

本書は、要件定義書、基本設計書、DB設計書をもとに、タスク管理機能で必要となるAPIの構成、エンドポイント、認証・認可、リクエスト、レスポンス、エラー方針を整理することを目的とする。

後続工程である詳細設計、実装、テスト設計へ進むために、フロントエンドとバックエンドの責務境界を明確にする。

## 2. 対象範囲

本書では、初期MVPであるカンバン方式タスク管理機能に必要なAPIを対象とする。

対象APIは以下とする。

- 認証API
- パスワードリセットAPI
- プロジェクトAPI
- プロジェクトメンバーAPI
- 招待API
- タスクAPI
- コメントAPI
- 添付ファイルAPI

以下は初期MVPでは対象外とする。

- Wiki API
- WBS、ガントチャート API
- プロジェクト内チャット API
- 通知 API
- 外部ストレージ連携 API

## 3. API設計方針

APIはREST形式を基本とする。

URLはリソース単位で設計する。

認証が必要なAPIでは、ログイン済みユーザーを識別し、操作対象リソースに対する権限を確認する。

権限のない操作はバックエンド側で拒否する。

削除操作は物理削除ではなく論理削除として扱う。

レスポンスはJSON形式を基本とする。  
添付ファイルのダウンロードのみ、ファイルデータを返す。

認証方式はセッション方式を採用する。

ログイン成功時にサーバー側でセッションを作成し、ブラウザにはセッション識別用Cookieを保持させる。

ログイン後のAPIでは、Cookieをもとにログイン済みユーザーを判定する。

## 4. 認証・認可方針

### 認証

ログインAPIで認証を行う。

認証済みユーザーのみ、プロジェクト、タスク、コメント、添付ファイルなどのAPIを利用できる。

認証方式はセッション方式とする。

ログイン成功後、サーバー側でセッションを管理する。

ログアウト時は、サーバー側のセッションを破棄する。

認証が必要なAPIでは、セッション情報からログイン済みユーザーを取得する。

### 認可

本システムでは、以下のロールを扱う。

- システム管理者
- プロジェクト管理者
- メンバー
- 閲覧者

システム管理者は、全プロジェクトの閲覧および管理操作ができる。

プロジェクト管理者は、対象プロジェクト内の管理操作ができる。

メンバーは、対象プロジェクト内のタスク操作、コメント操作、添付ファイル追加ができる。

閲覧者は、対象プロジェクト内の閲覧のみできる。

APIでは、以下を確認する。

- ログイン済みか
- 対象プロジェクトに参加しているか
- 必要なロールを持っているか
- 自分のコメントなど、本人条件を満たしているか

## 5. 共通仕様

### 共通レスポンス形式

通常レスポンスはJSON形式とする。

### 共通エラーレスポンス

| HTTPステータス | 用途 |
|---|---|
| 400 | リクエスト不正 |
| 401 | 未認証 |
| 403 | 権限不足 |
| 404 | 対象データなし |
| 409 | 業務制約違反 |
| 413 | ファイルサイズ超過 |
| 415 | 許可されていないファイル種別 |
| 500 | サーバーエラー |

### 共通エラー項目

| 項目 | 内容 |
|---|---|
| code | エラーコード |
| message | エラーメッセージ |
| details | 詳細情報 |

## 6. API一覧

| 分類 | メソッド | パス | 概要 | 認証 |
|---|---|---|---|---|
| 認証 | POST | /api/auth/login | ログイン | 不要 |
| 認証 | POST | /api/auth/logout | ログアウト | 必要 |
| 認証 | GET | /api/auth/me | ログインユーザー取得 | 必要 |
| パスワードリセット | POST | /api/password-reset/request | リセット申請 | 不要 |
| パスワードリセット | POST | /api/password-reset/confirm | パスワード再設定 | 不要 |
| プロジェクト | GET | /api/projects | プロジェクト一覧取得 | 必要 |
| プロジェクト | POST | /api/projects | プロジェクト作成 | 必要 |
| プロジェクト | GET | /api/projects/{projectId} | プロジェクト詳細取得 | 必要 |
| プロジェクト | PUT | /api/projects/{projectId} | プロジェクト更新 | 必要 |
| プロジェクト | DELETE | /api/projects/{projectId} | プロジェクト削除 | 必要 |
| メンバー | GET | /api/projects/{projectId}/members | メンバー一覧取得 | 必要 |
| メンバー | PUT | /api/projects/{projectId}/members/{memberId}/role | ロール変更 | 必要 |
| メンバー | DELETE | /api/projects/{projectId}/members/{memberId} | メンバー削除 | 必要 |
| 招待 | POST | /api/projects/{projectId}/invitations | 招待作成 | 必要 |
| 招待 | GET | /api/invitations/{token} | 招待情報取得 | 不要 |
| 招待 | POST | /api/invitations/{token}/register | 未登録ユーザー登録・招待承認 | 不要 |
| 招待 | POST | /api/invitations/{token}/accept | 招待承認 | 必要 |
| タスク | GET | /api/projects/{projectId}/tasks | タスク一覧取得 | 必要 |
| タスク | POST | /api/projects/{projectId}/tasks | タスク作成 | 必要 |
| タスク | GET | /api/projects/{projectId}/tasks/{taskId} | タスク詳細取得 | 必要 |
| タスク | PUT | /api/projects/{projectId}/tasks/{taskId} | タスク更新 | 必要 |
| タスク | PATCH | /api/projects/{projectId}/tasks/{taskId}/status | タスクステータス変更 | 必要 |
| タスク | DELETE | /api/projects/{projectId}/tasks/{taskId} | タスク削除 | 必要 |
| コメント | POST | /api/projects/{projectId}/tasks/{taskId}/comments | コメント作成 | 必要 |
| コメント | PUT | /api/projects/{projectId}/tasks/{taskId}/comments/{commentId} | コメント更新 | 必要 |
| コメント | DELETE | /api/projects/{projectId}/tasks/{taskId}/comments/{commentId} | コメント削除 | 必要 |
| 添付 | POST | /api/projects/{projectId}/tasks/{taskId}/attachments | 添付ファイル追加 | 必要 |
| 添付 | GET | /api/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId} | 添付ファイル取得 | 必要 |
| 添付 | DELETE | /api/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId} | 添付ファイル削除 | 必要 |

## 7. 認証API

### 7.1 ログイン

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/auth/login |
| 認証 | 不要 |
| 概要 | メールアドレスとパスワードでログインする |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| email | 必須 | メールアドレス |
| password | 必須 | パスワード |

#### レスポンス

| 項目 | 内容 |
|---|---|
| userId | ユーザーID |
| email | メールアドレス |
| displayName | 表示名 |
| systemRole | システムロール |

#### 主なエラー

| HTTPステータス | 内容 |
|---|---|
| 400 | 入力不正 |
| 401 | メールアドレスまたはパスワードが不正 |

### 7.2 ログアウト

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/auth/logout |
| 認証 | 必要 |
| 概要 | ログアウトする |

### 7.3 ログインユーザー取得

| 項目 | 内容 |
|---|---|
| メソッド | GET |
| パス | /api/auth/me |
| 認証 | 必要 |
| 概要 | 現在ログインしているユーザー情報を取得する |

## 8. パスワードリセットAPI

### 8.1 パスワードリセット申請

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/password-reset/request |
| 認証 | 不要 |
| 概要 | パスワードリセットURLをメール送信する |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| email | 必須 | メールアドレス |

#### 制約

- リセットURLの有効期限は30分
- 存在しないメールアドレスの場合でも、セキュリティ上、同じ応答にする

### 8.2 パスワード再設定

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/password-reset/confirm |
| 認証 | 不要 |
| 概要 | リセットURLのtokenを利用して新しいパスワードを設定する |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| token | 必須 | リセットトークン |
| newPassword | 必須 | 新しいパスワード |

#### 主なエラー

| HTTPステータス | 内容 |
|---|---|
| 400 | 入力不正 |
| 404 | トークンが存在しない |
| 409 | トークン期限切れ、使用済み |

## 9. プロジェクトAPI

### 9.1 プロジェクト一覧取得

| 項目 | 内容 |
|---|---|
| メソッド | GET |
| パス | /api/projects |
| 認証 | 必要 |
| 権限 | システム管理者は全プロジェクト、通常ユーザーは参加済プロジェクトのみ |
| 概要 | 閲覧可能なプロジェクト一覧を取得する |

### 9.2 プロジェクト作成

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/projects |
| 認証 | 必要 |
| 権限 | システム管理者 |
| 概要 | プロジェクトを作成する |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| name | 必須 | プロジェクト名 |
| description | 任意 | プロジェクト説明 |

#### 制約

- プロジェクト作成はシステム管理者のみ可能
- 作成したシステム管理者を、作成したプロジェクトのPROJECT_ADMINとしてProjectMemberに登録する
- システム管理者は、作成後に任意のユーザーをPROJECT_ADMINへ変更できる

### 9.3 プロジェクト詳細取得

| 項目 | 内容 |
|---|---|
| メソッド | GET |
| パス | /api/projects/{projectId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクト参加者 |
| 概要 | プロジェクト詳細を取得する |

### 9.4 プロジェクト更新

| 項目 | 内容 |
|---|---|
| メソッド | PUT |
| パス | /api/projects/{projectId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 概要 | プロジェクト情報を更新する |

### 9.5 プロジェクト削除

| 項目 | 内容 |
|---|---|
| メソッド | DELETE |
| パス | /api/projects/{projectId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 概要 | プロジェクトを論理削除する |

#### 制約

- プロジェクト削除時は、紐づくタスク、コメント、添付ファイルも論理削除扱いとする

## 10. プロジェクトメンバーAPI

### 10.1 メンバー一覧取得

| 項目 | 内容 |
|---|---|
| メソッド | GET |
| パス | /api/projects/{projectId}/members |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクト参加者 |
| 概要 | プロジェクトメンバー一覧を取得する |

### 10.2 ロール変更

| 項目 | 内容 |
|---|---|
| メソッド | PUT |
| パス | /api/projects/{projectId}/members/{memberId}/role |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 概要 | プロジェクトメンバーのロールを変更する |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| projectRole | 必須 | PROJECT_ADMIN / MEMBER / VIEWER |

#### 制約

- プロジェクト管理者が0人になる変更は禁止する
- プロジェクト管理者自身のロール変更は、対象プロジェクト内に他のプロジェクト管理者が存在する場合のみ可能

### 10.3 メンバー削除

| 項目 | 内容 |
|---|---|
| メソッド | DELETE |
| パス | /api/projects/{projectId}/members/{memberId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 概要 | プロジェクトメンバーを論理削除する |

#### 制約

- プロジェクト管理者が0人になる削除は禁止する

## 11. 招待API

### 11.1 招待作成

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/projects/{projectId}/invitations |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 概要 | メールアドレスを指定してプロジェクトへ招待する |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| email | 必須 | 招待先メールアドレス |

#### 制約

- 招待URLの有効期限は24時間
- 招待時の初期ロールはMEMBER固定
- 招待状態はPENDINGで作成する

### 11.2 招待情報取得

| 項目 | 内容 |
|---|---|
| メソッド | GET |
| パス | /api/invitations/{token} |
| 認証 | 不要 |
| 概要 | 招待URLのtokenから招待情報を取得する |

### 11.3 未登録ユーザー登録・招待承認

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/invitations/{token}/register |
| 認証 | 不要 |
| 概要 | 招待URLから未登録ユーザーを登録し、対象プロジェクトへ参加させる |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| displayName | 必須 | 表示名 |
| password | 必須 | パスワード |

#### 制約

- 招待URLが有効期限内であること
- 招待状態がPENDINGであること
- 招待先メールアドレスをユーザーのメールアドレスとして登録する
- 登録後、対象プロジェクトにMEMBERとして参加させる
- 登録後、招待状態はACCEPTEDに変更する
- 承認済み、期限切れ、無効化済みの招待は利用できない

### 11.4 招待承認

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/invitations/{token}/accept |
| 認証 | 必要 |
| 概要 | 登録済みユーザーが招待を承認し、対象プロジェクトに参加する |

#### 制約

- 有効期限切れの招待は承認できない
- 承認済みの招待は再利用できない
- 招待先メールアドレスとログインユーザーのメールアドレスが一致すること
- 承認後、対象プロジェクトにMEMBERとして参加させる
- 承認後、招待状態はACCEPTEDに変更する

## 12. タスクAPI

### 12.1 タスク一覧取得

| 項目 | 内容 |
|---|---|
| メソッド | GET |
| パス | /api/projects/{projectId}/tasks |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクト参加者 |
| 概要 | 対象プロジェクトのタスク一覧を取得する |

#### 制約

- deleted_at が設定されたタスクは返却しない

### 12.2 タスク作成

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/projects/{projectId}/tasks |
| 認証 | 必要 |
| 権限 | システム管理者、プロジェクト管理者、メンバー |
| 概要 | タスクを作成する |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| summary | 必須 | タスク概要 |
| description | 任意 | タスク説明 |
| assigneeId | 任意 | 担当者ユーザーID |
| dueDate | 任意 | 期限 |
| priority | 必須 | HIGHEST / HIGH / MEDIUM / LOW / LOWEST |

#### 制約

- 初期StatusはTODOとする
- dueDateを指定する場合は未来日とする

### 12.3 タスク詳細取得

| 項目 | 内容 |
|---|---|
| メソッド | GET |
| パス | /api/projects/{projectId}/tasks/{taskId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクト参加者 |
| 概要 | タスク詳細を取得する |

### 12.4 タスク更新

| 項目 | 内容 |
|---|---|
| メソッド | PUT |
| パス | /api/projects/{projectId}/tasks/{taskId} |
| 認証 | 必要 |
| 権限 | システム管理者、プロジェクト管理者、メンバー |
| 概要 | タスク情報を更新する |

### 12.5 タスクステータス変更

| 項目 | 内容 |
|---|---|
| メソッド | PATCH |
| パス | /api/projects/{projectId}/tasks/{taskId}/status |
| 認証 | 必要 |
| 権限 | システム管理者、プロジェクト管理者、メンバー |
| 概要 | タスクのStatusを変更する |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| status | 必須 | TODO / IN_PROGRESS / REVIEW / DONE |

### 12.6 タスク削除

| 項目 | 内容 |
|---|---|
| メソッド | DELETE |
| パス | /api/projects/{projectId}/tasks/{taskId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 概要 | タスクを論理削除する |

## 13. コメントAPI

### 13.1 コメント作成

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/projects/{projectId}/tasks/{taskId}/comments |
| 認証 | 必要 |
| 権限 | システム管理者、プロジェクト管理者、メンバー |
| 概要 | タスクにコメントを作成する |

#### リクエスト

| 項目 | 必須 | 内容 |
|---|---|---|
| body | 必須 | コメント本文 |

### 13.2 コメント更新

| 項目 | 内容 |
|---|---|
| メソッド | PUT |
| パス | /api/projects/{projectId}/tasks/{taskId}/comments/{commentId} |
| 認証 | 必要 |
| 権限 | コメント投稿者本人 |
| 概要 | 自分のコメントを更新する |

### 13.3 コメント削除

| 項目 | 内容 |
|---|---|
| メソッド | DELETE |
| パス | /api/projects/{projectId}/tasks/{taskId}/comments/{commentId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 概要 | コメントを論理削除する |

## 14. 添付ファイルAPI

### 14.1 添付ファイル追加

| 項目 | 内容 |
|---|---|
| メソッド | POST |
| パス | /api/projects/{projectId}/tasks/{taskId}/attachments |
| 認証 | 必要 |
| 権限 | システム管理者、プロジェクト管理者、メンバー |
| 概要 | タスクに添付ファイルを追加する |

#### 制約

- 1ファイルあたり5MBまで
- 許可するファイル種別は画像、PDF、テキスト、Office系ファイル
- ファイル本体はDBに保存する

### 14.2 添付ファイル取得

| 項目 | 内容 |
|---|---|
| メソッド | GET |
| パス | /api/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクト参加者 |
| 概要 | 添付ファイルを取得する |

### 14.3 添付ファイル削除

| 項目 | 内容 |
|---|---|
| メソッド | DELETE |
| パス | /api/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId} |
| 認証 | 必要 |
| 権限 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 概要 | 添付ファイルを論理削除する |

## 15. 認可チェック一覧

| 操作 | 必要な権限 |
|---|---|
| 全プロジェクト閲覧 | システム管理者 |
| 参加済プロジェクト閲覧 | 対象プロジェクト参加者 |
| プロジェクト作成 | システム管理者 |
| プロジェクト更新 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| プロジェクト削除 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| メンバー招待 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| ロール変更 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| タスク作成 | システム管理者または対象プロジェクトのプロジェクト管理者、メンバー |
| タスク更新 | システム管理者または対象プロジェクトのプロジェクト管理者、メンバー |
| タスク削除 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| コメント作成 | システム管理者または対象プロジェクトのプロジェクト管理者、メンバー |
| コメント更新 | コメント投稿者本人 |
| コメント削除 | システム管理者または対象プロジェクトのプロジェクト管理者 |
| 添付ファイル追加 | システム管理者または対象プロジェクトのプロジェクト管理者、メンバー |
| 添付ファイル削除 | システム管理者または対象プロジェクトのプロジェクト管理者 |

## 16. DB設計との対応

| API分類 | 主に参照・更新するデータ |
|---|---|
| 認証 | User, SystemRole |
| パスワードリセット | User, PasswordResetToken |
| プロジェクト | Project, ProjectMember, ProjectRole |
| プロジェクトメンバー | ProjectMember, ProjectRole |
| 招待 | ProjectInvitation, ProjectMember, User |
| タスク | Task, TaskStatus, TaskPriority, ProjectMember |
| コメント | TaskComment, Task, ProjectMember |
| 添付ファイル | Attachment, Task, ProjectMember |

## 17. APIレスポンス詳細JSON構造

本章では、主要APIのレスポンスJSON構造を整理する。

レスポンス項目名は、フロントエンドで扱いやすいようにcamelCaseを基本とする。

日時はISO 8601形式の文字列で返却する。

削除済みデータは、通常の一覧レスポンスには含めない。  
ただし、履歴や管理用途で必要な場合は、後続工程で専用APIを検討する。

## 17.1 共通レスポンス方針

### 成功レスポンス

単一リソースを返す場合は、対象リソースのオブジェクトを返す。

一覧を返す場合は、`items` に配列を格納する。

ページングが必要な一覧では、`page` 情報を含める。

### 共通エラーレスポンス

```json
{
  "code": "ERROR_CODE",
  "message": "エラーメッセージ",
  "details": {}
}
```

| 項目 | 内容 |
| --- | --- |
| code | フロントエンドで判定しやすいエラーコード|
| message | ユーザーまたは開発者向けのメッセージ |
| details | 入力項目ごとのエラーなど補足情報 |

## 17.2 ページング共通構造

一覧APIで件数が多くなる可能性がある場合、以下の形式を基本とする。

``` json
{
  "items": [],
  "page": {
    "pageNumber": 1,
    "pageSize": 20,
    "totalItems": 100,
    "totalPages": 5
  }
}
```

| 項目 | 内容 |
| --- | --- |
| pageNumber | 現在のページ番号 |
| pageSize | 1ページあたりの件数 |
| totalItems | 全件数 |
| totalPages | 全ページ数 |

初期MVPでは、以下の一覧APIでページングを検討する。

- プロジェクト一覧
- メンバー一覧
- タスク一覧

ただし、カンバン画面のタスク一覧はステータス別表示が必要なため、通常のページングではなく、ステータスごとの配列で返す方式も検討対象とする。

## 17.3 ログインレスポンス

対象API:

- `POST /api/auth/login`
- `GET /api/auth/me`

``` json
{
  "userId": 1,
  "email": "user@example.com",
  "displayName": "山田太郎",
  "systemRole": "USER"
}
```

| 項目 | 内容 |
| --- | --- |
| userId | ユーザーID |
| email | メールアドレス |
| displayName | 表示名 |
| systemRole | SYSTEM_ADMIN または USER |

セッション方式を採用するため、ログイン成功時のセッションIDはJSONでは返さず、Cookieで管理する。

## 17.4 プロジェクト一覧レスポンス

対象API:

- `GET /api/projects`

``` json
{
  "items": [
    {
      "projectId": 1,
      "name": "Task Management App",
      "description": "タスク管理機能開発プロジェクト",
      "myProjectRole": "PROJECT_ADMIN",
      "createdAt": "2026-06-09T10:00:00+09:00",
      "updatedAt": "2026-06-09T10:00:00+09:00"
    }
  ],
  "page": {
    "pageNumber": 1,
    "pageSize": 20,
    "totalItems": 1,
    "totalPages": 1
  }
}
```

| 項目 | 内容 |
| --- | --- |
| projectId | プロジェクトID |
| name | プロジェクト名 |
| description | プロジェクト説明 |
| myProjectRole | ログインユーザーの対象プロジェクト内ロール |
| createdAt | 作成日時 |
| updatedAt | 更新日時 |

システム管理者が全プロジェクトを閲覧する場合、参加していないプロジェクトでは `myProjectRole` は `null` とする。

## 17.5 プロジェクト詳細レスポンス

対象API:

- `GET /api/projects/{projectId}`

``` json
{
  "projectId": 1,
  "name": "Task Management App",
  "description": "タスク管理機能開発プロジェクト",
  "myProjectRole": "PROJECT_ADMIN",
  "createdAt": "2026-06-09T10:00:00+09:00",
  "updatedAt": "2026-06-09T10:00:00+09:00"
}
```

## 17.6 メンバー一覧レスポンス

対象API:

- `GET /api/projects/{projectId}/members`

``` json
{
  "items": [
    {
      "memberId": 1,
      "userId": 1,
      "email": "admin@example.com",
      "displayName": "管理者ユーザー",
      "projectRole": "PROJECT_ADMIN",
      "joinedAt": "2026-06-09T10:00:00+09:00"
    },
    {
      "memberId": 2,
      "userId": 2,
      "email": "member@example.com",
      "displayName": "メンバーユーザー",
      "projectRole": "MEMBER",
      "joinedAt": "2026-06-09T10:10:00+09:00"
    }
  ],
  "page": {
    "pageNumber": 1,
    "pageSize": 20,
    "totalItems": 2,
    "totalPages": 1
  }
}
```

削除済みユーザーの場合、`displayName` は `削除済ユーザー` として返却する。

## 17.7 招待情報レスポンス

対象API:

- `GET /api/invitations/{token}`

``` json
{
  "invitationId": 1,
  "projectId": 1,
  "projectName": "Task Management App",
  "email": "invitee@example.com",
  "status": "PENDING",
  "expiresAt": "2026-06-10T10:00:00+09:00"
}
```

| 項目 | 内容 |
| --- | --- |
| invitationId | 招待ID |
| projectId | プロジェクトID |
| projectName | 招待先プロジェクト名 |
| email | 招待先メールアドレス |
| status | PENDING / ACCEPTED / EXPIRED / REVOKED |
| expiresAt | 有効期限 |

## 17.8 タスク一覧レスポンス

対象API:

- `GET /api/projects/{projectId}/tasks`

カンバン画面で扱いやすいよう、Statusごとに配列を分けて返却する。

``` json
{
  "statuses": [
    {
      "status": "TODO",
      "label": "ToDo",
      "tasks": [
        {
          "taskId": 1,
          "summary": "ログイン画面を作成する",
          "assignee": {
            "userId": 2,
            "displayName": "メンバーユーザー"
          },
          "priority": "HIGH",
          "dueDate": "2026-06-20",
          "createdAt": "2026-06-09T10:00:00+09:00",
          "updatedAt": "2026-06-09T10:00:00+09:00"
        }
      ]
    },
    {
      "status": "IN_PROGRESS",
      "label": "In Progress",
      "tasks": []
    },
    {
      "status": "REVIEW",
      "label": "Review",
      "tasks": []
    },
    {
      "status": "DONE",
      "label": "Done",
      "tasks": []
    }
  ]
}
```

| 項目 | 内容 |
| --- | --- |
| status | TODO / IN_PROGRESS / REVIEW / DONE |
| label | 画面表示用ラベル |
| tasks | 対象Statusのタスク一覧 |

タスク一覧では、カンバン表示に必要な最小情報を返す。
コメントや添付ファイルの詳細はタスク詳細APIで取得する。

## 17.9 タスク詳細レスポンス

対象API:

- `GET /api/projects/{projectId}/tasks/{taskId}`

``` json
{
  "taskId": 1,
  "projectId": 1,
  "summary": "ログイン画面を作成する",
  "description": "メールアドレスとパスワードでログインできる画面を作成する",
  "status": "TODO",
  "assignee": {
    "userId": 2,
    "displayName": "メンバーユーザー"
  },
  "dueDate": "2026-06-20",
  "priority": "HIGH",
  "creator": {
    "userId": 1,
    "displayName": "管理者ユーザー"
  },
  "comments": [
    {
      "commentId": 1,
      "body": "ログイン失敗時のエラー表示も考慮してください",
      "author": {
        "userId": 1,
        "displayName": "管理者ユーザー"
      },
      "createdAt": "2026-06-09T10:30:00+09:00",
      "updatedAt": "2026-06-09T10:30:00+09:00"
    }
  ],
  "attachments": [
    {
      "attachmentId": 1,
      "fileName": "screen_image.png",
      "contentType": "image/png",
      "fileSize": 102400,
      "uploadedBy": {
        "userId": 2,
        "displayName": "メンバーユーザー"
      },
      "createdAt": "2026-06-09T10:40:00+09:00"
    }
  ],
  "createdAt": "2026-06-09T10:00:00+09:00",
  "updatedAt": "2026-06-09T10:00:00+09:00"
}
```

削除済みユーザーが作成者、担当者、コメント投稿者、アップロード者の場合、`displayName` は `削除済ユーザー` として返却する。

## 17.10 タスク作成・更新レスポンス

対象API:

- `POST /api/projects/{projectId}/tasks`
- `PUT /api/projects/{projectId}/tasks/{taskId}`
- `PATCH /api/projects/{projectId}/tasks/{taskId}/status`

``` json
{
  "taskId": 1,
  "projectId": 1,
  "summary": "ログイン画面を作成する",
  "description": "メールアドレスとパスワードでログインできる画面を作成する",
  "status": "TODO",
  "assignee": {
    "userId": 2,
    "displayName": "メンバーユーザー"
  },
  "dueDate": "2026-06-20",
  "priority": "HIGH",
  "creator": {
    "userId": 1,
    "displayName": "管理者ユーザー"
  },
  "createdAt": "2026-06-09T10:00:00+09:00",
  "updatedAt": "2026-06-09T10:00:00+09:00"
}
```

作成・更新系APIでは、処理後の最新状態を返却する。

## 17.11 コメント作成・更新レスポンス

対象API:

- `POST /api/projects/{projectId}/tasks/{taskId}/comments`
- `PUT /api/projects/{projectId}/tasks/{taskId}/comments/{commentId}`

``` json
{
  "commentId": 1,
  "taskId": 1,
  "body": "ログイン失敗時のエラー表示も考慮してください",
  "author": {
    "userId": 1,
    "displayName": "管理者ユーザー"
  },
  "createdAt": "2026-06-09T10:30:00+09:00",
  "updatedAt": "2026-06-09T10:30:00+09:00"
}
```

## 17.12 添付ファイル追加レスポンス

対象API:

- `POST /api/projects/{projectId}/tasks/{taskId}/attachments`

``` json
{
  "attachmentId": 1,
  "taskId": 1,
  "fileName": "screen_image.png",
  "contentType": "image/png",
  "fileSize": 102400,
  "uploadedBy": {
    "userId": 2,
    "displayName": "メンバーユーザー"
  },
  "createdAt": "2026-06-09T10:40:00+09:00"
}
```
ファイル本体はレスポンスJSONには含めない。
ファイル取得APIで取得する。

## 17.13 添付ファイル取得レスポンス

対象API:

- `GET /api/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId}`

添付ファイル取得APIでは、JSONではなくファイルデータを返却する。

レスポンスヘッダーで以下を返却する。

| ヘッダー | 内容 |
| --- | --- |
| Content-Type | ファイル種別 |
| Content-Disposition | ダウンロード時のファイル名 |
| Content-Length | ファイルサイズ |

## 17.14 削除系APIレスポンス

対象API:

- `DELETE /api/projects/{projectId}`
- `DELETE /api/projects/{projectId}/members/{memberId}`
- `DELETE /api/projects/{projectId}/tasks/{taskId}`
- `DELETE /api/projects/{projectId}/tasks/{taskId}/comments/{commentId}`
- `DELETE /api/projects/{projectId}/tasks/{taskId}/attachments/{attachmentId}`

削除系APIでは、論理削除後に以下を返却する。

``` json
{
  "deleted": true
}
```

## 17.15 ロール変更レスポンス

対象API:

- `PUT /api/projects/{projectId}/members/{memberId}/role`

``` json
{
  "memberId": 2,
  "userId": 2,
  "displayName": "メンバーユーザー",
  "projectRole": "PROJECT_ADMIN",
  "updatedAt": "2026-06-09T11:00:00+09:00"
}
```

## 17.16 パスワードリセット系レスポンス

対象API:

- `POST /api/password-reset/request`

セキュリティ上、対象メールアドレスの存在有無に関わらず同じレスポンスを返す。

``` json
{
  "accepted": true
}
```

対象API:

- `POST /api/password-reset/confirm`

```json
{
  "completed": true
}
```

## 17.17 招待作成レスポンス

対象API:

- `POST /api/projects/{projectId}/invitations`

```json
{
  "invitationId": 1,
  "projectId": 1,
  "email": "invitee@example.com",
  "status": "PENDING",
  "expiresAt": "2026-06-10T10:00:00+09:00",
  "createdAt": "2026-06-09T10:00:00+09:00"
}
```

## 17.18 招待承認レスポンス

対象API:

- `POST /api/invitations/{token}/register`
- `POST /api/invitations/{token}/accept`

```json
{
  "accepted": true,
  "projectId": 1,
  "projectRole": "MEMBER"
}
```

未登録ユーザー登録APIでは、登録されたユーザー情報も返却する。

``` json
{
  "accepted": true,
  "user": {
    "userId": 3,
    "email": "invitee@example.com",
    "displayName": "招待ユーザー",
    "systemRole": "USER"
  },
  "projectId": 1,
  "projectRole": "MEMBER"
}
```

## 18. 未決事項

以下は後続工程で検討する。

- ページング方式
- ソート・検索条件
- ファイルアップロード時の具体的なリクエスト形式
- 招待情報の期限切れ状態を自動更新するか、参照時に判定するか
- パスワードリセット情報の期限切れ状態を自動更新するか、参照時に判定するか
- セッションCookieの詳細設定
- CSRF対策の具体的な方式
- セッション有効期限