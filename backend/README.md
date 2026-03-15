# 請求・支払管理システム

## 概要
企業や個人事業主が、請求情報と支払情報を管理することを想定した業務系Webアプリケーションです。  
請求の登録、支払状況の管理、ステータス更新などを通して、実務に近いバックエンド設計を意識して開発しています。

## 作成目的
本アプリは、Java / Spring Boot を用いた業務系システム開発のポートフォリオとして作成しています。  
CRUD機能だけでなく、以下のような実務でよく使われる要素を取り入れることを目的としています。

- エンティティ設計
- DTOによるデータ受け渡し
- Service / ServiceImpl による責務分離
- 例外処理
- ステータス管理
- テスト実装
- README / GitHub を含めた開発プロセスの可視化

## 主な機能（予定含む）
- 顧客情報管理
- 請求情報管理
- 支払情報管理
- 請求ステータス更新
- 支払状況の確認
- 検索・一覧表示
- 例外処理
- テストコード作成

## 使用技術
### バックエンド
- Java
- Spring Boot
- Gradle
- JPA / Hibernate

### データベース
- H2 Database または PostgreSQL（予定/環境に応じて変更）

### 開発ツール
- Eclipse
- Git / GitHub
- Postman（API確認用予定）

## 現在の実装状況
現在は以下まで実装済みです。

- Entity
- Repository
- DTO
- Service
- ServiceImpl
- Exception クラス（一部）

※ Controller、テストコード、README詳細整備は今後対応予定です。

## ディレクトリ構成
```text
src/main/java/com/example/project
├─ controller
├─ dto
├─ entity
├─ exception
├─ repository
├─ service
    └─ impl
```

## 今後の実装予定

- Controller 作成  
- API動作確認  
- バリデーション追加  
- テストコード作成  
- READMEの改善  
- GitHub公開用の整備  


## 工夫した点

- Entity / DTO / Service を分離し、責務を明確にしたこと  
- 実務を意識して ServiceImpl を分けて実装していること  
- 例外処理を導入し、保守性を意識していること  


## 課題

- Controller 未実装のため、APIとしての動作確認が未完了  
- テストコードが未整備  
- READMEの内容は今後さらに改善予定  


## 開発者

- 作成者: k  
- 目的: Javaバックエンド開発力を示すためのポートフォリオ作成  
