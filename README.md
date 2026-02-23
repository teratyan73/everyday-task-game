# 習慣化タスクゲーム (Everyday Task Game)

毎日のタスクをゲーム感覚で継続できる Android アプリです。
タスクをこなすたびにポイントとストリークが積み重なり、キャラクターが進化します。

---

## アプリの概要

「習慣化タスクゲーム」は、日々のルーティンタスクを楽しく続けられるよう設計されたハビットトラッカーアプリです。
タスクを毎日チェックすることで連続達成日数（ストリーク）とポイントが増加し、キャラクターが段階的に進化します。
ゲーム的な達成感によって習慣化のモチベーションを維持することを目的としています。

---

## 主な機能

### タスク管理
- タスクの追加・編集・削除
- 毎日のタスクチェック（完了 / 未完了の切り替え）
- 日付が変わると自動的にチェック状態がリセット

### ストリーク（連続達成）
- 各タスクの連続達成日数を個別に記録
- 全タスクを連続して毎日完了した「全体ストリーク」も管理
- 未達成の日はそのタスクのストリークが 0 にリセット

### ポイントシステム
- 総合ポイント = 各タスクのストリーク合計 + 全体ストリーク
- タスクを継続するほどポイントが積み重なる

### キャラクター進化
| 段階 | 名前 | 必要ポイント |
|------|------|------------|
| 0 | タマゴ 🥚 | 0 pt〜 |
| 1 | ひよこ 🐣 | 10 pt〜 |
| 2 | そだちかけ 🐤 | 30 pt〜 |
| 3 | おとな 🦅 | 60 pt〜 |
| 4 | でんせつ 🌟 | 100 pt〜 |

---

## 使用技術スタック

| カテゴリ | 技術 |
|---------|------|
| 言語 | Kotlin 1.9.22 |
| UI | Jetpack Compose (BOM 2024.06.00) / Material3 |
| アーキテクチャ | MVVM (ViewModel + StateFlow + UiState) |
| ナビゲーション | Navigation Compose 2.7.7 |
| データベース | Room 2.6.1 (SQLite) |
| DI | Hilt 2.50 |
| 非同期処理 | Kotlin Coroutines 1.7.3 / Flow |
| ビルドツール | Gradle 8.4 / AGP 8.2.2 |
| 最小 SDK | API 26 (Android 8.0) |
| ターゲット SDK | API 34 (Android 14) |

---

## 画面構成

### ホーム画面 (`HomeScreen`)
- キャラクター表示エリア（進化段階・絵文字・次の進化までのポイント）
- 統計カード（全体ストリーク・総合ポイント）
- 今日のタスク一覧（チェックボックス・ストリーク表示）
- タスク追加ボタン（FAB）

### タスク編集画面 (`TaskEditScreen`)
- タスク名の入力・編集
- 新規追加 / 既存タスクの更新
- タスクの削除（確認ダイアログ付き）

---

## プロジェクト構成

```
app/src/main/java/com/example/everydaytaskgame/
├── HabitApp.kt                   # Application クラス（Hilt 初期化）
├── MainActivity.kt               # エントリーポイント・NavHost 定義
├── config/
│   └── CharacterConfig.kt        # キャラクター進化設定
├── data/
│   ├── db/
│   │   ├── AppDatabase.kt        # Room データベース定義
│   │   ├── dao/                  # DAO インターフェース群
│   │   └── entity/               # エンティティクラス群
│   └── repository/
│       ├── TaskRepository.kt
│       └── AppStateRepository.kt
├── di/
│   └── AppModule.kt              # Hilt DI モジュール
└── ui/
    ├── home/
    │   ├── HomeScreen.kt
    │   └── HomeViewModel.kt
    ├── task/
    │   ├── TaskEditScreen.kt
    │   └── TaskEditViewModel.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

---

## 今後の予定

### フェーズ2：ジェム機能
- タスク達成でジェム（ゲーム内通貨）を獲得
- ストリーク保護シールド（ジェムで購入）
- ストリーク復活権（ジェムで購入）
- ジェムショップ画面

### フェーズ3：課金・広告
- 広告視聴によるジェム獲得
- 広告非表示パック（アプリ内課金）
- プレミアムキャラクタースキン（アプリ内課金）
- Google Play Billing 連携

---

## セットアップ

1. リポジトリをクローン
```bash
git clone <repo-url>
cd everyday-task-game
```

2. Android Studio で開く（Hedgehog 2023.1.1 以上推奨）

3. エミュレーター or 実機（API 26 以上）でビルド・実行
