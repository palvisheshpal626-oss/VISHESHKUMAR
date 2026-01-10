package com.codinglearning.app.data.model

data class UserProgress(
    val selectedLanguage: String = "",
    val highestCompletedLevel: Int = 0,
    val coins: Int = 0,
    val completedLevels: Set<Int> = emptySet()
)
