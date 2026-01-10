package com.vishesh.codinglearning.ui.mcq

import com.vishesh.codinglearning.data.model.McqQuestion

data class McqState(
    val questions: List<McqQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val answeredQuestions: Map<Int, Int> = emptyMap(), // questionId to answerIndex
    val correctAnswersCount: Int = 0,
    val coins: Int = 0,
    val showHint: Boolean = false,
    val hintText: String = "",
    val isLoading: Boolean = false,
    val showResult: Boolean = false
) {
    val currentQuestion: McqQuestion?
        get() = questions.getOrNull(currentQuestionIndex)
    
    val isLastQuestion: Boolean
        get() = currentQuestionIndex == questions.size - 1
    
    val totalQuestions: Int
        get() = questions.size
}
