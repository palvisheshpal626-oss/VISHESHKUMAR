package com.vishesh.codinglearning.data.repository

import com.vishesh.codinglearning.data.model.Level
import kotlinx.coroutines.flow.Flow

interface LevelRepository {
    fun getLevels(): Flow<List<Level>>
    suspend fun updateLevel(level: Level)
    suspend fun completeLevel(levelId: Int)
}
