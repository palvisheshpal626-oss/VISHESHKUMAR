package com.vishesh.codinglearning.domain.usecase

import com.vishesh.codinglearning.data.UserPreferencesRepository
import com.vishesh.codinglearning.data.api.CompilerRequest
import com.vishesh.codinglearning.data.model.Problem
import com.vishesh.codinglearning.data.model.ProblemSubmitResult
import com.vishesh.codinglearning.data.model.ProblemTestCaseModel
import com.vishesh.codinglearning.data.repository.CompilerRepository

class SubmitProblemUseCase(
    private val compilerRepository: CompilerRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(problem: Problem, code: String): ProblemSubmitResult {
        val testCases = problem.testCases
        var passedCount = 0
        
        for (testCase in testCases) {
            val request = CompilerRequest(
                code = code,
                language = problem.language,
                input = testCase.input
            )
            
            val result = compilerRepository.runCode(request)
            
            result.fold(
                onSuccess = { response ->
                    if (response.error != null) {
                        // Compilation/runtime error
                        return ProblemSubmitResult(
                            success = false,
                            totalTestCases = testCases.size,
                            passedTestCases = passedCount,
                            failedTestCase = testCase,
                            error = response.error
                        )
                    }
                    
                    val output = response.output?.trim() ?: ""
                    val expected = testCase.expectedOutput.trim()
                    
                    if (output == expected) {
                        passedCount++
                    } else {
                        // Test case failed
                        return ProblemSubmitResult(
                            success = false,
                            totalTestCases = testCases.size,
                            passedTestCases = passedCount,
                            failedTestCase = testCase,
                            actualOutput = output
                        )
                    }
                },
                onFailure = { error ->
                    return ProblemSubmitResult(
                        success = false,
                        totalTestCases = testCases.size,
                        passedTestCases = passedCount,
                        error = error.message
                    )
                }
            )
        }
        
        // All test cases passed
        val coinsAwarded = when (problem.difficulty) {
            com.vishesh.codinglearning.data.model.Difficulty.EASY -> 5
            com.vishesh.codinglearning.data.model.Difficulty.MEDIUM -> 10
            com.vishesh.codinglearning.data.model.Difficulty.HARD -> 15
        }
        
        userPreferencesRepository.addCoins(coinsAwarded)
        
        return ProblemSubmitResult(
            success = true,
            totalTestCases = testCases.size,
            passedTestCases = passedCount
        )
    }
}
