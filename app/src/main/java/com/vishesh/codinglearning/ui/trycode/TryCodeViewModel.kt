package com.vishesh.codinglearning.ui.trycode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishesh.codinglearning.data.api.CompilerRequest
import com.vishesh.codinglearning.data.model.ProgrammingLanguage
import com.vishesh.codinglearning.domain.usecase.RunCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TryCodeViewModel(
    private val runCodeUseCase: RunCodeUseCase,
    private val language: ProgrammingLanguage
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TryCodeState())
    val uiState: StateFlow<TryCodeState> = _uiState.asStateFlow()
    
    init {
        loadExampleCode()
    }
    
    private fun loadExampleCode() {
        val exampleCode = when (language) {
            ProgrammingLanguage.PYTHON -> """
                # Simple Python example
                print("Hello, World!")
                
                # Variables and arithmetic
                x = 5
                y = 3
                print(f"Sum: {x + y}")
            """.trimIndent()
            
            ProgrammingLanguage.JAVA -> """
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("Hello, World!");
                    }
                }
            """.trimIndent()
            
            ProgrammingLanguage.C -> """
                #include <stdio.h>
                
                int main() {
                    printf("Hello, World!\n");
                    return 0;
                }
            """.trimIndent()
            
            ProgrammingLanguage.CPP -> """
                #include <iostream>
                using namespace std;
                
                int main() {
                    cout << "Hello, World!" << endl;
                    return 0;
                }
            """.trimIndent()
        }
        
        _uiState.update { it.copy(code = exampleCode) }
    }
    
    fun updateCode(newCode: String) {
        _uiState.update { it.copy(code = newCode) }
    }
    
    fun runCode() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRunning = true, output = "", error = null) }
            
            val request = CompilerRequest(
                code = _uiState.value.code,
                language = language
            )
            
            val result = runCodeUseCase(request)
            
            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRunning = false,
                            output = response.output ?: "",
                            error = response.error
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isRunning = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }
}
