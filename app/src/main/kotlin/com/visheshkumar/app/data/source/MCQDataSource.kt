package com.visheshkumar.app.data.source

import com.visheshkumar.app.data.model.MCQ

/**
 * Data source for MCQs (Multiple Choice Questions).
 * Provides placeholder MCQs for all levels in the app.
 * 
 * Phase 3: This provides placeholder questions with the text "Question will be added later"
 * Future: Replace placeholders with real questions by updating the question data
 */
object MCQDataSource {
    
    // Number of MCQs per level (can be adjusted)
    private const val MCQS_PER_LEVEL = 5
    
    /**
     * Get all MCQs for a specific level.
     * Currently returns placeholder MCQs.
     * 
     * @param levelId The ID of the level (format: languageId_section_N_level_M)
     * @return List of MCQs for the level (5 questions per level)
     */
    fun getMCQsForLevel(levelId: String): List<MCQ> {
        return (1..MCQS_PER_LEVEL).map { questionNum ->
            createPlaceholderMCQ(levelId, questionNum)
        }
    }
    
    /**
     * Get a specific MCQ by ID.
     * 
     * @param mcqId The ID of the MCQ (format: levelId_mcq_N)
     * @return The MCQ or null if not found
     */
    fun getMCQById(mcqId: String): MCQ? {
        // Parse the mcqId to extract levelId and question number
        // Format: levelId_mcq_N
        val parts = mcqId.split("_mcq_")
        if (parts.size != 2) return null
        
        val levelId = parts[0]
        val questionNum = parts[1].toIntOrNull() ?: return null
        
        if (questionNum !in 1..MCQS_PER_LEVEL) return null
        
        return createPlaceholderMCQ(levelId, questionNum)
    }
    
    /**
     * Get all MCQs across all levels.
     * Warning: This generates 10,000 MCQs (2000 levels × 5 MCQs).
     * Use with caution.
     * 
     * @return List of all MCQs
     */
    fun getAllMCQs(): List<MCQ> {
        return LanguageDataSource.getAllLevels().flatMap { level ->
            getMCQsForLevel(level.id)
        }
    }
    
    /**
     * Create a placeholder MCQ for a specific level and question number.
     * This is used in Phase 3 to provide structure without real content.
     * 
     * @param levelId The ID of the level
     * @param questionNumber The question number (1-5)
     * @return A placeholder MCQ
     */
    private fun createPlaceholderMCQ(levelId: String, questionNumber: Int): MCQ {
        return MCQ(
            id = "${levelId}_mcq_$questionNumber",
            levelId = levelId,
            questionNumber = questionNumber,
            questionText = "Question will be added later",
            options = listOf(
                "Option A will be added later",
                "Option B will be added later",
                "Option C will be added later",
                "Option D will be added later"
            ),
            correctAnswerIndex = 0, // First option by default
            explanation = "Explanation will be added later",
            isPlaceholder = true
        )
    }
    
    /**
     * Helper function to update the number of MCQs per level.
     * This can be used in future to adjust the question count.
     * 
     * Note: Currently returns a constant value. In future, this could be
     * made configurable per language or difficulty level.
     */
    fun getMCQCountForLevel(levelId: String): Int {
        return MCQS_PER_LEVEL
    }
    
    /**
     * Check if a level has MCQs available.
     * Currently always returns true as we provide placeholders.
     * 
     * @param levelId The ID of the level
     * @return True if MCQs are available
     */
    fun hasMCQsForLevel(levelId: String): Boolean {
        return true // Always true for placeholder system
    }
}
