package com.vishesh.codinglearning.data.repository

import com.vishesh.codinglearning.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class ProblemRepositoryImpl : ProblemRepository {
    
    private val problems = MutableStateFlow(createSampleProblems())
    
    override fun getProblems(language: ProgrammingLanguage, difficulty: Difficulty?): Flow<List<Problem>> {
        return problems.map { allProblems ->
            allProblems.filter { problem ->
                problem.language == language && (difficulty == null || problem.difficulty == difficulty)
            }
        }
    }
    
    override suspend fun updateProblem(problem: Problem) {
        problems.update { currentProblems ->
            currentProblems.map {
                if (it.id == problem.id) problem else it
            }
        }
    }
    
    private fun createSampleProblems(): List<Problem> {
        return listOf(
            Problem(
                id = 1,
                title = "Sum of Two Numbers",
                statement = "Write a program to find the sum of two numbers.",
                inputFormat = "Two integers a and b",
                outputFormat = "Single integer - sum of a and b",
                examples = listOf(
                    ProblemExample(
                        input = "5 3",
                        output = "8",
                        explanation = "5 + 3 = 8"
                    )
                ),
                testCases = listOf(
                    ProblemTestCaseModel("5 3", "8"),
                    ProblemTestCaseModel("10 20", "30"),
                    ProblemTestCaseModel("0 0", "0", isHidden = true)
                ),
                difficulty = Difficulty.EASY,
                language = ProgrammingLanguage.PYTHON,
                isLocked = false
            ),
            Problem(
                id = 2,
                title = "Reverse a String",
                statement = "Write a program to reverse a given string.",
                inputFormat = "A single string",
                outputFormat = "Reversed string",
                examples = listOf(
                    ProblemExample(
                        input = "hello",
                        output = "olleh"
                    )
                ),
                testCases = listOf(
                    ProblemTestCaseModel("hello", "olleh"),
                    ProblemTestCaseModel("world", "dlrow"),
                    ProblemTestCaseModel("a", "a", isHidden = true)
                ),
                difficulty = Difficulty.MEDIUM,
                language = ProgrammingLanguage.PYTHON,
                isLocked = true
            )
        )
    }
}
