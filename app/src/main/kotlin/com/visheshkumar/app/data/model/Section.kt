package com.visheshkumar.app.data.model

/**
 * Represents a section within a programming language.
 * Each language has multiple sections covering different topics.
 * 
 * @property id Unique identifier for the section
 * @property languageId ID of the parent language
 * @property sectionNumber Sequential number of this section (1-10)
 * @property title Title of the section
 * @property description Brief description of what this section covers
 */
data class Section(
    val id: String,
    val languageId: String,
    val sectionNumber: Int,
    val title: String,
    val description: String
)
