package com.visheshkumar.app

import com.visheshkumar.app.data.source.LanguageDataSource

/**
 * Main application class - demonstrates the language structure.
 * This verifies that the language data source is working correctly.
 */
object App {
    
    /**
     * Print summary of the language structure
     */
    fun printStructureSummary() {
        val languages = LanguageDataSource.getLanguages()
        val totalSections = LanguageDataSource.getAllSections().size
        val totalLevels = LanguageDataSource.getAllLevels().size
        
        println("=== VISHESHKUMAR App Structure ===")
        println("Total Languages: ${languages.size}")
        println("Total Sections: $totalSections")
        println("Total Levels: $totalLevels")
        println("\nLanguages:")
        languages.forEach { lang ->
            println("  - ${lang.name} (${lang.id})")
        }
        
        // Verify structure for first language
        val firstLang = languages.first()
        val sections = LanguageDataSource.getSectionsForLanguage(firstLang.id)
        println("\n${firstLang.name} Structure:")
        println("  Sections: ${sections.size}")
        
        val firstSection = sections.first()
        val levels = LanguageDataSource.getLevelsForSection(firstSection.id)
        println("  Levels per section: ${levels.size}")
        println("  Total content items: ${sections.size * levels.size}")
    }
}
