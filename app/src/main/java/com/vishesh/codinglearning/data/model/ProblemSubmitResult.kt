package com.vishesh.codinglearning.data.model

data class ProblemSubmitResult(
    val success: Boolean,
    val totalTestCases: Int,
    val passedTestCases: Int,
    val failedTestCase: ProblemTestCaseModel? = null,
    val actualOutput: String? = null,
    val error: String? = null
)
