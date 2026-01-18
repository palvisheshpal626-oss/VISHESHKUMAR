package com.codinglearning.app.data.repository

import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.data.model.*

class LevelRepository(private val prefsManager: PreferencesManager) {
    
    data class Section(
        val number: Int,
        val title: String,
        val description: String,
        val difficulty: LevelSection
    )
    
    fun getLevels(language: String): List<Level> {
        // Comprehensive level structure: 10 sections × 10 levels = 100 levels per language
        // Total: 500 levels across all 5 languages
        // Structure ready for content addition by developer
        return when (language.lowercase()) {
            "python" -> getPythonLevels()
            "java" -> getJavaLevels()
            "javascript" -> getJavaScriptLevels()
            "kotlin" -> getKotlinLevels()
            "c++" -> getCppLevels()
            else -> emptyList()
        }
    }
    
    fun getLevelStates(language: String): List<LevelState> {
        val levels = getLevels(language)
        val highestCompleted = prefsManager.getHighestCompletedLevel()
        
        return levels.map { level ->
            val stars = prefsManager.getLevelStars(level.id)
            val isSectionUnlocked = isSectionUnlocked(level, levels)
            
            LevelState(
                level = level,
                isUnlocked = isSectionUnlocked && (level.id <= highestCompleted + 1),
                isCompleted = prefsManager.isLevelCompleted(level.id),
                starsEarned = stars
            )
        }
    }
    
    private fun isSectionUnlocked(level: Level, allLevels: List<Level>): Boolean {
        return when (level.section) {
            LevelSection.EASY -> true // Easy is always unlocked
            LevelSection.MEDIUM -> {
                // Unlock Medium if user has at least 50% of possible stars from Easy
                val easyLevels = allLevels.filter { it.section == LevelSection.EASY }
                val easyLevelIds = easyLevels.map { it.id }
                val easyStars = prefsManager.getStarsInSection(easyLevelIds)
                val maxPossibleEasyStars = easyLevels.size * 3
                easyStars >= (maxPossibleEasyStars * 0.5).toInt()
            }
            LevelSection.HARD -> {
                // Unlock Hard if user has at least 60% of possible stars from Easy + Medium
                val easyMediumLevels = allLevels.filter { 
                    it.section == LevelSection.EASY || it.section == LevelSection.MEDIUM 
                }
                val easyMediumLevelIds = easyMediumLevels.map { it.id }
                val totalStars = prefsManager.getStarsInSection(easyMediumLevelIds)
                val maxPossibleStars = easyMediumLevels.size * 3
                totalStars >= (maxPossibleStars * 0.6).toInt()
            }
        }
    }
    
    private fun getPythonLevels(): List<Level> {
        // Python: 10 sections × 10 levels = 100 levels total
        val sections = listOf(
            Section(1, "Python Basics", "Learn Python basics including syntax, variables, and basic operations", LevelSection.EASY),
            Section(2, "Control Flow", "Master if-else statements, loops, and conditional logic", LevelSection.EASY),
            Section(3, "Functions", "Learn to create and use functions effectively", LevelSection.EASY),
            Section(4, "Data Structures", "Work with lists, dictionaries, tuples, and sets", LevelSection.MEDIUM),
            Section(5, "Object-Oriented Programming", "Understand classes, objects, and OOP principles", LevelSection.MEDIUM),
            Section(6, "File Handling", "Learn to read from and write to files", LevelSection.MEDIUM),
            Section(7, "Advanced OOP", "Master inheritance, polymorphism, and encapsulation", LevelSection.MEDIUM),
            Section(8, "Libraries & Modules", "Explore Python's standard library and popular modules", LevelSection.HARD),
            Section(9, "Advanced Topics", "Learn decorators, generators, and advanced Python features", LevelSection.HARD),
            Section(10, "Expert Challenges", "Solve complex problems and real-world scenarios", LevelSection.HARD)
        )
        
        return sections.flatMapIndexed { sectionIndex, section ->
            (1..10).map { levelInSection ->
                val levelId = sectionIndex * 10 + levelInSection
                createLevel(
                    id = levelId,
                    sectionNumber = section.number,
                    levelInSection = levelInSection,
                    sectionTitle = section.title,
                    language = "Python",
                    difficulty = section.difficulty
                )
            }
        }
    }
    
    private fun getJavaLevels(): List<Level> {
        // Java: 10 sections × 10 levels = 100 levels total
        val sections = listOf(
            Section(1, "Java Fundamentals", "Learn Java basics including syntax, variables, and data types", LevelSection.EASY),
            Section(2, "Control Structures", "Master if-else, switch, loops, and flow control", LevelSection.EASY),
            Section(3, "Methods", "Learn to create and use methods effectively", LevelSection.EASY),
            Section(4, "Arrays & Collections", "Work with arrays, ArrayList, HashMap, and more", LevelSection.MEDIUM),
            Section(5, "OOP Principles", "Understand classes, objects, inheritance, and polymorphism", LevelSection.MEDIUM),
            Section(6, "Exception Handling", "Master try-catch, throw, and custom exceptions", LevelSection.MEDIUM),
            Section(7, "Advanced OOP", "Learn interfaces, abstract classes, and design principles", LevelSection.MEDIUM),
            Section(8, "Multithreading", "Understand threads, synchronization, and concurrency", LevelSection.HARD),
            Section(9, "Java 8+ Features", "Explore lambda, streams, and modern Java features", LevelSection.HARD),
            Section(10, "Design Patterns", "Master common design patterns and best practices", LevelSection.HARD)
        )
        
        return sections.flatMapIndexed { sectionIndex, section ->
            (1..10).map { levelInSection ->
                val levelId = sectionIndex * 10 + levelInSection
                createLevel(
                    id = levelId,
                    sectionNumber = section.number,
                    levelInSection = levelInSection,
                    sectionTitle = section.title,
                    language = "Java",
                    difficulty = section.difficulty
                )
            }
        }
    }
    
    private fun getJavaScriptLevels(): List<Level> {
        // JavaScript: 10 sections × 10 levels = 100 levels total
        val sections = listOf(
            Section(1, "JavaScript Basics", "Learn JS fundamentals including variables, types, and operators", LevelSection.EASY),
            Section(2, "DOM Manipulation", "Master document object model and web page interaction", LevelSection.EASY),
            Section(3, "Functions & Scope", "Understand functions, closures, and scope", LevelSection.EASY),
            Section(4, "Arrays & Objects", "Work with arrays, objects, and JSON", LevelSection.MEDIUM),
            Section(5, "Async Programming", "Learn callbacks, promises, and async/await", LevelSection.MEDIUM),
            Section(6, "ES6+ Features", "Explore arrow functions, destructuring, and modules", LevelSection.MEDIUM),
            Section(7, "API Integration", "Master fetch, AJAX, and REST APIs", LevelSection.MEDIUM),
            Section(8, "Frameworks Introduction", "Get started with React, Vue, or Angular concepts", LevelSection.HARD),
            Section(9, "Advanced Patterns", "Learn design patterns and advanced techniques", LevelSection.HARD),
            Section(10, "Performance Optimization", "Master optimization and best practices", LevelSection.HARD)
        )
        
        return sections.flatMapIndexed { sectionIndex, section ->
            (1..10).map { levelInSection ->
                val levelId = sectionIndex * 10 + levelInSection
                createLevel(
                    id = levelId,
                    sectionNumber = section.number,
                    levelInSection = levelInSection,
                    sectionTitle = section.title,
                    language = "JavaScript",
                    difficulty = section.difficulty
                )
            }
        }
    }
    
    private fun getKotlinLevels(): List<Level> {
        // Kotlin: 10 sections × 10 levels = 100 levels total
        val sections = listOf(
            Section(1, "Kotlin Basics", "Learn Kotlin fundamentals including val, var, and basic types", LevelSection.EASY),
            Section(2, "Control Flow", "Master when, if, loops, and ranges", LevelSection.EASY),
            Section(3, "Functions", "Learn function syntax, parameters, and lambdas", LevelSection.EASY),
            Section(4, "Collections", "Work with lists, sets, maps, and collection operations", LevelSection.MEDIUM),
            Section(5, "OOP in Kotlin", "Understand classes, data classes, and objects", LevelSection.MEDIUM),
            Section(6, "Null Safety", "Master nullable types and safe calls", LevelSection.MEDIUM),
            Section(7, "Coroutines Basics", "Learn asynchronous programming with coroutines", LevelSection.MEDIUM),
            Section(8, "Advanced Coroutines", "Master suspend functions, flows, and channels", LevelSection.HARD),
            Section(9, "DSL & Extensions", "Create DSLs and extension functions", LevelSection.HARD),
            Section(10, "Android Development", "Apply Kotlin to Android app development", LevelSection.HARD)
        )
        
        return sections.flatMapIndexed { sectionIndex, section ->
            (1..10).map { levelInSection ->
                val levelId = sectionIndex * 10 + levelInSection
                createLevel(
                    id = levelId,
                    sectionNumber = section.number,
                    levelInSection = levelInSection,
                    sectionTitle = section.title,
                    language = "Kotlin",
                    difficulty = section.difficulty
                )
            }
        }
    }
    
    private fun getCppLevels(): List<Level> {
        // C++: 10 sections × 10 levels = 100 levels total
        val sections = listOf(
            Section(1, "C++ Fundamentals", "Learn C++ basics including syntax, variables, and I/O", LevelSection.EASY),
            Section(2, "Control Structures", "Master if-else, switch, loops, and flow control", LevelSection.EASY),
            Section(3, "Functions", "Learn function declaration, definition, and overloading", LevelSection.EASY),
            Section(4, "Pointers & References", "Understand memory addresses, pointers, and references", LevelSection.MEDIUM),
            Section(5, "OOP in C++", "Learn classes, constructors, destructors, and inheritance", LevelSection.MEDIUM),
            Section(6, "Templates", "Master function templates and class templates", LevelSection.MEDIUM),
            Section(7, "STL Containers", "Work with vectors, lists, maps, and sets", LevelSection.MEDIUM),
            Section(8, "Memory Management", "Understand dynamic memory, new, delete, and smart pointers", LevelSection.HARD),
            Section(9, "Advanced STL", "Master algorithms, iterators, and functional programming", LevelSection.HARD),
            Section(10, "Modern C++", "Explore C++11/14/17 features and best practices", LevelSection.HARD)
        )
        
        return sections.flatMapIndexed { sectionIndex, section ->
            (1..10).map { levelInSection ->
                val levelId = sectionIndex * 10 + levelInSection
                createLevel(
                    id = levelId,
                    sectionNumber = section.number,
                    levelInSection = levelInSection,
                    sectionTitle = section.title,
                    language = "C++",
                    difficulty = section.difficulty
                )
            }
        }
    }
    
    // Helper function to create a level with placeholder content
    private fun createLevel(
        id: Int,
        sectionNumber: Int,
        levelInSection: Int,
        sectionTitle: String,
        language: String,
        difficulty: LevelSection
    ): Level {
        val langPrefix = when(language.lowercase()) {
            "python" -> "py"
            "java" -> "java"
            "javascript" -> "js"
            "kotlin" -> "kt"
            "c++" -> "cpp"
            else -> "lang"
        }
        
        return Level(
            id = id,
            title = "$sectionTitle - Level $levelInSection",
            language = language,
            mcqQuestions = listOf(
                createPlaceholderMCQ(langPrefix, sectionNumber, levelInSection, 1),
                createPlaceholderMCQ(langPrefix, sectionNumber, levelInSection, 2)
            ),
            codeExample = CodeExample(
                title = "Example for $sectionTitle Level $levelInSection",
                code = "// Code example will be added by developer\n// This section covers: $sectionTitle\n// Level: $levelInSection of 10",
                language = language,
                description = "This section contains 10 levels. Content will be added later."
            ),
            problems = listOf(
                Problem(
                    id = "${langPrefix}_s${sectionNumber}_l${levelInSection}_p1",
                    title = "Practice Problem",
                    description = "Problem description will be added by developer",
                    difficulty = when(difficulty) {
                        LevelSection.EASY -> Difficulty.EASY
                        LevelSection.MEDIUM -> Difficulty.MEDIUM
                        LevelSection.HARD -> Difficulty.HARD
                    },
                    language = language,
                    starterCode = "// Starter code will be added by developer",
                    testCases = listOf(
                        TestCase("", "Expected output")
                    ),
                    coinsReward = 30
                )
            ),
            videoUrl = null,
            coinsReward = 50,
            section = difficulty
        )
    }
    
    // Helper function to create placeholder MCQ questions
    private fun createPlaceholderMCQ(
        langPrefix: String,
        sectionNumber: Int,
        levelInSection: Int,
        questionNumber: Int
    ): MCQQuestion {
        return MCQQuestion(
            id = "${langPrefix}_s${sectionNumber}_l${levelInSection}_q${questionNumber}",
            question = "[SECTION $sectionNumber] Question $questionNumber for Level $levelInSection - To be added by developer",
            options = listOf(
                "Option A (Incorrect) - To be added",
                "Option B (CORRECT ANSWER) - To be added",
                "Option C (Incorrect) - To be added",
                "Option D (Incorrect) - To be added"
            ),
            correctAnswerIndex = 1, // Option B is marked as correct for easy identification
            hint = "Hint will be added later by developer",
            explanation = "Detailed explanation will be added later by developer"
        )
    }
}
