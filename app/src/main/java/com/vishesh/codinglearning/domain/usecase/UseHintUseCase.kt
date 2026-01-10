package com.vishesh.codinglearning.domain.usecase

import com.vishesh.codinglearning.data.UserPreferencesRepository

sealed class HintResult {
    data class Success(val hint: String) : HintResult()
    object InsufficientCoins : HintResult()
    object WatchAdRequired : HintResult()
}

class UseHintUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(hint: String, useCoins: Boolean): HintResult {
        return if (useCoins) {
            val success = userPreferencesRepository.deductCoins(2)
            if (success) {
                HintResult.Success(hint)
            } else {
                HintResult.InsufficientCoins
            }
        } else {
            // User chose to watch ad for hint
            HintResult.WatchAdRequired
        }
    }
    
    suspend fun applyHintAfterAd(hint: String): HintResult {
        return HintResult.Success(hint)
    }
}
