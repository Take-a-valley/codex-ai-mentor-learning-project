# Current Tasks

## 現在の工程

- 実装フェーズ準備

## 現在のタスク

- 詳細設計フェーズは主要設計書の作成・レビューまで完了
- `技術方針設計書.md` は作成・レビュー済み
- `バックエンド構成設計書.md` は作成・レビュー済み
- `Entity設計書.md` は作成・レビュー済み
- `DTO設計書.md` は作成・レビュー済み
- `Repository設計書.md` は作成・レビュー済み
- `Service設計書.md` は作成・レビュー済み
- `Controller設計書.md` は作成・レビュー済み
- `例外設計書.md` は作成・レビュー済み
- `セキュリティ設計書.md` は作成・レビュー済み
- `Flywayマイグレーション設計書.md` は作成・レビュー済み
- `Docker_Compose設計書.md` は作成・レビュー済み
- `フロントエンド構成設計書.md` は作成・レビュー済み
- `フロントエンド状態管理設計書.md` は作成・レビュー済み
- `フロントエンドAPIクライアント設計書.md` は作成・レビュー済み
- `フロントエンドコンポーネント設計書.md` は作成・レビュー済み
- `テストケース設計書.md` は作成・レビュー済み
- `実装計画書.md` は作成・レビュー済み
- 詳細設計一式の横断レビューは完了
- ワイヤーフレームHTMLは `docs/architecture/タスク管理機能/画面設計/ワイヤーフレーム.html` に配置済み
- クライアントモードで、実装開始に必要な未決事項のみ最終整理する方針を確認済み
- 実装用ディレクトリ構成は作成済み
- バックエンドのJavaパッケージは `backend/src/main/java/com/example/devsupport/` 配下に整理済み
- `.env.example` は作成・レビュー済み
- `.gitignore` は `.env`、`.env.*` を除外し、`.env.example` のみGit管理対象にする設定を確認済み
- Dockerの基本概念、Docker Compose、ポートフォリオ公開を見据えたDocker化方針を整理済み
- 設計書には、初期実装ではMySQLとMailpitのみDocker Composeで管理し、将来的にfrontend/backendもDocker化する方針を反映済み
- `infra/docker/docker-compose.yml` は作成・レビュー済み
- Docker ComposeでMySQL 8系とMailpitを起動し、両コンテナの `healthy` を確認済み
- Mailpit Web UIは `http://localhost:8025` で確認済み
- MySQLの接続確認は完了済み
- 3306番ポート競合に対応するため、ローカル接続ポートは `3307` を使用する方針
- `backend/pom.xml` は作成・レビュー済み
- `mvn -f backend/pom.xml validate` は成功済み
- `backend/src/main/resources/application.yml` は作成・レビュー済み
- 実装準備として、Docker Compose、Maven、Spring Boot設定の初期確認は完了済み
- Spring Boot起動クラス `DevSupportApplication` は作成・レビュー済み
- Spring Bootの最小起動確認は完了済み
- Spring Boot起動時にMySQL 8系へ接続できることを確認済み
- Flyway初期マイグレーション作成に着手済み
- `V1__create_master_tables.sql` は作成・レビュー済み
- `V2__create_users_table.sql` は作成・レビュー済み
- Flywayマイグレーション設計書のV2ファイル名例は、実ファイル名に合わせて修正済み
- `spring-boot-starter-flyway` 追加後、FlywayがSpring Boot起動時に動作することを確認済み
- FlywayによりV1/V2が適用され、固定マスタテーブルと `users` テーブルが作成済み
- `flyway_schema_history` に version 1 と version 2 の成功履歴が記録済み
- `V3__create_project_tables.sql` は作成・レビュー・Flyway適用確認済み
- `V4__create_task_tables.sql` は作成・レビュー・Flyway適用確認済み
- `V5__create_indexes.sql` は作成・レビュー・Flyway適用確認済み
- `V6__create_task_other_tables.sql` は作成・レビュー・Flyway適用確認済み
- `V7__create_history_tables.sql` は作成・レビュー・Flyway適用確認済み
- `flyway_schema_history` に version 1 から version 7 までの成功履歴が記録済み
- MySQL上で `task_comments`、`attachments`、`project_invitations`、`password_reset_tokens` の作成確認済み
- Flywayマイグレーション設計書のファイル命名方針は、V5以降の実装順に合わせて更新済み

## 直近レビューでの残修正候補

- なし

## 継続運用ルール

- ユーザーが解説を希望した用語、または学習上重要な用語は `docs/context/glossary/glossary.md` または `docs/context/glossary/` 配下の学習用Markdownに追記する
- 用語集追記時は、意味と本プロジェクトでの扱い方を簡潔に記録する

## 次回タスク

- Gitで `Flywayマイグレーション設計書.md`、`V6__create_task_other_tables.sql`、`V7__create_history_tables.sql`、本コンテキスト更新を確認する
- 問題なければV6/V7関連の変更をコミットする
- 次のマイグレーションとして `V8__insert_master_data.sql` の作成に進む
- V8では固定マスタ初期データの投入内容を、設計書と既存マスタテーブル定義に基づいて確認する
