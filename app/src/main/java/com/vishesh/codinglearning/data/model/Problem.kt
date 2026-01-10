package com.vishesh.codinglearning.data.model

data class Problem(
    val id: Int,
    val title: String,
    val statement: String,
    val inputFormat: String,
    val outputFormat: String,
    val examples: List<ProblemExample>,
    val testCases: List<ProblemTestCaseModel>,
    val difficulty: Difficulty,
    val language: ProgrammingLanguage,
    val isLocked: Boolean = true,
    val isSolved: Boolean = false
)

data class ProblemExample(
    val input: String,
    val output: String,
    val explanation: String = ""
)

enum class Difficulty {
    EASY, MEDIUM, HARD
}
