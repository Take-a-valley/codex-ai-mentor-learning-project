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
| 共通マイグレーション配置場所 | `src/main/resources/db/migration` |
| 開発専用マイグレーション配置場所 | `src/main/resources/db/dev-migration` |
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
| 開発用初期データ | V9__insert_dev_initial_admin.sql | 開発環境用の初期管理者投入。 |
| 開発用データ形式更新（未作成） | V10__prefix_dev_password_hashes.sql | 開発DBの識別子なしBCryptハッシュへ{bcrypt}を追加 |

V9__insert_dev_initial_admin.sql は `src/main/resources/db/dev-migration` 配下に配置する。
通常の `src/main/resources/db/migration` 配下には置かない。
通常の `application.yml` では `classpath:db/migration` のみを読み込む。
`application-dev.yml` では `classpath:db/migration` と `classpath:db/dev-migration` の両方を読み込む。

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

### 23.1 初期管理者ハッシュ形式

#### 確認済み状態と実装方針（以下の過去の未確認記録より優先）

適用後確認もユーザー提示SQL結果で完了：total_users=1、prefixed_users=1、bcrypt_only_users=0、version=10 / description=prefix dev password hashes / success=1。件数維持と識別子付き形式への移行を確認した。照合成功や認証APIの動作は別途検証する。以下の適用後未確認という記録は過去状態。

2026-11-13にV10のSQLをレビューし、ユーザー提示のFlywayログ（16:43:22.913 +09:00）で1件適用成功・version v10を確認した。実行時間はログ表示0.016秒。AIによる適用は行っていない。以下のV10未作成・未適用は過去状態であり解消。更新後のusers件数・形式判定はまだ未提示で、影響行数や照合成功はこのログだけでは保証しない。適用済みV10は以後変更しない。

- ユーザー提示結果でV1〜V9がsuccess=1、usersが1件、形式判定はBCRYPT_ONLYが1件と確認。判定は先頭形式に基づき、パスワードとの照合成功・BCrypt文字列全体の妥当性を保証しない。
- 適用済みV9を変更せず、src/main/resources/db/dev-migration/V10__prefix_dev_password_hashes.sqlを追加する方針。ファイルはまだ未作成。通常のdb/migrationへ置かず、devプロファイルでのみ適用する。
- 更新対象は開発DBのusersのうち、保存値の先頭が$2a$・$2b$・$2y$である識別子なしBCrypt形式の行に限定する。{bcrypt}付きやその他形式の行は対象外とし、ハッシュ本体を保ったまま先頭へ{bcrypt}を付ける。再ハッシュ・平文保存・ユーザー削除/再投入は行わない。
- 現状の想定対象は1件。適用直前に件数・形式を再確認し、適用後は履歴V10の成功、users件数維持、PREFIXEDが1件・BCRYPT_ONLYが0件を確認する。ハッシュ全体は表示しない。
- 新規開発DBでもV9の投入後にV10が変換する。既存開発DBではV10のみ追加適用する。適用後のSQL変更や、既適用V9の書き換えによるチェックサム不整合を避ける。
- PasswordEncoder Beanと照合単体テストは別途実装する。DB形式変換の成功だけでログイン成功とは扱わない。

続くユーザー提示のCOUNT結果でusersは1件と確認した。「DBは空」という当初報告は現在の状態ではない。V9適用済み・users 1件を前提に既存データの形式移行を検討する。ただしDB上の実際のpassword_hash形式と対象ユーザーの同定は未確認。既存データを削除・再投入せず、形式を確認して開発用追加マイグレーションの対象条件を確定する。

2026-11-13のユーザー提示SQL結果で、version 1〜9がすべてsuccess=1、V9のdescriptionがinsert dev initial adminであることを確認した。以下の適用履歴未確認という記録はこの結果で解消。適用済みV9は変更しない。ユーザー件数の結果は未提示であり、0件とは確定しない。開発用の追加マイグレーションの更新・投入対象は件数確認後に決める。

- DelegatingPasswordEncoder採用に合わせ、開発用初期管理者も{bcrypt}付きの保存形式を使用する。BCryptハッシュ本体のみでは新しい照合契約に適合しない。
- 2026-11-13にユーザーからDBは空との報告あり。ただしV9のFlyway適用履歴が残るかは未確認。usersが0件という情報だけで適用済みSQLを変更してよいと判断しない。
- V9の既存環境への適用状況を確認し、未適用かつ共有済みの適用環境もないと確認できた場合に限り、V9の初期値形式の修正を検討する。適用履歴がある場合はV9を維持し、開発用の追加マイグレーション等を別途設計する。ファイル名・番号・具体的SQLは履歴確認後に確定する。
- 空DBであっても、現行V9をそのまま適用すると識別子なしハッシュが再生成されるため、初回起動・ログイン確認より前に整合を取る。破壊的なDB再作成・履歴の手動削除・Flyway repairを回避策として自動実行しない。
- 今回は文書上の方針変更のみ。V9・その他SQL・DB・設定は未変更。初期データ適用後の形式確認とログイン接続確認は別途必要。

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
