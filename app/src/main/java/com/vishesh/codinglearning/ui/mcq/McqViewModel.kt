package com.vishesh.codinglearning.ui.mcq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishesh.codinglearning.data.UserPreferencesRepository
import com.vishesh.codinglearning.data.repository.McqRepository
import com.vishesh.codinglearning.domain.usecase.SubmitMcqAnswerUseCase
import com.vishesh.codinglearning.domain.usecase.UseHintUseCase
import com.vishesh.codinglearning.domain.usecase.HintResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class McqViewModel(
    private val mcqRepository: McqRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val submitMcqAnswerUseCase: SubmitMcqAnswerUseCase,
    private val useHintUseCase: UseHintUseCase,
    private val levelId: Int
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(McqState())
    val uiState: StateFlow<McqState> = _uiState.asStateFlow()
    
    init {
        loadQuestions()
        observeCoins()
    }
    
    private fun loadQuestions() {
        viewModelScope.launch {
            mcqRepository.getQuestionsForLevel(levelId).collect { questions ->
                _uiState.update { it.copy(questions = questions) }
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
    
    fun onEvent(event: McqEvent) {
        when (event) {
            is McqEvent.SelectAnswer -> selectAnswer(event.answerIndex)
            McqEvent.NextQuestion -> nextQuestion()
            McqEvent.GetHint -> getHint(useCoins = true)
            McqEvent.WatchAdForHint -> getHint(useCoins = false)
        }
    }
    
    private fun selectAnswer(answerIndex: Int) {
        _uiState.update { it.copy(selectedAnswerIndex = answerIndex) }
    }
    
    private fun nextQuestion() {
        val currentState = _uiState.value
        val currentQuestion = currentState.currentQuestion ?: return
        val selectedIndex = currentState.selectedAnswerIndex ?: return
        
        viewModelScope.launch {
            // Submit answer
            val isCorrect = submitMcqAnswerUseCase(currentQuestion, selectedIndex)
            
            val newAnsweredQuestions = currentState.answeredQuestions + 
                (currentQuestion.id to selectedIndex)
            
            val newCorrectCount = if (isCorrect) {
                currentState.correctAnswersCount + 1
            } else {
                currentState.correctAnswersCount
            }
            
            if (currentState.isLastQuestion) {
                // Show result
                _uiState.update {
                    it.copy(
                        answeredQuestions = newAnsweredQuestions,
                        correctAnswersCount = newCorrectCount,
                        showResult = true
                    )
                }
            } else {
                // Move to next question
                _uiState.update {
                    it.copy(
                        currentQuestionIndex = it.currentQuestionIndex + 1,
                        selectedAnswerIndex = null,
                        answeredQuestions = newAnsweredQuestions,
                        correctAnswersCount = newCorrectCount,
                        showHint = false,
                        hintText = ""
                    )
                }
            }
        }
    }
    
    private fun getHint(useCoins: Boolean) {
        val currentQuestion = _uiState.value.currentQuestion ?: return
        
        viewModelScope.launch {
            when (val result = useHintUseCase(currentQuestion.hint, useCoins)) {
                is HintResult.Success -> {
                    _uiState.update {
                        it.copy(
                            showHint = true,
                            hintText = result.hint
                        )
                    }
                }
                HintResult.InsufficientCoins -> {
                    // Show message to watch ad
                    // This will be handled by UI
                }
                HintResult.WatchAdRequired -> {
                    // Trigger rewarded ad
                    // After ad completion, call onHintAdCompleted()
                }
            }
        }
    }
    
    fun onHintAdCompleted() {
        val currentQuestion = _uiState.value.currentQuestion ?: return
        viewModelScope.launch {
            val result = useHintUseCase.applyHintAfterAd(currentQuestion.hint)
            if (result is HintResult.Success) {
                _uiState.update {
                    it.copy(
                        showHint = true,
                        hintText = result.hint
                    )
                }
            }
        }
    }
}
