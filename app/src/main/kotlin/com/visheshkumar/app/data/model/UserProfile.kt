package com.visheshkumar.app.data.model

/**
 * User profile data model
 * 
 * Represents user information and progress statistics
 */
data class UserProfile(
    val username: String,
    val totalCoins: Int,
    val languagesProgress: List<LanguageProgress>,
    val totalSectionsCompleted: Int,
    val totalLevelsCompleted: Int,
    val totalStarsEarned: Int,
    val joinDate: Long, // Timestamp in milliseconds
    val lastActiveDate: Long // Timestamp in milliseconds
)

/**
 * Language progress data model
 * 
 * Tracks progress for a specific programming language
 */
data class LanguageProgress(
    val languageId: String,
    val languageName: String,
    val sectionsCompleted: Int,
    val totalSections: Int,
    val levelsCompleted: Int,
    val totalLevels: Int,
    val starsEarned: Int,
    val maxStars: Int,
    val completionPercentage: Int // 0-100
) {
    /**
     * Check if language is fully completed
     */
    fun isFullyCompleted(): Boolean {
        return sectionsCompleted == totalSections
    }
    
    /**
     * Get progress emoji based on completion percentage
     */
    fun getProgressEmoji(): String {
        return when {
            completionPercentage >= 100 -> "✅"
            completionPercentage >= 75 -> "🔥"
            completionPercentage >= 50 -> "💪"
            completionPercentage >= 25 -> "📈"
            else -> "🌱"
        }
    }
}
