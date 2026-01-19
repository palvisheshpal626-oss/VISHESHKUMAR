package com.visheshkumar.app.data.model

/**
 * Request model for Piston API code execution
 */
data class CodeExecutionRequest(
    val language: String,
    val version: String,
    val files: List<SourceFile>,
    val stdin: String = "",
    val args: List<String> = emptyList(),
    val compile_timeout: Int = 10000,
    val run_timeout: Int = 3000,
    val compile_memory_limit: Long = -1,
    val run_memory_limit: Long = -1
) {
    data class SourceFile(
        val name: String? = null,
        val content: String
    )
}
