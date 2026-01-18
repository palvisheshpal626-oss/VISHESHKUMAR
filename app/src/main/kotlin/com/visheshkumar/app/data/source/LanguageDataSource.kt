package com.visheshkumar.app.data.source

import com.visheshkumar.app.data.model.Language
import com.visheshkumar.app.data.model.Section
import com.visheshkumar.app.data.model.Level

/**
 * Data source for programming languages, sections, and levels.
 * Provides structured data for 20 programming languages.
 */
object LanguageDataSource {
    
    // Cached list of all languages
    private val languages = listOf(
        Language("kotlin", "Kotlin", "Modern, concise JVM language for Android & backend"),
        Language("java", "Java", "Enterprise-grade, object-oriented programming language"),
        Language("python", "Python", "High-level language for data science, web & automation"),
        Language("javascript", "JavaScript", "Dynamic language for web development"),
        Language("typescript", "TypeScript", "Typed superset of JavaScript"),
        Language("cpp", "C++", "High-performance systems programming language"),
        Language("c", "C", "Low-level procedural programming language"),
        Language("csharp", "C#", "Modern language for .NET ecosystem"),
        Language("swift", "Swift", "Apple's language for iOS & macOS development"),
        Language("go", "Go", "Google's fast, simple concurrent programming language"),
        Language("rust", "Rust", "Memory-safe systems programming language"),
        Language("php", "PHP", "Server-side scripting language for web"),
        Language("ruby", "Ruby", "Dynamic, elegant language with Rails framework"),
        Language("dart", "Dart", "Language for Flutter cross-platform development"),
        Language("r", "R", "Statistical computing and graphics language"),
        Language("scala", "Scala", "Functional & object-oriented JVM language"),
        Language("sql", "SQL", "Database query and management language"),
        Language("html", "HTML", "Markup language for web page structure"),
        Language("css", "CSS", "Style sheet language for web design"),
        Language("bash", "Bash", "Unix shell scripting language")
    )
    
    // Cached map for O(1) language lookups
    private val languageMap = languages.associateBy { it.id }
    
    /**
     * Get all available programming languages.
     * @return List of 20 programming languages
     */
    fun getLanguages(): List<Language> = languages
    
    /**
     * Get all sections for a specific language.
     * @param languageId The ID of the language
     * @return List of 10 sections for the language, or empty list if language doesn't exist
     */
    fun getSectionsForLanguage(languageId: String): List<Section> {
        // Validate language exists and cache the name
        val language = getLanguageById(languageId) ?: return emptyList()
        val languageName = language.name
        
        return (1..10).map { sectionNum ->
            Section(
                id = "${languageId}_section_$sectionNum",
                languageId = languageId,
                sectionNumber = sectionNum,
                title = "Section $sectionNum",
                description = "Learn $languageName - Part $sectionNum"
            )
        }
    }
    
    /**
     * Get all levels for a specific section.
     * @param sectionId The ID of the section
     * @return List of 10 levels for the section
     */
    fun getLevelsForSection(sectionId: String): List<Level> {
        return (1..10).map { levelNum ->
            Level(
                id = "${sectionId}_level_$levelNum",
                sectionId = sectionId,
                levelNumber = levelNum,
                title = "Level $levelNum",
                difficulty = getDifficultyForLevel(levelNum),
                isLocked = levelNum > 1 // First level unlocked, rest locked
            )
        }
    }
    
    /**
     * Get all sections across all languages.
     * Optimized to generate sections directly without redundant validations.
     * @return List of all sections (200 total: 20 languages × 10 sections)
     */
    fun getAllSections(): List<Section> {
        return languages.flatMap { language ->
            (1..10).map { sectionNum ->
                Section(
                    id = "${language.id}_section_$sectionNum",
                    languageId = language.id,
                    sectionNumber = sectionNum,
                    title = "Section $sectionNum",
                    description = "Learn ${language.name} - Part $sectionNum"
                )
            }
        }
    }
    
    /**
     * Get all levels across all sections.
     * Optimized to generate levels directly without redundant validations.
     * @return List of all levels (2000 total: 200 sections × 10 levels)
     */
    fun getAllLevels(): List<Level> {
        return languages.flatMap { language ->
            (1..10).flatMap { sectionNum ->
                val sectionId = "${language.id}_section_$sectionNum"
                (1..10).map { levelNum ->
                    Level(
                        id = "${sectionId}_level_$levelNum",
                        sectionId = sectionId,
                        levelNumber = levelNum,
                        title = "Level $levelNum",
                        difficulty = getDifficultyForLevel(levelNum),
                        isLocked = levelNum > 1
                    )
                }
            }
        }
    }
    
    /**
     * Get a specific language by ID.
     * Uses cached map for O(1) lookup.
     * @param languageId The ID of the language
     * @return The language or null if not found
     */
    fun getLanguageById(languageId: String): Language? = languageMap[languageId]
    
    /**
     * Get a specific section by ID.
     * @param sectionId The ID of the section (format: languageId_section_N)
     * @return The section or null if not found
     */
    fun getSectionById(sectionId: String): Section? {
        // Parse the sectionId to extract languageId and section number
        // Format: languageId_section_N
        val parts = sectionId.split("_section_")
        if (parts.size != 2) return null
        
        val languageId = parts[0]
        val sectionNum = parts[1].toIntOrNull() ?: return null
        
        if (sectionNum !in 1..10) return null
        
        return getSectionsForLanguage(languageId).getOrNull(sectionNum - 1)
    }
    
    /**
     * Get a specific level by ID.
     * @param levelId The ID of the level (format: languageId_section_N_level_M)
     * @return The level or null if not found
     */
    fun getLevelById(levelId: String): Level? {
        // Parse the levelId to extract sectionId and level number
        // Format: languageId_section_N_level_M
        val parts = levelId.split("_level_")
        if (parts.size != 2) return null
        
        val sectionId = parts[0]
        val levelNum = parts[1].toIntOrNull() ?: return null
        
        if (levelNum !in 1..10) return null
        
        return getLevelsForSection(sectionId).getOrNull(levelNum - 1)
    }
    
    /**
     * Determine difficulty based on level number.
     * Levels 1-3: Easy
     * Levels 4-7: Medium
     * Levels 8-10: Hard
     */
    private fun getDifficultyForLevel(levelNum: Int): String {
        return when (levelNum) {
            in 1..3 -> "Easy"
            in 4..7 -> "Medium"
            in 8..10 -> "Hard"
            else -> "Easy"
        }
    }
}
