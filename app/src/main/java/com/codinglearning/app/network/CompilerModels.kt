package com.codinglearning.app.network

import com.google.gson.annotations.SerializedName

// Piston API Request Models
data class PistonExecuteRequest(
    val language: String,
    val version: String,
    val files: List<PistonFile>,
    val stdin: String = "",
    val args: List<String> = emptyList(),
    val compile_timeout: Int = 10000,
    val run_timeout: Int = 3000,
    val compile_memory_limit: Long = -1,
    val run_memory_limit: Long = -1
)

data class PistonFile(
    val name: String? = null,
    val content: String
)

// Piston API Response Models
data class PistonExecuteResponse(
    val language: String,
    val version: String,
    val run: PistonRunResult,
    val compile: PistonCompileResult? = null
)

data class PistonRunResult(
    val stdout: String,
    val stderr: String,
    val output: String,
    val code: Int,
    val signal: String?
)

data class PistonCompileResult(
    val stdout: String,
    val stderr: String,
    val output: String,
    val code: Int,
    val signal: String?
)

// Language mapping data
data class PistonRuntime(
    val language: String,
    val version: String,
    val aliases: List<String>
)

// Simplified models for app usage
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
