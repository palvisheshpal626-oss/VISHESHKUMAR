package com.visheshkumar.app.data.model

/**
 * Represents a level within a section.
 * Each section has multiple levels with increasing difficulty.
 * 
 * @property id Unique identifier for the level
 * @property sectionId ID of the parent section
 * @property levelNumber Sequential number of this level (1-10)
 * @property title Title of the level
 * @property difficulty Difficulty rating (e.g., "Easy", "Medium", "Hard")
 * @property isLocked Whether this level is locked for the user
 */
data class Level(
    val id: String,
    val sectionId: String,
    val levelNumber: Int,
    val title: String,
    val difficulty: String,
    val isLocked: Boolean = true
)
