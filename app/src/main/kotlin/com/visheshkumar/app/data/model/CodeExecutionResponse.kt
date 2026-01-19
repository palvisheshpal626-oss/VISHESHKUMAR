package com.visheshkumar.app.data.model

/**
 * Response model from Piston API after code execution
 */
data class CodeExecutionResponse(
    val language: String,
    val version: String,
    val run: ExecutionResult,
    val compile: ExecutionResult? = null
) {
    data class ExecutionResult(
        val stdout: String,
        val stderr: String,
        val code: Int,
        val signal: String? = null,
        val output: String
    )
    
    /**
     * Check if execution was successful
     */
    fun isSuccess(): Boolean {
        return run.code == 0 && run.stderr.isEmpty()
    }
    
    /**
     * Get the output to display to user
     */
    fun getDisplayOutput(): String {
        return when {
            // Compilation error
            compile != null && compile.code != 0 -> {
                "Compilation Error:\n${compile.stderr.ifEmpty { compile.stdout }}"
            }
            // Runtime error
            run.code != 0 -> {
                "Runtime Error:\n${run.stderr.ifEmpty { run.stdout }}"
            }
            // Success with stderr (warnings)
            run.stderr.isNotEmpty() -> {
                "Output:\n${run.stdout}\n\nWarnings:\n${run.stderr}"
            }
            // Success
            else -> {
                "Output:\n${run.stdout.ifEmpty { "(No output)" }}"
            }
        }
    }
}
