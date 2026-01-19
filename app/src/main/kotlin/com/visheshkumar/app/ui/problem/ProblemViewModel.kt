package com.visheshkumar.app.ui.problem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.visheshkumar.app.data.model.Problem
import com.visheshkumar.app.data.source.ProblemDataSource

/**
 * ViewModel for Problem Solving screen.
 * 
 * Manages problem state, timer, and solution checking.
 */
class ProblemViewModel : ViewModel() {
    
    private val _currentProblem = MutableLiveData<Problem?>()
    val currentProblem: LiveData<Problem?> = _currentProblem
    
    private val _code = MutableLiveData<String>()
    val code: LiveData<String> = _code
    
    private val _output = MutableLiveData<String>()
    val output: LiveData<String> = _output
    
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _startTime = MutableLiveData<Long>(0L)
    val startTime: LiveData<Long> = _startTime
    
    private val _elapsedTime = MutableLiveData<Long>(0L)
    val elapsedTime: LiveData<Long> = _elapsedTime
    
    /**
     * Load a problem by its level ID and problem number
     */
    fun loadProblem(levelId: String, problemNumber: Int) {
        val problemId = "${levelId}_problem_$problemNumber"
        val problem = ProblemDataSource.getProblemById(problemId)
        _currentProblem.value = problem
        
        // Set starter code
        problem?.let {
            _code.value = it.starterCode
        }
    }
    
    /**
     * Update the code being edited
     */
    fun updateCode(newCode: String) {
        _code.value = newCode
    }
    
    /**
     * Reset code to starter template
     */
    fun resetCode() {
        _currentProblem.value?.let { problem ->
            _code.value = problem.starterCode
            _output.value = ""
        }
    }
    
    /**
     * Start the timer
     */
    fun startTimer(currentTime: Long) {
        _startTime.value = currentTime
    }
    
    /**
     * Update elapsed time
     */
    fun updateElapsedTime(elapsed: Long) {
        _elapsedTime.value = elapsed
    }
    
    /**
     * Set loading state
     */
    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
    
    /**
     * Set output text
     */
    fun setOutput(output: String) {
        _output.value = output
    }
    
    /**
     * Clear all state
     */
    fun clear() {
        _currentProblem.value = null
        _code.value = ""
        _output.value = ""
        _isLoading.value = false
        _startTime.value = 0L
        _elapsedTime.value = 0L
    }
}
