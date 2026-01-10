package com.vishesh.codinglearning.data.model

data class McqQuestion(
    val id: Int,
    val levelId: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val hint: String,
    val explanation: String
)
