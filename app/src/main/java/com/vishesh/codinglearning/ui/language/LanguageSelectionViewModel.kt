package com.vishesh.codinglearning.ui.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishesh.codinglearning.data.UserPreferencesRepository
import com.vishesh.codinglearning.data.model.ProgrammingLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LanguageSelectionViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LanguageUiState())
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()
    
    init {
        observeSelectedLanguage()
    }
    
    private fun observeSelectedLanguage() {
        viewModelScope.launch {
            userPreferencesRepository.selectedLanguageFlow.collect { language ->
                _uiState.update { it.copy(selectedLanguage = language) }
            }
        }
    }
    
    fun selectLanguage(language: ProgrammingLanguage) {
        viewModelScope.launch {
            userPreferencesRepository.setSelectedLanguage(language)
            _uiState.update { it.copy(selectedLanguage = language) }
        }
    }
}
