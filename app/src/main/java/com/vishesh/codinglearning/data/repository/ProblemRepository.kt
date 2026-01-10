package com.vishesh.codinglearning.data.repository

import com.vishesh.codinglearning.data.model.Difficulty
import com.vishesh.codinglearning.data.model.Problem
import com.vishesh.codinglearning.data.model.ProgrammingLanguage
import kotlinx.coroutines.flow.Flow

interface ProblemRepository {
    fun getProblems(language: ProgrammingLanguage, difficulty: Difficulty? = null): Flow<List<Problem>>
    suspend fun updateProblem(problem: Problem)
}
