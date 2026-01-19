package com.visheshkumar.app.ui.compiler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visheshkumar.app.data.model.CodeExecutionResponse
import com.visheshkumar.app.data.repository.CompilerRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for code compiler and execution screen
 */
class CompilerViewModel : ViewModel() {
    
    private val repository = CompilerRepository()
    
    // UI State
    private val _isExecuting = MutableLiveData(false)
    val isExecuting: LiveData<Boolean> = _isExecuting
    
    private val _executionResult = MutableLiveData<String>()
    val executionResult: LiveData<String> = _executionResult
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _supportsExecution = MutableLiveData(true)
    val supportsExecution: LiveData<Boolean> = _supportsExecution
    
    // Current state
    private var currentLanguageId: String = "kotlin"
    private var currentCode: String = ""
    private var currentStdin: String = ""
    
    /**
     * Set the language for code execution
     */
    fun setLanguage(languageId: String) {
        currentLanguageId = languageId
        _supportsExecution.value = repository.supportsExecution(languageId)
        
        // Load default template if no code exists
        if (currentCode.isEmpty()) {
            currentCode = repository.getDefaultCodeTemplate(languageId)
        }
    }
    
    /**
     * Get default code template for current language
     */
    fun getDefaultCodeTemplate(): String {
        return repository.getDefaultCodeTemplate(currentLanguageId)
    }
    
    /**
     * Update the code to be executed
     */
    fun updateCode(code: String) {
        currentCode = code
    }
    
    /**
     * Update the standard input
     */
    fun updateStdin(stdin: String) {
        currentStdin = stdin
    }
    
    /**
     * Execute the current code
     */
    fun executeCode() {
        if (currentCode.isEmpty()) {
            _errorMessage.value = "Please enter some code first"
            return
        }
        
        if (!repository.supportsExecution(currentLanguageId)) {
            _errorMessage.value = "$currentLanguageId does not support code execution. Use learning mode instead."
            return
        }
        
        _isExecuting.value = true
        _errorMessage.value = null
        _executionResult.value = "Executing..."
        
        viewModelScope.launch {
            try {
                val result = repository.executeCode(
                    languageId = currentLanguageId,
                    code = currentCode,
                    stdin = currentStdin
                )
                
                result.fold(
                    onSuccess = { response ->
                        _executionResult.value = response.getDisplayOutput()
                        _isExecuting.value = false
                    },
                    onFailure = { error ->
                        _errorMessage.value = "Error: ${error.message}"
                        _executionResult.value = ""
                        _isExecuting.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected error: ${e.message}"
                _executionResult.value = ""
                _isExecuting.value = false
            }
        }
    }
    
    /**
     * Clear the output
     */
    fun clearOutput() {
        _executionResult.value = ""
        _errorMessage.value = null
    }
    
    /**
     * Reset to default code
     */
    fun resetCode() {
        currentCode = repository.getDefaultCodeTemplate(currentLanguageId)
        clearOutput()
    }
}
