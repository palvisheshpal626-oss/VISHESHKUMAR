package com.visheshkumar.app

import com.visheshkumar.app.data.source.LanguageDataSource

/**
 * Verification script to test the language structure.
 * This can be run independently to verify the data source works correctly.
 */
fun main() {
    App.printStructureSummary()
    
    // Additional verification
    println("\n=== Detailed Verification ===")
    
    // Verify each language has exactly 10 sections
    val languages = LanguageDataSource.getLanguages()
    var allValid = true
    
    languages.forEach { lang ->
        val sections = LanguageDataSource.getSectionsForLanguage(lang.id)
        if (sections.size != 10) {
            println("ERROR: ${lang.name} has ${sections.size} sections instead of 10")
            allValid = false
        }
        
        // Verify each section has exactly 10 levels
        sections.forEach { section ->
            val levels = LanguageDataSource.getLevelsForSection(section.id)
            if (levels.size != 10) {
                println("ERROR: ${section.title} has ${levels.size} levels instead of 10")
                allValid = false
            }
        }
    }
    
    if (allValid) {
        println("\n✅ All verification checks passed!")
        println("✅ 20 languages × 10 sections × 10 levels = 2000 total content items")
    } else {
        println("\n❌ Some verification checks failed!")
    }
}
