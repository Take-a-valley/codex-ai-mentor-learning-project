# Entity設計書

## 1. 本書の目的

本書は、タスク管理機能の初期MVPにおけるEntity設計を定義することを目的とする。

DB設計書で定義したテーブルを、Spring Boot / Spring Data JPAで扱うEntityとして整理する。

本書では、Entity一覧、各Entityの責務、主な項目、リレーション、論理削除、監査項目、固定マスタの扱いを定義する。

## 2. 対象範囲

対象に含めるものは以下とする。

- Entity一覧
- Entityごとの責務
- Entityごとの主な項目
- Entity間のリレーション
- 論理削除方針
- 監査項目方針
- 固定マスタEntity方針
- 履歴系Entity方針

対象外とするものは以下とする。

- Repositoryのメソッド詳細
- Serviceの業務処理詳細
- ControllerのAPI詳細
- DTOの詳細項目
- FlywayのSQL定義

## 3. Entity設計方針

EntityはDBテーブルに対応するデータ構造とする。

EntityはAPIレスポンスとして直接返却しない。

API入出力にはDTOを使用する。

Entityには業務処理を過度に持たせず、業務ルールはService層で扱う。

通常取得では、論理削除済みデータを除外する。

## 4. Entity一覧

| Entity | 概要 |
|---|---|
| User | システムを利用するユーザー |
| SystemRole | システム全体ロール |
| Project | プロジェクト |
| ProjectMember | プロジェクト参加ユーザーとプロジェクト内ロール |
| ProjectRole | プロジェクト内ロール |
| Task | カンバンで管理するタスク |
| TaskStatus | タスクのStatus |
| TaskPriority | タスクのPriority |
| TaskComment | タスクに紐づくコメント |
| Attachment | タスクに紐づく添付ファイル |
| ProjectInvitation | プロジェクト招待情報 |
| PasswordResetToken | パスワードリセット情報 |

## 5. 共通項目方針

主要Entityには、作成日時、更新日時、削除日時を持たせる。

| 項目 | 概要 |
|---|---|
| created_at | 作成日時 |
| updated_at | 更新日時 |
| deleted_at | 削除日時 |
| deleted_by | 削除者ユーザーID |

`deleted_at` が設定されているデータは、通常の一覧取得・詳細取得では表示しない。

`deleted_by` は、削除操作を行ったユーザーを記録するために使用する。

履歴として残すProjectInvitation、PasswordResetTokenは、物理削除せずstatusで状態を管理する。

## 6. User Entity

### 概要

システムを利用するユーザーを表す。

### 主な項目

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

### リレーション

| 関連先 | 関係 |
|---|---|
| SystemRole | 多対1 |
| Project | 作成者として1対多 |
| ProjectMember | 1対多 |
| Task | 作成者・担当者として1対多 |
| TaskComment | 投稿者として1対多 |
| Attachment | アップロード者として1対多 |
| PasswordResetToken | 1対多 |
| ProjectInvitation | 招待者として1対多 |

### 制約

- emailは一意とする
- パスワードは平文で保存しない
- システム管理者はSystemRoleで判定する
- ユーザー削除時は論理削除とする
- ユーザーが作成したタスク、コメント、添付ファイル、プロジェクトは削除しない
- 削除済みユーザーは画面上で「削除済ユーザー」と表示する

## 7. SystemRole Entity

### 概要

システム全体のロールを表す固定マスタ。

### 主な項目

| 項目 | 概要 | 必須 |
|---|---|---|
| id | システムロールID | 必須 |
| code | ロールコード | 必須 |
| name | 表示名 | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |

### 初期データ

| code | 概要 |
|---|---|
| SYSTEM_ADMIN | システム管理者 |
| USER | 一般ユーザー |

### 制約

- codeは一意とする
- 初期MVPでは画面から追加・変更・削除しない
- システム管理者が0人になる操作は禁止する

## 8. Project Entity

### 概要

開発プロジェクトを表す。

### 主な項目

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

### リレーション

| 関連先 | 関係 |
|---|---|
| User | 作成者として多対1 |
| ProjectMember | 1対多 |
| Task | 1対多 |
| ProjectInvitation | 1対多 |

### 制約

- プロジェクト作成はSYSTEM_ADMINのみ可能
- プロジェクト作成直後、作成者をProjectMemberとして登録しPROJECT_ADMINにする
- プロジェクト削除時は論理削除とする
- プロジェクト削除時は関連するタスク、コメント、添付ファイル、プロジェクトメンバーも論理削除する

## 9. ProjectMember Entity

### 概要

ユーザーがどのプロジェクトに参加しているか、またプロジェクト内でどのロールを持つかを表す。

### 主な項目

| 項目 | 概要 | 必須 |
|---|---|---|
| id | プロジェクトメンバーID | 必須 |
| project_id | プロジェクトID | 必須 |
| user_id | ユーザーID | 必須 |
| project_role_id | プロジェクト内ロールID | 必須 |
| joined_at | 参加日時 | 必須 |
| deleted_at | 削除日時 | 任意 |
| deleted_by | 削除者ユーザーID | 任意 |

### リレーション

| 関連先 | 関係 |
|---|---|
| Project | 多対1 |
| User | 多対1 |
| ProjectRole | 多対1 |

### 制約

- project_idとuser_idの組み合わせは一意とする
- 1つのプロジェクトには最低1人のPROJECT_ADMINが必要
- PROJECT_ADMINが0人になるロール変更・削除は禁止する
- deleted_atが設定されたメンバーは通常の参加メンバーとして扱わない

## 10. ProjectRole Entity

### 概要

プロジェクト内ロールを表す固定マスタ。

### 主な項目

| 項目 | 概要 | 必須 |
|---|---|---|
| id | プロジェクトロールID | 必須 |
| code | ロールコード | 必須 |
| name | 表示名 | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |

### 初期データ

| code | 概要 |
|---|---|
| PROJECT_ADMIN | プロジェクト管理者 |
| MEMBER | メンバー |
| VIEWER | 閲覧者 |

### 制約

- codeは一意とする
- 初期MVPでは画面から追加・変更・削除しない

## 11. Task Entity

### 概要

カンバンで管理するタスクを表す。

### 主な項目

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

### リレーション

| 関連先 | 関係 |
|---|---|
| Project | 多対1 |
| TaskStatus | 多対1 |
| TaskPriority | 多対1 |
| User | 作成者として多対1 |
| User | 担当者として多対1 |
| TaskComment | 1対多 |
| Attachment | 1対多 |

### 制約

- summaryは必須
- due_dateは任意
- タスク作成・編集・移動はPROJECT_ADMINまたはMEMBERが可能
- タスク削除はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- タスク削除時は論理削除とする
- deleted_atが設定されたタスクは通常のカンバン画面には表示しない

## 12. TaskStatus Entity

### 概要

タスクのStatusを表す固定マスタ。

### 主な項目

| 項目 | 概要 | 必須 |
|---|---|---|
| id | Status ID | 必須 |
| code | Statusコード | 必須 |
| name | 表示名 | 必須 |
| sort_order | 表示順 | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |

### 初期データ

| code | name | sort_order |
|---|---|---|
| TODO | ToDo | 1 |
| IN_PROGRESS | In Progress | 2 |
| REVIEW | Review | 3 |
| DONE | Done | 4 |

### 制約

- codeは一意とする
- 初期MVPでは画面から追加・変更・削除しない
- 将来的にステータス列のカスタマイズを検討する

## 13. TaskPriority Entity

### 概要

タスクのPriorityを表す固定マスタ。

### 主な項目

| 項目 | 概要 | 必須 |
|---|---|---|
| id | Priority ID | 必須 |
| code | Priorityコード | 必須 |
| name | 表示名 | 必須 |
| sort_order | 表示順 | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |

### 初期データ

| code | name | sort_order |
|---|---|---|
| HIGHEST | Highest | 1 |
| HIGH | High | 2 |
| MEDIUM | Medium | 3 |
| LOW | Low | 4 |
| LOWEST | Lowest | 5 |

### 制約

- codeは一意とする
- 初期MVPでは画面から追加・変更・削除しない

## 14. TaskComment Entity

### 概要

タスクに紐づくコメントを表す。

### 主な項目

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

### リレーション

| 関連先 | 関係 |
|---|---|
| Task | 多対1 |
| User | 投稿者として多対1 |

### 制約

- コメント作成はPROJECT_ADMINまたはMEMBERが可能
- コメント編集は投稿者本人のみ可能
- コメント削除はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- VIEWERはコメント閲覧のみ可能
- コメント削除時は論理削除とする
- deleted_atが設定されたコメントは通常表示しない

## 15. Attachment Entity

### 概要

タスクに添付されたファイル本体とメタ情報を表す。

ファイル本体はDBに保存する。

### 主な項目

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

### リレーション

| 関連先 | 関係 |
|---|---|
| Task | 多対1 |
| User | アップロード者として多対1 |

### 制約

- 1ファイルあたり5MBまでとする
- 添付可能なファイル種別は画像、PDF、テキスト、Office系ファイルとする
- 添付ファイル追加はPROJECT_ADMINまたはMEMBERが可能
- 添付ファイル削除はSYSTEM_ADMINまたはPROJECT_ADMINのみ可能
- 添付ファイル削除時は論理削除とする

## 16. ProjectInvitation Entity

### 概要

プロジェクトへのメール招待情報を表す。

招待情報は履歴として残す。

### 主な項目

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
| status | 招待状態 | 必須 |
| created_at | 作成日時 | 必須 |
| updated_at | 更新日時 | 必須 |

### リレーション

| 関連先 | 関係 |
|---|---|
| Project | 多対1 |
| User | 招待者として多対1 |
| ProjectRole | 初期ロールとして多対1 |

### status

| status | 概要 |
|---|---|
| PENDING | 招待中 |
| ACCEPTED | 承認済み |
| EXPIRED | 期限切れ |
| REVOKED | 無効化済み |

### 制約

- 招待URLの有効期限は24時間
- 招待時の初期ロールはMEMBER固定
- 未登録ユーザーは招待URLからユーザー登録後、対象プロジェクトに参加できる
- tokenは推測困難な値とする
- 使用済み、期限切れ、無効化済みの招待は再利用できない

## 17. PasswordResetToken Entity

### 概要

パスワードリセット用のトークン情報を表す。

パスワードリセット情報は履歴として残す。

### 主な項目

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

### リレーション

| 関連先 | 関係 |
|---|---|
| User | 多対1 |

### status

| status | 概要 |
|---|---|
| ACTIVE | 使用可能 |
| USED | 使用済み |
| EXPIRED | 期限切れ |

### 制約

- リセットURLの有効期限は30分
- tokenは推測困難な値とする
- 使用済みトークンは再利用できない
- 期限切れトークンは使用できない

## 18. リレーション方針

Entity間の主な関係は以下とする。

| 起点 | 関連先 | 関係 |
|---|---|---|
| User | SystemRole | 多対1 |
| Project | User | 作成者として多対1 |
| ProjectMember | Project | 多対1 |
| ProjectMember | User | 多対1 |
| ProjectMember | ProjectRole | 多対1 |
| Task | Project | 多対1 |
| Task | TaskStatus | 多対1 |
| Task | TaskPriority | 多対1 |
| Task | User | 作成者・担当者として多対1 |
| TaskComment | Task | 多対1 |
| TaskComment | User | 投稿者として多対1 |
| Attachment | Task | 多対1 |
| Attachment | User | アップロード者として多対1 |
| ProjectInvitation | Project | 多対1 |
| PasswordResetToken | User | 多対1 |

## 19. 論理削除方針

以下のEntityは論理削除対象とする。

- User
- Project
- ProjectMember
- Task
- TaskComment
- Attachment

論理削除では `deleted_at` に削除日時を設定する。

削除操作を行ったユーザーを記録できるEntityでは `deleted_by` を設定する。

通常の一覧取得・詳細取得では、論理削除済みデータを返却しない。

## 20. 固定マスタ方針

以下のEntityは初期MVPでは固定マスタとして扱う。

- SystemRole
- ProjectRole
- TaskStatus
- TaskPriority

固定マスタは初期データとして登録する。

初期MVPでは、画面やAPIから追加・変更・削除しない。

固定マスタEntityでは、実装上の判定に使用する `code` と、画面表示に使用する `name` を分けて管理する。

DB設計書で項目が簡略化されている固定マスタについても、Entity設計では実装時に必要となる項目を具体化する。

## 21. 履歴系Entity方針

以下のEntityは履歴として残す。

- ProjectInvitation
- PasswordResetToken

履歴系Entityは物理削除しない。

使用済み、期限切れ、無効化などの状態はstatusで管理する。

ProjectInvitationは、対象プロジェクトが論理削除された場合でも履歴として保持する。

ただし、論理削除済みプロジェクトに対する未承認の招待は、招待承認処理では利用不可とする。

## 22. 決定事項

- EntityはDBテーブルに対応させる
- EntityはAPIレスポンスとして直接返却しない
- API入出力にはDTOを使用する
- 主要データは論理削除する
- 固定マスタは初期MVPでは画面から変更しない
- 招待情報とパスワードリセット情報は履歴として残す
- 添付ファイル本体はDBに保存する
- 添付ファイル本体のDB型は、MySQLのLONGBLOBを基本とする
- tokenは推測困難な文字列として保存する

## 23. 未決事項

以下は後続工程で具体化する。

- 正式なJavaクラス名
- Java上の型
- IDの型
- 日時項目の型
- Entity間リレーションの実装方法
- 論理削除の実装方法
- 監査項目の共通化方法
- 固定マスタの初期データ投入方法
- Entity設計で具体化した固定マスタ項目をDB設計書へ反映するか