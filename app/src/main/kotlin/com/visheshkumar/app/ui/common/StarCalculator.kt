package com.visheshkumar.app.ui.common

import com.visheshkumar.app.data.model.StarResult

/**
 * Utility class to calculate star ratings based on user performance
 * 
 * Star Rating Rules:
 * - 3 stars: Fast & perfect (no hints, no wrong answers, quick completion)
 * - 2 stars: Good (acceptable performance with minor mistakes)
 * - 1 star: Slow (took too long or multiple wrong answers)
 * - 0 stars: Failed (used hints or too many wrong answers)
 */
object StarCalculator {
    
    // MCQ thresholds
    private const val MCQ_MAX_WRONG_FOR_3_STARS = 0
    private const val MCQ_MAX_WRONG_FOR_2_STARS = 1
    private const val MCQ_MAX_WRONG_FOR_1_STAR = 2
    
    // Problem thresholds (milliseconds)
    private const val PROBLEM_FAST_TIME_MS = 60_000L  // 1 minute
    private const val PROBLEM_MEDIUM_TIME_MS = 180_000L  // 3 minutes
    
    /**
     * Calculate stars for MCQ quiz completion
     * 
     * @param totalQuestions Total number of questions in the quiz
     * @param correctAnswers Number of correct answers
     * @param hintsUsed Number of hints used
     * @param timeSpentMs Time spent on the quiz in milliseconds
     * @return Star rating (0-3)
     */
    fun calculateMCQStars(
        totalQuestions: Int,
        correctAnswers: Int,
        hintsUsed: Int,
        timeSpentMs: Long
    ): Int {
        // Rule: If any hints were used, 0 stars
        if (hintsUsed > 0) {
            return StarResult.STARS_FAILED
        }
        
        val wrongAnswers = totalQuestions - correctAnswers
        
        // Rule: Perfect score with no hints = 3 stars
        if (wrongAnswers == MCQ_MAX_WRONG_FOR_3_STARS) {
            return StarResult.STARS_PERFECT
        }
        
        // Rule: Minor mistakes (1 wrong) = 2 stars
        if (wrongAnswers <= MCQ_MAX_WRONG_FOR_2_STARS) {
            return StarResult.STARS_GOOD
        }
        
        // Rule: Some mistakes (2 wrong) = 1 star
        if (wrongAnswers <= MCQ_MAX_WRONG_FOR_1_STAR) {
            return StarResult.STARS_SLOW
        }
        
        // Rule: Too many mistakes = 0 stars
        return StarResult.STARS_FAILED
    }
    
    /**
     * Calculate stars for problem solving
     * 
     * @param solveTimeMs Time taken to solve the problem in milliseconds
     * @param isCorrect Whether the solution was correct
     * @param attemptCount Number of attempts made
     * @return Star rating (0-3)
     */
    fun calculateProblemStars(
        solveTimeMs: Long,
        isCorrect: Boolean,
        attemptCount: Int
    ): Int {
        // Rule: If solution is wrong, 0 stars
        if (!isCorrect) {
            return StarResult.STARS_FAILED
        }
        
        // Rule: Multiple attempts = lower stars
        if (attemptCount > 3) {
            return StarResult.STARS_FAILED
        }
        
        // Rule: Fast solve (≤1 min) on first attempt = 3 stars
        if (solveTimeMs <= PROBLEM_FAST_TIME_MS && attemptCount == 1) {
            return StarResult.STARS_PERFECT
        }
        
        // Rule: Medium time (≤3 min) = 2 stars
        if (solveTimeMs <= PROBLEM_MEDIUM_TIME_MS && attemptCount <= 2) {
            return StarResult.STARS_GOOD
        }
        
        // Rule: Slow but correct = 1 star
        if (attemptCount <= 3) {
            return StarResult.STARS_SLOW
        }
        
        return StarResult.STARS_FAILED
    }
    
    /**
     * Calculate combined stars for a level (MCQs + Problems)
     * Takes the average of all completed activities
     * 
     * @param mcqStars Stars earned from MCQ quiz (null if not completed)
     * @param problemStars List of stars earned from problems
     * @return Overall star rating for the level (0-3)
     */
    fun calculateLevelStars(
        mcqStars: Int?,
        problemStars: List<Int>
    ): Int {
        val allStars = mutableListOf<Int>()
        
        mcqStars?.let { allStars.add(it) }
        allStars.addAll(problemStars)
        
        if (allStars.isEmpty()) {
            return StarResult.STARS_FAILED
        }
        
        // Calculate average and round down
        val average = allStars.average()
        return average.toInt().coerceIn(StarResult.MIN_STARS, StarResult.MAX_STARS)
    }
    
    /**
     * Check if section should be unlocked based on stars earned in previous section
     * 
     * @param previousSectionStars Total stars earned in previous section
     * @param previousSectionMaxStars Maximum possible stars in previous section
     * @param requiredPercentage Required percentage of stars (default 50%)
     * @return True if section should be unlocked
     */
    fun canUnlockSection(
        previousSectionStars: Int,
        previousSectionMaxStars: Int,
        requiredPercentage: Double = 0.5
    ): Boolean {
        if (previousSectionMaxStars == 0) return true
        
        val percentage = previousSectionStars.toDouble() / previousSectionMaxStars
        return percentage >= requiredPercentage
    }
    
    /**
     * Get performance tier based on star count
     */
    fun getPerformanceTier(stars: Int): String {
        return when (stars) {
            StarResult.STARS_PERFECT -> "Excellent"
            StarResult.STARS_GOOD -> "Good"
            StarResult.STARS_SLOW -> "Fair"
            else -> "Poor"
        }
    }
    
    /**
     * Get color resource for star display
     * Returns color name from colors.xml
     */
    fun getStarColor(stars: Int): String {
        return when (stars) {
            StarResult.STARS_PERFECT -> "colorSuccess"
            StarResult.STARS_GOOD -> "colorInfo"
            StarResult.STARS_SLOW -> "colorWarning"
            else -> "colorTextSecondary"
        }
    }
}
