package com.vishesh.codinglearning.data.repository

import com.vishesh.codinglearning.data.model.McqQuestion
import kotlinx.coroutines.flow.Flow

interface McqRepository {
    fun getQuestionsForLevel(levelId: Int): Flow<List<McqQuestion>>
}
