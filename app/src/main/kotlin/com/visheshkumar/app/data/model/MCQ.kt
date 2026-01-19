package com.visheshkumar.app.data.model

/**
 * Represents a Multiple Choice Question (MCQ) within a level.
 * Each level contains multiple MCQs for learning and assessment.
 * 
 * @property id Unique identifier for the MCQ
 * @property levelId ID of the parent level
 * @property questionNumber Sequential number of this question within the level
 * @property questionText The question text to be displayed
 * @property options List of answer options (typically 4 options)
 * @property correctAnswerIndex Index of the correct answer in the options list (0-based)
 * @property explanation Optional explanation shown after answering
 * @property isPlaceholder Whether this is a placeholder question (true for Phase 3)
 */
data class MCQ(
    val id: String,
    val levelId: String,
    val questionNumber: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String? = null,
    val isPlaceholder: Boolean = false
)
