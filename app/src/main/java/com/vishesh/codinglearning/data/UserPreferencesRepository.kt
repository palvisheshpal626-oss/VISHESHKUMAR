package com.vishesh.codinglearning.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vishesh.codinglearning.data.model.ProgrammingLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class UserPreferencesRepository(private val context: Context) {
    
    private object PreferencesKeys {
        val COINS = intPreferencesKey("coins")
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
        val HIGHEST_COMPLETED_LEVEL = intPreferencesKey("highest_completed_level")
    }
    
    val coinsFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.COINS] ?: 0
    }
    
    val selectedLanguageFlow: Flow<ProgrammingLanguage?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.SELECTED_LANGUAGE]?.let { 
            ProgrammingLanguage.valueOf(it)
        }
    }
    
    val highestCompletedLevelFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.HIGHEST_COMPLETED_LEVEL] ?: 0
    }
    
    suspend fun addCoins(amount: Int) {
        context.dataStore.edit { preferences ->
            val currentCoins = preferences[PreferencesKeys.COINS] ?: 0
            preferences[PreferencesKeys.COINS] = currentCoins + amount
        }
    }
    
    suspend fun deductCoins(amount: Int): Boolean {
        var success = false
        context.dataStore.edit { preferences ->
            val currentCoins = preferences[PreferencesKeys.COINS] ?: 0
            if (currentCoins >= amount) {
                preferences[PreferencesKeys.COINS] = currentCoins - amount
                success = true
            }
        }
        return success
    }
    
    suspend fun setSelectedLanguage(language: ProgrammingLanguage) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_LANGUAGE] = language.name
        }
    }
    
    suspend fun setHighestCompletedLevel(level: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.HIGHEST_COMPLETED_LEVEL] ?: 0
            if (level > current) {
                preferences[PreferencesKeys.HIGHEST_COMPLETED_LEVEL] = level
            }
        }
    }
}
