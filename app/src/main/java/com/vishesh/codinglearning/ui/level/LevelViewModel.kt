package com.vishesh.codinglearning.ui.level

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishesh.codinglearning.data.UserPreferencesRepository
import com.vishesh.codinglearning.data.model.Level
import com.vishesh.codinglearning.data.repository.LevelRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LevelViewModel(
    private val levelRepository: LevelRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LevelState())
    val uiState: StateFlow<LevelState> = _uiState.asStateFlow()
    
    init {
        observeLevels()
        observeCoins()
        observeHighestCompletedLevel()
    }
    
    private fun observeLevels() {
        viewModelScope.launch {
            combine(
                levelRepository.getLevels(),
                userPreferencesRepository.highestCompletedLevelFlow
            ) { levels, highestCompleted ->
                // Unlock levels up to highestCompleted + 1
                levels.map { level ->
                    level.copy(
                        isLocked = level.number > highestCompleted + 1
                    )
                }
            }.collect { updatedLevels ->
                _uiState.update { it.copy(levels = updatedLevels) }
            }
        }
    }
    
    private fun observeCoins() {
        viewModelScope.launch {
            userPreferencesRepository.coinsFlow.collect { coins ->
                _uiState.update { it.copy(coins = coins) }
            }
        }
    }
    
    private fun observeHighestCompletedLevel() {
        viewModelScope.launch {
            userPreferencesRepository.highestCompletedLevelFlow.collect { level ->
                _uiState.update { it.copy(highestCompletedLevel = level) }
            }
        }
    }
    
    suspend fun skipLevel(levelNumber: Int, hasEnoughCoins: Boolean): Boolean {
        return if (hasEnoughCoins) {
            // Deduct 5 coins and require ad
            val success = userPreferencesRepository.deductCoins(5)
            if (success) {
                unlockLevel(levelNumber)
                true
            } else {
                false
            }
        } else {
            // User needs to watch 2 rewarded ads
            // This will be handled by the UI
            true
        }
    }
    
    fun onRewardedAdCompleted(levelNumber: Int) {
        viewModelScope.launch {
            unlockLevel(levelNumber)
        }
    }
    
    private suspend fun unlockLevel(levelNumber: Int) {
        userPreferencesRepository.setHighestCompletedLevel(levelNumber - 1)
    }
    
    fun completeLevel(levelNumber: Int) {
        viewModelScope.launch {
            levelRepository.completeLevel(levelNumber)
            userPreferencesRepository.setHighestCompletedLevel(levelNumber)
        }
    }
}
