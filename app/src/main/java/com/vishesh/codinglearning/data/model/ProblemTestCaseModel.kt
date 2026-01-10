package com.vishesh.codinglearning.data.model

data class ProblemTestCaseModel(
    val input: String,
    val expectedOutput: String,
    val isHidden: Boolean = false
)
