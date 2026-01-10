package com.vishesh.codinglearning.ui.level

import com.vishesh.codinglearning.data.model.Level

data class LevelState(
    val levels: List<Level> = emptyList(),
    val coins: Int = 0,
    val highestCompletedLevel: Int = 0,
    val isLoading: Boolean = false
)
