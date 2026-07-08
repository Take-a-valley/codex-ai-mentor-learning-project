# Flyway 解説

## 1. Flywayとは

Flywayは、DBの構造変更をファイルで管理するためのDBマイグレーションツールです。

アプリケーション開発では、テーブルを作成したり、カラムを追加したり、インデックスを追加したりする場面があります。

これらを手作業でMySQLにログインして実行すると、次のような問題が起きやすくなります。

- どのSQLを実行したか分からなくなる
- 他のPCで同じDB構造を再現しづらい
- 開発環境と本番環境でDB構造がズレる
- 変更履歴をGitで管理しづらい
- チーム開発時にメンバーごとのDB状態が揃わない

Flywayを使うと、DB変更用のSQLファイルを決まった場所に置き、アプリケーション起動時などに未実行のSQLだけを順番に実行できます。

## 2. Flywayの役割

FlywayはDB設計を自動で考えてくれるツールではありません。

役割分担は以下のように考えます。

```text
DB設計書
  何のテーブルを作るか、どんなカラムを持つかを決める

マイグレーションSQL
  DB設計をMySQLへ反映するためのSQLを書く

Flyway
  SQLファイルを順番に実行し、実行履歴を管理する
```

つまり、設計は人間が行い、Flywayはその設計をDBへ安全に反映するための管理役です。

## 3. 本プロジェクトでの配置場所

本プロジェクトでは、`application.yml` に以下の設定があります。

```text
spring.flyway.locations: classpath:db/migration
```

Spring Bootでの `classpath:db/migration` は、実ファイル上では以下を指します。

```text
backend/src/main/resources/db/migration/
```

そのため、FlywayのSQLファイルはこのディレクトリ配下に作成します。

## 4. マイグレーションファイルの命名規則

FlywayのSQLファイルには命名規則があります。

基本形は以下です。

```text
Vバージョン番号__説明.sql
```

例:

```text
V1__create_initial_tables.sql
V2__add_task_indexes.sql
V3__insert_initial_admin_user.sql
```

重要な点は、バージョン番号と説明の間がアンダースコア2つであることです。

```text
正しい: V1__create_initial_tables.sql
誤り:   V1_create_initial_tables.sql
```

アンダースコアが1つだと、Flywayがマイグレーションファイルとして正しく認識できない場合があります。

## 5. 初期マイグレーションとは

初期マイグレーションとは、アプリケーションが最初に必要とするDB構造を作るためのマイグレーションです。

本プロジェクトの初期MVPでは、最終的に以下のようなテーブルが必要になります。

- users
- projects
- project_members
- tasks
- task_comments
- task_attachments
- project_invitations
- password_reset_tokens

最初のマイグレーションファイルでは、DB設計書に従って、これらのテーブルを作成します。

## 6. テーブル作成順が重要な理由

DBには外部キー制約があります。

たとえば、タスクはプロジェクトに所属します。

```text
projects
  ↓
tasks
```

この場合、`tasks` テーブルは `projects` テーブルを参照します。

そのため、`tasks` テーブルを先に作ろうとすると、参照先の `projects` テーブルがまだ存在せず、エラーになります。

基本的には、参照される親テーブルから先に作成します。

本プロジェクトでは、概ね以下の順序を意識します。

```text
users
projects
project_members
tasks
task_comments
task_attachments
project_invitations
password_reset_tokens
```

実際の順序は、DB設計書と外部キーの関係に合わせて確認します。

## 7. Flywayの実行タイミング

Spring BootでFlywayを有効にしている場合、アプリケーション起動時にFlywayが動作します。

流れは以下です。

```text
Spring Boot起動
  ↓
application.ymlを読み込む
  ↓
Flywayの設定を確認する
  ↓
db/migration配下のSQLファイルを探す
  ↓
未実行のマイグレーションを実行する
  ↓
実行履歴をDBに記録する
```

すでに実行済みのSQLファイルは、通常は再実行されません。

## 8. flyway_schema_history

Flywayを使うと、DB内にFlyway用の管理テーブルが作成されます。

代表的なテーブル名は以下です。

```text
flyway_schema_history
```

このテーブルには、以下のような情報が記録されます。

- 実行されたマイグレーションのバージョン
- ファイル名
- 実行日時
- 実行結果
- チェックサム

Flywayはこの履歴を見て、どのSQLを実行済みか判断します。

## 9. チェックサムとは

チェックサムとは、ファイル内容から計算される確認用の値です。

Flywayは、マイグレーションファイルを実行したときに、そのファイルのチェックサムを記録します。

その後、実行済みのマイグレーションファイルを書き換えると、DBに記録されているチェックサムと現在のファイル内容が一致しなくなります。

その場合、Flywayはエラーを出します。

これは、すでに実行されたDB変更の履歴が後から書き換えられると危険なためです。

## 10. 実行済みマイグレーションを変更しない理由

実務では、一度共有環境や本番環境で実行されたマイグレーションファイルは基本的に変更しません。

理由は、すでにそのSQLを実行済みの環境が存在するためです。

もしファイルを書き換えると、環境ごとにDB構造がズレる可能性があります。

修正が必要な場合は、既存のファイルを書き換えるのではなく、新しいマイグレーションファイルを追加します。

例:

```text
V1__create_initial_tables.sql
V2__add_deleted_at_to_tasks.sql
```

ただし、学習中でまだ誰にも共有していないローカルDBの場合は、DB volumeを削除して最初からやり直すこともあります。

## 11. よくあるエラー

Flyway初期マイグレーションでは、以下のエラーが起きやすいです。

### 11.1 ファイル名の形式が違う

例:

```text
V1_create_initial_tables.sql
```

このようにアンダースコアが1つだと、Flywayの命名規則に合いません。

正しくは以下です。

```text
V1__create_initial_tables.sql
```

### 11.2 SQL構文エラー

MySQLのSQLとして正しくない場合、起動時にエラーになります。

カンマの抜け、型の誤り、括弧の閉じ忘れなどが原因になりやすいです。

### 11.3 外部キーの参照先テーブルが存在しない

参照する側のテーブルを先に作ると、外部キー制約でエラーになることがあります。

親テーブルを先に作成する必要があります。

### 11.4 同じテーブルがすでに存在する

すでに手動で作成したテーブルがある状態で、Flywayが同じテーブルを作ろうとするとエラーになります。

学習中は、DBを空にしてからFlywayで作成する方が状態を理解しやすいです。

### 11.5 実行済みファイルを書き換えた

Flywayの履歴に記録されたチェックサムと、現在のSQLファイルの内容がズレるとエラーになります。

実行済みマイグレーションは原則変更せず、追加修正は新しいバージョンで行います。

## 12. 本プロジェクトで次に行うこと

次に行う作業は以下です。

1. DB設計書を確認する
2. Flywayマイグレーション設計書を確認する
3. 初期マイグレーションファイル名を決める
4. `backend/src/main/resources/db/migration/` にSQLファイルを作成する
5. DB設計書に従って `CREATE TABLE` 文を書く
6. Spring Bootを起動してFlywayが実行されるか確認する
7. MySQLにテーブルが作成されたか確認する

## 13. 確認すべき設計書

Flyway初期マイグレーション作成時は、以下を確認します。

```text
docs/architecture/タスク管理機能/基本設計/DB設計書.md
docs/architecture/タスク管理機能/詳細設計/Flywayマイグレーション設計書.md
docs/architecture/タスク管理機能/詳細設計/Entity設計書.md
```

特に `DB設計書.md` では、テーブル名、カラム名、型、制約、論理削除方針を確認します。

## 14. 自己学習におすすめの検索ワード

以下の順番で調べると理解しやすいです。

1. `Flyway とは 初心者`
2. `Spring Boot Flyway 使い方`
3. `Flyway migration file naming`
4. `Flyway V1__init.sql 命名規則`
5. `Flyway schema history とは`
6. `Flyway checksum エラー`
7. `Spring Boot Flyway MySQL create table`
8. `MySQL 外部キー 作成順`
9. `MySQL 論理削除 カラム 設計`
10. `CREATE TABLE MySQL 基本`

## 15. 本プロジェクトでの理解ポイント

本プロジェクトでは、Flywayを次の目的で使います。

- DB構造をSQLファイルで管理する
- DB変更履歴をGitで追えるようにする
- 開発環境を再現しやすくする
- 後からテーブル追加や変更を安全に行えるようにする

Flywayは便利ですが、SQL自体を正しく設計する責任は開発者にあります。

そのため、DB設計書、Entity設計書、マイグレーションSQLの整合性を意識することが重要です。
