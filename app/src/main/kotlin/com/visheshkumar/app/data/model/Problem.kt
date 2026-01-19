package com.visheshkumar.app.data.model

/**
 * Represents a coding problem for problem-solving practice.
 * 
 * Problems are different from MCQs - they require writing actual code
 * and have time-based reward mechanics.
 */
data class Problem(
    val id: String,
    val levelId: String,
    val problemNumber: Int,
    val title: String,
    val description: String,
    val difficulty: String, // "easy", "medium", "hard"
    val languageId: String,
    val starterCode: String,
    val testCases: List<TestCase>,
    val timeLimit: Long = 60_000, // 1 minute in milliseconds
    val isPlaceholder: Boolean = true,
    val hasBeenSolved: Boolean = false, // Track if user has solved this before
    val bestSolveTime: Long = 0L // Best time in milliseconds (0 = not solved yet)
)

/**
 * Represents a test case for a problem
 */
data class TestCase(
    val input: String,
    val expectedOutput: String,
    val isHidden: Boolean = false // Hidden test cases not shown to user
)
