# Flywayマイグレーション設計書

## 1. 本書の目的

本書は、タスク管理機能の初期MVPにおけるFlywayマイグレーション設計を定義することを目的とする。

DB設計書、Entity設計書、Repository設計書をもとに、MySQL 8系で作成するテーブル、固定マスタ、制約、インデックス、マイグレーションファイルの作成順序を整理する。

## 2. 対象範囲

対象に含めるものは以下とする。

- Flywayの利用方針
- マイグレーションファイルの命名方針
- テーブル作成順序
- 固定マスタ投入方針
- 外部キー制約方針
- 一意制約方針
- インデックス方針
- 論理削除カラム方針
- 添付ファイル本体カラム方針
- 初期データ投入方針

対象外とするものは以下とする。

- SQL本文の完全な記述
- Java Entityの実装詳細
- Repositoryメソッドの実装詳細
- 本番DB運用設計
- バックアップ・リストア設計

## 3. Flyway利用方針

DBマイグレーションにはFlywayを使用する。

テーブル定義、固定マスタ投入、初期データ投入はFlywayのマイグレーションファイルで管理する。

DB構造の変更履歴をファイルとして残し、開発環境を再作成しても同じDB構造を再現できるようにする。

手動SQLのみでDBを変更しない。

## 4. マイグレーションファイル配置方針

Flywayのマイグレーションファイルは、Spring Boot標準の配置場所を基本とする。

| 項目 | 方針 |
|---|---|
| 配置場所 | `src/main/resources/db/migration` |
| ファイル形式 | SQL |
| 文字コード | UTF-8 |
| DB | MySQL 8系 |

## 5. ファイル命名方針

Flywayのバージョン付きマイグレーションファイルとして管理する。

命名は以下を基本とする。

| 種別 | 命名例 | 内容 |
|---|---|---|
| テーブル作成 | V1__create_master_tables.sql | 固定マスタ系テーブル作成 |
| テーブル作成 | V2__create_users_table.sql | ユーザー系テーブル作成 |
| テーブル作成 | V3__create_project_tables.sql | プロジェクト系テーブル作成 |
| テーブル作成 | V4__create_task_tables.sql | タスク系テーブル作成 |
| テーブル作成 | V5__create_indexes.sql | 各テーブルのインデックス追加 |
| テーブル作成 | V6__create_task_other_tables.sql | タスク系テーブル作成 |
| テーブル作成 | V7__create_history_tables.sql | 招待・パスワードリセット系テーブル作成 |
| 初期データ | V8__insert_master_data.sql | 固定マスタ初期データ投入 |
| 開発用初期データ | V9__insert_dev_initial_admin.sql | 開発環境用の初期管理者投入。共通マイグレーションには含めない |

ファイル番号は一度適用した後に変更しない。

## 6. マイグレーション作成順序

外部キー参照の依存関係を考慮し、親テーブルから順に作成する。

作成順序は以下を基本とする。

1. 固定マスタテーブル
2. Userテーブル
3. Projectテーブル
4. ProjectMemberテーブル
5. Taskテーブル
6. TaskCommentテーブル
7. Attachmentテーブル
8. ProjectInvitationテーブル
9. PasswordResetTokenテーブル
10. 固定マスタ初期データ
11. 開発用初期管理者データ

## 7. 対象テーブル一覧

| テーブル | 概要 |
|---|---|
| system_roles | システム全体ロール |
| project_roles | プロジェクト内ロール |
| task_statuses | タスクStatus |
| task_priorities | タスクPriority |
| users | ユーザー |
| projects | プロジェクト |
| project_members | プロジェクトメンバー |
| tasks | タスク |
| task_comments | タスクコメント |
| attachments | 添付ファイル |
| project_invitations | プロジェクト招待 |
| password_reset_tokens | パスワードリセットトークン |

## 8. 固定マスタテーブル方針

以下のテーブルは固定マスタとして扱う。

- system_roles
- project_roles
- task_statuses
- task_priorities

固定マスタは初期MVPでは画面やAPIから追加・変更・削除しない。

固定マスタには、実装上の判定に使う `code` と、画面表示に使う `name` を持たせる。

表示順が必要なマスタには `sort_order` を持たせる。

## 9. 固定マスタ初期データ

### system_roles

| code | name |
|---|---|
| SYSTEM_ADMIN | システム管理者 |
| USER | 一般ユーザー |

### project_roles

| code | name |
|---|---|
| PROJECT_ADMIN | プロジェクト管理者 |
| MEMBER | メンバー |
| VIEWER | 閲覧者 |

### task_statuses

| code | name | sort_order |
|---|---|---|
| TODO | ToDo | 1 |
| IN_PROGRESS | In Progress | 2 |
| REVIEW | Review | 3 |
| DONE | Done | 4 |

### task_priorities

| code | name | sort_order |
|---|---|---|
| HIGHEST | Highest | 1 |
| HIGH | High | 2 |
| MEDIUM | Medium | 3 |
| LOW | Low | 4 |
| LOWEST | Lowest | 5 |

## 10. usersテーブル方針

usersはシステム利用者を管理する。

主な項目は以下とする。

| 項目 | 概要 |
|---|---|
| id | ユーザーID |
| email | メールアドレス |
| password_hash | ハッシュ化済みパスワード |
| display_name | 表示名 |
| system_role_id | システムロールID |
| created_at | 作成日時 |
| updated_at | 更新日時 |
| deleted_at | 削除日時 |
| deleted_by | 削除者ユーザーID |

emailは一意制約を設定する。

論理削除済みユーザーと同じemailでの再登録は、初期MVPでは許可しない方針とする。

## 11. projectsテーブル方針

projectsはプロジェクトを管理する。

主な項目は以下とする。

| 項目 | 概要 |
|---|---|
| id | プロジェクトID |
| name | プロジェクト名 |
| description | プロジェクト説明 |
| created_by | 作成者ユーザーID |
| created_at | 作成日時 |
| updated_at | 更新日時 |
| deleted_at | 削除日時 |
| deleted_by | 削除者ユーザーID |

プロジェクト削除は論理削除とする。

プロジェクト削除時の関連データ論理削除はService層で制御する。

## 12. project_membersテーブル方針

project_membersはユーザーとプロジェクトの参加関係を管理する。

主な項目は以下とする。

| 項目 | 概要 |
|---|---|
| id | プロジェクトメンバーID |
| project_id | プロジェクトID |
| user_id | ユーザーID |
| project_role_id | プロジェクトロールID |
| joined_at | 参加日時 |
| deleted_at | 削除日時 |
| deleted_by | 削除者ユーザーID |

project_idとuser_idの組み合わせは一意制約を設定する。

論理削除済みProjectMemberを再招待・再参加させる場合は、既存レコードを復元する。

## 13. tasksテーブル方針

tasksはカンバンで扱うタスクを管理する。

主な項目は以下とする。

| 項目 | 概要 |
|---|---|
| id | タスクID |
| project_id | プロジェクトID |
| summary | タスク概要 |
| description | タスク説明 |
| status_id | Status ID |
| priority_id | Priority ID |
| assignee_id | 担当者ユーザーID |
| creator_id | 作成者ユーザーID |
| due_date | 期限 |
| created_at | 作成日時 |
| updated_at | 更新日時 |
| deleted_at | 削除日時 |
| deleted_by | 削除者ユーザーID |

status_idはtask_statusesを参照する。

priority_idはtask_prioritiesを参照する。

assignee_idは任意とする。

## 14. task_commentsテーブル方針

task_commentsはタスクに紐づくコメントを管理する。

主な項目は以下とする。

| 項目 | 概要 |
|---|---|
| id | コメントID |
| task_id | タスクID |
| user_id | 投稿者ユーザーID |
| body | コメント本文 |
| created_at | 作成日時 |
| updated_at | 更新日時 |
| deleted_at | 削除日時 |
| deleted_by | 削除者ユーザーID |

コメント削除は論理削除とする。

削除済みユーザーが投稿したコメントも履歴として残す。

## 15. attachmentsテーブル方針

attachmentsはタスク添付ファイルのメタ情報とファイル本体を管理する。

主な項目は以下とする。

| 項目 | 概要 |
|---|---|
| id | 添付ファイルID |
| task_id | タスクID |
| uploaded_by | アップロード者ユーザーID |
| file_name | 元ファイル名 |
| file_data | ファイル本体データ |
| file_size | ファイルサイズ |
| content_type | ファイル種別 |
| created_at | 作成日時 |
| deleted_at | 削除日時 |
| deleted_by | 削除者ユーザーID |

file_dataはMySQLでバイナリデータを保存できる型を使用する。

1ファイルあたり5MBまで保存できる型を選択する。

添付ファイル削除は論理削除とし、通常取得では削除済み添付ファイルを除外する。

## 16. project_invitationsテーブル方針

project_invitationsはプロジェクト招待情報を管理する。

主な項目は以下とする。

| 項目 | 概要 |
|---|---|
| id | 招待ID |
| project_id | プロジェクトID |
| email | 招待先メールアドレス |
| token | 招待URL用トークン |
| invited_by | 招待者ユーザーID |
| initial_project_role_id | 初期プロジェクトロールID |
| expires_at | 有効期限 |
| accepted_at | 承認日時 |
| status | 招待状態 |
| created_at | 作成日時 |
| updated_at | 更新日時 |

招待情報は履歴として残す。

tokenには一意制約を設定する。

statusは `PENDING`、`ACCEPTED`、`EXPIRED`、`REVOKED` のいずれかとする。

## 17. password_reset_tokensテーブル方針

password_reset_tokensはパスワードリセット情報を管理する。

主な項目は以下とする。

| 項目 | 概要 |
|---|---|
| id | パスワードリセットID |
| user_id | ユーザーID |
| token | リセットURL用トークン |
| status | パスワードリセット状態 |
| expires_at | 有効期限 |
| used_at | 使用日時 |
| created_at | 作成日時 |
| updated_at | 更新日時 |

パスワードリセット情報は履歴として残す。

tokenには一意制約を設定する。

statusは `ACTIVE`、`USED`、`EXPIRED` のいずれかとする。

## 18. 外部キー制約方針

外部キー制約は、主要な親子関係に設定する。

対象例は以下とする。

| 子テーブル | 親テーブル |
|---|---|
| users.system_role_id | system_roles.id |
| projects.created_by | users.id |
| project_members.project_id | projects.id |
| project_members.user_id | users.id |
| project_members.project_role_id | project_roles.id |
| tasks.project_id | projects.id |
| tasks.status_id | task_statuses.id |
| tasks.priority_id | task_priorities.id |
| tasks.assignee_id | users.id |
| tasks.creator_id | users.id |
| task_comments.task_id | tasks.id |
| task_comments.user_id | users.id |
| attachments.task_id | tasks.id |
| attachments.uploaded_by | users.id |
| project_invitations.project_id | projects.id |
| project_invitations.invited_by | users.id |
| project_invitations.initial_project_role_id | project_roles.id |
| password_reset_tokens.user_id | users.id |
| users.deleted_by | users.id |
| projects.deleted_by | users.id |
| project_members.deleted_by | users.id |
| tasks.deleted_by | users.id |
| task_comments.deleted_by | users.id |
| attachments.deleted_by | users.id |

物理削除を前提にしないため、外部キーのカスケード削除は使用しない方針とする。

関連データの論理削除はService層で制御する。

deleted_byは削除操作を行ったユーザーIDを記録する監査項目とする。

初期MVPでは、deleted_byにもusers.idへの外部キー制約を設定する方針とする。

ただし、削除済みユーザーが存在しても履歴を保持するため、ユーザーの物理削除は行わない。

## 19. 一意制約方針

一意制約は、DB側でも担保する。

| 対象 | 一意条件 |
|---|---|
| users | email |
| system_roles | code |
| project_roles | code |
| task_statuses | code |
| task_priorities | code |
| project_members | project_id + user_id |
| project_invitations | token |
| password_reset_tokens | token |

Repositoryによる事前確認だけに依存せず、DB制約でも不整合を防ぐ。

## 20. インデックス方針

検索条件として頻繁に使う項目にはインデックスを検討する。

| テーブル | インデックス候補 |
|---|---|
| users | email, deleted_at |
| projects | created_by, deleted_at |
| project_members | project_id, user_id, deleted_at |
| tasks | project_id, status_id, assignee_id, deleted_at |
| task_comments | task_id, user_id, deleted_at |
| attachments | task_id, uploaded_by, deleted_at |
| project_invitations | project_id, email, token, status, expires_at |
| password_reset_tokens | user_id, token, status, expires_at |

初期MVPでは、取得条件として明確に使用する項目を中心に設定する。

## 21. 論理削除カラム方針

以下のテーブルは論理削除対象とする。

- users
- projects
- project_members
- tasks
- task_comments
- attachments

論理削除対象テーブルには `deleted_at` を持たせる。

削除者を記録する必要があるテーブルには `deleted_by` を持たせる。

通常取得では `deleted_at is null` を条件に含める。

## 22. 履歴系テーブル方針

以下のテーブルは履歴として残す。

- project_invitations
- password_reset_tokens

履歴系テーブルは物理削除しない。

状態はstatusで管理する。

期限切れ状態はDB上のstatusを必ず即時更新するのではなく、参照時に `expires_at` と現在日時を比較して判定する。

必要に応じて、利用不可と判定したタイミングでstatusを `EXPIRED` に更新してもよい。

## 23. 初期管理者データ方針

開発環境では、初期ログイン用のSYSTEM_ADMINユーザーを用意する。

初期管理者のメールアドレス、表示名、初期パスワードは、設計書に固定値として記載しない。

初期パスワードは平文で保存せず、ハッシュ化済みの値を登録する。

初期管理者データの投入方法は、環境変数、初期セットアップ手順、または開発用マイグレーションで扱う。

本番環境での初期管理者作成方法は後続工程で具体化する。

開発用初期管理者データは、共通マイグレーションではなく、開発環境専用の投入方法で管理する。

本番環境に適用される通常のFlywayマイグレーションには、初期管理者の固定データを含めない。

## 24. 環境別マイグレーション方針

テーブル定義と固定マスタは、環境に依存しない共通マイグレーションとして扱う。

開発用初期管理者など、環境依存のデータは本番環境へ不用意に適用しない。

初期MVPでは、開発環境で学習しやすいことを優先し、開発用初期データの扱いを後続工程で整理する。

## 25. ロールバック方針

Flywayでは、適用済みマイグレーションを安易に修正しない。

開発初期でDBを作り直せる段階では、DB再作成によりやり直す。

チーム開発や共有DBで適用済みのマイグレーションを変更する場合は、新しいバージョンのマイグレーションを追加する。

## 26. 禁止事項

以下を禁止する。

- 適用済みマイグレーションファイルを無断で変更する
- 手動SQLでのみDB構造を変更する
- DB制約なしでアプリケーション側の事前チェックだけに依存する
- パスワードやtokenを平文でログに出力する
- 外部キーのカスケード削除で業務データを物理削除する

## 27. 決定事項

- DBマイグレーションにはFlywayを使用する
- テーブル定義と固定マスタ投入をFlywayで管理する
- MySQL 8系を前提とする
- 固定マスタは初期データとして投入する
- 主要データは論理削除とする
- 履歴系テーブルは物理削除しない
- 外部キーのカスケード削除は使用しない
- 添付ファイル本体はDBに保存する
- 一意制約はDB側でも設定する

## 28. 未決事項

以下は後続工程で具体化する。

- 正式なSQLファイル分割
- 各カラムの具体的なDB型
- 文字数上限
- file_dataの具体的なDB型
- tokenを平文保存するかハッシュ化するか
- 開発用初期管理者データの投入方法