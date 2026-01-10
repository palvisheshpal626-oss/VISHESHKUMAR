package com.vishesh.codinglearning.data.repository

import com.vishesh.codinglearning.data.model.Level
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LevelRepositoryImpl : LevelRepository {
    
    private val levels = MutableStateFlow(createInitialLevels())
    
    override fun getLevels(): Flow<List<Level>> = levels
    
    override suspend fun updateLevel(level: Level) {
        levels.update { currentLevels ->
            currentLevels.map { 
                if (it.id == level.id) level else it 
            }
        }
    }
    
    override suspend fun completeLevel(levelId: Int) {
        levels.update { currentLevels ->
            currentLevels.map { level ->
                if (level.id == levelId) {
                    level.copy(isCompleted = true)
                } else {
                    level
                }
            }
        }
    }
    
    private fun createInitialLevels(): List<Level> {
        return (1..20).map { number ->
            Level(
                id = number,
                number = number,
                title = "Level $number",
                isLocked = number > 1,
                isCompleted = false
            )
        }
    }
}
