# フロントエンドコンポーネント設計書

## 1. 本書の目的

本書は、タスク管理機能の初期MVPにおけるフロントエンドコンポーネントの分割方針、責務、利用関係を定義する。

画面ごとの実装が肥大化しないように、画面、業務コンポーネント、共通UIコンポーネント、モーダル、フォーム、レイアウトを整理する。

## 2. 対象範囲

対象に含めるものは以下とする。

- コンポーネント分類
- 画面別コンポーネント構成
- レイアウトコンポーネント方針
- 共通UIコンポーネント方針
- 業務コンポーネント方針
- フォームコンポーネント方針
- モーダル、ダイアログ方針
- 権限による表示制御方針
- ローディング、エラー表示方針

対象外とするものは以下とする。

- コンポーネントの実装コード
- 詳細なCSS実装
- APIクライアント実装
- TanStack Queryの具体的な実装
- E2Eテストケース

## 3. 前提

フロントエンドはNext.js、TypeScript、Tailwind CSSで構成する。

API通信は共通APIクライアントを経由する。

サーバー状態はTanStack Queryで管理する。

画面状態はReact標準の状態管理で扱う。

認可の最終判定はバックエンドで行う。

## 4. コンポーネント分類

コンポーネントは以下に分類する。

| 分類 | 役割 |
|---|---|
| Page Component | ルーティング単位の画面 |
| Layout Component | ヘッダー、サイドバー、画面枠 |
| Feature Component | 業務機能に紐づく表示部品 |
| Form Component | 入力フォーム |
| Modal Component | 作成、編集、確認用のモーダル |
| UI Component | ボタン、入力欄、テーブルなどの汎用部品 |
| Feedback Component | ローディング、エラー、空状態表示 |

## 5. Page Component方針

Page Componentは画面単位の入口とする。

Page Componentは以下を担当する。

- 画面全体の構成
- 必要なFeature Componentの配置
- URLパラメータの受け取り
- 画面単位の権限チェック結果の表示制御
- ローディング、エラー、空状態の切り替え

Page Componentには複雑な業務処理を持たせない。

## 6. Layout Component方針

認証後画面では、共通レイアウトを使用する。

共通レイアウトは以下を含む。

- ヘッダー
- サイドバー
- ログインユーザー表示
- プロジェクト切り替え導線
- ログアウト導線

ログイン画面、パスワードリセット画面、招待参加画面では、認証後レイアウトを使用しない。

## 7. 共通UIコンポーネント方針

共通UIコンポーネントは、業務ロジックを持たない汎用部品とする。

対象例は以下とする。

- Button
- Input
- Textarea
- Select
- Checkbox
- Modal
- Dialog
- Table
- Badge
- ErrorMessage
- LoadingIndicator
- EmptyState

共通UIコンポーネントは、表示と基本的な操作イベントの受け渡しに責務を限定する。

## 8. 業務コンポーネント方針

業務コンポーネントは、プロジェクト、タスク、コメント、添付ファイル、メンバーなどの業務単位で分割する。

業務コンポーネントは、必要に応じてAPI取得結果、画面状態、権限情報を受け取り、表示を行う。

業務コンポーネントは、必要に応じてQuery Hooksを利用してデータ取得や更新処理を呼び出す。

ただし、共通APIクライアントを直接呼び出さない。

UI ComponentにはAPI通信処理を持たせない。

## 9. フォームコンポーネント方針

フォームコンポーネントは入力項目、入力エラー、送信操作を扱う。

対象例は以下とする。

- LoginForm
- PasswordResetRequestForm
- PasswordResetConfirmForm
- ProjectForm
- TaskForm
- CommentForm
- InvitationForm
- MemberRoleForm

フォームの入力値は画面状態として管理する。

最終的な入力チェックはバックエンドで行う。

## 10. モーダル方針

タスク作成・編集はモーダルで行う。

対象モーダルは以下とする。

| モーダル | 用途 |
|---|---|
| TaskCreateModal | タスク作成 |
| TaskEditModal | タスク編集 |
| DeleteConfirmDialog | 削除確認 |
| RoleChangeConfirmDialog | ロール変更確認 |
| AttachmentDeleteConfirmDialog | 添付ファイル削除確認 |

削除やロール変更など影響が大きい操作では確認ダイアログを表示する。

## 11. 画面別コンポーネント構成

画面ごとの主なコンポーネント構成は以下とする。

| 画面 | 主なコンポーネント |
|---|---|
| ログイン画面 | LoginForm |
| パスワードリセット申請画面 | PasswordResetRequestForm |
| パスワード再設定画面 | PasswordResetConfirmForm |
| プロジェクト一覧画面 | ProjectList, ProjectListItem |
| プロジェクト作成画面 | ProjectForm |
| プロジェクト編集画面 | ProjectForm, ProjectDeleteSection |
| カンバン画面 | KanbanBoard, KanbanColumn, TaskCard, TaskCreateModal, TaskEditModal |
| タスク詳細画面 | TaskDetail, CommentList, CommentForm, AttachmentList, AttachmentUploadForm |
| メンバー管理画面 | MemberTable, MemberRoleForm |
| メンバー招待画面 | InvitationForm |
| 招待参加画面 | InvitationConfirm, InvitationAcceptForm |

## 12. カンバン関連コンポーネント

カンバン画面では以下のコンポーネントを使用する。

| コンポーネント | 役割 |
|---|---|
| KanbanBoard | Status列全体を表示する |
| KanbanColumn | Statusごとのタスク一覧を表示する |
| TaskCard | タスク概要を表示する |
| TaskCreateModal | タスク作成フォームを表示する |
| TaskEditModal | タスク編集フォームを表示する |

ドラッグアンドドロップによるStatus変更は、KanbanBoardまたはKanbanColumnを中心に扱う。

同一Status内の並び替えは初期MVPの対象外とする。

## 13. タスク詳細関連コンポーネント

タスク詳細画面では以下のコンポーネントを使用する。

| コンポーネント | 役割 |
|---|---|
| TaskDetail | タスクの詳細情報を表示する |
| CommentList | コメント一覧を表示する |
| CommentItem | コメント単体を表示する |
| CommentForm | コメント作成、編集入力を扱う |
| AttachmentList | 添付ファイル一覧を表示する |
| AttachmentUploadForm | 添付ファイル追加を扱う |

AttachmentUploadFormは、ファイル選択、ファイルサイズ確認、ファイル種別確認、アップロード操作を扱う。

フロントエンドでは、1ファイルあたり5MB以下であること、許可されたファイル種別であることを送信前に確認する。

最終的な判定はバックエンドで行う。

削除済みユーザーが作成した情報は、表示名を「削除済ユーザー」とする。

## 14. メンバー管理関連コンポーネント

メンバー管理画面では以下のコンポーネントを使用する。

| コンポーネント | 役割 |
|---|---|
| MemberTable | メンバー一覧を表示する |
| MemberRoleSelect | ロール選択を表示する |
| MemberRoleForm | ロール変更を扱う |
| MemberRemoveButton | メンバー削除操作を扱う |

プロジェクト管理者が0人になる操作は禁止する。

ロール変更や削除の最終判定はバックエンドで行う。

## 15. 権限による表示制御

フロントエンドでは、ログインユーザーのシステムロール、対象プロジェクトのプロジェクトロールに応じて操作部品を表示制御する。

| 操作 | 表示対象 |
|---|---|
| プロジェクト作成 | SYSTEM_ADMIN |
| プロジェクト編集 | SYSTEM_ADMIN、PROJECT_ADMIN |
| メンバー管理 | SYSTEM_ADMIN、PROJECT_ADMIN |
| タスク作成 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| タスク編集 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| タスク削除 | SYSTEM_ADMIN、PROJECT_ADMIN |
| コメント作成 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| コメント編集 | コメント投稿者本人 |
| コメント削除 | SYSTEM_ADMIN、PROJECT_ADMIN |
| 添付追加 | SYSTEM_ADMIN、PROJECT_ADMIN、MEMBER |
| 添付削除 | SYSTEM_ADMIN、PROJECT_ADMIN |

表示制御はユーザー体験向上のための補助であり、認可の最終判定はバックエンドで行う。

## 16. ローディング表示方針

データ取得中はローディング表示を行う。

対象例は以下とする。

- 画面初期表示
- 一覧再取得
- フォーム送信中
- 添付ファイルアップロード中

送信中のボタンは無効化し、二重送信を防止する。

## 17. エラー表示方針

APIエラーは、画面または部品単位で表示する。

共通エラー表示コンポーネントは、APIクライアントで変換された共通エラー情報を受け取り、画面に表示する。

入力エラーのように項目単位で表示するものはフォームコンポーネントで扱う。

認証エラー、認可エラー、サーバーエラーなど画面全体に関わるものはPage ComponentまたはFeature Componentで扱う。

| エラー種別 | 表示方針 |
|---|---|
| 入力エラー | 対象フォーム付近に表示する |
| 認証エラー | ログイン画面へ誘導する |
| 認可エラー | 権限がない旨を表示する |
| データなし | 空状態表示を行う |
| サーバーエラー | 共通エラーメッセージを表示する |

画面固有の文言はPageまたはFeature側で管理し、共通UIには持たせない。

## 18. 空状態表示方針

一覧データが存在しない場合は、空状態を表示する。

対象例は以下とする。

- プロジェクトがない
- タスクがない
- コメントがない
- 添付ファイルがない
- メンバーがいない

空状態では、権限がある場合のみ作成導線を表示する。

## 19. コンポーネントとAPIの関係

コンポーネントは、直接APIクライアントを呼び出さず、Query Hooksまたは機能別APIモジュールを経由する。

Page ComponentまたはFeature Componentがデータ取得の入口となる。

UI ComponentはAPI通信を行わない。

## 20. コンポーネントと状態管理の関係

サーバー状態はTanStack Queryで管理する。

画面状態はReact標準の状態管理で扱う。

モーダルの開閉状態、フォーム入力値、選択中のタスクなどは画面状態として扱う。

プロジェクト一覧、タスク一覧、コメント一覧、添付ファイル一覧などはサーバー状態として扱う。

## 21. テスト観点

コンポーネント設計では以下をテスト観点とする。

- 権限に応じてボタンが表示、非表示になること
- ローディング表示が出ること
- エラー表示が出ること
- 空状態表示が出ること
- フォーム入力ができること
- 送信中はボタンが無効化されること
- タスク作成・編集モーダルが開閉できること
- カンバン列にタスクが表示されること
- コメント、添付ファイル、メンバー一覧が表示されること

## 22. 決定事項

- コンポーネントはPage、Layout、Feature、Form、Modal、UI、Feedbackに分類する
- Page Componentには複雑な業務処理を持たせない
- UI Componentには業務ロジックを持たせない
- タスク作成・編集はモーダルで行う
- 削除やロール変更では確認ダイアログを使用する
- サーバー状態はTanStack Queryで管理する
- 画面状態はReact標準の状態管理で扱う
- API通信はQuery Hooksまたは機能別APIモジュールを経由する
- 認可の最終判定はバックエンドで行う

## 23. 未決事項

以下は後続工程または実装時に具体化する。

- 各コンポーネントの具体的なファイル配置
- 共通UIコンポーネントにshadcn/uiをどこまで使用するか
- ドラッグアンドドロップライブラリの選定
- フォーム管理ライブラリを採用するか
- 詳細なレスポンシブ対応範囲