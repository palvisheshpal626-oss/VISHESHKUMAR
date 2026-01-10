package com.vishesh.codinglearning.data.api

import com.vishesh.codinglearning.data.model.ProgrammingLanguage

data class CompilerRequest(
    val code: String,
    val language: ProgrammingLanguage,
    val input: String = ""
)

data class CompilerResponse(
    val output: String?,
    val error: String?,
    val executionTime: Long = 0
)

interface CompilerApi {
    suspend fun executeCode(request: CompilerRequest): Result<CompilerResponse>
}
