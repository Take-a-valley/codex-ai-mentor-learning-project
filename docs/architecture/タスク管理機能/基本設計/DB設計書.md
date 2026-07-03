# データ概念設計 / DB設計書

## 1. 本書の目的

本書は、要件定義書および基本設計書をもとに、本システムで管理するデータ、データ同士の関係、主要な属性、制約を整理することを目的とする。

後続工程であるAPI設計、詳細設計、実装、テスト設計へ進むために、データ構造の前提を明確にする。

## 2. 対象範囲

本書では、初期MVPであるカンバン方式タスク管理機能に必要なデータを対象とする。

対象データは以下とする。

- ユーザー
- システムロール
- プロジェクト
- プロジェクトメンバー
- プロジェクト内ロール
- タスク
- タスクステータス
- タスク優先度
- タスクコメント
- 添付ファイル
- プロジェクト招待
- パスワードリセット

以下は初期MVPでは対象外とする。

- Wiki関連データ
- WBS、ガントチャート関連データ
- プロジェクト内チャット関連データ
- 通知データ
- 外部ストレージ連携データ

## 3. データ設計方針

本システムでは、MySQLを利用して業務データを管理する。

システム全体ロールとプロジェクト内ロールは役割が異なるため、分けて管理する。

添付ファイル本体は初期MVPではDBに保存し、DBにはファイル名、ファイル種別、サイズ、ファイル本体データなどを保存する。

タスクのStatusとPriorityは、初期MVPでは固定値として扱う。

## 4. 主要エンティティ一覧

| エンティティ | 概要 |
|---|---|
| User | システムを利用するユーザー |
| SystemRole | システム全体のロール |
| Project | プロジェクト |
| ProjectMember | プロジェクト参加ユーザーとプロジェクト内ロール |
| ProjectRole | プロジェクト内ロール |
| Task | カンバンで管理するタスク |
| TaskStatus | タスクの状態 |
| TaskPriority | タスクの優先度 |
| TaskComment | タスクに紐づくコメント |
| Attachment | タスクに紐づく添付ファイル |
| ProjectInvitation | プロジェクト招待情報 |
| PasswordResetToken | パスワードリセット情報 |

## 5. エンティティ関連概要

主な関係は以下とする。

- 1人のユーザーは複数のプロジェクトに参加できる
- 1つのプロジェクトには複数のユーザーが参加できる
- ユーザーとプロジェクトの関係はProjectMemberで管理する
- ProjectMemberはプロジェクト内ロールを持つ
- 1つのプロジェクトには複数のタスクが存在する
- 1つのタスクは1つのプロジェクトに所属する
- 1つのタスクには0件以上のコメントが紐づく
- 1つのタスクには0件以上の添付ファイルが紐づく
- 1つの招待情報は1つのプロジェクトに紐づく
- 1つのパスワードリセット情報は1人のユーザーに紐づく

## 6. User

### 概要

システムを利用するユーザーを表す。

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | ユーザーID | 必須 |
| email | メールアドレス | 必須 |
| password_hash | ハッシュ化済みパスワード | 必須 |
| display_name | 表示名 | 必須 |
| system_role_id | システム全体ロールID | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |
| deleted_at | 削除日時 | 任意 |
| deleted_by | 削除者ユーザーID | 任意 |

### 制約

- emailは一意とする
- パスワードは平文で保存しない
- システム管理者はSystemRoleで判定する
- ユーザー削除時は論理削除とする。
- ユーザーが作成したタスク、コメント、添付ファイル、プロジェクトは削除しない。
- 削除済みユーザーの表示名は画面上で「削除済ユーザー」と表示する。

## 7. SystemRole

### 概要

システム全体に対するロールを表す。

### 初期値

| ロール | 概要 |
|---|---|
| SYSTEM_ADMIN | システム全体を管理できる |
| USER | 通常ユーザー |

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | システムロールID | 必須 |
| name | ロール名 | 必須 |

## 8. Project

### 概要

開発プロジェクトを表す。

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | プロジェクトID | 必須 |
| name | プロジェクト名 | 必須 |
| description | プロジェクト説明 | 任意 |
| created_by | 作成者ユーザーID | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |
| deleted_at | 削除日時 | 任意 |
| deleted_by | 削除者ユーザーID | 任意 |

### 制約

- プロジェクト名は必須
- プロジェクト管理操作は、システム管理者または対象プロジェクトのプロジェクト管理者のみ可能
- プロジェクト削除時は論理削除とする。
- プロジェクトに紐づくタスク、コメント、添付ファイルも論理削除扱いとする。
- プロジェクト作成時は、作成したシステム管理者をProjectMemberに登録し、project_role_idにはPROJECT_ADMINを設定する。
- システム管理者は、プロジェクト作成後に任意のユーザーをPROJECT_ADMINへ変更できる。
- プロジェクトには常に1人以上のPROJECT_ADMINが存在する必要がある。

## 9. ProjectMember

### 概要

ユーザーがどのプロジェクトに参加しているか、またそのプロジェクト内でどのロールを持つかを表す。

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | プロジェクトメンバーID | 必須 |
| project_id | プロジェクトID | 必須 |
| user_id | ユーザーID | 必須 |
| project_role_id | プロジェクト内ロールID | 必須 |
| joined_at | 参加日時 | 必須 |
| deleted_at | 削除日時 | 任意 |
| deleted_by | 削除者ユーザーID | 任意 |

### 制約

- project_id と user_id の組み合わせは一意とする
- 1つのプロジェクトには最低1人のプロジェクト管理者が必要
- プロジェクト管理者が0人になる更新・削除は禁止する
- プロジェクトメンバー削除時は論理削除とする。
- deleted_at が設定されたメンバーは、通常の参加メンバーとして扱わない。

## 10. ProjectRole

### 概要

プロジェクト内でのロールを表す。

### 初期値

| ロール | 概要 |
|---|---|
| PROJECT_ADMIN | プロジェクト内の管理者 |
| MEMBER | タスク操作ができるメンバー |
| VIEWER | 閲覧のみできるユーザー |

## 11. Task

### 概要

カンバンで管理するタスクを表す。

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | タスクID | 必須 |
| project_id | プロジェクトID | 必須 |
| summary | タスク概要 | 必須 |
| description | タスク説明 | 任意 |
| status_id | Status ID | 必須 |
| assignee_id | 担当者ユーザーID | 任意 |
| due_date | 期限 | 任意 |
| priority_id | Priority ID | 必須 |
| creator_id | 作成者ユーザーID | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |
| deleted_at | 削除日時 | 任意 |
| deleted_by | 削除者ユーザーID | 任意 |

### 制約

- summaryは必須
- due_dateは任意
- due_dateを入力する場合は未来日とする
- タスク削除はシステム管理者またはプロジェクト管理者のみ可能
- タスク作成・編集・移動はプロジェクト管理者またはメンバーが可能
- タスク削除時は論理削除とする。
- deleted_at が設定されたタスクは通常のカンバン画面には表示しない。
- TaskStatusとTaskPriorityは、初期MVPではユーザーが追加・変更できない固定マスタとして扱う。ただし、DB上はマスタテーブルとして保持し、Taskから外部キーで参照する。

## 12. TaskStatus

### 概要

タスクの状態を表す。

### 初期値

| Status | 概要 |
|---|---|
| TODO | ToDo |
| IN_PROGRESS | In Progress |
| REVIEW | Review |
| DONE | Done |

### 方針

初期MVPでは固定値として扱う。  
ステータス列のカスタマイズは将来拡張とする。

## 13. TaskPriority

### 概要

タスクの優先度を表す。

### 初期値

| Priority | 概要 |
|---|---|
| HIGHEST | Highest |
| HIGH | High |
| MEDIUM | Medium |
| LOW | Low |
| LOWEST | Lowest |

## 14. TaskComment

### 概要

タスクに紐づくコメントを表す。

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | コメントID | 必須 |
| task_id | タスクID | 必須 |
| user_id | 投稿者ユーザーID | 必須 |
| body | コメント本文 | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |
| deleted_at | 削除日時 | 任意 |
| deleted_by | 削除者ユーザーID | 任意 |

### 制約

- コメント作成はプロジェクト管理者またはメンバーが可能
- コメント編集は投稿者本人のみ可能
- コメント削除はシステム管理者またはプロジェクト管理者のみ可能
- 閲覧者はコメント閲覧のみ可能
- コメント削除時は論理削除とする。
- deleted_at が設定されたコメントは通常表示しない。

## 15. Attachment

### 概要

タスクに添付されたファイル本体とメタ情報を表す。
ファイル本体はDBに保存する。

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | 添付ファイルID | 必須 |
| task_id | タスクID | 必須 |
| uploaded_by | アップロード者ユーザーID | 必須 |
| file_name | 元ファイル名 | 必須 |
| file_data | ファイル本体データ | 必須 |
| file_size | ファイルサイズ | 必須 |
| content_type | ファイル種別 | 必須 |
| created_at | 作成日時 | 必須 |
| deleted_at | 削除日時 | 任意 |
| deleted_by | 削除者ユーザーID | 任意 |

### 制約

- ファイル本体はMySQLに保存する
- 1ファイルあたり5MBまでとする
- 許可するファイル種別は画像、PDF、テキスト、Office系ファイルとする
- 添付ファイル追加はプロジェクト管理者またはメンバーが可能
- 添付ファイル削除はシステム管理者またはプロジェクト管理者のみ可能
- 添付ファイル削除時は論理削除とする。
- DBに保存されたファイル本体データは履歴保持方針に従って残す。

## 16. ProjectInvitation

### 概要

プロジェクトへのメール招待情報を表す。

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | 招待ID | 必須 |
| project_id | プロジェクトID | 必須 |
| email | 招待先メールアドレス | 必須 |
| token | 招待URL用トークン | 必須 |
| invited_by | 招待者ユーザーID | 必須 |
| initial_project_role_id | 招待時の初期ロールID | 必須 |
| expires_at | 有効期限 | 必須 |
| accepted_at | 承認日時 | 任意 |
| created_at | 作成日時 | 必須 |
| status | 招待状態 | 必須 |
| updated_at | 更新日時 | 必須 |

### 制約

- 招待URLの有効期限は24時間
- 招待時の初期ロールはMEMBER固定
- 未登録ユーザーは招待URLからユーザー登録後、対象プロジェクトに参加できる
- tokenは推測困難な値とする
- 招待情報は履歴として残す。
- 期限切れ、承認済み、無効化済みの状態を管理する。
- statusは以下のいずれかとする。
    - PENDING: 招待中
    - ACCEPTED: 承認済み
    - EXPIRED: 期限切れ
    - REVOKED: 無効化済み

## 17. PasswordResetToken

### 概要

パスワードリセット用のトークン情報を表す。

### 主な属性

| 項目 | 概要 | 必須 |
|---|---|---|
| id | パスワードリセットID | 必須 |
| user_id | ユーザーID | 必須 |
| token | リセットURL用トークン | 必須 |
| status | パスワードリセット状態 | 必須 |
| expires_at | 有効期限 | 必須 |
| used_at | 使用日時 | 任意 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |

### 制約

- リセットURLの有効期限は30分
- tokenは推測困難な値とする
- 使用済みトークンは再利用できない
- パスワードリセット情報は履歴として残す。
- 使用済み、期限切れの状態を管理し、再利用できないようにする。
- statusは以下のいずれかとする。
    - ACTIVE: 使用可能
    - USED: 使用済み
    - EXPIRED: 期限切れ

## 18. 主なリレーション

| 親 | 子 | 関係 |
|---|---|---|
| User | Project | User creates Project |
| User | ProjectMember | User joins Project through ProjectMember |
| Project | ProjectMember | Project has many members |
| ProjectRole | ProjectMember | ProjectMember has one ProjectRole |
| Project | Task | Project has many Tasks |
| User | Task | User creates Tasks |
| User | Task | User can be assigned to Tasks |
| TaskStatus | Task | Task has one Status |
| TaskPriority | Task | Task has one Priority |
| Task | TaskComment | Task has many Comments |
| User | TaskComment | User writes Comments |
| Task | Attachment | Task has many Attachments |
| User | Attachment | User uploads Attachments |
| Project | ProjectInvitation | Project has many Invitations |
| User | PasswordResetToken | User has many PasswordResetTokens |

## 19. 削除方針

初期MVPでは、主要データの削除は論理削除とする。

ユーザー削除時は、作成済みタスク、コメント、添付ファイル、プロジェクトは削除しない。
削除済みユーザーは画面上で「削除済ユーザー」と表示する。

プロジェクト削除時は、プロジェクトに紐づくタスク、コメント、添付ファイルも論理削除扱いとする。

招待情報とパスワードリセット情報は履歴として残す。

## 20. インデックス・一意制約方針

詳細なインデックス設計はDB詳細設計で行う。

現時点で必要と考えられる一意制約は以下とする。

- User.email
- ProjectMember.project_id + ProjectMember.user_id
- ProjectInvitation.token
- PasswordResetToken.token

## 21. 未決事項

以下は後続工程で検討する。

- 招待情報の期限切れ後の扱い
- パスワードリセット情報の期限切れ後の扱い