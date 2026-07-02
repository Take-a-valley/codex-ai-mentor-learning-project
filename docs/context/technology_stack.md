# Technology Stack

## 使用予定技術

このファイルには、プロジェクトで使用する技術スタックを記録します。

| 領域 | 技術 | 採用理由 | 未決事項 |
|---|---|---|---|
| エディタ | 未定 | 未定 | 未定 |
| AIツール | 未定 | 未定 | 未定 |
| フロントエンド | Next.js 16.x系または作成時点のlatest / App Router / TypeScript / Node.js 24.14.1 / npm 11.11.0 / Tailwind CSS / 必要に応じてshadcn/ui / TanStack Query | クライアント希望。画面中心のWebアプリを構築しやすい。Node.jsとnpmは現PCの環境を極力採用する | なし |
| バックエンド | Spring Boot 4.1.x / Java 25 / Maven 3.9.9 / Spring Data JPA / Flyway | クライアント希望。業務アプリ向けのAPI実装を学びやすい。JavaとMavenは現PCの環境を極力採用する | パッケージ構成、Entity / DTO / Repository / Service / Controllerの詳細 |
| データストア | MySQL 8系 | クライアント希望。リレーショナルな業務データ設計を学びやすい。PC上のMariaDB 10.2.40ではなくMySQL 8系を別途採用する | Docker Compose上の具体設定 |
| 認証・認可 | メールログイン、パスワードリセット、セッション方式、Spring Security、CSRFトークン取得API、ロール別権限 | クライアント希望。システム管理者、プロジェクト管理者、メンバー、閲覧者で管理する | ログイン失敗回数制限は将来拡張 |
| 開発環境 | Docker Compose / MySQL 8系 / Mailpit | 学習目的で使用する。MySQL 8系と開発用メール確認環境を再現しやすい | Composeファイルの具体構成 |
| テスト | Spring Boot Test / JUnit / Vitest + React Testing Library / Playwright | バックエンド、API、画面、E2Eの観点を分けて確認しやすい | 各テストツールを使用する範囲、CIで実行する範囲 |

## 技術選定時に確認すること

- なぜその技術を使うのか
- 学習目的に合っているか
- 実務での採用例があるか
- 初学者にとって難しすぎないか
- 長期運用・保守に向いているか
- 代替技術と比べたメリット・デメリットは何か
- プロジェクトの要件に合っているか

## 未決事項

- パッケージ構成
- Entity / DTO / Repository / Service / Controllerの詳細
- Docker Composeファイルの具体構成
- デプロイ方法
- CI/CD
- 本番向けメール送信方式
