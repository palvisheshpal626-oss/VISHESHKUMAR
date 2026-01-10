package com.vishesh.codinglearning.data.model

data class Level(
    val id: Int,
    val number: Int,
    val title: String,
    val isLocked: Boolean = true,
    val isCompleted: Boolean = false,
    val mcqCompleted: Boolean = false,
    val codeRunCompleted: Boolean = false,
    val problemSolved: Boolean = false
)
