# Codex AI Mentor Project Template

Codexを「コード生成AI」ではなく、**自己学習を支援するAIメンター**として使うためのプロジェクトテンプレートです。

開発未経験・ジュニアエンジニアが、AIに実装を丸投げせず、要件定義・設計・実装・レビュー・保守の流れを学ぶことを目的にしています。

---

## 概要

AIツールを使うと、コード生成や修正は非常に簡単になります。

一方で、開発経験が浅い段階でAIに実装を任せすぎると、次のような状態になりやすいです。

- 動くものはできたが、中身を説明できない
- エラーが出たときに原因を追えない
- 設計や実装方針の判断基準が身につかない
- AIがどこを変更したのか把握できない
- 再起動後にAIが過去の文脈を忘れてしまう

このテンプレートでは、Codexにコード生成をさせるのではなく、以下のような支援を行うAIメンターとして振る舞わせることを目的としています。

- 実装方針の整理
- 要件定義・設計の壁打ち
- 設計書・コードのレビュー
- エラー原因の切り分け支援
- 調査キーワードや学習順序の提示
- 作業ログとコンテキストの整理
- 次回作業の引き継ぎ

---

## このテンプレートでできること

- 長大なプロンプトを `PROMPT.md` に保存し、Codexに読み込ませて運用できます
- `AGENTS.md` にAIエージェント共通の最上位ルールを定義できます
- `/docs/context/` に現在の状況、決定事項、未決事項、学習メモを保存できます
- `/docs/logs/` に作業ログや議事録を保存できます
- `/docs/architecture/` にユーザー作成の設計書を配置できます
- AIに直接編集させる範囲を限定できます
- `【クライアント】` モードで要件定義・ヒアリングの練習ができます
- `終了報告` により学習ログと次回タスクを継続できます

---

## 想定する利用者

- AIを使いながら開発学習したい人
- Codexにコードを丸投げせず、自分で理解して実装したい人
- ジュニアエンジニア・未経験エンジニア
- 要件定義や設計も少しずつ学びたい人
- AIエージェント用の運用ルールを整備したい人
- プロジェクトごとのAI用メモリ・ログ管理を整備したい人

---

## 利用できるプロジェクト例

このテンプレートは、特定の技術スタックやアプリケーションに依存しない汎用テンプレートです。

たとえば、以下のような学習・個人開発プロジェクトに利用できます。

- タスク管理アプリ
- 家計簿アプリ
- 予約管理システム
- 在庫管理システム
- ブログ・CMS
- チャットアプリ
- ポートフォリオサイト
- APIサーバー
- 業務管理システム

プロジェクト固有の内容は、主に以下のファイルを編集して調整してください。

- `PROMPT.md`
- `docs/context/project_overview.md`
- `docs/context/technology_stack.md`
- `docs/context/scope.md`
- `docs/context/current_tasks.md`

---

## 技術スタックについて

このテンプレートは、特定の技術スタックを前提にしていません。

以下のように、あなたの学習目的に合わせて自由に書き換えてください。

| 領域 | 記入例 |
|---|---|
| エディタ | VSCode / JetBrains IDE / Cursor など |
| AIツール | Codex / ChatGPT / Claude Code など |
| フロントエンド | React / Next.js / Vue / Nuxt / Svelte など |
| バックエンド | Spring Boot / Node.js / Rails / Django / Go など |
| DB | MySQL / PostgreSQL / SQLite / MongoDB など |
| 認証 | メールログイン / OAuth / セッション / JWT など |
| インフラ | Docker / AWS / GCP / Vercel / Render など |

初期状態では、`docs/context/technology_stack.md` に記入欄があります。  
利用開始前に、自分のプロジェクトに合わせて編集してください。

---

## ディレクトリ構成

```text
.
├─ PROMPT.md
├─ AGENTS.md
├─ CLAUDE.md
├─ README.md
├─ LICENSE
├─ CONTRIBUTING.md
├─ CODE_OF_CONDUCT.md
├─ SECURITY.md
├─ CHANGELOG.md
├─ .gitignore
├─ .github/
│  ├─ ISSUE_TEMPLATE/
│  │  ├─ bug_report.md
│  │  └─ feature_request.md
│  └─ pull_request_template.md
└─ docs/
   ├─ context/
   │  ├─ current_tasks.md
   │  ├─ project_overview.md
   │  ├─ architecture_summary.md
   │  ├─ technology_stack.md
   │  ├─ scope.md
   │  ├─ decisions.md
   │  ├─ open_questions.md
   │  ├─ glossary.md
   │  └─ learning_notes.md
   ├─ logs/
   │  ├─ AI_mentor_log.md
   │  └─ Agenda_log.md
   └─ architecture/
      ├─ README.md
      └─ .gitkeep
```

---

## 重要ファイル

### `PROMPT.md`

Codexに読み込ませるメインプロンプトです。

長大なプロンプトを毎回チャットに貼り付ける代わりに、Codexへ以下のように指示します。

```text
PROMPT.md を読み込んで、このプロジェクトのAIメンターとして動作してください。
あわせて AGENTS.md、README.md、docs/context/、docs/logs/、docs/architecture/ を確認し、現在の状況を復元してください。
```

### `AGENTS.md`

AIエージェント全体の最上位ルールです。

Codex、Claude Code、ChatGPTなど、複数のAIエージェントで共有する基本方針を定義します。

### `CLAUDE.md`

Claude Code等を使う場合の補助ルールです。  
Codex以外のAIエージェントを使わない場合は必須ではありません。

### `docs/context/`

現在の状況、決定事項、未決事項、学習メモなどを保存します。  
AIが直接編集可能な領域です。

### `docs/logs/`

通常メンターモードの作業ログと、クライアントモードの議事録を保存します。  
AIが直接編集可能な領域です。

### `docs/architecture/`

ユーザーが作成する設計書を保存します。  
AIはこの配下を直接編集せず、レビュー・助言に徹します。

---

## セットアップ

### 1. このテンプレートを取得する

GitHubからcloneします。

```bash
git clone https://github.com/Take-a-valley/codex-ai-mentor-template
cd codex-ai-mentor-template
```

または、このリポジトリをテンプレートとしてコピーして使ってください。

### 2. プロジェクト情報を書き換える

まず、以下のファイルを自分のプロジェクト用に編集してください。

```text
docs/context/project_overview.md
docs/context/technology_stack.md
docs/context/scope.md
docs/context/current_tasks.md
```

必要に応じて、`PROMPT.md` の「作成するもの」「使用技術」「開発プロセス」なども調整してください。

### 3. エディタで開く

VSCodeを使う場合は以下です。

```bash
code .
```

### 4. Codexに初期プロンプトを送る

```text
PROMPT.md を読み込んで、このプロジェクトのAIメンターとして動作してください。
あわせて AGENTS.md、README.md、docs/context/、docs/logs/、docs/architecture/ を確認し、現在の状況を復元してください。
```

Codexが以下を確認できれば開始準備完了です。

- 現在の工程
- プロジェクト概要
- 使用技術
- 未決事項
- 次にやること
- 編集許可範囲
- 禁止事項

---

## 基本的な使い方

### 通常メンターモード

通常時、CodexはAIメンターとして振る舞います。

主な役割は以下です。

- 実装方針の整理
- 設計レビュー
- コードレビュー
- エラー原因の切り分け支援
- 調査キーワードの提示
- 学習順序の提案
- 作業ログの整理

### クライアントモード

ユーザーが文頭に `【クライアント】` と入力した場合、CodexはPMとして振る舞います。

```text
【クライアント】
このような機能を持つWebシステムを作りたいです。
```

このモードでは、Codexは要件定義のためのヒアリング、未決事項整理、議事録作成を行います。

クライアントモードを終了する場合は、以下を入力します。

```text
クライアント終了
```

Codexはクライアントモード中のやり取りを `docs/logs/Agenda_log.md` に保存します。

### 終了報告

作業終了時は以下を入力します。

```text
終了報告
```

Codexは以下を整理し、`docs/logs/AI_mentor_log.md` に追記します。

- 今日の作業内容
- 学習内容
- 決定事項
- 未決事項
- 次回タスク

---

## 編集許可範囲

AIが直接編集してよいファイルは以下です。

```text
docs/context/*.md
docs/logs/*.md
```

以下は原則として直接編集禁止です。

```text
docs/architecture/*.md
ソースコード
設定ファイル
packageや依存関係ファイル
Markdown以外のファイル
```

---

## 設計書の扱い

設計書はユーザーの学習成果物として `/docs/architecture/` に作成します。

AIは以下を行えます。

- 設計書の目的説明
- 構成案の提示
- 記載粒度の説明
- レビュー
- 改善提案
- 不足観点の指摘

ただし、AIは `/docs/architecture/` 配下を直接編集しません。

---

## カスタマイズ例

### プロジェクト概要を変更する

`docs/context/project_overview.md` を編集します。

### 技術スタックを変更する

`docs/context/technology_stack.md` を編集します。

### AIに許可する編集範囲を変更する

`AGENTS.md` と `PROMPT.md` の編集許可範囲を変更します。

### 設計書の種類を変更する

`docs/architecture/README.md` を編集します。

---

## 注意事項

このテンプレートは、AIに実装を丸投げするためのものではありません。

AIを使いながらも、自分で考え、自分で実装できる力を身につけることを目的としています。

また、業務コードや機密情報をAIツールに入力する場合は、所属組織やプロジェクトの利用ルールを必ず確認してください。

---
## ライセンス

このリポジトリは MIT License で公開しています。

---

## 関連記事

Qiita記事:

[未経験エンジニアがCodexを「プログラミング学習メンター」として使う 第2回：PROMPT.mdで運用を安定させる](https://qiita.com/Take-a-valley/items/ed6c524d78d16c0a8bc5)


