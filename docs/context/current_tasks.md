# Current Tasks

## 現在の工程

- 詳細設計フェーズ

## 現在のタスク

- 基本設計書は `docs/architecture/Task management function/Basic design/` に集約済み
- 詳細設計書は `docs/architecture/Task management function/Detail design/` に作成する
- 基本設計書一式の横断レビューでは大きな問題なし
- API設計書の認可表タイポは修正済み
- 画面/API対応表のプロジェクト削除API配置は修正済み
- 画面設計書のプロジェクト編集画面に削除ボタンは追記済み
- 画面設計書のプロジェクト編集画面の操作一覧に、プロジェクト削除操作の行が追記済み
- `technical_decision_design.md` は作成済み
- `technology_selection_decision.md` は削除済み
- 採用済み技術が未決事項に残っていた箇所は概ね修正済み
- `backend_structure_design.md` は作成・レビュー済み
- `entity_design.md` は作成・レビュー済み
- `dto_design.md` は作成・レビュー済み
- `repository_design.md` は作成・レビュー済み
- DBはMySQL 8系を別途採用する
- DB以外は現PCにインストールされている環境を極力採用する
- Dockerは学習目的で使用する
- 次回は `repository_design.md` の軽微な補足修正反映を確認する
- 問題なければ `service_design.md` の作成へ進む

## 直近レビューでの残修正候補

- `repository_design.md` に、論理削除済みユーザーと同じemailで再登録を許可するかを補足する
- `repository_design.md` に、論理削除済みProjectMemberの再招待・再参加時の復元方針を補足する
- `repository_design.md` に、Projectが論理削除済みの場合は配下データを通常操作対象外とする方針を補足する
- `repository_design.md` に、同一プロジェクト・同一メールのPENDING招待確認を追加する
- `repository_design.md` に、ユーザーのACTIVEパスワードリセットトークン取得を追加する

## 残っている主な詳細設計書

- `service_design.md`
- `controller_design.md`
- `exception_design.md`
- `security_design.md` または `authentication_authorization_detail_design.md`
- `flyway_migration_design.md`
- `docker_compose_design.md`
- `frontend_structure_design.md`
- `frontend_state_design.md`
