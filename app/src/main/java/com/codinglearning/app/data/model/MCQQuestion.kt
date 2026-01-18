package com.codinglearning.app.data.model

data class MCQQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val hint: String,
    val explanation: String,
    val coinsReward: Int = 10
)
