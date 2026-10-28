# Current Tasks

## 2026-10-29 終了時点（最新）

- UT-USER-001〜004は実装・レビュー済み、4件成功はユーザー報告。UT-MASTER-001も実装・レビュー済み、1件成功はユーザー報告。AIの再実行と、最後のコメント追加後の実行は未確認。
- UT-MASTER-002は未作成。次回は3つのRepositoryが空リストを返す設定、レスポンス本体と各リストの非null・空確認、検索各1回の検証を記述する。
- UT-MASTER-001のコメントは「List.ofは指定した引数順の変更不可リストを作成」「when/thenReturnは準備したリストを返す設定」として整理する。TaskStatus・TaskPriorityのスタブ説明を「ID順でリスト化」としない。今回ソースは変更していない。
- 002のレビュー・実行後、mvn -f backend/pom.xml testで全6ケースを確認する。実行結果・日時・対象状態・証跡を単体テスト仕様書へ反映する必要がある。現時点で全6件成功とは扱わない。
- 現在ブランチはfeature/user-service-tests、HEADはc9fe57b。UserServiceTestと文書に未コミット変更、MasterDataServiceTestは新規未追跡。コミット・push・マージはユーザー指示時のみ。
- 機能単位の開発方針は合意済み。Serviceテスト完了後、実装計画の整合を確認し、認証・フロント基盤からログイン等を画面まで接続する。

## 2026-10-29 開発方針・直近タスク（以下の過去記録より優先）

- 今後は共通基盤を整えた後、操作を完結できる機能単位でバックエンド・画面・必要なテストを進める。各タスクにAPI契約・画面・権限・テスト・完了条件を含め、実装・レビュー直後の単体テスト、接続後の結合テストを継続する。
- ユーザー報告によりUT-USER-001〜004の4件成功、UT-MASTER-001の1件成功を確認。AIによる再実行は未実施。過去の「UserServiceTest作成途中」は現在の状態ではない。
- 直近はUT-MASTER-002の作成・レビュー・実行。その後、Service単体テスト6件をまとめて実行し、結果を記録する。UT-MASTER-002とまとめ実行の成功は未確認。
- 次はフロントエンド共通基盤と認証・通信設計を確認し、ログイン・本人情報取得・ログアウトを画面まで接続する。その後プロジェクト一覧・選択等を操作単位で進める。着手時に既存設計と照合し具体化する。
- 実装計画書等には旧来の層別順序が残るため整合更新が必要。今回の依頼ではログ・コンテキストのみ更新し、設計書本文は変更していない。

## 2026-10-28 終了時点（以下の過去記録より優先）

- 現在ブランチはfeature/user-service-tests、HEADはf1cc92c。User.java・UserServiceTest.javaと運用ドキュメントに未コミット変更あり。Gitの変更操作は未実施。
- 明示依頼によりUserServiceTestの4ケース分のコードをチャットへ提示した。提示時にAIはソースを編集していない。コード提示は実装完了・検証成功を意味しない。
- 実ファイルはUT-USER-001のみ作成中。BeforeEachによるモック・Service準備、SystemRole・User作成、固定日時、検索スタブ、Service呼び出しまで記述済み。DTOのアサーションと検索回数の検証は未記述。
- 最優先修正：ReflectionTestUtilsのフィールド名がcreateAtになっているが、Userの実フィールドはcreatedAt。現在のまま実行するとフィールド設定で失敗する。
- 不要import（Mock、Flyway内部Database、UserSummaryResponse、同一パッケージのUserService）とAssertionsのワイルドカードを整理する。
- User.javaの変更はコメント追加・引数なしコンストラクタの整形。今回確認した差分に機能変更はない。
- 次回はフィールド名修正、DTO全7項目の一致と検索1回の検証追加、UT-USER-001の実行・レビュー。その後UT-USER-002〜004、MasterDataServiceTestの順に進める。
- コンパイル・テスト実行は今回未実施。仕様書の状態は引き続き作成中・未実施。

## 2026-10-27 作業再開・Git運用変更（最新）

- 開発は作業目的ごとの作業用ブランチで行い、対象作業の実装・レビュー・必要なテスト完了後にmainへマージする。PRは必須としない。
- 確認時の現在ブランチはfeature/user-service-tests、作業ツリーはクリーン。ブランチ作成はユーザー側で実施済みの状態を確認したもので、AIは作成していない。リモートへのpush状況は未確認。
- HEADはf1cc92c。前回のテスト方針・仕様書・ログ・作成途中のテストはコミット済みであり、下記の「終了時点」の未コミット記録は過去の状態。
- 次の開発作業は引き続きUserServiceTestのUT-USER-001の準備部分の修正。単体テストは作成中・未実施で、mainへのマージ完了とは扱わない。
- コミット・push・マージのタイミングはユーザーが判断する。AIは明示指示なくGitの変更操作を実行しない。

## 2026-10-27 終了時点（最新）

- UserServiceTest.javaを作成中。UT-USER-001の準備段階で、Mockito.mockによるUserRepositoryの生成まで記述済み。
- 未修正：whenのstatic import、Userコンストラクタの引数不一致、実際のServiceと異なるfindByIdのスタブ設定、SystemRoleの準備不足。テスト対象の実行・アサーション・検索1回の検証は未記述。
- 次回はSystemRoleを用意し、Userの実際のコンストラクタに合わせて作成する。ID・固定日時をReflectionTestUtils等で設定し、findByIdAndDeletedAtIsNullの戻り値を設定する。
- ADMINは定義済みロールではないためUSER等へ合わせる。不要import・ワイルドカードimportを整理する。
- モック生成は@Mockとの併用を避け、まず明示的なmockで学習する。Repositoryを代用し、UserService自体は実物を動かす。
- UT-USER-001は作成中・未実施。他の5ケースは未作成。コンパイル・JUnit実行・依存関係取得の完了報告は未確認。
- ヒント・検索観点を中心に支援し、直接的な実装コードは明示依頼があるまで提示しない。
- 終了時点のHEADはa2791c7。テスト方針・実装計画・仕様書・ログと作成途中のテストが未コミット。Git操作手順を案内する。


## 単体テスト仕様の管理先（2026-10-25）

- 個別ケースの正本は `docs/architecture/タスク管理機能/詳細設計/単体テスト仕様書.md` とする。MDで管理しExcel版との二重管理はしない。
- テストケース設計書4.1から既存6ケースを移管済み。現時点はテストコード未作成・未実施。UserServiceのUT-USER-001から着手する。


## 2026-10-25 テスト方針変更後のタスク（最新）

- ユーザー合意により、処理の実装・レビュー直後に単体テストを追加し、機能接続後に結合テストを行う。10月23日の機能完成後にまとめて追加する方針を更新する。
- 次はUserServiceTestの4ケース（ID・メールの取得成功と不在例外）を作成・実行する。続いてMasterDataServiceTestのDTO変換・空リストを確認する。
- 使用技術は既存のJUnit Jupiter・Mockito。テストコードもユーザーが実装し、AIは考え方の解説・レビューを担当する。
- テスト作成前に現状のcompileを確認する。実行時はMavenのtestを使用し、compile成功だけを動作確認としない。
- 共通ハンドラのHTTP応答検証、Repositoryと実DBの検索検証は別の範囲として計画する。新規の業務APIをテスト目的だけで追加しない。
- CurrentUserResponse・AuthService等の新規実装は、既存Serviceの単体テスト追加後に再開する。


## 2026-10-25 再開確認（最新）

- HEADはa2791c7（Implement user lookup service and common error handling）。確認開始時の作業ツリーはクリーン。ローカルmainと保存済みorigin/main参照は一致。リモート問い合わせは未実施。
- UserService・共通例外・エラーDTO・ハンドラ・関連設計書の前回変更はコミット済み。
- AuthService、AuthController、CurrentUserResponse、ログイン基盤は未実装。SecurityConfigは固定マスタAPIをpermitAllにする暫定設定のまま。
- 最初の作業は最終状態のコンパイル確認。その後CurrentUserResponseの作成、認証済み本人IDの受け渡し・認証不在時の契約確認、AuthServiceと認証基盤の実装へ進む。
- AuthControllerを作成するだけでは本人情報取得は動作完了しない。任意IDでの公開検索や認証省略のAPI追加は行わず、API設計書7.4とController設計書7.4〜7.6に従う。
- 本日はビルド・コンテナ・DB・HTTP確認は未実施。


## 2026-10-23 終了時点（最新・過去のタスクより優先）

- UserServiceのID検索・メール検索・UserResponse変換・削除済み表示名を切り替えるUserSummaryResponse変換を実装し、コードレビュー済み。
- ResourceNotFoundException、ErrorDetail、ErrorResponse、GlobalExceptionHandlerを作成済み。対象データなしを404 / RESOURCE_NOT_FOUNDへ変換する構成をレビュー済み。
- ErrorResponse.timestampはOffsetDateTime。現在のnow()は実行環境の既定タイムゾーンを使用し、日本時間固定ではない。
- ユーザーからコンパイル確認完了の報告あり。ただし、その後のメール検索・簡易DTO変換を含む最終状態のコンパイル成功は未確認。DB・HTTP・自動テストは未検証。
- API設計書7.4とController設計書7.4〜7.6にUser関連仕様を反映済み。本人情報APIはGET /api/auth/me、AuthController→AuthServiceを維持。最新情報参照にUserServiceを利用できる。
- Service設計書のUserService追補を通常の第10章へ統合し、一覧・共通方針・AuthServiceの連携説明と章番号を更新済み。
- /api/users等の独立したユーザー管理APIは未定義。追加する要件・認可・画面の詳細は未確定。
- 次回は最終状態のコンパイルを確認し、AuthService・CurrentUserResponse・認証基盤の既存設計を読み合わせ、本人情報取得に必要な実装順序を整理する。専用UserControllerや単体テストへの移行を自動的に前提としない。
- 機能単位で実装を進め、その後に単体・結合テストを追加する方針。途中でテストを提案する場合は理由を説明する。
- UserSummaryResponse変換は現在privateで未使用。他機能との接続時に配置・共有方法を整理する。
- 未コミット変更はUserService、API・Controller・Service設計書、進捗ログ。common・exception配下に新規ファイルあり。Git操作はユーザー指示時のみ扱う。


## 2026-10-21 終了時点（最新）

- UserServiceはID検索とDTO変換を途中まで実装済み。10月20日時点の空クラスという記録から進捗あり。
- レビューでは検索の7回呼び出し、不要なcast、Optional.get()の繰り返し、toUserResponse未使用、不在時の例外整合を指摘。修正後の再レビューは未実施。
- 次回は検索を1回にまとめ、User全体を保持してtoUserResponseへ渡す処理を確認する。不在時の例外方針を整理してコンパイル確認後、メール検索・簡易DTO変換へ進む。
- ログ更新前のHEADは `9451e8b`（WIPコミット）、作業ツリーはクリーン。リモートの最新状態とプッシュ成否は未確認。
- 本日のチャット内容とレビュー詳細はAI_mentor_log.mdの2026-10-21節に記録済み。本日のコンパイル・実行確認は未実施。


## 2026-10-20 作業再開時の確認（最新）

- 最新コミットは `7e84f45`（2026-08-22、User DTOとService設計追補）。ローカルmainとローカルのorigin/main参照は一致。リモートへの問い合わせは未実施。
- 固定マスタAPIの動作確認・文字化けの切り分けは前回完了済み。SecurityConfigは開発確認用の一時設定として残す決定済み。
- User DTO 4件はrecordで実装・コミット済み。前回のコンパイル成功はユーザー報告による。
- `user/service/UserService.java` は未追跡で、`@Service` と `@Transactional` を付けた空クラスまで作成済み。次の作業はこのファイルの取得処理・DTO変換の実装。
- 初期取得処理は読み取り専用とし、UserRepositoryをコンストラクタ注入して未削除ユーザーをID・メールアドレスで取得する。
- 存在しないユーザーの扱いは例外設計との照合が必要。共通例外・ハンドラは未実装。
- `GET /api/users/{id}` は現行API設計に未定義。過去チャットでの提案を確定仕様として実装しない。API公開前に用途・認可を確定する。
- 登録・更新Request DTOの入力検証、認証認可の本実装、業務Service/Controller、フロントエンド、自動テストは後続作業。
- 今回はコンパイル・DB接続・API再起動確認を実施していない。下記の古い次回タスクより本節を優先する。

## 現在の工程

- バックエンド実装フェーズ

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
- `V8__insert_master_data.sql` は作成・レビュー・Flyway適用確認済み
- `flyway_schema_history` に version 1 から version 7 までの成功履歴が記録済み
- `flyway_schema_history` に version 8 の成功履歴が記録済み
- MySQL上で `task_comments`、`attachments`、`project_invitations`、`password_reset_tokens` の作成確認済み
- MySQL上で固定マスタ初期データの投入確認済み
- 固定マスタの日本語表示名は、`HEX(name)` によりUTF-8バイト列で保存されていることを確認済み
- PowerShell上で `?` と表示される件は、DB保存値ではなく表示側の文字化けとして扱う
- Flywayマイグレーション設計書のファイル命名方針は、V5以降の実装順に合わせて更新済み
- `V9__insert_dev_initial_admin.sql` は開発用マイグレーションとして扱う方針
- Gitのコミット・プッシュはユーザー指示時に行う方針
- 固定マスタ系Entity/Repositoryは作成・レビュー・コンパイル確認済み
- User系Entity/Repositoryは作成・レビュー・コンパイル確認済み
- Project系Entity/Repositoryは作成・レビュー・コンパイル確認済み
- Task系Entity/Repositoryは作成・レビュー・コンパイル確認済み
- ProjectInvitation、PasswordResetToken、関連Enum、Repositoryは作成・レビュー・コンパイル確認済み
- Entity/Repository実装と設計書の照合は完了済み
- 固定マスタDTOは作成・レビュー・コンパイル確認済み
- `MasterDataService.java` は作成・レビュー・コンパイル確認済み
- 固定マスタ取得APIとして、API設計書に `GET /api/master-data` を追記済み
- Controller設計書に `MasterDataController` を追記済み
- `MasterDataController.java` は作成・レビュー・コンパイル確認済み
- 固定マスタ取得API追加分はコミット済み

## 直近レビューでの残修正候補

- なし

## 継続運用ルール

- ユーザーが解説を希望した用語、または学習上重要な用語は `docs/context/glossary/glossary.md` または `docs/context/glossary/` 配下の学習用Markdownに追記する
- 用語集追記時は、意味と本プロジェクトでの扱い方を簡潔に記録する
- `/docs/architecture/` 配下の設計書Markdownは、ユーザーから明示指示がある場合に限り、AIが編集・新規作成できる
- Gitのコミット・プッシュは、ユーザーから明示指示があった場合のみタスクとして扱う
- 通常の次回タスクや作業手順には、Git関連作業を含めない
- ただし作業開始時のブランチ確認は行い、開発は作業用ブランチ上で進める。対象作業完了後にmainへマージする方針は2026-10-27以降に適用する
- 作業終了前にユーザーがコミット・プッシュを指示した場合、その時点で差分確認、ステージング、コミット、プッシュの手順を提示する

## 次回タスク

- 固定マスタ取得APIの動作確認を行う
  - Docker MySQLの起動状態を確認する
  - Spring Bootを起動する
  - `GET /api/master-data` を実行する
  - `projectRoles`、`taskStatuses`、`taskPriorities` が返ることを確認する
  - 認証エラーが出た場合は、Security設定・ログイン状態の問題として切り分ける

---

## 2026-08-21 現在の進捗

- `MasterDataController` / `MasterDataService` / 固定マスタDTOの作成とコンパイル確認は完了。
- `GET /api/master-data` のブラウザ確認は完了。
- APIレスポンスで `projectRoles` / `taskStatuses` / `taskPriorities` が返却されることを確認済み。
- MySQL上の日本語データは `HEX(name)` によりUTF-8として正常保存されていることを確認済み。
- ブラウザでは日本語が正常表示されたため、アプリケーション側の文字化け問題は解決済みとして扱う。
- PowerShellで発生した文字化けは、表示環境・文字コード変換の問題として扱う。
- `SecurityConfig` は `GET /api/master-data` の開発確認用一時設定として残す。
- Gitのコミット・プッシュはユーザー指示時のみ扱う。通常の次タスクには含めない。

## 次の開発タスク

- 一時的な `SecurityConfig` の扱いを開発方針として整理する。
- `MasterDataController` の実装内容と現在の設計書記述に大きなずれがないか確認する。
- 次の実装対象として、ユーザー系機能のService/DTO/Controllerの設計方針を確認する。
- まずは `UserService` で扱う処理範囲を決める。
  - ユーザー取得
  - ユーザー登録
  - ユーザー更新
  - 論理削除
  - 管理者ユーザー確認
- `UserService` に入る前に、User用DTOの要否とレスポンス項目を設計書と照合する。
