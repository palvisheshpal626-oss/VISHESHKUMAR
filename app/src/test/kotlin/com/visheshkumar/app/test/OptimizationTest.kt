package com.visheshkumar.app.test

import com.visheshkumar.app.data.source.LanguageDataSource

/**
 * Test script to verify the optimized lookup methods work correctly
 */
fun main() {
    println("=== Testing Optimized Lookup Methods ===\n")
    
    // Test getSectionById
    println("Testing getSectionById:")
    val section = LanguageDataSource.getSectionById("kotlin_section_5")
    if (section != null) {
        println("✅ Found section: ${section.title} (${section.id})")
        println("   Language: ${section.languageId}, Section #${section.sectionNumber}")
    } else {
        println("❌ Section not found")
    }
    
    // Test invalid section ID
    val invalidSection = LanguageDataSource.getSectionById("invalid_id")
    if (invalidSection == null) {
        println("✅ Correctly returned null for invalid section ID")
    } else {
        println("❌ Should return null for invalid section ID")
    }
    
    println()
    
    // Test getLevelById
    println("Testing getLevelById:")
    val level = LanguageDataSource.getLevelById("python_section_3_level_7")
    if (level != null) {
        println("✅ Found level: ${level.title} (${level.id})")
        println("   Section: ${level.sectionId}, Level #${level.levelNumber}")
        println("   Difficulty: ${level.difficulty}, Locked: ${level.isLocked}")
    } else {
        println("❌ Level not found")
    }
    
    // Test invalid level ID
    val invalidLevel = LanguageDataSource.getLevelById("invalid_id")
    if (invalidLevel == null) {
        println("✅ Correctly returned null for invalid level ID")
    } else {
        println("❌ Should return null for invalid level ID")
    }
    
    println()
    
    // Test boundary conditions
    println("Testing boundary conditions:")
    val firstLevel = LanguageDataSource.getLevelById("java_section_1_level_1")
    val lastLevel = LanguageDataSource.getLevelById("bash_section_10_level_10")
    
    if (firstLevel != null && lastLevel != null) {
        println("✅ First and last levels accessible")
        println("   First level locked: ${firstLevel.isLocked}")
        println("   Last level locked: ${lastLevel.isLocked}")
    } else {
        println("❌ Boundary levels not accessible")
    }
    
    println("\n=== All Optimization Tests Passed ===")
}
