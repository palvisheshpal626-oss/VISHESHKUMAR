package com.vishesh.codinglearning.ui.problem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishesh.codinglearning.data.model.Difficulty
import com.vishesh.codinglearning.data.model.Problem
import com.vishesh.codinglearning.data.model.ProgrammingLanguage
import com.vishesh.codinglearning.data.repository.ProblemRepository
import com.vishesh.codinglearning.domain.usecase.SubmitProblemUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProblemViewModel(
    private val problemRepository: ProblemRepository,
    private val submitProblemUseCase: SubmitProblemUseCase,
    private val language: ProgrammingLanguage
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProblemState())
    val uiState: StateFlow<ProblemState> = _uiState.asStateFlow()
    
    init {
        loadProblems()
    }
    
    private fun loadProblems(difficulty: Difficulty? = null) {
        viewModelScope.launch {
            problemRepository.getProblems(language, difficulty).collect { problems ->
                _uiState.update { it.copy(problems = problems) }
            }
        }
    }
    
    fun filterByDifficulty(difficulty: Difficulty?) {
        _uiState.update { it.copy(selectedDifficulty = difficulty) }
        loadProblems(difficulty)
    }
    
    fun selectProblem(problem: Problem) {
        val templateCode = getTemplateCode(problem)
        _uiState.update {
            it.copy(
                selectedProblem = problem,
                code = templateCode,
                submitResult = null
            )
        }
    }
    
    private fun getTemplateCode(problem: Problem): String {
        return when (problem.language) {
            ProgrammingLanguage.PYTHON -> """
                # ${problem.title}
                # Write your solution here
                
                def solution():
                    # Your code here
                    pass
                
                # Read input and call solution
                solution()
            """.trimIndent()
            
            ProgrammingLanguage.JAVA -> """
                import java.util.*;
                
                public class Solution {
                    public static void main(String[] args) {
                        Scanner sc = new Scanner(System.in);
                        // Your code here
                        sc.close();
                    }
                }
            """.trimIndent()
            
            ProgrammingLanguage.C -> """
                #include <stdio.h>
                
                int main() {
                    // Your code here
                    return 0;
                }
            """.trimIndent()
            
            ProgrammingLanguage.CPP -> """
                #include <iostream>
                using namespace std;
                
                int main() {
                    // Your code here
                    return 0;
                }
            """.trimIndent()
        }
    }
    
    fun updateCode(newCode: String) {
        _uiState.update { it.copy(code = newCode) }
    }
    
    fun submitSolution() {
        val problem = _uiState.value.selectedProblem ?: return
        val code = _uiState.value.code
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            
            val result = submitProblemUseCase(problem, code)
            
            _uiState.update {
                it.copy(
                    isSubmitting = false,
                    submitResult = result
                )
            }
            
            // If successful, mark problem as solved
            if (result.success) {
                val updatedProblem = problem.copy(isSolved = true)
                problemRepository.updateProblem(updatedProblem)
            }
        }
    }
    
    fun clearResult() {
        _uiState.update { it.copy(submitResult = null) }
    }
}
