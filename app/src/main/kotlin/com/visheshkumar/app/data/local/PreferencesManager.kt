package com.visheshkumar.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.visheshkumar.app.data.model.StarResult

/**
 * Manages user preferences, coin system, and star ratings using SharedPreferences.
 * 
 * Phase 4: Implements strict coin rules to prevent guessing:
 * - Correct MCQ: +10 coins
 * - Wrong MCQ: -20 coins
 * - Coins never go below 0
 * - If coins < 20: 1 rewarded ad required
 * - Hint cost: 25 coins
 * 
 * Phase 7: Implements star rating system:
 * - 3 stars: Fast & perfect (no hints, no wrong answers)
 * - 2 stars: Good (acceptable performance)
 * - 1 star: Slow (took too long or multiple mistakes)
 * - 0 stars: Failed (used hints or too many wrong answers)
 * - Stars are saved per level for section unlocking
 */
class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "visheshkumar_prefs"
        private const val KEY_COINS = "user_coins"
        private const val KEY_AD_WATCHED = "ad_watched"
        private const val KEY_STAR_PREFIX = "stars_" // Prefix for star storage: stars_levelId
        private const val KEY_SECTION_UNLOCKED_PREFIX = "section_unlocked_" // section_unlocked_sectionId
        private const val KEY_USERNAME = "username"
        private const val KEY_JOIN_DATE = "join_date"
        private const val KEY_LAST_ACTIVE_DATE = "last_active_date"
        
        // Coin rules
        const val COINS_FOR_CORRECT_ANSWER = 10
        const val COINS_DEDUCTED_FOR_WRONG_ANSWER = 20
        const val MINIMUM_COINS_TO_CONTINUE = 20
        const val HINT_COST = 25
        const val INITIAL_COINS = 100 // Starting coins for new users
    }
    
    /**
     * Get current coin balance.
     * @return Current number of coins
     */
    fun getCoins(): Int {
        return prefs.getInt(KEY_COINS, INITIAL_COINS)
    }
    
    /**
     * Set coin balance (private - use specific methods instead).
     * Ensures coins never go below 0.
     */
    private fun setCoins(coins: Int) {
        val safeCoins = maxOf(0, coins) // Never allow negative coins
        prefs.edit().putInt(KEY_COINS, safeCoins).apply()
    }
    
    /**
     * Add coins for correct answer.
     * @return New coin balance
     */
    fun addCoinsForCorrectAnswer(): Int {
        val currentCoins = getCoins()
        val newCoins = currentCoins + COINS_FOR_CORRECT_ANSWER
        setCoins(newCoins)
        return newCoins
    }
    
    /**
     * Deduct coins for wrong answer.
     * @return New coin balance (never goes below 0)
     */
    fun deductCoinsForWrongAnswer(): Int {
        val currentCoins = getCoins()
        val newCoins = currentCoins - COINS_DEDUCTED_FOR_WRONG_ANSWER
        setCoins(newCoins)
        return maxOf(0, newCoins)
    }
    
    /**
     * Check if user has enough coins to continue (minimum 20).
     * @return True if coins >= 20, false otherwise
     */
    fun hasEnoughCoinsToC ontinue(): Boolean {
        return getCoins() >= MINIMUM_COINS_TO_CONTINUE
    }
    
    /**
     * Check if user has enough coins to use hint (25 coins).
     * @return True if coins >= 25, false otherwise
     */
    fun hasEnoughCoinsForHint(): Boolean {
        return getCoins() >= HINT_COST
    }
    
    /**
     * Deduct coins for using a hint.
     * @return New coin balance, or -1 if not enough coins
     */
    fun useHint(): Int {
        if (!hasEnoughCoinsForHint()) {
            return -1 // Not enough coins
        }
        
        val currentCoins = getCoins()
        val newCoins = currentCoins - HINT_COST
        setCoins(newCoins)
        return newCoins
    }
    
    /**
     * Mark that user has watched a rewarded ad.
     * This allows them to continue if they had less than 20 coins.
     */
    fun markAdWatched() {
        prefs.edit().putBoolean(KEY_AD_WATCHED, true).apply()
    }
    
    /**
     * Check if user has watched ad (allows bypass of coin requirement).
     * @return True if ad was watched, false otherwise
     */
    fun hasWatchedAd(): Boolean {
        return prefs.getBoolean(KEY_AD_WATCHED, false)
    }
    
    /**
     * Reset ad watched status (e.g., after user gets more coins).
     */
    fun resetAdWatched() {
        prefs.edit().putBoolean(KEY_AD_WATCHED, false).apply()
    }
    
    /**
     * Check if user needs to watch an ad to continue.
     * Required when coins < 20 and ad not yet watched.
     */
    fun needsToWatchAd(): Boolean {
        return !hasEnoughCoinsToC ontinue() && !hasWatchedAd()
    }
    
    /**
     * Add coins manually (e.g., for testing or bonus rewards).
     * @param amount Number of coins to add
     * @return New coin balance
     */
    fun addCoins(amount: Int): Int {
        val currentCoins = getCoins()
        val newCoins = currentCoins + amount
        setCoins(newCoins)
        return newCoins
    }
    
    /**
     * Reset all coin-related data (for testing or reset feature).
     */
    fun resetCoins() {
        setCoins(INITIAL_COINS)
        resetAdWatched()
    }
    
    /**
     * Get formatted coin display string.
     * @return String like "50 coins" or "1 coin"
     */
    fun getCoinsDisplayString(): String {
        val coins = getCoins()
        return if (coins == 1) "$coins coin" else "$coins coins"
    }
    
    // ========================================
    // Phase 7: Star Rating System
    // ========================================
    
    /**
     * Save star rating for a level.
     * @param levelId The level ID (e.g., "kotlin_section_1_level_1")
     * @param stars Number of stars earned (0-3)
     */
    fun saveLevelStars(levelId: String, stars: Int) {
        require(stars in StarResult.MIN_STARS..StarResult.MAX_STARS) {
            "Stars must be between ${StarResult.MIN_STARS} and ${StarResult.MAX_STARS}"
        }
        
        val key = KEY_STAR_PREFIX + levelId
        prefs.edit().putInt(key, stars).apply()
    }
    
    /**
     * Get star rating for a level.
     * @param levelId The level ID
     * @return Number of stars earned, or -1 if level not completed
     */
    fun getLevelStars(levelId: String): Int {
        val key = KEY_STAR_PREFIX + levelId
        return prefs.getInt(key, -1)
    }
    
    /**
     * Check if a level has been completed (has star rating).
     * @param levelId The level ID
     * @return True if level has been completed
     */
    fun isLevelCompleted(levelId: String): Boolean {
        return getLevelStars(levelId) >= 0
    }
    
    /**
     * Get total stars earned in a section.
     * @param sectionId The section ID (e.g., "kotlin_section_1")
     * @param levelsInSection Number of levels in the section (default 10)
     * @return Total stars earned across all levels in the section
     */
    fun getSectionStars(sectionId: String, levelsInSection: Int = 10): Int {
        var totalStars = 0
        
        for (levelNumber in 1..levelsInSection) {
            val levelId = "${sectionId}_level_$levelNumber"
            val stars = getLevelStars(levelId)
            if (stars >= 0) {
                totalStars += stars
            }
        }
        
        return totalStars
    }
    
    /**
     * Check if a section is unlocked.
     * First section is always unlocked.
     * @param sectionId The section ID to check
     * @return True if section is unlocked
     */
    fun isSectionUnlocked(sectionId: String): Boolean {
        // First section of any language is always unlocked
        if (sectionId.endsWith("_section_1")) {
            return true
        }
        
        val key = KEY_SECTION_UNLOCKED_PREFIX + sectionId
        return prefs.getBoolean(key, false)
    }
    
    /**
     * Unlock a section.
     * @param sectionId The section ID to unlock
     */
    fun unlockSection(sectionId: String) {
        val key = KEY_SECTION_UNLOCKED_PREFIX + sectionId
        prefs.edit().putBoolean(key, true).apply()
    }
    
    /**
     * Check if next section can be unlocked based on current section stars.
     * Requires at least 50% of possible stars.
     * @param currentSectionId Current section ID
     * @param nextSectionId Next section ID
     * @param levelsInSection Number of levels in the section (default 10)
     * @return True if next section can be unlocked
     */
    fun canUnlockNextSection(
        currentSectionId: String,
        nextSectionId: String,
        levelsInSection: Int = 10
    ): Boolean {
        // If already unlocked, return true
        if (isSectionUnlocked(nextSectionId)) {
            return true
        }
        
        // Calculate stars earned in current section
        val starsEarned = getSectionStars(currentSectionId, levelsInSection)
        val maxPossibleStars = levelsInSection * StarResult.MAX_STARS
        
        // Require at least 50% of possible stars
        val requiredStars = (maxPossibleStars * 0.5).toInt()
        
        return starsEarned >= requiredStars
    }
    
    /**
     * Get completion percentage for a section (0-100).
     * @param sectionId The section ID
     * @param levelsInSection Number of levels in the section (default 10)
     * @return Completion percentage as an integer (0-100)
     */
    fun getSectionCompletionPercentage(sectionId: String, levelsInSection: Int = 10): Int {
        var completedLevels = 0
        
        for (levelNumber in 1..levelsInSection) {
            val levelId = "${sectionId}_level_$levelNumber"
            if (isLevelCompleted(levelId)) {
                completedLevels++
            }
        }
        
        return (completedLevels * 100) / levelsInSection
    }
    
    /**
     * Get total stars earned across all languages.
     * @return Total number of stars earned
     */
    fun getTotalStarsEarned(): Int {
        var total = 0
        
        // Iterate through all preferences looking for star entries
        for ((key, value) in prefs.all) {
            if (key.startsWith(KEY_STAR_PREFIX) && value is Int && value >= 0) {
                total += value
            }
        }
        
        return total
    }
    
    /**
     * Clear all star data (for testing or reset).
     */
    fun clearAllStars() {
        val editor = prefs.edit()
        
        // Remove all star entries
        for (key in prefs.all.keys) {
            if (key.startsWith(KEY_STAR_PREFIX) || key.startsWith(KEY_SECTION_UNLOCKED_PREFIX)) {
                editor.remove(key)
            }
        }
        
        editor.apply()
    }
    
    /**
     * Get star emoji representation for a level.
     * @param levelId The level ID
     * @return Emoji string like "⭐⭐⭐" or "☆☆☆"
     */
    fun getLevelStarsEmoji(levelId: String): String {
        val stars = getLevelStars(levelId)
        
        return when {
            stars < 0 -> "☆☆☆" // Not completed
            stars == StarResult.STARS_PERFECT -> "⭐⭐⭐"
            stars == StarResult.STARS_GOOD -> "⭐⭐☆"
            stars == StarResult.STARS_SLOW -> "⭐☆☆"
            else -> "☆☆☆"
        }
    }
    
    // ========== PROFILE SYSTEM METHODS (Phase 8) ==========
    
    /**
     * Get username
     * 
     * @return Username or null if not set
     */
    fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }
    
    /**
     * Save username
     * 
     * @param username The username to save
     */
    fun saveUsername(username: String) {
        prefs.edit().putString(KEY_USERNAME, username).apply()
    }
    
    /**
     * Get join date (when user first started using the app)
     * 
     * @return Join date timestamp in milliseconds
     */
    fun getJoinDate(): Long {
        val joinDate = prefs.getLong(KEY_JOIN_DATE, 0L)
        
        // If not set, set it now
        if (joinDate == 0L) {
            val now = System.currentTimeMillis()
            prefs.edit().putLong(KEY_JOIN_DATE, now).apply()
            return now
        }
        
        return joinDate
    }
    
    /**
     * Update last active date
     * 
     * @param timestamp The timestamp in milliseconds
     */
    fun updateLastActiveDate(timestamp: Long) {
        prefs.edit().putLong(KEY_LAST_ACTIVE_DATE, timestamp).apply()
    }
    
    /**
     * Get last active date
     * 
     * @return Last active date timestamp in milliseconds
     */
    fun getLastActiveDate(): Long {
        return prefs.getLong(KEY_LAST_ACTIVE_DATE, System.currentTimeMillis())
    }
}
