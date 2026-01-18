package com.codinglearning.app.data.model

data class Problem(
    val id: String,
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    val language: String,
    val starterCode: String,
    val testCases: List<TestCase>,
    val coinsReward: Int = 30
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}

data class TestCase(
    val input: String,
    val expectedOutput: String
)
