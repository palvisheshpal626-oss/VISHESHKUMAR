package com.vishesh.codinglearning.domain.usecase

import com.vishesh.codinglearning.data.UserPreferencesRepository
import com.vishesh.codinglearning.data.model.McqQuestion

class SubmitMcqAnswerUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(question: McqQuestion, selectedAnswerIndex: Int): Boolean {
        val isCorrect = selectedAnswerIndex == question.correctAnswerIndex
        
        // Award 1 coin for correct answer
        if (isCorrect) {
            userPreferencesRepository.addCoins(1)
        }
        
        return isCorrect
    }
}
