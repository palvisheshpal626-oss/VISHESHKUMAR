package com.visheshkumar.app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.visheshkumar.app.data.local.PreferencesManager
import com.visheshkumar.app.data.model.LanguageProgress
import com.visheshkumar.app.data.model.UserProfile
import com.visheshkumar.app.data.source.LanguageDataSource

/**
 * ViewModel for Profile screen
 * 
 * Manages user profile data and statistics
 */
class ProfileViewModel : ViewModel() {
    
    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    /**
     * Load user profile data
     */
    fun loadUserProfile(preferencesManager: PreferencesManager) {
        _isLoading.value = true
        
        // Get username (default if not set)
        val username = preferencesManager.getUsername() ?: "Learner"
        
        // Get total coins
        val totalCoins = preferencesManager.getCoins()
        
        // Get total stars
        val totalStarsEarned = preferencesManager.getTotalStarsEarned()
        
        // Calculate language progress for all 20 languages
        val languagesProgress = calculateLanguagesProgress(preferencesManager)
        
        // Calculate total sections and levels completed
        var totalSectionsCompleted = 0
        var totalLevelsCompleted = 0
        
        languagesProgress.forEach { progress ->
            totalSectionsCompleted += progress.sectionsCompleted
            totalLevelsCompleted += progress.levelsCompleted
        }
        
        // Get join date (default to current time if not set)
        val joinDate = preferencesManager.getJoinDate()
        
        // Get last active date (current time)
        val lastActiveDate = System.currentTimeMillis()
        preferencesManager.updateLastActiveDate(lastActiveDate)
        
        // Create user profile
        val profile = UserProfile(
            username = username,
            totalCoins = totalCoins,
            languagesProgress = languagesProgress,
            totalSectionsCompleted = totalSectionsCompleted,
            totalLevelsCompleted = totalLevelsCompleted,
            totalStarsEarned = totalStarsEarned,
            joinDate = joinDate,
            lastActiveDate = lastActiveDate
        )
        
        _userProfile.value = profile
        _isLoading.value = false
    }
    
    /**
     * Calculate progress for all languages
     */
    private fun calculateLanguagesProgress(preferencesManager: PreferencesManager): List<LanguageProgress> {
        val languages = LanguageDataSource.getLanguages()
        
        return languages.map { language ->
            // Count completed sections (unlocked sections)
            val sections = LanguageDataSource.getSectionsForLanguage(language.id)
            var sectionsCompleted = 0
            var levelsCompleted = 0
            var starsEarned = 0
            
            sections.forEach { section ->
                val sectionStars = preferencesManager.getSectionStars(section.id)
                val levels = LanguageDataSource.getLevelsForSection(section.id)
                
                // Count completed levels (levels with stars)
                var sectionLevelsCompleted = 0
                levels.forEach { level ->
                    val levelStars = preferencesManager.getLevelStars(level.id)
                    if (levelStars > 0) {
                        sectionLevelsCompleted++
                        starsEarned += levelStars
                    }
                }
                
                // Section is completed if all its levels are completed
                if (sectionLevelsCompleted == levels.size) {
                    sectionsCompleted++
                }
                
                levelsCompleted += sectionLevelsCompleted
            }
            
            val totalSections = sections.size
            val totalLevels = sections.size * 10 // 10 levels per section
            val maxStars = totalLevels * 3 // 3 stars per level
            
            val completionPercentage = if (totalLevels > 0) {
                (levelsCompleted * 100) / totalLevels
            } else {
                0
            }
            
            LanguageProgress(
                languageId = language.id,
                languageName = language.name,
                sectionsCompleted = sectionsCompleted,
                totalSections = totalSections,
                levelsCompleted = levelsCompleted,
                totalLevels = totalLevels,
                starsEarned = starsEarned,
                maxStars = maxStars,
                completionPercentage = completionPercentage
            )
        }
    }
    
    /**
     * Update username
     */
    fun updateUsername(preferencesManager: PreferencesManager, newUsername: String) {
        preferencesManager.saveUsername(newUsername)
        loadUserProfile(preferencesManager)
    }
    
    /**
     * Reset all progress (for testing or user request)
     */
    fun resetProgress(preferencesManager: PreferencesManager) {
        preferencesManager.clearAllStars()
        preferencesManager.resetCoins()
        loadUserProfile(preferencesManager)
    }
}
