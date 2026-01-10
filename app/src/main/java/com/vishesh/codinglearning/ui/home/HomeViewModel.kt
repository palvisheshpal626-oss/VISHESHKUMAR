package com.vishesh.codinglearning.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishesh.codinglearning.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        observeCoins()
    }
    
    private fun observeCoins() {
        viewModelScope.launch {
            userPreferencesRepository.coinsFlow.collect { coins ->
                _uiState.update { it.copy(coins = coins) }
            }
        }
    }
    
    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.StartLearning -> {
                // Navigation will be handled by the composable
            }
            HomeEvent.DailyChallenge -> {
                handleDailyChallenge()
            }
            HomeEvent.GetFreeCoins -> {
                // Trigger rewarded ad - handled by composable
            }
        }
    }
    
    private fun handleDailyChallenge() {
        viewModelScope.launch {
            // Award bonus coins for daily challenge
            userPreferencesRepository.addCoins(5)
        }
    }
    
    fun onRewardedAdCompleted() {
        viewModelScope.launch {
            // Award 3 coins after watching rewarded ad
            userPreferencesRepository.addCoins(3)
        }
    }
}
