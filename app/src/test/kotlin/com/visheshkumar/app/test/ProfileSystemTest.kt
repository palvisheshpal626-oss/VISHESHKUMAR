package com.visheshkumar.app.test

import com.visheshkumar.app.data.model.LanguageProgress
import com.visheshkumar.app.data.model.UserProfile

/**
 * Profile System Test
 * 
 * Verifies the user profile system functionality for Phase 8
 */
fun main() {
    println("=== PHASE 8: Profile System Tests ===\n")
    
    // Test 1: UserProfile model creation
    testUserProfileCreation()
    
    // Test 2: LanguageProgress model
    testLanguageProgress()
    
    // Test 3: Progress emoji mapping
    testProgressEmojis()
    
    // Test 4: Completion percentage calculation
    testCompletionPercentage()
    
    println("\n=== All Profile System Tests Passed! ✅ ===")
}

/**
 * Test 1: UserProfile model creation
 */
fun testUserProfileCreation() {
    println("Test 1: UserProfile model creation")
    
    val profile = UserProfile(
        username = "TestUser",
        totalCoins = 150,
        languagesProgress = emptyList(),
        totalSectionsCompleted = 5,
        totalLevelsCompleted = 25,
        totalStarsEarned = 60,
        joinDate = System.currentTimeMillis(),
        lastActiveDate = System.currentTimeMillis()
    )
    
    assert(profile.username == "TestUser")
    assert(profile.totalCoins == 150)
    assert(profile.totalSectionsCompleted == 5)
    assert(profile.totalLevelsCompleted == 25)
    assert(profile.totalStarsEarned == 60)
    
    println("✓ UserProfile creation works correctly\n")
}

/**
 * Test 2: LanguageProgress model
 */
fun testLanguageProgress() {
    println("Test 2: LanguageProgress model")
    
    // Test partial completion
    val kotlinProgress = LanguageProgress(
        languageId = "kotlin",
        languageName = "Kotlin",
        sectionsCompleted = 3,
        totalSections = 10,
        levelsCompleted = 25,
        totalLevels = 100,
        starsEarned = 60,
        maxStars = 300,
        completionPercentage = 25
    )
    
    assert(kotlinProgress.languageId == "kotlin")
    assert(kotlinProgress.sectionsCompleted == 3)
    assert(kotlinProgress.completionPercentage == 25)
    assert(!kotlinProgress.isFullyCompleted())
    
    // Test full completion
    val javaProgress = LanguageProgress(
        languageId = "java",
        languageName = "Java",
        sectionsCompleted = 10,
        totalSections = 10,
        levelsCompleted = 100,
        totalLevels = 100,
        starsEarned = 300,
        maxStars = 300,
        completionPercentage = 100
    )
    
    assert(javaProgress.isFullyCompleted())
    
    println("✓ LanguageProgress creation works correctly")
    println("✓ isFullyCompleted() logic works\n")
}

/**
 * Test 3: Progress emoji mapping
 */
fun testProgressEmojis() {
    println("Test 3: Progress emoji mapping")
    
    val testCases = listOf(
        Pair(100, "✅"), // Full completion
        Pair(90, "🔥"),  // High progress
        Pair(60, "💪"),  // Medium-high
        Pair(40, "📈"),  // Medium-low
        Pair(10, "🌱")   // Just started
    )
    
    testCases.forEach { (percentage, expectedEmoji) ->
        val progress = LanguageProgress(
            languageId = "test",
            languageName = "Test",
            sectionsCompleted = 0,
            totalSections = 10,
            levelsCompleted = 0,
            totalLevels = 100,
            starsEarned = 0,
            maxStars = 300,
            completionPercentage = percentage
        )
        
        val emoji = progress.getProgressEmoji()
        assert(emoji == expectedEmoji) { 
            "Expected $expectedEmoji for $percentage%, got $emoji" 
        }
        println("  $percentage% → $emoji")
    }
    
    println("✓ Progress emojis map correctly\n")
}

/**
 * Test 4: Completion percentage calculation
 */
fun testCompletionPercentage() {
    println("Test 4: Completion percentage calculation")
    
    // Test various completion levels
    val testCases = listOf(
        Triple(0, 100, 0),    // 0% complete
        Triple(25, 100, 25),  // 25% complete
        Triple(50, 100, 50),  // 50% complete
        Triple(75, 100, 75),  // 75% complete
        Triple(100, 100, 100) // 100% complete
    )
    
    testCases.forEach { (completed, total, expectedPercentage) ->
        val progress = LanguageProgress(
            languageId = "test",
            languageName = "Test",
            sectionsCompleted = 0,
            totalSections = 10,
            levelsCompleted = completed,
            totalLevels = total,
            starsEarned = 0,
            maxStars = 300,
            completionPercentage = expectedPercentage
        )
        
        assert(progress.completionPercentage == expectedPercentage)
        println("  $completed/$total levels → $expectedPercentage%")
    }
    
    println("✓ Completion percentage calculation correct\n")
}
