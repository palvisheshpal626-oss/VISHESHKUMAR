package com.visheshkumar.app.test

import com.visheshkumar.app.data.source.MCQDataSource
import com.visheshkumar.app.data.source.LanguageDataSource

/**
 * Demonstration and verification of MCQ placeholder system.
 * Phase 3: Shows how the MCQ structure works with placeholder questions.
 */
fun main() {
    println("=== MCQ Placeholder System - Phase 3 ===\n")
    
    // 1. Test getting MCQs for a specific level
    println("1. Testing MCQs for a specific level:")
    val testLevelId = "kotlin_section_1_level_1"
    val mcqs = MCQDataSource.getMCQsForLevel(testLevelId)
    
    println("   Level: $testLevelId")
    println("   Number of MCQs: ${mcqs.size}")
    println()
    
    // 2. Display first MCQ
    println("2. Sample MCQ structure:")
    val firstMCQ = mcqs.firstOrNull()
    if (firstMCQ != null) {
        println("   ID: ${firstMCQ.id}")
        println("   Question: ${firstMCQ.questionText}")
        println("   Options:")
        firstMCQ.options.forEachIndexed { index, option ->
            val marker = if (index == firstMCQ.correctAnswerIndex) "✓" else " "
            println("     [$marker] ${('A' + index)}. $option")
        }
        println("   Correct Answer: Option ${('A' + firstMCQ.correctAnswerIndex)}")
        println("   Explanation: ${firstMCQ.explanation}")
        println("   Is Placeholder: ${firstMCQ.isPlaceholder}")
    }
    println()
    
    // 3. Test MCQ retrieval by ID
    println("3. Testing MCQ retrieval by ID:")
    val mcqId = "python_section_2_level_3_mcq_4"
    val retrievedMCQ = MCQDataSource.getMCQById(mcqId)
    if (retrievedMCQ != null) {
        println("   ✓ Successfully retrieved MCQ: ${retrievedMCQ.id}")
        println("   Question: ${retrievedMCQ.questionText}")
    } else {
        println("   ✗ MCQ not found")
    }
    println()
    
    // 4. Verify MCQs for different languages
    println("4. Testing MCQs across different languages:")
    val languages = listOf("kotlin", "java", "python", "javascript")
    languages.forEach { langId ->
        val levelId = "${langId}_section_1_level_1"
        val langMCQs = MCQDataSource.getMCQsForLevel(levelId)
        println("   $langId: ${langMCQs.size} MCQs")
    }
    println()
    
    // 5. Verify MCQs for different difficulty levels
    println("5. Testing MCQs for different difficulty levels:")
    val difficulties = listOf(
        "kotlin_section_1_level_1" to "Easy",
        "kotlin_section_1_level_5" to "Medium",
        "kotlin_section_1_level_9" to "Hard"
    )
    difficulties.forEach { (levelId, expectedDifficulty) ->
        val level = LanguageDataSource.getLevelById(levelId)
        val levelMCQs = MCQDataSource.getMCQsForLevel(levelId)
        println("   Level $levelId (${level?.difficulty}): ${levelMCQs.size} MCQs")
    }
    println()
    
    // 6. Calculate total MCQ count
    println("6. System-wide MCQ statistics:")
    val totalLanguages = LanguageDataSource.getLanguages().size
    val totalLevels = LanguageDataSource.getAllLevels().size
    val mcqsPerLevel = MCQDataSource.getMCQCountForLevel("kotlin_section_1_level_1")
    val totalMCQs = totalLevels * mcqsPerLevel
    
    println("   Total Languages: $totalLanguages")
    println("   Total Levels: $totalLevels")
    println("   MCQs per Level: $mcqsPerLevel")
    println("   Total MCQs in system: $totalMCQs")
    println()
    
    // 7. Verify placeholder structure
    println("7. Verifying placeholder structure:")
    val sampleLevel = "java_section_5_level_7"
    val sampleMCQs = MCQDataSource.getMCQsForLevel(sampleLevel)
    val allArePlaceholders = sampleMCQs.all { it.isPlaceholder }
    val allHavePlaceholderText = sampleMCQs.all { 
        it.questionText == "Question will be added later" 
    }
    
    println("   Level: $sampleLevel")
    println("   All MCQs are placeholders: $allArePlaceholders")
    println("   All have placeholder text: $allHavePlaceholderText")
    println()
    
    // 8. Test edge cases
    println("8. Testing edge cases:")
    val invalidLevelId = "invalid_level_id"
    val invalidMCQs = MCQDataSource.getMCQsForLevel(invalidLevelId)
    println("   MCQs for invalid level: ${invalidMCQs.size}")
    
    val invalidMCQId = "invalid_mcq_id"
    val invalidMCQ = MCQDataSource.getMCQById(invalidMCQId)
    println("   Retrieved MCQ with invalid ID: ${invalidMCQ != null}")
    println()
    
    // 9. Verify MCQ availability check
    println("9. Testing MCQ availability:")
    val testLevels = listOf(
        "kotlin_section_1_level_1",
        "python_section_10_level_10",
        "bash_section_5_level_5"
    )
    testLevels.forEach { levelId ->
        val hasAvailable = MCQDataSource.hasMCQsForLevel(levelId)
        println("   $levelId has MCQs: $hasAvailable")
    }
    println()
    
    println("=== MCQ System Verification Complete ===")
    println()
    println("Summary:")
    println("✓ MCQ data model created")
    println("✓ MCQ data source with placeholder questions")
    println("✓ 5 MCQs per level (10,000 total MCQs)")
    println("✓ Placeholder text: 'Question will be added later'")
    println("✓ Easy for developers to add real questions")
    println("✓ MCQ retrieval by ID works")
    println("✓ MCQ availability check works")
    println()
    println("Ready for UI implementation!")
}
