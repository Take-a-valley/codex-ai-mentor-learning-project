# pom.xml全行解説

このファイルは、バックエンドの `pom.xml` に記述する内容と、各行の意味を学習用に整理する。

## 対象ファイル

```text
backend/pom.xml
```

## pom.xmlとは

`pom.xml` は、MavenでJavaプロジェクトを管理するための設定ファイルである。

主に以下を定義する。

- プロジェクト名
- Javaバージョン
- Spring Bootバージョン
- 使用するライブラリ
- テスト用ライブラリ
- ビルド方法

## 今回のpom.xml記述例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.1.0</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>dev-support-backend</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dev-support-backend</name>
    <description>Backend application for the development support system.</description>

    <properties>
        <java.version>25</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-mysql</artifactId>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## 全行解説

```xml
<?xml version="1.0" encoding="UTF-8"?>
```

このファイルがXML形式であり、文字コードがUTF-8であることを示す。

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
```

Mavenプロジェクト定義の開始行。

`xmlns` は、このXMLがMaven POMのルールに従うことを示す。

```xml
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
```

XML Schemaを使って、このXML構造を検証できるようにするための名前空間定義。

```xml
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
```

Maven POMの構造定義ファイルの場所を示す。

```xml
    <modelVersion>4.0.0</modelVersion>
```

Maven POMのモデルバージョン。

通常は `4.0.0` を使用する。

```xml
    <parent>
```

親POMの定義開始。

Spring Bootの標準設定をまとめて利用するために使う。

```xml
        <groupId>org.springframework.boot</groupId>
```

親POMのグループID。

Spring Boot公式の成果物であることを示す。

```xml
        <artifactId>spring-boot-starter-parent</artifactId>
```

Spring Boot用の親POMを指定する。

依存関係のバージョン管理やMavenプラグイン設定をSpring Boot標準に寄せられる。

```xml
        <version>4.1.0</version>
```

使用するSpring Bootのバージョン。

本プロジェクトでは設計方針としてSpring Boot 4.1.x系を採用候補としているため、ここでは `4.1.0` とする。

実際の実装時にMavenで解決できない場合は、使用可能な4.1.x系のパッチバージョンへ調整する。

```xml
        <relativePath/>
```

ローカルの親POMを探さず、Mavenリポジトリから親POMを取得する指定。

Spring Bootプロジェクトではよく使われる。

```xml
    </parent>
```

親POM定義の終了。

```xml
    <groupId>com.example</groupId>
```

このプロジェクトのグループID。

Javaパッケージ構成の `com.example.devsupport` と対応しやすい。

```xml
    <artifactId>dev-support-backend</artifactId>
```

このプロジェクトの成果物名。

ビルド時に作成されるjar名にも関係する。

```xml
    <version>0.0.1-SNAPSHOT</version>
```

アプリケーションのバージョン。

`SNAPSHOT` は開発中バージョンを意味する。

```xml
    <name>dev-support-backend</name>
```

プロジェクトの表示名。

```xml
    <description>Backend application for the development support system.</description>
```

プロジェクトの説明。

```xml
    <properties>
```

プロジェクト内で使う共通設定値の定義開始。

```xml
        <java.version>25</java.version>
```

使用するJavaバージョン。

本プロジェクトではPCにインストール済みのJava 25を採用候補としている。

```xml
    </properties>
```

共通設定値の定義終了。

```xml
    <dependencies>
```

使用するライブラリの定義開始。

```xml
        <dependency>
```

1つの依存関係の定義開始。

```xml
            <groupId>org.springframework.boot</groupId>
```

依存関係の提供元グループ。

```xml
            <artifactId>spring-boot-starter-web</artifactId>
```

Spring MVCによるWeb API開発に必要な依存関係。

REST APIのControllerを作るために使用する。

```xml
        </dependency>
```

依存関係の定義終了。

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
```

入力チェックに使う依存関係。

DTOの項目に `NotBlank`、`Size`、`Email` などのバリデーションを付けるために使用する。

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
```

Spring Data JPAを使うための依存関係。

Entity、Repository、トランザクション、DBアクセスに使用する。

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```

Spring Securityを使うための依存関係。

ログイン、ログアウト、セッション、CSRF、認可制御に使用する。

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
```

メール送信に使う依存関係。

パスワードリセットメールやプロジェクト招待メールをMailpitへ送信するために使用する。

```xml
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
```

Flyway本体の依存関係。

DBマイグレーションを管理するために使用する。

```xml
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-mysql</artifactId>
        </dependency>
```

FlywayでMySQLを扱うための依存関係。

MySQL 8系に対してマイグレーションを実行するために使用する。

```xml
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
```

JavaアプリケーションからMySQLへ接続するためのJDBCドライバ。

`scope` を `runtime` にすることで、コンパイル時ではなく実行時に必要な依存関係として扱う。

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
```

Spring Bootのテストに必要な依存関係。

JUnitやSpring Boot Testなどが含まれる。

`scope` が `test` のため、テスト時のみ使用される。

```xml
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
```

Spring Securityのテストに使う依存関係。

認証済みユーザーを模擬したテストや、権限テストで使用する。

```xml
    </dependencies>
```

依存関係定義の終了。

```xml
    <build>
```

ビルド設定の開始。

```xml
        <plugins>
```

Mavenプラグイン定義の開始。

```xml
            <plugin>
```

1つのMavenプラグイン定義の開始。

```xml
                <groupId>org.springframework.boot</groupId>
```

Spring Bootが提供するMavenプラグインであることを示す。

```xml
                <artifactId>spring-boot-maven-plugin</artifactId>
```

Spring Bootアプリケーションを実行可能jarとしてビルドしたり、`mvn spring-boot:run` で起動したりするためのプラグイン。

```xml
            </plugin>
```

Mavenプラグイン定義の終了。

```xml
        </plugins>
```

Mavenプラグイン一覧の終了。

```xml
    </build>
```

ビルド設定の終了。

```xml
</project>
```

Mavenプロジェクト定義の終了。

## このpom.xmlで可能になること

- Spring Bootアプリケーションとして起動できる
- REST APIを作成できる
- DTOの入力チェックを実装できる
- Spring Data JPAでDBアクセスできる
- Spring Securityで認証・認可を実装できる
- Mailpitへメール送信できる
- FlywayでDBマイグレーションを管理できる
- MySQLへ接続できる
- テストコードを書ける

