package com.codinglearning.app.data.model

data class Level(
    val id: Int,
    val title: String,
    val language: String,
    val mcqQuestions: List<MCQQuestion>,
    val codeExample: CodeExample,
    val problems: List<Problem>,
    val videoUrl: String?,
    val coinsReward: Int = 50,
    val section: LevelSection = LevelSection.EASY
)

data class LevelState(
    val level: Level,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val starsEarned: Int = 0
)

enum class LevelSection {
    EASY,
    MEDIUM,
    HARD
}
