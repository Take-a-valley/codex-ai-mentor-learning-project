# Docker Compose設計書

## 1. 本書の目的

本書は、タスク管理機能の初期MVPにおけるDocker Composeを利用した開発環境設計を定義することを目的とする。

本書では、開発環境で起動するMySQL 8系、Mailpit、ネットワーク、ボリューム、環境変数、ポート、データ永続化、起動順序を整理する。

## 2. 対象範囲

対象に含めるものは以下とする。

- Docker Composeの利用方針
- 起動対象サービス
- MySQLコンテナ設計
- Mailpitコンテナ設計
- ネットワーク方針
- ボリューム方針
- 環境変数方針
- ポート方針
- Spring Bootとの接続方針
- Flywayとの関係
- 開発環境の起動・停止方針

対象外とするものは以下とする。

- 本番環境のDocker設計
- 本番DB運用設計
- 本番メール送信基盤
- フロントエンドコンテナ化
- バックエンドコンテナ化
- CI/CD上のDocker実行設計
- Kubernetesなどの本番オーケストレーション設計

## 3. Docker Compose利用方針

初期MVPでは、学習目的と開発環境の再現性を高めるためにDocker Composeを使用する。

Docker Composeでは、開発に必要なミドルウェアを起動する。

初期段階では、フロントエンドとバックエンドはローカルPC上で起動する。

Docker Composeで起動する対象は以下とする。

- MySQL 8系
- Mailpit

将来的に必要になった場合、フロントエンドとバックエンドのDocker化も検討する。

本リポジトリは将来的にポートフォリオとして公開することを想定する。
そのため、初期実装では学習容易性を優先してMySQLとMailpitのみをDocker Composeで管理し、アプリケーション実装が安定した段階でフロントエンドとバックエンドもDocker Compose管理に追加する。

## 4. 起動サービス一覧

| サービス | 用途 |
|---|---|
| mysql | アプリケーションDB |
| mailpit | 開発用メール確認 |

## 5. 全体構成

開発環境では、以下の構成とする。

| 対象 | 起動方法 |
|---|---|
| フロントエンド | ローカルPCで起動 |
| バックエンド | ローカルPCで起動 |
| MySQL | Docker Composeで起動 |
| Mailpit | Docker Composeで起動 |

バックエンドは、Docker Composeで起動したMySQLへ接続する。

バックエンドは、Docker Composeで起動したMailpitへSMTP接続する。

## 6. MySQLサービス設計

MySQLは、アプリケーションの業務データを保存するために使用する。

| 項目 | 方針 |
|---|---|
| サービス名 | mysql |
| イメージ | mysql:8 |
| 用途 | アプリケーションDB |
| ポート | 3306 |
| データ永続化 | volumeを使用 |
| 文字コード | utf8mb4 |
| タイムゾーン | Asia/Tokyoを基本とする |

MySQLは8系を採用する。

初期MVPでは `mysql:8` を基本とし、実装時点で必要があれば8系の具体的なタグに固定する。

MySQLには以下のデータを保存する。

- ユーザー
- システムロール
- プロジェクト
- プロジェクトメンバー
- プロジェクトロール
- タスク
- タスクStatus
- タスクPriority
- コメント
- 添付ファイル
- 招待情報
- パスワードリセット情報

## 7. MySQL環境変数方針

MySQLコンテナでは、DB名、ユーザー名、パスワードを環境変数で設定する。

環境変数の具体値は、`.env` などで管理する。

設計書には本番パスワードや秘密情報を記載しない。

| 環境変数 | 用途 |
|---|---|
| MYSQL_DATABASE | アプリケーション用DB名 |
| MYSQL_USER | アプリケーション接続用ユーザー |
| MYSQL_PASSWORD | アプリケーション接続用パスワード |
| MYSQL_ROOT_PASSWORD | rootユーザー用パスワード |
| TZ | タイムゾーン |

## 8. MySQLボリューム方針

MySQLのデータはDocker volumeで永続化する。

コンテナを停止しても、DBデータが消えないようにする。

DBを初期化したい場合は、対象volumeを削除して再作成する。

| ボリューム | 用途 |
|---|---|
| mysql_data | MySQLデータ永続化 |

## 9. MySQLポート方針

ローカルPC上のバックエンドから接続できるように、MySQLのポートをホストへ公開する。

| コンテナ側 | ホスト側 | 用途 |
|---|---|---|
| 3306 | 3306 | MySQL接続 |

すでにローカルPCで3306番ポートを使用している場合は、ホスト側ポートを変更する。

## 10. Mailpitサービス設計

Mailpitは、開発環境で送信されたメールをブラウザ上で確認するために使用する。

実際の外部メール送信は行わない。

| 項目 | 方針 |
|---|---|
| サービス名 | mailpit |
| イメージ | axllent/mailpit |
| 用途 | 開発用メール確認 |
| SMTPポート | 1025 |
| Web UIポート | 8025 |

Mailpitは開発用メール確認ツールとして使用する。

Docker ComposeではMailpitのDockerイメージを使用し、SMTP受信とWeb UI確認を行う。

バックエンドはSMTPサーバーとしてMailpitへ接続する。

開発者はMailpitのWeb UIでメール内容を確認する。

## 11. Mailpitポート方針

| コンテナ側 | ホスト側 | 用途 |
|---|---|---|
| 1025 | 1025 | SMTP受信用 |
| 8025 | 8025 | Web UI確認用 |

ブラウザでは以下のURLでMailpitを確認する想定とする。

```text
http://localhost:8025
```

## 12. ネットワーク方針

Docker Compose内のサービスは、同一ネットワーク上で通信できるようにする。

MySQLとMailpitは、Docker Composeのデフォルトネットワークまたは専用ネットワークに参加する。

初期MVPでは、専用ネットワーク名を定義して管理しやすくする方針とする。

| ネットワーク | 用途 |
|---|---|
| app_network | MySQL、Mailpit用ネットワーク |

## 13. バックエンド接続方針

バックエンドはローカルPC上で起動する。

そのため、バックエンドからMySQLへはホスト公開ポートを通じて接続する。

| 接続先 | 方針 |
|---|---|
| DB host | localhost |
| DB port | MYSQL_PORTの値。未指定時は3306 |
| DB name | 環境変数で管理 |
| DB user | 環境変数で管理 |
| DB password | 環境変数で管理 |

バックエンドからMailpitへは以下で接続する。

| 接続先 | 方針 |
|---|---|
| SMTP host | localhost |
| SMTP port | MAILPIT_SMTP_PORTの値。未指定時は1025 |

## 14. Spring Boot設定との関係

Spring Bootでは、MySQLとMailpitへの接続情報を設定する。

接続情報は、環境変数または開発用設定ファイルで管理する。

設計上は、以下の情報が必要になる。

| 分類 | 項目 |
|---|---|
| DB | URL |
| DB | ユーザー名 |
| DB | パスワード |
| DB | ドライバ |
| Mail | SMTPホスト |
| Mail | SMTPポート |
| Flyway | 有効化 |
| Flyway | マイグレーション配置場所 |

秘密情報はGit管理対象に含めない。

## 15. Flywayとの関係

Docker ComposeはMySQLを起動する。

FlywayはSpring Bootアプリケーション起動時にMySQLへ接続し、マイグレーションを適用する。

初期MVPでは、Flyway専用コンテナは用意せず、Spring Boot側からFlywayを実行する方針とする。

テーブル定義と固定マスタ投入は、Flywayマイグレーションで管理する。

開発用初期管理者データは、共通マイグレーションではなく開発環境専用の方法で管理する。

## 16. docker-compose.yml方針

`docker-compose.yml` には、MySQLとMailpitを定義する。

設計上、以下の要素を含める。

- services
- mysql
- mailpit
- volumes
- networks
- environment
- ports

具体的なYAMLは実装工程で作成する。

本設計書では、構成方針を定義する。

## 17. .env方針

Docker Composeで使用する環境変数は `.env` で管理する方針とする。

`.env` にはパスワードなどの秘密情報を含む可能性があるため、Git管理対象に含めない。

`.env.example` を用意し、必要な環境変数名のみを共有する。

| ファイル | 用途 | Git管理 |
|---|---|---|
| .env | 実際の環境変数 | 含めない |
| .env.example | 環境変数のサンプル | 含める |

## 18. 環境変数一覧

初期MVPで想定する環境変数は以下とする。

| 環境変数 | 用途 |
|---|---|
| MYSQL_DATABASE | アプリケーションDB名 |
| MYSQL_USER | アプリケーション接続用DBユーザー |
| MYSQL_PASSWORD | アプリケーション接続用DBパスワード |
| MYSQL_ROOT_PASSWORD | MySQL rootパスワード |
| MYSQL_PORT | ホスト側MySQLポート |
| MAILPIT_SMTP_PORT | Mailpit SMTPポート |
| MAILPIT_WEB_PORT | Mailpit Web UIポート |
| TZ | タイムゾーン |

## 19. ポート一覧

| サービス | ホスト側ポート | コンテナ側ポート | 用途 |
|---|---|---|---|
| mysql | MYSQL_PORTの値。未指定時は3306 | 3306 | MySQL接続 |
| mailpit | MAILPIT_SMTP_PORTの値。未指定時は1025 | 1025 | SMTP |
| mailpit | MAILPIT_WEB_PORTの値。未指定時は8025 | 8025 | Web UI |

ホスト側ポートは、ローカルPCの利用状況に応じて変更できる。

ホスト側ポートを変更した場合は、Spring Boot側の接続設定も同じ値に合わせる。

## 20. データ初期化方針

DBを初期化したい場合は、MySQLのvolumeを削除する。

volume削除後にDocker Composeを再起動し、Spring Bootを起動すると、Flywayによりテーブル定義と固定マスタが再作成される。

開発用初期管理者データを投入する場合は、Spring Bootをdev profileで起動し、db/dev-migrationを追加で読み込む。

## 21. 起動順序方針

開発環境の起動順序は以下を基本とする。

1. Docker ComposeでMySQLとMailpitを起動する
2. MySQLの起動完了を確認する
3. Spring Bootバックエンドを起動する
4. Spring Boot起動時にFlywayマイグレーションを適用する
5. Next.jsフロントエンドを起動する
6. Mailpit Web UIでメール確認ができることを確認する

MySQLの起動確認は、コンテナログ、Docker Composeの状態確認、またはSpring Bootからの接続確認で行う。

実装時に必要であれば、MySQLコンテナにhealthcheckを設定する。

## 22. 停止方針

開発環境の停止では、Docker ComposeでMySQLとMailpitを停止する。

通常停止ではvolumeを削除しない。

DBを初期化したい場合のみvolumeを削除する。

| 操作 | 方針 |
|---|---|
| 通常停止 | コンテナ停止、volume保持 |
| 初期化 | コンテナ停止、volume削除 |

## 23. セキュリティ方針

開発環境であっても、パスワードなどの秘密情報はGit管理しない。

`.env` はGit管理対象外とする。

`.env.example` には実際のパスワードを記載しない。

本番環境の認証情報は、開発環境の `.env` とは分けて管理する。

Mailpitは開発用であり、本番環境では使用しない。

## 24. ログ確認方針

開発中は、Docker Composeで起動したサービスのログを確認できるようにする。

確認対象は以下とする。

| サービス | 確認内容 |
|---|---|
| mysql | 起動状況、接続エラー |
| mailpit | メール受信状況 |

Spring BootとNext.jsのログは、それぞれローカル起動したプロセスのログで確認する。

## 25. トラブルシューティング方針

想定されるトラブルと確認観点は以下とする。

| 事象 | 確認観点 |
|---|---|
| MySQLに接続できない | コンテナ起動状況、ポート、DB名、ユーザー、パスワード |
| Flywayが失敗する | MySQL起動完了、SQLエラー、適用済みバージョン |
| Mailpitにメールが届かない | SMTPホスト、SMTPポート、バックエンドのメール設定 |
| Mailpit Web UIが開けない | 8025ポート、コンテナ起動状況 |
| ポート競合 | ホスト側ポートを変更する |
| DBを初期化できない | volume削除の有無 |

## 26. 将来拡張方針

初期MVPではMySQLとMailpitのみDocker Composeで起動する。

将来的に必要になった場合、以下をDocker Compose管理に追加することを検討する。

- Spring Bootバックエンド
- Next.jsフロントエンド
- テスト用DB
- E2Eテスト実行環境
- 本番に近いメール送信検証環境

ポートフォリオ公開時には、第三者がローカル環境に過度に依存せず動作確認できるように、以下の構成を目指す。

- frontend: Next.jsアプリケーション
- backend: Spring Bootアプリケーション
- mysql: MySQL 8系
- mailpit: 開発用メール確認

この段階では、`frontend/Dockerfile` および `backend/Dockerfile` を追加し、Docker Composeから各アプリケーションコンテナを起動できるようにする。

ただし、初期MVPの実装中はDocker設定の複雑化を避けるため、フロントエンドとバックエンドはローカルPC上で起動する。

## 27. 決定事項

- Docker Composeを開発環境で使用する
- Docker ComposeではMySQL 8系とMailpitを起動する
- フロントエンドとバックエンドは初期段階ではローカルPC上で起動する
- MySQLデータはvolumeで永続化する
- Mailpitは開発用メール確認に使用する
- MySQLのホスト側ポートは3306を基本とする
- MailpitのSMTPポートは1025を基本とする
- MailpitのWeb UIポートは8025を基本とする
- `.env` はGit管理対象外とする
- `.env.example` はGit管理対象に含める
- FlywayはSpring Boot起動時に実行する
- 初期実装では、フロントエンドとバックエンドはDocker Composeに含めない
- ポートフォリオ公開を見据え、実装安定後にフロントエンドとバックエンドをDocker Compose管理へ追加する
- 将来的には、Docker Composeでfrontend、backend、mysql、mailpitをまとめて起動できる構成を目指す

## 28. 未決事項

以下は後続工程で具体化する。

- docker-compose.ymlの正式なファイル内容
- .env.exampleの正式な内容
- MySQLのホスト側ポートを3306以外に変更するか
- 開発用初期管理者データの投入方法
- Spring BootとNext.jsをDocker Composeに含める時期
- ポートフォリオ公開用の `frontend/Dockerfile` の内容
- ポートフォリオ公開用の `backend/Dockerfile` の内容
- Docker Composeでアプリケーション全体を起動する場合の起動順序
- READMEに記載するDocker起動手順
- テスト用DBを別コンテナで用意するか