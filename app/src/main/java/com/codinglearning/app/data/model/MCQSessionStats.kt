package com.codinglearning.app.data.model

data class MCQSessionStats(
    val levelId: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val hintsUsed: Int,
    val totalTimeSeconds: Long,
    val questionTimes: List<Long> = emptyList()
) {
    fun calculateStars(): Int {
        // Calculate stars based on performance (0-3 stars)
        var stars = 3
        
        // Deduct for wrong answers
        if (wrongAnswers > 0) stars--
        
        // Deduct for excessive hints
        if (hintsUsed > 1) stars--
        
        // Deduct for slow completion (>5 minutes)
        if (totalTimeSeconds > 300) stars--
        
        // Ensure minimum 0 stars
        return maxOf(0, stars)
    }
    
    fun getAccuracy(): Int {
        return if (totalQuestions > 0) {
            (correctAnswers * 100) / totalQuestions
        } else 0
    }
}
