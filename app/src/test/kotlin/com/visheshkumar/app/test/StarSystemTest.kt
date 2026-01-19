package com.visheshkumar.app.test

import com.visheshkumar.app.data.model.StarResult
import com.visheshkumar.app.ui.common.StarCalculator

/**
 * Verification tests for Phase 7: Star Rating System
 * 
 * This file validates:
 * - StarResult data model
 * - StarCalculator logic for MCQs
 * - StarCalculator logic for Problems
 * - Star-based section unlocking
 */
fun main() {
    println("=== Phase 7: Star Rating System Verification ===\n")
    
    var testsPass = 0
    var testsFail = 0
    
    // Test 1: StarResult model validation
    println("Test 1: StarResult Model")
    try {
        val star3 = StarResult("kotlin_section_1_level_1", StarResult.STARS_PERFECT)
        val star2 = StarResult("kotlin_section_1_level_2", StarResult.STARS_GOOD)
        val star1 = StarResult("kotlin_section_1_level_3", StarResult.STARS_SLOW)
        val star0 = StarResult("kotlin_section_1_level_4", StarResult.STARS_FAILED)
        
        assert(star3.stars == 3) { "3 stars should be 3" }
        assert(star2.stars == 2) { "2 stars should be 2" }
        assert(star1.stars == 1) { "1 star should be 1" }
        assert(star0.stars == 0) { "0 stars should be 0" }
        
        assert(star3.isPerfect()) { "3 stars should be perfect" }
        assert(!star2.isPerfect()) { "2 stars should not be perfect" }
        
        assert(star3.canUnlockNext()) { "3 stars should unlock next" }
        assert(star2.canUnlockNext()) { "2 stars should unlock next" }
        assert(star1.canUnlockNext()) { "1 star should unlock next" }
        assert(!star0.canUnlockNext()) { "0 stars should not unlock next" }
        
        assert(star3.toEmoji() == "⭐⭐⭐") { "3 stars emoji incorrect" }
        assert(star2.toEmoji() == "⭐⭐") { "2 stars emoji incorrect" }
        assert(star1.toEmoji() == "⭐") { "1 star emoji incorrect" }
        assert(star0.toEmoji() == "☆☆☆") { "0 stars emoji incorrect" }
        
        println("✓ StarResult model works correctly")
        testsPass++
    } catch (e: Exception) {
        println("✗ StarResult model failed: ${e.message}")
        testsFail++
    }
    
    // Test 2: Invalid star values should throw
    println("\nTest 2: StarResult Validation")
    try {
        var exceptionThrown = false
        try {
            StarResult("test", 4) // Invalid: > 3
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
        }
        assert(exceptionThrown) { "Should throw for stars > 3" }
        
        exceptionThrown = false
        try {
            StarResult("test", -1) // Invalid: < 0
        } catch (e: IllegalArgumentException) {
            exceptionThrown = true
        }
        assert(exceptionThrown) { "Should throw for stars < 0" }
        
        println("✓ StarResult validation works correctly")
        testsPass++
    } catch (e: Exception) {
        println("✗ StarResult validation failed: ${e.message}")
        testsFail++
    }
    
    // Test 3: MCQ star calculation
    println("\nTest 3: MCQ Star Calculation")
    try {
        // 3 stars: Perfect score, no hints
        val stars3 = StarCalculator.calculateMCQStars(
            totalQuestions = 5,
            correctAnswers = 5,
            hintsUsed = 0,
            timeSpentMs = 60000
        )
        assert(stars3 == 3) { "Perfect score should give 3 stars, got $stars3" }
        
        // 2 stars: 1 wrong answer, no hints
        val stars2 = StarCalculator.calculateMCQStars(
            totalQuestions = 5,
            correctAnswers = 4,
            hintsUsed = 0,
            timeSpentMs = 60000
        )
        assert(stars2 == 2) { "1 wrong should give 2 stars, got $stars2" }
        
        // 1 star: 2 wrong answers, no hints
        val stars1 = StarCalculator.calculateMCQStars(
            totalQuestions = 5,
            correctAnswers = 3,
            hintsUsed = 0,
            timeSpentMs = 60000
        )
        assert(stars1 == 1) { "2 wrong should give 1 star, got $stars1" }
        
        // 0 stars: Used hints
        val stars0 = StarCalculator.calculateMCQStars(
            totalQuestions = 5,
            correctAnswers = 5,
            hintsUsed = 1,
            timeSpentMs = 60000
        )
        assert(stars0 == 0) { "Using hints should give 0 stars, got $stars0" }
        
        // 0 stars: Too many wrong
        val stars0b = StarCalculator.calculateMCQStars(
            totalQuestions = 5,
            correctAnswers = 2,
            hintsUsed = 0,
            timeSpentMs = 60000
        )
        assert(stars0b == 0) { "3+ wrong should give 0 stars, got $stars0b" }
        
        println("✓ MCQ star calculation works correctly")
        testsPass++
    } catch (e: Exception) {
        println("✗ MCQ star calculation failed: ${e.message}")
        testsFail++
    }
    
    // Test 4: Problem star calculation
    println("\nTest 4: Problem Star Calculation")
    try {
        // 3 stars: Fast solve (≤1 min), first attempt
        val stars3 = StarCalculator.calculateProblemStars(
            solveTimeMs = 45000, // 45 seconds
            isCorrect = true,
            attemptCount = 1
        )
        assert(stars3 == 3) { "Fast solve should give 3 stars, got $stars3" }
        
        // 2 stars: Medium time (≤3 min), 2 attempts
        val stars2 = StarCalculator.calculateProblemStars(
            solveTimeMs = 120000, // 2 minutes
            isCorrect = true,
            attemptCount = 2
        )
        assert(stars2 == 2) { "Medium solve should give 2 stars, got $stars2" }
        
        // 1 star: Slow but correct
        val stars1 = StarCalculator.calculateProblemStars(
            solveTimeMs = 240000, // 4 minutes
            isCorrect = true,
            attemptCount = 3
        )
        assert(stars1 == 1) { "Slow solve should give 1 star, got $stars1" }
        
        // 0 stars: Incorrect
        val stars0 = StarCalculator.calculateProblemStars(
            solveTimeMs = 30000,
            isCorrect = false,
            attemptCount = 1
        )
        assert(stars0 == 0) { "Incorrect should give 0 stars, got $stars0" }
        
        // 0 stars: Too many attempts
        val stars0b = StarCalculator.calculateProblemStars(
            solveTimeMs = 30000,
            isCorrect = true,
            attemptCount = 5
        )
        assert(stars0b == 0) { "Too many attempts should give 0 stars, got $stars0b" }
        
        println("✓ Problem star calculation works correctly")
        testsPass++
    } catch (e: Exception) {
        println("✗ Problem star calculation failed: ${e.message}")
        testsFail++
    }
    
    // Test 5: Level star calculation (combined)
    println("\nTest 5: Combined Level Stars")
    try {
        // MCQ: 3 stars, Problems: [3, 2, 3] → Average: 2.75 → 2 stars
        val levelStars1 = StarCalculator.calculateLevelStars(
            mcqStars = 3,
            problemStars = listOf(3, 2, 3)
        )
        assert(levelStars1 == 2) { "Average 2.75 should give 2 stars, got $levelStars1" }
        
        // MCQ: 2 stars, Problems: [2, 2, 2] → Average: 2 → 2 stars
        val levelStars2 = StarCalculator.calculateLevelStars(
            mcqStars = 2,
            problemStars = listOf(2, 2, 2)
        )
        assert(levelStars2 == 2) { "Average 2 should give 2 stars, got $levelStars2" }
        
        // No MCQ, Problems: [3, 3] → Average: 3 → 3 stars
        val levelStars3 = StarCalculator.calculateLevelStars(
            mcqStars = null,
            problemStars = listOf(3, 3)
        )
        assert(levelStars3 == 3) { "Average 3 should give 3 stars, got $levelStars3" }
        
        // Nothing completed → 0 stars
        val levelStars0 = StarCalculator.calculateLevelStars(
            mcqStars = null,
            problemStars = emptyList()
        )
        assert(levelStars0 == 0) { "Nothing completed should give 0 stars, got $levelStars0" }
        
        println("✓ Combined level star calculation works correctly")
        testsPass++
    } catch (e: Exception) {
        println("✗ Combined level star calculation failed: ${e.message}")
        testsFail++
    }
    
    // Test 6: Section unlocking logic
    println("\nTest 6: Section Unlocking")
    try {
        // 50% of stars: Should unlock
        val canUnlock1 = StarCalculator.canUnlockSection(
            previousSectionStars = 15,
            previousSectionMaxStars = 30,
            requiredPercentage = 0.5
        )
        assert(canUnlock1) { "50% stars should unlock section" }
        
        // 40% of stars: Should not unlock (< 50%)
        val canUnlock2 = StarCalculator.canUnlockSection(
            previousSectionStars = 12,
            previousSectionMaxStars = 30,
            requiredPercentage = 0.5
        )
        assert(!canUnlock2) { "40% stars should not unlock section" }
        
        // 70% of stars: Should unlock
        val canUnlock3 = StarCalculator.canUnlockSection(
            previousSectionStars = 21,
            previousSectionMaxStars = 30,
            requiredPercentage = 0.5
        )
        assert(canUnlock3) { "70% stars should unlock section" }
        
        // 0 max stars: Always unlock
        val canUnlock4 = StarCalculator.canUnlockSection(
            previousSectionStars = 0,
            previousSectionMaxStars = 0,
            requiredPercentage = 0.5
        )
        assert(canUnlock4) { "0 max stars should always unlock" }
        
        println("✓ Section unlocking logic works correctly")
        testsPass++
    } catch (e: Exception) {
        println("✗ Section unlocking logic failed: ${e.message}")
        testsFail++
    }
    
    // Test 7: Performance tier classification
    println("\nTest 7: Performance Tiers")
    try {
        assert(StarCalculator.getPerformanceTier(3) == "Excellent")
        assert(StarCalculator.getPerformanceTier(2) == "Good")
        assert(StarCalculator.getPerformanceTier(1) == "Fair")
        assert(StarCalculator.getPerformanceTier(0) == "Poor")
        
        println("✓ Performance tier classification works correctly")
        testsPass++
    } catch (e: Exception) {
        println("✗ Performance tier classification failed: ${e.message}")
        testsFail++
    }
    
    // Test 8: Star color mapping
    println("\nTest 8: Star Colors")
    try {
        assert(StarCalculator.getStarColor(3) == "colorSuccess")
        assert(StarCalculator.getStarColor(2) == "colorInfo")
        assert(StarCalculator.getStarColor(1) == "colorWarning")
        assert(StarCalculator.getStarColor(0) == "colorTextSecondary")
        
        println("✓ Star color mapping works correctly")
        testsPass++
    } catch (e: Exception) {
        println("✗ Star color mapping failed: ${e.message}")
        testsFail++
    }
    
    // Summary
    println("\n=== Verification Summary ===")
    println("Tests Passed: $testsPass")
    println("Tests Failed: $testsFail")
    println("Total Tests: ${testsPass + testsFail}")
    
    if (testsFail == 0) {
        println("\n✅ All verification checks passed!")
        println("✅ Star rating system is working correctly")
        println("\nKey Features Verified:")
        println("  ✓ StarResult data model with validation")
        println("  ✓ MCQ star calculation (0-3 based on performance)")
        println("  ✓ Problem star calculation (time-based)")
        println("  ✓ Combined level star calculation")
        println("  ✓ Section unlocking based on stars")
        println("  ✓ Performance tiers and color mapping")
        println("\nStar System Rules:")
        println("  • 3 stars: Fast & perfect (no hints, no wrong answers)")
        println("  • 2 stars: Good (minor mistakes)")
        println("  • 1 star: Slow (took too long or multiple mistakes)")
        println("  • 0 stars: Failed (used hints or too many wrong answers)")
        println("  • Section unlock: Requires 50% of possible stars")
    } else {
        println("\n❌ Some verification checks failed!")
        println("Please review the failures above.")
    }
}
