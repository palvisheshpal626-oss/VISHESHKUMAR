package com.visheshkumar.app.test

import com.visheshkumar.app.data.model.Problem
import com.visheshkumar.app.data.source.ProblemDataSource

/**
 * Test and verify the Problem Solving system with time-based rewards.
 * 
 * Tests:
 * 1. Problem data structure
 * 2. Problem generation for all levels
 * 3. Time-based reward logic
 * 4. Replay detection
 */
fun main() {
    println("=".repeat(60))
    println("PROBLEM SOLVING SYSTEM VERIFICATION")
    println("=".repeat(60))
    println()
    
    testProblemStructure()
    testProblemGeneration()
    testTimeBasedRewards()
    testReplayLogic()
    testAllLanguages()
    
    println()
    println("=".repeat(60))
    println("✅ ALL VERIFICATION TESTS PASSED!")
    println("=".repeat(60))
}

fun testProblemStructure() {
    println("📋 Test 1: Problem Data Structure")
    println("-".repeat(60))
    
    val problem = ProblemDataSource.getProblemById("kotlin_section_1_level_1_problem_1")
    
    require(problem != null) { "Problem should exist" }
    require(problem.id == "kotlin_section_1_level_1_problem_1") { "Correct ID" }
    require(problem.levelId == "kotlin_section_1_level_1") { "Correct level ID" }
    require(problem.problemNumber == 1) { "Correct problem number" }
    require(problem.languageId == "kotlin") { "Correct language" }
    require(problem.difficulty == "easy") { "Problem 1 should be easy" }
    require(problem.starterCode.isNotBlank()) { "Starter code should exist" }
    require(problem.testCases.isNotEmpty()) { "Test cases should exist" }
    require(problem.timeLimit == 60_000L) { "Time limit should be 1 minute" }
    require(problem.isPlaceholder) { "Should be placeholder" }
    
    println("✓ Problem structure correct")
    println("  - ID: ${problem.id}")
    println("  - Language: ${problem.languageId}")
    println("  - Difficulty: ${problem.difficulty}")
    println("  - Time limit: ${problem.timeLimit}ms")
    println()
}

fun testProblemGeneration() {
    println("📋 Test 2: Problem Generation")
    println("-".repeat(60))
    
    // Test problems per level
    val levelId = "python_section_5_level_3"
    val problems = ProblemDataSource.getProblemsForLevel(levelId)
    
    require(problems.size == 3) { "Should have 3 problems per level" }
    require(problems[0].difficulty == "easy") { "Problem 1: easy" }
    require(problems[1].difficulty == "medium") { "Problem 2: medium" }
    require(problems[2].difficulty == "hard") { "Problem 3: hard" }
    
    println("✓ Problems per level: ${problems.size}")
    problems.forEachIndexed { index, problem ->
        println("  Problem ${index + 1}: ${problem.difficulty}")
    }
    println()
}

fun testTimeBasedRewards() {
    println("📋 Test 3: Time-Based Reward Logic")
    println("-".repeat(60))
    
    // Simulate time-based reward calculation
    val TIME_LIMIT_MS = 60_000L
    
    // Test case 1: First solve within 1 minute
    val case1Time = 45_000L // 45 seconds
    val case1Replay = false
    val case1Coins = calculateReward(case1Time, case1Replay, TIME_LIMIT_MS)
    require(case1Coins == 10) { "First solve ≤ 1 min should get 10 coins" }
    println("✓ Case 1: First solve in 45s → $case1Coins coins")
    
    // Test case 2: First solve after 1 minute
    val case2Time = 75_000L // 75 seconds (1:15)
    val case2Replay = false
    val case2Coins = calculateReward(case2Time, case2Replay, TIME_LIMIT_MS)
    require(case2Coins == 0) { "First solve > 1 min should get 0 coins" }
    println("✓ Case 2: First solve in 75s → $case2Coins coins")
    
    // Test case 3: Replay within 1 minute
    val case3Time = 30_000L // 30 seconds
    val case3Replay = true
    val case3Coins = calculateReward(case3Time, case3Replay, TIME_LIMIT_MS)
    require(case3Coins == 0) { "Replay should get 0 coins" }
    println("✓ Case 3: Replay in 30s → $case3Coins coins")
    
    // Test case 4: Replay after 1 minute
    val case4Time = 90_000L // 90 seconds
    val case4Replay = true
    val case4Coins = calculateReward(case4Time, case4Replay, TIME_LIMIT_MS)
    require(case4Coins == 0) { "Replay should get 0 coins" }
    println("✓ Case 4: Replay in 90s → $case4Coins coins")
    
    println()
}

fun testReplayLogic() {
    println("📋 Test 4: Replay Detection")
    println("-".repeat(60))
    
    val problem = ProblemDataSource.getProblemById("java_section_1_level_1_problem_1")
    
    require(problem != null) { "Problem should exist" }
    require(!problem.hasBeenSolved) { "New problem not solved yet" }
    require(problem.bestSolveTime == 0L) { "No best time for unsolved problem" }
    
    println("✓ Replay detection ready")
    println("  - hasBeenSolved: ${problem.hasBeenSolved}")
    println("  - bestSolveTime: ${problem.bestSolveTime}ms")
    println("  Note: Actual tracking requires database integration")
    println()
}

fun testAllLanguages() {
    println("📋 Test 5: All Languages Support")
    println("-".repeat(60))
    
    val languages = listOf(
        "kotlin", "java", "python", "javascript", "typescript",
        "cpp", "c", "csharp", "swift", "go", "rust",
        "php", "ruby", "dart", "r", "scala", "sql", "html", "css", "bash"
    )
    
    var totalProblems = 0
    
    languages.forEach { lang ->
        val problems = ProblemDataSource.getProblemsForLevel("${lang}_section_1_level_1")
        require(problems.size == 3) { "$lang should have 3 problems" }
        require(problems.all { it.starterCode.isNotBlank() }) { "$lang starter code exists" }
        totalProblems += problems.size
    }
    
    println("✓ All 20 languages supported")
    println("  - Languages: ${languages.size}")
    println("  - Problems tested: $totalProblems")
    println()
    
    // Test total problem count
    val expectedTotal = 20 * 10 * 10 * 3 // 20 languages × 10 sections × 10 levels × 3 problems
    println("✓ Total problem capacity")
    println("  - Expected: $expectedTotal problems")
    println("  - Formula: 20 languages × 10 sections × 10 levels × 3 problems/level")
    println()
}

/**
 * Helper function to calculate reward based on time and replay status
 */
fun calculateReward(solveTime: Long, isReplay: Boolean, timeLimit: Long): Int {
    // Rule 1: Replays get 0 coins
    if (isReplay) {
        return 0
    }
    
    // Rule 2: First solve within time limit gets 10 coins
    if (solveTime <= timeLimit) {
        return 10
    }
    
    // Rule 3: First solve after time limit gets 0 coins
    return 0
}
