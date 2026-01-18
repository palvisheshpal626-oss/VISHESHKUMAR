package com.visheshkumar.app.data.model

/**
 * Represents a programming language in the app.
 * 
 * @property id Unique identifier for the language
 * @property name Display name of the programming language
 * @property description Brief description of the language
 * @property iconResId Resource ID for the language icon (optional)
 */
data class Language(
    val id: String,
    val name: String,
    val description: String,
    val iconResId: Int? = null
)
