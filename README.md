# 開発支援アプリケーション学習プロジェクト

このリポジトリは、CodexをAIメンターとして活用しながら、開発支援アプリケーションを要件定義、設計、実装、レビューまで順番に学習するためのプロジェクトです。

AIに実装を丸投げするのではなく、ユーザーが主体となって設計・実装を行い、Codexは技術メンター、設計レビュー担当、コードレビュー担当、デバッグ支援担当として利用します。

## プロジェクトの目的

本プロジェクトの目的は、開発未経験・ジュニアエンジニアが、実務に近い流れでWebアプリケーション開発を学ぶことです。

特に以下を重視します。

- 要件定義から実装までの工程を順番に理解する
- 設計書を自分で作成できるようになる
- AIの提案を鵜呑みにせず、自分で判断できるようになる
- コードが動くだけでなく、なぜその設計・実装にしたのか説明できるようになる
- エラー発生時に原因を切り分ける力を身につける
- ポートフォリオとして説明可能な成果物を作る

## 作成するシステム

システム開発に必要な機能を集約した、小規模開発チーム向けの開発支援アプリケーションを作成します。

将来的には、以下の機能を段階的に追加する想定です。

1. Jiraのようなカンバン方式によるタスク管理
2. Wikiによるドキュメント管理
3. WBS・ガントチャート
4. プロジェクト内チャットの統合管理

初期MVPでは、まず **複数プロジェクト対応のカンバン型タスク管理機能** を実装します。

## 初期MVPの主な機能

初期MVPでは、以下を対象とします。

- メールアドレスとパスワードによるログイン
- パスワードリセット
- セッション方式による認証
- CSRF対策
- システム管理者、プロジェクト管理者、メンバー、閲覧者の権限管理
- 複数プロジェクトの作成・切り替え
- プロジェクトメンバーのメール招待
- 未登録ユーザーの招待URL経由登録
- カンバン方式のタスク管理
- 固定ステータス列: `ToDo / In Progress / Review / Done`
- タスク作成・編集・削除
- ドラッグアンドドロップによるタスクのStatus変更
- タスクコメントの作成・編集・削除
- 添付ファイルの追加・取得・削除
- 主要データの論理削除

## 使用技術

本プロジェクトでは、以下の技術を使用します。

| 領域 | 技術 |
|---|---|
| フロントエンド | Next.js / TypeScript / App Router / Tailwind CSS / TanStack Query |
| バックエンド | Spring Boot / Java / Maven |
| DB | MySQL 8系 |
| DBアクセス | Spring Data JPA |
| DBマイグレーション | Flyway |
| 認証・認可 | Spring Security / セッション方式 / CSRF |
| 開発用メール確認 | Mailpit |
| 開発環境 | Docker Compose |
| テスト | Spring Boot Test / JUnit / Vitest / React Testing Library / Playwright |

## AIメンター運用

本プロジェクトでは、Codexを「実装代行AI」ではなく「AIメンター」として使用します。

Codexの主な役割は以下です。

- 要件整理の壁打ち
- 設計書の章構成や記載観点の提示
- ユーザーが作成した設計書のレビュー
- 実装方針の整理
- エラー原因の切り分け支援
- 用語解説と学習メモの整理
- 作業状況の引き継ぎ管理

一方で、AIが勝手に実装や設計変更を進めないよう、編集できる範囲を制限しています。

AIが直接編集してよい範囲:

```text
docs/context/ 配下のMarkdown
docs/logs/ 配下のMarkdown
```

原則としてAIが直接編集しない範囲:

```text
docs/architecture/ 配下の設計書
ソースコード
設定ファイル
packageや依存関係ファイル
Markdown以外のファイル
```

設計書やコードはユーザーが作成し、Codexはレビューと学習支援を行います。

## ディレクトリ構成

主なディレクトリ構成は以下です。

```text
.
├─ backend/
│  ├─ pom.xml
│  └─ src/
│     └─ main/
│        ├─ java/
│        │  └─ com/example/devsupport/
│        └─ resources/
│           ├─ application.yml
│           └─ db/migration/
├─ frontend/
├─ infra/
│  └─ docker/
│     └─ docker-compose.yml
├─ docs/
│  ├─ architecture/
│  │  └─ タスク管理機能/
│  │     ├─ 基本設計/
│  │     ├─ 詳細設計/
│  │     └─ 画面設計/
│  ├─ context/
│  └─ logs/
├─ AGENTS.md
├─ PROMPT.md
├─ README.md
└─ .env.example
```

## ドキュメント構成

### `AGENTS.md`

AIエージェントが守る最上位ルールを定義します。

AI主導開発を禁止し、ユーザー主体で進行するための方針を記載しています。

### `PROMPT.md`

CodexをAIメンターとして動作させるためのメインプロンプトです。

作業開始時は、このファイルを読み込ませてプロジェクト状況を復元します。

### `docs/context/`

現在の状況、決定事項、未決事項、学習メモ、用語集を管理します。

AIが直接編集できる領域です。

### `docs/logs/`

作業ログや議事録を管理します。

作業終了時の `終了報告` で、次回作業に必要な情報を記録します。

### `docs/architecture/`

要件定義書、基本設計書、詳細設計書などの設計成果物を管理します。

この配下の設計書は、ユーザーが作成・修正します。

## 現在の進捗

現在は、タスク管理機能の基本設計・詳細設計が完了し、実装準備からバックエンド最小起動確認まで進んでいます。

完了済みの主な作業:

- 要件定義書の作成
- 基本設計書一式の作成
- 詳細設計書一式の作成
- ワイヤーフレームHTMLの作成
- 実装計画書の作成
- Docker ComposeによるMySQL、Mailpit起動確認
- `pom.xml` 作成とMaven検証
- `application.yml` 作成
- Spring Boot起動クラス作成
- Spring Boot最小起動確認
- MySQL接続確認

次の主な作業:

- Flyway初期マイグレーション作成
- DBテーブル作成
- Entity実装
- Repository実装
- 共通例外実装
- Security実装

## 開発環境の起動

MySQLとMailpitはDocker Composeで起動します。

プロジェクトルートで以下を実行します。

```bash
docker compose --env-file .env -f infra/docker/docker-compose.yml up -d
```

起動状態の確認:

```bash
docker compose --env-file .env -f infra/docker/docker-compose.yml ps
```

Mailpit Web UI:

```text
http://localhost:8025
```

## バックエンドの起動

Spring Bootバックエンドは、以下で起動します。

```bash
mvn -f backend/pom.xml spring-boot:run
```

起動成功時は、ログに以下のような表示が出ます。

```text
Started DevSupportApplication
Tomcat started on port 8080
```

## 学習メモ

本プロジェクトでは、学習中に出てきた重要用語を以下に記録しています。

```text
docs/context/glossary/
```

主な内容:

- Docker / Docker Compose
- Maven / pom.xml
- application.yml
- Spring Boot起動クラス
- Flyway
- JPA
- Spring Security
- TanStack Query

単にコードを書くのではなく、用語や仕組みを理解しながら進めることを重視しています。

## 今後の方針

まずはタスク管理機能の初期MVPを完成させます。

その後、以下の順に段階的な拡張を検討します。

1. Wiki機能
2. WBS・ガントチャート機能
3. プロジェクト内チャット機能
4. frontend/backendを含めたDocker Compose構成
5. ポートフォリオ公開に向けたREADME・起動手順整備
6. デプロイ・CI/CD検討

## 注意事項

このリポジトリは、AIに成果物を丸投げするためのものではありません。

AIを活用しながらも、設計判断、実装、レビュー観点を自分で理解できるようになることを目的としています。

そのため、AIの回答は必ず確認し、分からない用語や設計意図は都度学習メモに残していきます。
