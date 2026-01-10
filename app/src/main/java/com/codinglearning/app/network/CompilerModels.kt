package com.codinglearning.app.network

data class CodeExecutionRequest(
    val code: String,
    val language: String,
    val input: String = ""
)

data class CodeExecutionResponse(
    val output: String,
    val error: String?,
    val executionTime: Long
)
