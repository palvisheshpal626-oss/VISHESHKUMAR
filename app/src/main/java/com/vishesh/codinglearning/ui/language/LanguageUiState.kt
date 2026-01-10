package com.vishesh.codinglearning.ui.language

import com.vishesh.codinglearning.data.model.ProgrammingLanguage

data class LanguageUiState(
    val selectedLanguage: ProgrammingLanguage? = null,
    val isLoading: Boolean = false
)
