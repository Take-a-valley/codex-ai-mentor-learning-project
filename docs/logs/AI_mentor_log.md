# AI Mentor Log

このファイルは、通常メンターモードでの作業内容、学習内容、レビュー結果、次回タスクを記録する。

## ログ

まだ記録はありません。

---

## 2026-06-09 要件定義書レビュー

### 作業内容

- `docs/architecture/requirements.md` の要件定義書をレビューした
- システム管理者とプロジェクト管理者の責務分離を確認した
- ロール数、ロール定義の重複、管理者不在防止ルールの修正を確認した

### レビュー結果

- 要件定義書は、次工程へ進める状態
- 初期MVPの範囲、対象外範囲、ロール、機能一覧、機能詳細、権限一覧、管理データ、画面候補が整理されている
- 後続の基本設計、DB設計、API設計、画面設計へ接続できる粒度になっている

### 次回タスク

- 基本設計に進む
- 基本設計で、システム全体構成、主要機能の責務、画面構成、外部・内部構成の方針を整理する

---

## 2026-06-09 DB設計書レビュー

### 作業内容

- `docs/architecture/Task management function/db_design.md` をレビューした
- 要件定義書、基本設計書、DB設計書で添付ファイル保存方式がDB保存に揃っていることを確認した
- 論理削除、履歴系status、ProjectMemberの削除方針、PasswordResetTokenの更新日時を確認した

### レビュー結果

- DB設計書はAPI設計へ進める状態
- `SystemRole` と `ProjectRole` が分離されており、SYSTEM_ADMIN と PROJECT_ADMIN の兼任も表現できる
- 主要データの論理削除方針が整理されている
- 添付ファイル本体をDBに保存する方針が設計書間で整合している

### 次回タスク

- API設計に進む
- 認証、パスワードリセット、プロジェクト、プロジェクトメンバー、招待、タスク、コメント、添付ファイルのAPIを整理する
- 各APIの認可条件をDB設計と権限表に紐づけて確認する

---

## 2026-06-10 API設計レビュー

### 作業内容

- `docs/architecture/Task management function/api_design.md` のAPI設計書をレビューした
- 認証方式としてセッション方式を採用する方針を確認した
- 未登録ユーザーの招待URL経由登録APIを追加する方針を確認した
- APIレスポンス詳細JSON構造の章を追加する方針を確認した
- ステートレス、セッション、Cookie、CSRF、認証、認可、HTTPメソッド、JSON、ページングについて学習整理を行った

### レビュー結果

- API設計書は全体としてよく整理されている
- API一覧、認証・認可方針、DB設計との対応、レスポンスJSON構造が整理されている
- セッション方式採用により、Cookie、CSRF対策、セッション有効期限を後続工程で具体化する必要がある

### 残タスク

- `APIレスポンス詳細JSON構造` のページング対象から、初期MVPにAPIが存在しない `招待履歴` と `コメント一覧` を削除する
- 修正後、API設計書の最終レビューを行う

### 次回タスク

- API設計書の軽微修正確認
- 問題なければ画面設計に進む
- 画面一覧、画面遷移、画面ごとの利用API、ロール別表示制御を整理する

---

## 2026-06-12 作業再開・API設計最終確認

### 作業内容

- 作業再開に伴い、配下ドキュメントを確認した
- API設計書のページング対象から `招待履歴` と `コメント一覧` が削除されていることを確認した
- 要件定義書、基本設計書、DB設計書、API設計書の権限まわりの整合性を確認した

### レビュー結果

- 前回指摘していたページング対象の修正は完了している
- API設計書の認可チェック一覧に、コメント更新権限の表記ゆれが1点残っている

### 次回タスク

- コメント更新権限の表記を要件定義書・基本設計書・DB設計書と揃える
- 修正後、API設計書を完了扱いにして画面設計へ進む

---

## 2026-06-12 画面設計レビュー

### 作業内容

- `docs/architecture/Task management function/screen_design.md` の画面設計書をレビューした
- タスク作成・編集はモーダルで行う方針を確認した
- カンバン画面のドラッグアンドドロップを初期MVPに含める方針を確認した

### レビュー結果

- 画面構成、画面遷移、利用API、ロール別表示制御の大枠は整理されている
- 画面一覧の画面IDと各画面章のIDにずれがある
- タスク編集モーダルとドラッグアンドドロップ方針の反映が不足している
- プロジェクト作成権限について、API設計書との整合確認が必要

### 次回タスク

- 画面IDと章番号を揃える
- タスク作成・編集モーダルの表示項目、操作、入力チェックを追記する
- ドラッグアンドドロップによるStatus変更の画面仕様を追記する
- プロジェクト作成権限を設計書間で揃える

---

## 2026-06-13 終了報告

### 作業状況

- API設計書は前回指摘事項の修正を確認済みで、完了扱い
- 画面設計書のレビューを実施済み
- タスク作成・編集はモーダルで行う方針に決定
- カンバン画面のドラッグアンドドロップを初期MVPに含める方針に決定

### 次回開始時の確認事項

- 画面IDと各章の `SCR-xxx` が一致しているか確認する
- タスク作成・編集モーダルの表示項目、操作、入力チェックが追記されているか確認する
- ドラッグアンドドロップによるStatus変更の画面仕様が追記されているか確認する
- プロジェクト作成権限が、要件定義書・API設計書・画面設計書で揃っているか確認する

---

## 2026-06-18 作業再開

### 作業内容

- 作業再開に伴い、コンテキストファイル、ログ、画面設計書、API設計書を確認した
- API設計書は完了扱いであることを確認した
- 現在の主作業が画面設計レビューであることを確認した

### 現在の状況

- 画面設計書には、前回レビューで指摘した修正事項が残っている
- 基本設計フェーズの中では、画面設計の後に詳細なフロントエンド設計、バックエンド設計、認証・セキュリティ設計、テスト設計へ進む必要がある

### 次回タスク

- 画面設計書の残修正を反映する
- 画面設計書の最終レビューを行う
- 問題なければ次の基本設計系ドキュメントへ進む

---

## 2026-06-18 画面設計修正レビュー

### 作業内容

- `screen_design.md` の修正内容をレビューした
- 画面ID、章番号、タスク作成・編集モーダル、ドラッグアンドドロップ、プロジェクト作成権限の反映を確認した
- `api_design.md` のプロジェクト作成権限修正を確認した

### レビュー結果

- 画面設計書は大きな修正なしで次工程へ進める状態
- API設計書の `9.2 プロジェクト作成` に、対象プロジェクトが存在しない作成時の表現として不自然な文言が1点残っている

### 次回タスク

- API設計書のプロジェクト作成権限表記を自然な表現に修正する
- 認証・認可設計書に進む

---

## 2026-06-18 認証・認可設計書レビュー

### 作業内容

- `docs/architecture/Task management function/certification_approval_design.md` をレビューした
- CSRFトークン取得方針、認可判定の例外条件、管理者不在防止の対象範囲が追記されていることを確認した

### レビュー結果

- 認証・認可設計書は大きな修正なしで次工程へ進める状態
- API設計書の `9.2 プロジェクト作成` の権限表記に軽微な文言修正が残っている

### 次回タスク

- フロントエンド詳細設計書に進む
- Next.jsのルーティング、コンポーネント構成、状態管理、API呼び出し方針を整理する

---

## 2026-06-18 プロジェクト作成権限の方針変更

### 決定事項

- プロジェクト作成権限はシステム管理者のみとする

### 影響範囲

- 要件定義書
- 基本設計書
- DB設計書
- API設計書
- 画面設計書
- 認証・認可設計書
- フロントエンド設計書

### 次回タスク

- 各設計書のプロジェクト作成権限をシステム管理者のみへ統一する

---

## 2026-06-18 終了報告

### 作業内容

- 基本設計書一式の横断レビューを行った
- 基本設計書の配置変更を確認した
- 基本設計書は `docs/architecture/Task management function/Basic design/` に集約済み
- 詳細設計書は今後 `docs/architecture/Task management function/Detail design/` に作成する方針を確認した
- `docs/context/current_tasks.md` と `docs/context/architecture_summary.md` を更新した

### 現在の状況

- 基本設計書一式は大きな問題なし
- 作成済み基本設計書は以下
  - `requirements.md`
  - `basic_design.md`
  - `db_design.md`
  - `api_design.md`
  - `screen_design.md`
  - `certification_approval_design.md`
  - `frontend_design.md`
  - `backend_design.md`
  - `test_policy.md`
- 詳細設計用フォルダ `Detail design` は作成済み

### 残修正候補

- 画面設計書で、プロジェクト削除操作をプロジェクト編集画面に明記する
- 画面/API対応表で、プロジェクト削除APIをプロジェクト編集画面側に移す
- API設計書の認可表にある `ロジェクト管理者` のタイポを `プロジェクト管理者` に修正する

### 次回タスク

- 基本設計書の残修正反映を確認する
- 問題なければ基本設計フェーズを完了扱いにする
- 詳細設計フェーズで作成する設計書の順序と粒度を整理する

---

## 2026-06-19 終了報告

### 作業内容

- `technology_selection_decision.md` が削除済みであることを前提に、参照残りがないか確認した
- 技術選定・採用事項が各設計書へ反映されているかを横断レビューした
- `technical_decision_design.md`、基本設計書、コンテキストファイルの未決事項に古い情報が残っていないか確認した
- `docs/context/current_tasks.md` を更新した

### 確認結果

- `technology_selection_decision.md` への参照は残っていない
- `technical_decision_design.md` の採用技術一覧には、今回の採用事項が概ね反映されている
- 画面設計書のプロジェクト編集画面には、プロジェクト削除操作が追記済み
- 一部ドキュメントでは、採用済み技術が未決事項として残っている

### 残修正候補

- `technical_decision_design.md` のUIライブラリ方針を、Tailwind CSS基本採用、必要に応じてshadcn/ui採用に統一する
- `basic_design.md` と `requirements.md` の未決事項から、決定済みの技術項目を削除する
- `api_design.md` の未決事項から、決定済みのCSRF方式とセッション有効期限を削除する
- `backend_design.md` の使用技術表を、採用済みバージョンと周辺技術に具体化する
- `technology_stack.md` と `open_questions.md` の古い未決情報を必要に応じて更新する

### 次回タスク

- 上記の残修正が設計書へ反映されているかレビューする
- 問題なければ、詳細設計の次ドキュメント作成へ進む

---

## 2026-06-26 終了報告

### 作業内容

- 詳細設計フェーズの作業状況を復元した
- 採用済み技術が未決事項に残っていた箇所の修正反映を確認した
- `backend_structure_design.md` の作成内容をレビューした
- `entity_design.md` の作成内容をレビューした
- `dto_design.md` の作成内容をレビューした
- `repository_design.md` の作成内容をレビューした
- 次に作成する設計書候補を整理した
- `docs/context/current_tasks.md` を更新した

### 現在の状況

- 詳細設計書は `docs/architecture/Task management function/Detail design/` に作成中
- 作成済み詳細設計書は以下
  - `technical_decision_design.md`
  - `backend_structure_design.md`
  - `entity_design.md`
  - `dto_design.md`
  - `repository_design.md`
- `backend_structure_design.md`、`entity_design.md`、`dto_design.md` は大きな問題なし
- `repository_design.md` は大きな問題なしだが、実装時の詰まり防止として軽微な補足候補あり

### Repository設計書の残修正候補

- 論理削除済みユーザーと同じemailで再登録を許可するかを補足する
- 論理削除済みProjectMemberの再招待・再参加時の復元方針を補足する
- Projectが論理削除済みの場合は配下データを通常操作対象外とする方針を補足する
- 同一プロジェクト・同一メールのPENDING招待確認を追加する
- ユーザーのACTIVEパスワードリセットトークン取得を追加する

### 残っている主な詳細設計書

- `service_design.md`
- `controller_design.md`
- `exception_design.md`
- `security_design.md` または `authentication_authorization_detail_design.md`
- `flyway_migration_design.md`
- `docker_compose_design.md`
- `frontend_structure_design.md`
- `frontend_state_design.md`

### 次回タスク

- `repository_design.md` の軽微な補足修正が反映されているかレビューする
- 問題なければ `service_design.md` の作成へ進む

---

## 2026-06-27 終了報告

### 作業内容

- `frontend_state_design.md` の作成内容をレビューした
- TanStack Query、`useState`、`useReducer` について学習整理を行った
- これまで解説した用語を `docs/context/glossary.md` に追記した
- 今後、解説希望の用語や学習上重要な用語を `glossary.md` に追記する運用ルールを `learning_notes.md` と `current_tasks.md` に追加した
- タスク管理機能のワイヤーフレームHTML案を提示した
- ユーザーにより、ワイヤーフレームHTMLが `docs/architecture/Task management function/screen design/Task management function/wireframe.html` に移動されたことを確認した
- `docs/context/current_tasks.md` と `docs/context/architecture_summary.md` を更新した

### 現在の状況

- 詳細設計書は `docs/architecture/Task management function/Detail design/` に作成中
- 以下の詳細設計書は作成・レビュー済み
  - `technical_decision_design.md`
  - `backend_structure_design.md`
  - `entity_design.md`
  - `dto_design.md`
  - `repository_design.md`
  - `service_design.md`
  - `controller_design.md`
  - `exception_design.md`
  - `security_design.md`
  - `flyway_migration_design.md`
  - `docker_compose_design.md`
  - `frontend_structure_design.md`
  - `frontend_state_design.md`
- ワイヤーフレームHTMLは配置済み

### 次回タスク

- ワイヤーフレームHTMLをレビューし、画面設計・フロントエンド設計との整合を確認する
- 問題なければ、次の詳細設計として `frontend_api_client_design.md` または `test_case_design.md` の作成へ進む

---

## 2026-06-29 終了報告

### 作業内容

- ワイヤーフレームHTMLの修正内容をレビューした
- `frontend_api_client_design.md` の作成内容をレビューした
- Cookie送信、認証不要POSTのCSRF、403時の `errorCode` 判定、Query Key方針について補足観点を提示した
- `multipart/form-data`、`lib/features`、`Query Hooks`、`HttpOnly`、`JSESSIONID`、`Mutation` を解説した
- 解説した用語を `docs/context/glossary.md` に追記・補強した
- `frontend_component_design.md` の作成内容をレビューした
- `frontend_component_design.md` の修正内容を再レビューし、大きな問題がないことを確認した
- 次に作成する設計書として `test_case_design.md` の全文案を提示した
- `docs/context/current_tasks.md` を更新した

### 現在の状況

- 詳細設計書は `docs/architecture/Task management function/Detail design/` に作成中
- 以下の詳細設計書は作成・レビュー済み
  - `technical_decision_design.md`
  - `backend_structure_design.md`
  - `entity_design.md`
  - `dto_design.md`
  - `repository_design.md`
  - `service_design.md`
  - `controller_design.md`
  - `exception_design.md`
  - `security_design.md`
  - `flyway_migration_design.md`
  - `docker_compose_design.md`
  - `frontend_structure_design.md`
  - `frontend_state_design.md`
  - `frontend_api_client_design.md`
  - `frontend_component_design.md`
- ワイヤーフレームHTMLは配置済み

### 直近レビューでの残修正候補

- `frontend_component_design.md` の共通エラー表示コンポーネント説明は、内容は問題ないが「17. エラー表示方針」へ移動すると章構成として自然

### 次回タスク

- `test_case_design.md` を作成する
- 作成済みの場合は `test_case_design.md` をレビューする

---

## 2026-07-01 終了報告

### 作業内容

- 作業開始時に各Markdownファイルを読み込み、現在の作業状況を復元した
- `test_case_design.md` の作成内容をレビューした
- 詳細設計一式の横断レビューを実施した
- 横断レビューで、ファイル種別不正時のHTTPステータス、SYSTEM_ADMINロール変更の初期MVP対象外、Entity未決事項、Controllerでのログインユーザー取得方針、コメント編集表示方針を確認した
- ユーザーによる修正後、横断レビュー指摘事項が解消されていることを確認した
- クライアントモードに移行し、実装開始に必要な未決事項のみ最終整理する方針を確認した
- 一般的な開発順序に準じ、環境・DB・バックエンド基盤から実装を開始する方針を確認した
- 実装前の推奨採用事項として、App Router、shadcn/uiの限定利用、パスワードルール、ログイン失敗回数制限の初期MVP対象外、期限切れ処理の参照時判定、Java型方針、token保存形式、Controllerでのログインユーザー取得方針を整理した
- `docs/context/current_tasks.md`、`docs/context/decisions.md`、`docs/context/architecture_summary.md` を更新した

### 現在の状況

- 詳細設計フェーズの主要設計書は作成・レビュー済み
- 詳細設計一式の横断レビューは完了
- 実装開始前の最終整理では、実装開始に必要な未決事項のみ決める方針
- 開発順序は、一般的な業務アプリ開発に準じて環境・DB・バックエンド基盤から開始する方針

### 次回タスク

- 今回の最終整理内容を、どの設計書へ反映するか整理する
- 実装開始に必要な未決事項の採用内容を設計書へ反映する修正内容を提示する
- その後、実装順序を最終確定する

---

## 2026-07-03 終了報告

### 作業内容

- 作業開始時に各Markdownファイルを読み込み、現在の作業状況を復元した
- `.env.example` の内容をレビューした
- `.gitignore` を確認し、`.env` と `.env.*` がGit管理対象外、`.env.example` がGit管理対象であることを確認した
- GitHubへ上げてはいけないファイルが現時点で含まれていないか確認した
- Dockerの基本概念、Dockerでできること、Docker Composeの役割を未学者向けに解説した
- DockerにNext.jsやSpring Bootを含める構成について、初期実装ではMySQLとMailpitのみDocker Compose管理とし、将来的にfrontend/backendもDocker化する方針を説明した
- ポートフォリオ公開を見据えたDocker設定方針を整理した
- 設計書へ反映すべき対象、追記箇所、追記内容を提示した
- ユーザーによる設計書追記内容を確認し、大きな問題がないことを確認した
- `docker-compose.yml` に記述する内容を提示した
- `docker-compose.yml` の各行、`MYSQL_PORT` の扱い、`driver: local`、起動コマンドを解説した
- Docker関連用語を `docs/context/glossary.md` に追記した
- `docs/context/current_tasks.md` を更新した

### 現在の状況

- 詳細設計一式は作成・レビュー済み
- 実装用ディレクトリ構成は作成済み
- `.env.example` は作成・レビュー済み
- Docker Compose方針は設計書に反映済み
- 初期実装では、MySQL 8系とMailpitをDocker Composeで起動する
- Next.jsフロントエンドとSpring Bootバックエンドは、初期段階ではローカルPC上で起動する
- ポートフォリオ公開を見据え、実装安定後にfrontend/backendもDocker Compose管理に追加する方針

### 直近レビューでの残修正候補

- なし

### 次回タスク

- 必要に応じてローカル用 `.env` を作成する
- `infra/docker/docker-compose.yml` を作成する
- 作成した `docker-compose.yml` をレビューする
- Docker ComposeでMySQLとMailpitを起動確認する
- Mailpit Web UIを `http://localhost:8025` で確認する
- MySQLの起動状態、healthcheck、volume作成状況を確認する

---

## 2026-07-06 終了報告

### 作業内容

- Docker ComposeでMySQL 8系とMailpitを起動するための準備を進めた
- `infra/docker/docker-compose.yml` の内容をレビューし、MySQL、Mailpit、volume、network、healthcheckの設定方針を確認した
- Docker Desktop未起動時の接続エラー、Docker Engine、名前付きパイプの意味を整理した
- MySQLの3306番ポート競合を確認し、ローカル接続ポートを3307番へ変更する方針を整理した
- MySQLとMailpitのコンテナが `healthy` になることを確認した
- Mailpit Web UIとMySQL接続確認まで完了した
- `backend/pom.xml` の内容をレビューし、MySQL ConnectorのgroupId、XML構造、`build` 要素の配置、閉じタグの不足を確認した
- 修正後、`mvn -f backend/pom.xml validate` が成功したことを確認した
- Maven、POM、依存、JPA、Flyway、XML、XML Schema、jar、ビルドなどの用語を整理した
- `backend/src/main/resources/application.yml` の内容をレビューし、`spring.datasource`、`spring.jpa`、`spring.flyway`、`spring.mail` の階層構造を確認した
- `application.yml`、YAML、Markdownの役割と使い分けを整理した
- `docs/context/glossary/` 配下に、Docker Compose、pom.xml、application.ymlの学習用解説を整理した
- `docs/context/current_tasks.md` を、実装準備完了後の状態に更新した

### 現在の状況

- 詳細設計一式は作成・レビュー済み
- 実装用ディレクトリ構成は作成済み
- `.env.example`、`.env`、`.gitignore` の基本方針は確認済み
- Docker ComposeでMySQL 8系とMailpitを起動できる状態
- MySQLとMailpitはいずれも `healthy` を確認済み
- MySQLはローカルPC側の3307番ポートから接続する方針
- `backend/pom.xml` は作成・レビュー済みで、Maven validate成功済み
- `application.yml` は作成・レビュー済み
- 実装準備として、Docker Compose、Maven、Spring Boot設定の初期確認は完了

### 学習内容

- Docker Composeは、複数コンテナをまとめて定義・起動するための仕組み
- `.env` はローカル実行用の秘密情報や環境差分を持つためGit管理対象外にする
- `.env.example` は公開してよいサンプルとしてGit管理対象にする
- Dockerのポートマッピングは「PC側ポート:コンテナ側ポート」で考える
- MavenはJavaプロジェクトの依存関係管理とビルドを行うツール
- `pom.xml` はMavenにプロジェクト構成と依存関係を伝える設定ファイル
- `application.yml` はSpring Bootに実行時設定を伝えるファイル
- YAMLはインデントで階層を表すため、設定の親子関係が重要

### 直近レビューでの残修正候補

- なし

### 次回タスク

- Spring Boot起動クラス `DevSupportApplication` を作成する
- Spring Bootを最小構成で起動確認する
- `application.yml` の設定でMySQLへ接続できることを確認する
- Flywayの初期マイグレーション作成へ進む
- 初期マイグレーション作成後、DBスキーマと設計書の整合を確認する

---

## 2026-07-09 終了報告

### 作業内容

- 作業開始時に各Markdownファイルを読み込み、現在の作業状況を復元した
- Spring Boot起動クラス `DevSupportApplication` の内容をレビューした
- 起動クラス、`SpringApplication`、`SpringBootApplication`、エントリーポイントの役割を整理した
- Spring Bootを最小構成で起動し、Tomcat起動、MySQL接続、JPA初期化、Spring Security自動設定が行われていることをログから確認した
- Spring Boot起動成功ログの見方として、`Started DevSupportApplication`、`Tomcat started on port 8080`、`HikariPool-1 - Start completed` を確認ポイントとして整理した
- Flyway初期マイグレーション作成の考え方、配置場所、命名規則、作成順序、`flyway_schema_history`、チェックサムを学習した
- `docs/context/glossary/flyway_explanation.md` を作成し、Flywayの詳細解説を保存した
- README差し替え用ドラフトを `docs/context/readme_project_draft.md` として作成した
- `V1__create_master_tables.sql` と `V2__create_users_table.sql` の作成内容をレビューした
- 固定マスタテーブルの `code` カラム名は、設計書どおり各テーブル共通で `code` とする方針を確認した
- `users` テーブル名はFlywayマイグレーション設計書に合わせて複数形 `users` とする方針を確認した
- DB物理テーブル名は複数形 + snake_case、Java Entity名は単数形 + PascalCaseで整理する方針を確認した
- `docs/context/current_tasks.md` を、Flyway初期マイグレーション作成中の状態に更新した

### 現在の状況

- Spring Boot起動クラスは作成・レビュー済み
- Spring Bootの最小起動確認は完了
- MySQL 8系への接続確認は完了
- Flyway初期マイグレーション作成に着手済み
- `V2__create_users_table.sql` は大きな問題なし
- `V1__create_master_tables.sql` は固定マスタ4テーブルを作成する方針自体は正しいが、SQL構文上の残修正がある

### 学習内容

- 起動クラスはSpring Bootアプリケーションの入口
- `SpringApplication` はSpring Bootアプリを実際に起動する実行役
- `SpringBootApplication` は起動クラスであることを示し、自動設定やコンポーネントスキャンを有効にするアノテーション
- FlywayはDB構造変更をSQLファイルと履歴テーブルで管理するツール
- Flywayファイル名は `V1__create_xxx.sql` のように、バージョン番号と説明の間にアンダースコア2つを使う
- 実行済みマイグレーションは原則変更せず、必要に応じて次のバージョンを追加する
- DB制約には名前を付けると、エラー時に原因を追いやすい
- 同じカラムにインラインの `UNIQUE` と名前付き `CONSTRAINT` を二重に定義しない

### 直近レビューでの残修正候補

- `V1__create_master_tables.sql` の各テーブルで、`code` 行から `UNIQUE` を削除する
- `V1__create_master_tables.sql` の各テーブルで、`PRIMARY KEY (`id`)` の後ろにカンマを追加する

### 次回タスク

- `V1__create_master_tables.sql` の残修正を行う
- 修正後、`V1__create_master_tables.sql` を再レビューする
- 問題なければSpring Bootを起動し、FlywayでV1/V2が適用されることを確認する
- MySQL上に固定マスタテーブルと `users` テーブルが作成されたことを確認する
- 次のマイグレーションとしてプロジェクト系テーブル作成へ進む
