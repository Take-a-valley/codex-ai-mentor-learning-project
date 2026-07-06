# Docker Compose設定解説

このファイルは、初期MVPの開発環境で使用する `docker-compose.yml` の記述内容と、各行の意味を学習用に整理する。

## 対象ファイル

```text
infra/docker/docker-compose.yml
```

## 今回の目的

初期MVPでは、Docker Composeで以下を起動する。

- MySQL 8系
- Mailpit

Next.jsフロントエンドとSpring Bootバックエンドは、初期段階ではローカルPC上で起動する。

## docker-compose.yml記述例

```yaml
services:
  mysql:
    image: mysql:8
    container_name: dev-support-mysql
    restart: unless-stopped
    environment:
      MYSQL_DATABASE: ${MYSQL_DATABASE}
      MYSQL_USER: ${MYSQL_USER}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      TZ: Asia/Tokyo
    ports:
      - "${MYSQL_PORT:-3306}:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    command:
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_0900_ai_ci
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-p${MYSQL_ROOT_PASSWORD}"]
      interval: 10s
      timeout: 5s
      retries: 10
      start_period: 30s
    networks:
      - dev_support_network

  mailpit:
    image: axllent/mailpit:latest
    container_name: dev-support-mailpit
    restart: unless-stopped
    ports:
      - "${MAILPIT_SMTP_PORT:-1025}:1025"
      - "${MAILPIT_WEB_PORT:-8025}:8025"
    networks:
      - dev_support_network

volumes:
  mysql_data:
    driver: local

networks:
  dev_support_network:
    driver: bridge
```

## 全体像

このファイルは、Docker Composeに対して以下を指示する設定ファイルである。

- MySQLコンテナを起動する
- Mailpitコンテナを起動する
- MySQLのデータを永続化する
- PCからMySQLやMailpitへアクセスできるようにする
- MySQLとMailpitを同じDockerネットワークに参加させる

## 全行解説

```yaml
services:
```

Docker Composeで起動するサービス一覧を書く場所。

サービスとは、起動するコンテナの定義である。

```yaml
  mysql:
```

`mysql` というサービスを定義する。

この名前はDocker Compose内でのサービス名になる。

```yaml
    image: mysql:8
```

MySQL 8系のDockerイメージを使う指定。

Dockerイメージは、コンテナを作るためのテンプレートである。

```yaml
    container_name: dev-support-mysql
```

作成されるコンテナ名を `dev-support-mysql` にする。

指定しなくても動作するが、コンテナ名が分かりやすくなる。

```yaml
    restart: unless-stopped
```

コンテナが止まったときの再起動方針。

`unless-stopped` は、自分で停止した場合を除き、自動で再起動するという意味である。

```yaml
    environment:
```

コンテナに渡す環境変数を書く場所。

MySQLコンテナは、この環境変数を見て初期DBやユーザーを作成する。

```yaml
      MYSQL_DATABASE: ${MYSQL_DATABASE}
```

作成するDB名。

`${MYSQL_DATABASE}` は `.env` に書いた値を読み込むという意味である。

```yaml
      MYSQL_USER: ${MYSQL_USER}
```

アプリケーション接続用のDBユーザー名。

```yaml
      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
```

アプリケーション接続用DBユーザーのパスワード。

```yaml
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
```

MySQLの管理者ユーザーである `root` のパスワード。

```yaml
      TZ: Asia/Tokyo
```

コンテナのタイムゾーンを日本時間にする。

```yaml
    ports:
```

PC側のポートとコンテナ側のポートをつなぐ設定。

```yaml
      - "${MYSQL_PORT:-3306}:3306"
```

左側がPC側、右側がコンテナ側を表す。

`.env` に以下のように書かれている場合、

```env
MYSQL_PORT=3306
```

実質的には以下と同じ意味になる。

```yaml
      - "3306:3306"
```

PCから `localhost:3306` に接続すると、MySQLコンテナの `3306` 番へ接続される。

`:-3306` は、`MYSQL_PORT` が未設定の場合に `3306` を使うという意味である。

```yaml
    volumes:
```

コンテナのデータ保存先を指定する。

```yaml
      - mysql_data:/var/lib/mysql
```

`mysql_data` というDocker volumeを、コンテナ内の `/var/lib/mysql` に接続する。

MySQLの実データは `/var/lib/mysql` に保存されるため、この設定によりDBデータを永続化できる。

```yaml
    command:
```

MySQL起動時に追加で渡す設定。

```yaml
      - --character-set-server=utf8mb4
```

MySQLの文字コードを `utf8mb4` にする。

日本語や絵文字も扱いやすい文字コードである。

```yaml
      - --collation-server=utf8mb4_0900_ai_ci
```

文字列の比較や並び順のルール。

MySQL 8系で使える一般的な照合順序である。

```yaml
    healthcheck:
```

コンテナが正常に動いているか確認する設定。

コンテナが起動しただけでなく、MySQLとして応答できるかを確認する。

```yaml
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-p${MYSQL_ROOT_PASSWORD}"]
```

MySQLに対して `mysqladmin ping` を実行し、応答があるか確認する。

```yaml
      interval: 10s
```

10秒ごとに確認する。

```yaml
      timeout: 5s
```

5秒以内に応答がなければ失敗扱いにする。

```yaml
      retries: 10
```

最大10回まで再試行する。

```yaml
      start_period: 30s
```

起動直後30秒間は猶予期間にする。

MySQLは起動に少し時間がかかるためである。

```yaml
    networks:
```

このサービスが参加するDockerネットワークを指定する。

```yaml
      - dev_support_network
```

`dev_support_network` に参加させる。

```yaml
  mailpit:
```

`mailpit` というサービスを定義する。

Mailpitは開発中のメール確認ツールである。

```yaml
    image: axllent/mailpit:latest
```

MailpitのDockerイメージを使う。

`latest` は最新版という意味である。

```yaml
    container_name: dev-support-mailpit
```

作成されるコンテナ名を `dev-support-mailpit` にする。

```yaml
    restart: unless-stopped
```

Mailpitコンテナも、自分で停止した場合を除き自動再起動する。

```yaml
    ports:
```

MailpitのポートをPC側に公開する。

```yaml
      - "${MAILPIT_SMTP_PORT:-1025}:1025"
```

SMTP用のポート。

Spring Bootはこのポートへメールを送信する。

```yaml
      - "${MAILPIT_WEB_PORT:-8025}:8025"
```

MailpitのWeb画面用ポート。

ブラウザで `http://localhost:8025` を開くと、送信されたメールを確認できる。

```yaml
    networks:
      - dev_support_network
```

MailpitもMySQLと同じネットワークに参加させる。

```yaml
volumes:
```

Compose全体で使うvolumeを定義する。

```yaml
  mysql_data:
```

`mysql_data` というvolumeを作る。

```yaml
    driver: local
```

Dockerのローカル環境にvolumeを作る指定。

省略しても通常は `local` だが、学習中は明示しておくと分かりやすい。

```yaml
networks:
```

Compose全体で使うネットワークを定義する。

```yaml
  dev_support_network:
```

`dev_support_network` というネットワークを作る。

```yaml
    driver: bridge
```

Dockerの標準的なローカルネットワーク方式。

通常の開発環境ではこの指定でよい。

## 起動コマンド

リポジトリルートで実行する。

```bash
docker compose --env-file .env -f infra/docker/docker-compose.yml up -d
```

各要素の意味は以下の通り。

| 要素 | 意味 |
|---|---|
| `docker` | Dockerを操作する |
| `compose` | Docker Composeを使う |
| `--env-file .env` | ルートの `.env` を読み込む |
| `-f infra/docker/docker-compose.yml` | 使用するComposeファイルを指定する |
| `up` | サービスを起動する |
| `-d` | バックグラウンドで起動する |

## 状態確認

```bash
docker compose --env-file .env -f infra/docker/docker-compose.yml ps
```

起動中のサービス状態を確認する。

## ログ確認

```bash
docker compose --env-file .env -f infra/docker/docker-compose.yml logs
```

MySQLとMailpitのログを確認する。

## 停止

```bash
docker compose --env-file .env -f infra/docker/docker-compose.yml down
```

コンテナを停止し、Composeで作成したコンテナとネットワークを削除する。

通常停止ではvolumeは削除されないため、MySQLデータは残る。

## DBデータも含めて初期化する場合

```bash
docker compose --env-file .env -f infra/docker/docker-compose.yml down -v
```

`down -v` は `mysql_data` も削除するため、DBデータが消える。

普段は使わず、DBを作り直したいときだけ使う。

## 学習上の理解ポイント

まずは以下を押さえる。

| 項目 | 意味 |
|---|---|
| `services` | 起動するコンテナ一覧 |
| `image` | コンテナの元になるテンプレート |
| `ports` | PCからコンテナへ接続する入口 |
| `volumes` | コンテナを消しても残したいデータ置き場 |
| `environment` | コンテナへ渡す設定値 |

