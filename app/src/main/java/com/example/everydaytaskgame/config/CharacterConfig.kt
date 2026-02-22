package com.example.everydaytaskgame.config

/**
 * キャラクター進化設定ファイル。
 * 進化段階の閾値を変更することで、ゲームバランスを調整できます。
 */
object CharacterConfig {

    /**
     * 進化段階の定義。
     *
     * @param stage 進化段階番号 (0 = 最初期段階)
     * @param minPoints この段階に到達するための最低ポイント数
     * @param nameJa 段階名（日本語）
     * @param emoji プレースホルダー用の絵文字
     */
    data class EvolutionThreshold(
        val stage: Int,
        val minPoints: Int,
        val nameJa: String,
        val emoji: String
    )

    /**
     * 進化段階の閾値リスト（minPoints 昇順で定義すること）。
     * 後からここの数値を変更することでバランス調整が可能。
     */
    val thresholds: List<EvolutionThreshold> = listOf(
        EvolutionThreshold(stage = 0, minPoints = 0,   nameJa = "タマゴ",     emoji = "🥚"),
        EvolutionThreshold(stage = 1, minPoints = 10,  nameJa = "ひよこ",     emoji = "🐣"),
        EvolutionThreshold(stage = 2, minPoints = 30,  nameJa = "そだちかけ", emoji = "🐤"),
        EvolutionThreshold(stage = 3, minPoints = 60,  nameJa = "おとな",     emoji = "🦅"),
        EvolutionThreshold(stage = 4, minPoints = 100, nameJa = "でんせつ",   emoji = "🌟"),
    )

    /**
     * 総合ポイントから進化段階番号を返します。
     */
    fun getEvolutionStage(totalPoints: Int): Int {
        return thresholds
            .filter { it.minPoints <= totalPoints }
            .maxByOrNull { it.minPoints }
            ?.stage ?: 0
    }

    /**
     * 進化段階番号から [EvolutionThreshold] を返します。
     */
    fun getThreshold(stage: Int): EvolutionThreshold {
        return thresholds.find { it.stage == stage } ?: thresholds.first()
    }

    /**
     * 次の進化段階に必要な残りポイントを返します。
     * 最終段階の場合は null を返します。
     */
    fun pointsToNextStage(totalPoints: Int): Int? {
        val nextThreshold = thresholds
            .filter { it.minPoints > totalPoints }
            .minByOrNull { it.minPoints }
        return nextThreshold?.let { it.minPoints - totalPoints }
    }
}
