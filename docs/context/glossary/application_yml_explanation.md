# application.yml全行解説

このファイルは、バックエンドの `application.yml` に記述する内容と、各行の意味を学習用に整理する。

## 対象ファイル

```text
backend/src/main/resources/application.yml
```

## application.ymlとは

`application.yml` は、Spring Bootアプリケーションの実行時設定を書くファイルである。

主に以下を定義する。

- アプリケーション名
- サーバー設定
- セッション設定
- DB接続設定
- JPA設定
- Flyway設定
- メール送信設定
- ログ出力設定

## 今回のapplication.yml記述例

```yaml
spring:
  application:
    name: dev-support-backend

  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3307/dev_support}
    username: ${SPRING_DATASOURCE_USERNAME:dev_support_user}
    password: ${SPRING_DATASOURCE_PASSWORD:dev_support_password}
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration

  mail:
    host: ${SPRING_MAIL_HOST:localhost}
    port: ${SPRING_MAIL_PORT:1025}

server:
  port: ${SERVER_PORT:8080}
  servlet:
    session:
      timeout: 120m

logging:
  level:
    org.hibernate.SQL: debug
```

## 全行解説

```yaml
spring:
```

Spring Boot関連の設定をまとめる親項目。

DB接続、JPA、Flyway、メールなど、多くのSpring設定はこの配下に書く。

```yaml
  application:
```

アプリケーション自体に関する設定を書く場所。

```yaml
    name: dev-support-backend
```

Spring Bootアプリケーション名。

ログや管理情報でアプリケーションを識別しやすくする。

```yaml
  datasource:
```

DB接続設定を書く場所。

Spring Bootはこの設定を使ってMySQLへ接続する。

```yaml
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3307/dev_support}
```

DB接続URL。

`${SPRING_DATASOURCE_URL:...}` は、環境変数 `SPRING_DATASOURCE_URL` があればその値を使い、なければ `:` の後ろの値を使うという意味である。

今回の初期開発では、Docker上のMySQLをPC側の `3307` 番ポートで公開しているため、デフォルト値を `jdbc:mysql://localhost:3307/dev_support` としている。

```yaml
    username: ${SPRING_DATASOURCE_USERNAME:dev_support_user}
```

DB接続ユーザー名。

環境変数があればそれを使い、なければ `dev_support_user` を使う。

```yaml
    password: ${SPRING_DATASOURCE_PASSWORD:dev_support_password}
```

DB接続パスワード。

環境変数があればそれを使い、なければローカル開発用の値を使う。

本番環境では、設定ファイルに直接パスワードを書かず、環境変数などで管理する。

```yaml
    driver-class-name: com.mysql.cj.jdbc.Driver
```

MySQLへ接続するためのJDBCドライバクラス。

Spring Bootが自動判定できることも多いが、学習上分かりやすくするため明示している。

```yaml
  jpa:
```

JPA関連の設定を書く場所。

EntityとDBテーブルの対応、SQL出力などに関係する。

```yaml
    open-in-view: false
```

Controllerや画面表示の段階までDB接続を開き続ける仕組みを無効化する。

業務処理とDBアクセスはService層のトランザクション内で完結させる方針に合う。

```yaml
    hibernate:
```

JPA実装であるHibernateに関する設定を書く場所。

```yaml
      ddl-auto: validate
```

EntityとDBテーブル定義が一致しているか検証する設定。

テーブル作成はHibernateに任せず、Flywayで管理する。

```yaml
    properties:
```

Hibernateへ渡す追加設定を書く場所。

```yaml
      hibernate:
```

Hibernate固有の追加設定をまとめる場所。

```yaml
        format_sql: true
```

ログに出力されるSQLを読みやすく整形する。

```yaml
  flyway:
```

Flyway関連の設定を書く場所。

```yaml
    enabled: true
```

Spring Boot起動時にFlywayを有効にする。

```yaml
    locations: classpath:db/migration
```

FlywayのマイグレーションSQLファイルを探す場所。

`backend/src/main/resources/db/migration/` 配下に `V1__xxx.sql` のようなファイルを置く。

```yaml
  mail:
```

メール送信設定を書く場所。

```yaml
    host: ${SPRING_MAIL_HOST:localhost}
```

SMTPサーバーのホスト名。

初期開発ではDocker上のMailpitをPC側から `localhost` で参照する。

```yaml
    port: ${SPRING_MAIL_PORT:1025}
```

SMTPサーバーのポート番号。

MailpitのSMTPポートは初期設定で `1025` としている。

```yaml
server:
```

Spring Boot内蔵サーバーに関する設定を書く場所。

```yaml
  port: ${SERVER_PORT:8080}
```

Spring Bootアプリケーションを起動するポート。

環境変数 `SERVER_PORT` があればその値を使い、なければ `8080` で起動する。

```yaml
  servlet:
```

ServletベースのWebアプリケーション設定を書く場所。

```yaml
    session:
```

セッション関連の設定を書く場所。

```yaml
      timeout: 120m
```

セッション有効期限。

本プロジェクトでは、最終アクセスから120分を採用している。

```yaml
logging:
```

ログ出力設定を書く場所。

```yaml
  level:
```

ログレベルを設定する場所。

```yaml
    org.hibernate.SQL: debug
```

Hibernateが実行するSQLをログに出す設定。

開発中にDBアクセス内容を確認しやすくするために使用する。

## 注意点

`.env` はDocker Composeでは自動的に読み込めるが、Spring Bootをローカルで直接起動する場合、`.env` が自動で読み込まれるとは限らない。

そのため、今回の `application.yml` では環境変数が未設定でも動くようにデフォルト値を指定している。

将来的に本番環境やDocker化したbackendで動かす場合は、パスワードなどの秘密情報を環境変数で渡す方針にする。

## この設定で可能になること

- Spring BootがMySQLへ接続できる
- Spring Data JPAがMySQLを利用できる
- Flywayがマイグレーションファイルを読み込める
- Mailpitへメール送信できる
- セッション有効期限を120分にできる
- HibernateのSQLログを確認できる

