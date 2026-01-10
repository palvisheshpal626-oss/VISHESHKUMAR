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
    }
    
    fun saveUserProgress(progress: UserProgress) {
        prefs.edit().apply {
            putInt(KEY_COINS, progress.coins)
            putString(KEY_SELECTED_LANGUAGE, progress.selectedLanguage)
            putInt(KEY_HIGHEST_COMPLETED_LEVEL, progress.highestCompletedLevel)
            putStringSet(KEY_COMPLETED_LEVELS, progress.completedLevels.map { it.toString() }.toSet())
            apply()
        }
    }
    
    fun getUserProgress(): UserProgress {
        return UserProgress(
            coins = prefs.getInt(KEY_COINS, 0),
            selectedLanguage = prefs.getString(KEY_SELECTED_LANGUAGE, "") ?: "",
            highestCompletedLevel = prefs.getInt(KEY_HIGHEST_COMPLETED_LEVEL, 0),
            completedLevels = prefs.getStringSet(KEY_COMPLETED_LEVELS, emptySet())
                ?.mapNotNull { it.toIntOrNull() }
                ?.toSet() ?: emptySet()
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
    
    fun completeLevel(levelId: Int) {
        val currentHighest = prefs.getInt(KEY_HIGHEST_COMPLETED_LEVEL, 0)
        if (levelId > currentHighest) {
            prefs.edit().putInt(KEY_HIGHEST_COMPLETED_LEVEL, levelId).apply()
        }
        
        val completedLevels = prefs.getStringSet(KEY_COMPLETED_LEVELS, emptySet())
            ?.toMutableSet() ?: mutableSetOf()
        completedLevels.add(levelId.toString())
        prefs.edit().putStringSet(KEY_COMPLETED_LEVELS, completedLevels).apply()
    }
    
    fun getHighestCompletedLevel(): Int = prefs.getInt(KEY_HIGHEST_COMPLETED_LEVEL, 0)
    
    fun isLevelCompleted(levelId: Int): Boolean {
        val completedLevels = prefs.getStringSet(KEY_COMPLETED_LEVELS, emptySet())
        return completedLevels?.contains(levelId.toString()) ?: false
    }
}
