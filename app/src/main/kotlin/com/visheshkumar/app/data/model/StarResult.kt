package com.visheshkumar.app.data.model

/**
 * Star rating result for a level
 * Used to track user performance and unlock sections
 */
data class StarResult(
    val levelId: String,
    val stars: Int, // 0-3 stars
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        const val STARS_PERFECT = 3  // Fast & perfect (no hints, no wrong answers)
        const val STARS_GOOD = 2     // Good (some mistakes but acceptable)
        const val STARS_SLOW = 1     // Slow (took too long or multiple attempts)
        const val STARS_FAILED = 0   // Failed (used hints or too many wrong answers)
        
        const val MIN_STARS = 0
        const val MAX_STARS = 3
    }
    
    init {
        require(stars in MIN_STARS..MAX_STARS) {
            "Stars must be between $MIN_STARS and $MAX_STARS, got $stars"
        }
    }
    
    /**
     * Check if this result allows unlocking next section
     * Typically requires at least 1 star
     */
    fun canUnlockNext(): Boolean = stars >= STARS_SLOW
    
    /**
     * Check if this is a perfect score
     */
    fun isPerfect(): Boolean = stars == STARS_PERFECT
    
    /**
     * Get emoji representation of stars
     */
    fun toEmoji(): String = when (stars) {
        STARS_PERFECT -> "⭐⭐⭐"
        STARS_GOOD -> "⭐⭐"
        STARS_SLOW -> "⭐"
        else -> "☆☆☆"
    }
    
    /**
     * Get description of performance
     */
    fun getPerformanceDescription(): String = when (stars) {
        STARS_PERFECT -> "Perfect! Fast and accurate!"
        STARS_GOOD -> "Good job! Keep improving!"
        STARS_SLOW -> "You can do better! Try again!"
        else -> "Keep practicing!"
    }
}
