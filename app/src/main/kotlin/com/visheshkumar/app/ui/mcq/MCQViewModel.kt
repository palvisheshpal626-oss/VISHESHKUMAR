package com.visheshkumar.app.ui.mcq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.visheshkumar.app.data.model.MCQ
import com.visheshkumar.app.data.source.MCQDataSource

/**
 * ViewModel for MCQ screen.
 * Manages the state and business logic for displaying and answering MCQs.
 * 
 * Phase 3: Provides placeholder MCQs for testing the UI structure.
 * Future: Will handle real question logic, scoring, and progress tracking.
 */
class MCQViewModel : ViewModel() {
    
    // Current level ID
    private val _levelId = MutableLiveData<String>()
    val levelId: LiveData<String> = _levelId
    
    // List of MCQs for the current level
    private val _mcqs = MutableLiveData<List<MCQ>>()
    val mcqs: LiveData<List<MCQ>> = _mcqs
    
    // Current question index
    private val _currentQuestionIndex = MutableLiveData<Int>(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex
    
    // Current MCQ being displayed
    private val _currentMCQ = MutableLiveData<MCQ?>()
    val currentMCQ: LiveData<MCQ?> = _currentMCQ
    
    // Selected answer index (null if no answer selected)
    private val _selectedAnswerIndex = MutableLiveData<Int?>()
    val selectedAnswerIndex: LiveData<Int?> = _selectedAnswerIndex
    
    // Whether the answer has been submitted
    private val _isAnswerSubmitted = MutableLiveData<Boolean>(false)
    val isAnswerSubmitted: LiveData<Boolean> = _isAnswerSubmitted
    
    // Score tracking
    private val _correctAnswersCount = MutableLiveData<Int>(0)
    val correctAnswersCount: LiveData<Int> = _correctAnswersCount
    
    /**
     * Load MCQs for a specific level.
     * 
     * @param levelId The ID of the level
     */
    fun loadMCQs(levelId: String) {
        _levelId.value = levelId
        val mcqList = MCQDataSource.getMCQsForLevel(levelId)
        _mcqs.value = mcqList
        
        // Load the first question
        if (mcqList.isNotEmpty()) {
            _currentQuestionIndex.value = 0
            _currentMCQ.value = mcqList[0]
        }
    }
    
    /**
     * Select an answer option.
     * 
     * @param answerIndex The index of the selected answer
     */
    fun selectAnswer(answerIndex: Int) {
        if (_isAnswerSubmitted.value == false) {
            _selectedAnswerIndex.value = answerIndex
        }
    }
    
    /**
     * Submit the selected answer.
     * 
     * @return True if the answer is correct, false otherwise
     */
    fun submitAnswer(): Boolean {
        val selectedIndex = _selectedAnswerIndex.value
        val currentQuestion = _currentMCQ.value
        
        if (selectedIndex == null || currentQuestion == null) {
            return false
        }
        
        _isAnswerSubmitted.value = true
        
        val isCorrect = selectedIndex == currentQuestion.correctAnswerIndex
        if (isCorrect) {
            _correctAnswersCount.value = (_correctAnswersCount.value ?: 0) + 1
        }
        
        return isCorrect
    }
    
    /**
     * Move to the next question.
     * 
     * @return True if there is a next question, false if this was the last question
     */
    fun nextQuestion(): Boolean {
        val mcqList = _mcqs.value ?: return false
        val currentIndex = _currentQuestionIndex.value ?: 0
        
        val nextIndex = currentIndex + 1
        
        return if (nextIndex < mcqList.size) {
            _currentQuestionIndex.value = nextIndex
            _currentMCQ.value = mcqList[nextIndex]
            _selectedAnswerIndex.value = null
            _isAnswerSubmitted.value = false
            true
        } else {
            false
        }
    }
    
    /**
     * Reset the quiz to start from the beginning.
     */
    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        val mcqList = _mcqs.value
        if (!mcqList.isNullOrEmpty()) {
            _currentMCQ.value = mcqList[0]
        }
        _selectedAnswerIndex.value = null
        _isAnswerSubmitted.value = false
        _correctAnswersCount.value = 0
    }
    
    /**
     * Get the total number of questions.
     */
    fun getTotalQuestions(): Int {
        return _mcqs.value?.size ?: 0
    }
    
    /**
     * Get the current progress (1-based).
     */
    fun getCurrentProgress(): Int {
        return (_currentQuestionIndex.value ?: 0) + 1
    }
    
    /**
     * Check if this is the last question.
     */
    fun isLastQuestion(): Boolean {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val totalQuestions = getTotalQuestions()
        return currentIndex == totalQuestions - 1
    }
}
