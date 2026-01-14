package com.codinglearning.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.codinglearning.app.data.model.UserProgress

class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "coding_learning_prefs"
        private const val KEY_COINS = "coins"
        private const val KEY_SELECTED_LANGUAGE = "selected_language"
        private const val KEY_HIGHEST_COMPLETED_LEVEL = "highest_completed_level"
        private const val KEY_COMPLETED_LEVELS = "completed_levels"
        private const val KEY_TOTAL_STARS = "total_stars"
        private const val KEY_LEVEL_STARS_PREFIX = "level_stars_"
    }
    
    fun saveUserProgress(progress: UserProgress) {
        prefs.edit().apply {
            putInt(KEY_COINS, progress.coins)
            putString(KEY_SELECTED_LANGUAGE, progress.selectedLanguage)
            putInt(KEY_HIGHEST_COMPLETED_LEVEL, progress.highestCompletedLevel)
            putStringSet(KEY_COMPLETED_LEVELS, progress.completedLevels.map { it.toString() }.toSet())
            putInt(KEY_TOTAL_STARS, progress.totalStarsEarned)
            apply()
        }
    }
    
    fun getUserProgress(): UserProgress {
        val levelStarsMap = mutableMapOf<Int, Int>()
        val completedLevels = prefs.getStringSet(KEY_COMPLETED_LEVELS, emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet() ?: emptySet()
        
        // Load stars for all completed levels
        completedLevels.forEach { levelId ->
            val stars = prefs.getInt(KEY_LEVEL_STARS_PREFIX + levelId, 0)
            if (stars > 0) {
                levelStarsMap[levelId] = stars
            }
        }
        
        return UserProgress(
            coins = prefs.getInt(KEY_COINS, 0),
            selectedLanguage = prefs.getString(KEY_SELECTED_LANGUAGE, "") ?: "",
            highestCompletedLevel = prefs.getInt(KEY_HIGHEST_COMPLETED_LEVEL, 0),
            completedLevels = completedLevels,
            totalStarsEarned = prefs.getInt(KEY_TOTAL_STARS, 0),
            levelStars = levelStarsMap
        )
    }
    
    fun addCoins(amount: Int) {
        val currentCoins = prefs.getInt(KEY_COINS, 0)
        prefs.edit().putInt(KEY_COINS, currentCoins + amount).apply()
    }
    
    fun deductCoins(amount: Int): Boolean {
        val currentCoins = prefs.getInt(KEY_COINS, 0)
        if (currentCoins >= amount) {
            prefs.edit().putInt(KEY_COINS, currentCoins - amount).apply()
            return true
        }
        return false
    }
    
    fun getCoins(): Int = prefs.getInt(KEY_COINS, 0)
    
    fun setSelectedLanguage(language: String) {
        prefs.edit().putString(KEY_SELECTED_LANGUAGE, language).apply()
    }
    
    fun getSelectedLanguage(): String = prefs.getString(KEY_SELECTED_LANGUAGE, "") ?: ""
    
    fun completeLevel(levelId: Int, starsEarned: Int = 0) {
        val currentHighest = prefs.getInt(KEY_HIGHEST_COMPLETED_LEVEL, 0)
        if (levelId > currentHighest) {
            prefs.edit().putInt(KEY_HIGHEST_COMPLETED_LEVEL, levelId).apply()
        }
        
        val completedLevels = prefs.getStringSet(KEY_COMPLETED_LEVELS, emptySet())
            ?.toMutableSet() ?: mutableSetOf()
        completedLevels.add(levelId.toString())
        prefs.edit().putStringSet(KEY_COMPLETED_LEVELS, completedLevels).apply()
        
        // Save stars for this level (only if better than previous)
        if (starsEarned > 0) {
            val currentStars = prefs.getInt(KEY_LEVEL_STARS_PREFIX + levelId, 0)
            if (starsEarned > currentStars) {
                prefs.edit().putInt(KEY_LEVEL_STARS_PREFIX + levelId, starsEarned).apply()
                
                // Update total stars
                val starDifference = starsEarned - currentStars
                val currentTotal = prefs.getInt(KEY_TOTAL_STARS, 0)
                prefs.edit().putInt(KEY_TOTAL_STARS, currentTotal + starDifference).apply()
            }
        }
    }
    
    fun getHighestCompletedLevel(): Int = prefs.getInt(KEY_HIGHEST_COMPLETED_LEVEL, 0)
    
    fun isLevelCompleted(levelId: Int): Boolean {
        val completedLevels = prefs.getStringSet(KEY_COMPLETED_LEVELS, emptySet())
        return completedLevels?.contains(levelId.toString()) ?: false
    }
    
    fun getLevelStars(levelId: Int): Int {
        return prefs.getInt(KEY_LEVEL_STARS_PREFIX + levelId, 0)
    }
    
    fun getTotalStars(): Int {
        return prefs.getInt(KEY_TOTAL_STARS, 0)
    }
    
    fun getStarsInSection(levelIds: List<Int>): Int {
        var totalStars = 0
        levelIds.forEach { levelId ->
            totalStars += getLevelStars(levelId)
        }
        return totalStars
    }
}
