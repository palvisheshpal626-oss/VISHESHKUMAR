package com.vishesh.codinglearning.ui.trycode

data class TryCodeState(
    val code: String = "",
    val output: String = "",
    val isRunning: Boolean = false,
    val error: String? = null
)
