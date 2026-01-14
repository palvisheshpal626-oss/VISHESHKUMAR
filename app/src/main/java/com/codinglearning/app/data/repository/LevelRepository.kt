package com.codinglearning.app.data.repository

import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.data.model.*

class LevelRepository(private val prefsManager: PreferencesManager) {
    
    fun getLevels(language: String): List<Level> {
        // Sample levels for demonstration
        // In production, this would come from Firebase or a local database
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
        return listOf(
            Level(
                id = 1,
                title = "Introduction to Python",
                language = "Python",
                mcqQuestions = listOf(
                    MCQQuestion(
                        id = "py1_q1",
                        question = "What is Python?",
                        options = listOf(
                            "A snake",
                            "A high-level programming language",
                            "A low-level programming language",
                            "An operating system"
                        ),
                        correctAnswerIndex = 1,
                        hint = "Think about what we use Python for in programming",
                        explanation = "Python is a high-level, interpreted programming language known for its simplicity and readability."
                    ),
                    MCQQuestion(
                        id = "py1_q2",
                        question = "Which keyword is used to print output in Python?",
                        options = listOf("echo", "print", "console", "output"),
                        correctAnswerIndex = 1,
                        hint = "It's the same word we use to print documents!",
                        explanation = "The print() function is used to output text in Python."
                    )
                ),
                codeExample = CodeExample(
                    title = "Hello World in Python",
                    code = "# This is a comment\nprint(\"Hello, World!\")\nprint(\"Welcome to Python programming!\")",
                    language = "Python",
                    description = "This is your first Python program. The print() function displays text on the screen."
                ),
                problems = listOf(
                    Problem(
                        id = "py1_p1",
                        title = "Print Your Name",
                        description = "Write a program that prints your name.",
                        difficulty = Difficulty.EASY,
                        language = "Python",
                        starterCode = "# Write your code here\nname = \"Your Name\"\n# Print the name",
                        testCases = listOf(
                            TestCase("", "Your Name")
                        ),
                        coinsReward = 30
                    )
                ),
                videoUrl = null,
                coinsReward = 50,
                section = LevelSection.EASY
            ),
            Level(
                id = 2,
                title = "Variables and Data Types",
                language = "Python",
                mcqQuestions = listOf(
                    MCQQuestion(
                        id = "py2_q1",
                        question = "Which of these is a valid variable name in Python?",
                        options = listOf("2name", "my-name", "my_name", "my name"),
                        correctAnswerIndex = 2,
                        hint = "Variable names cannot start with numbers or contain spaces",
                        explanation = "my_name is valid. Variable names can contain letters, numbers, and underscores, but cannot start with a number or contain spaces."
                    )
                ),
                codeExample = CodeExample(
                    title = "Variables in Python",
                    code = "# Storing values in variables\nname = \"Alice\"\nage = 25\nheight = 5.6\n\nprint(f\"Name: {name}\")\nprint(f\"Age: {age}\")\nprint(f\"Height: {height}\")",
                    language = "Python",
                    description = "Variables store data that can be used later in your program."
                ),
                problems = listOf(
                    Problem(
                        id = "py2_p1",
                        title = "Calculate Sum",
                        description = "Create two variables with numbers and print their sum.",
                        difficulty = Difficulty.EASY,
                        language = "Python",
                        starterCode = "# Create two variables\na = 5\nb = 10\n# Calculate and print their sum",
                        testCases = listOf(
                            TestCase("", "15")
                        )
                    )
                ),
                videoUrl = null,
                coinsReward = 50,
                section = LevelSection.EASY
            )
        )
    }
    
    private fun getJavaLevels(): List<Level> {
        return listOf(
            Level(
                id = 1,
                title = "Introduction to Java",
                language = "Java",
                mcqQuestions = listOf(
                    MCQQuestion(
                        id = "java1_q1",
                        question = "What is the extension of Java source files?",
                        options = listOf(".java", ".class", ".jar", ".jav"),
                        correctAnswerIndex = 0,
                        hint = "The extension matches the language name",
                        explanation = "Java source files use the .java extension. When compiled, they become .class files."
                    )
                ),
                codeExample = CodeExample(
                    title = "Hello World in Java",
                    code = "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}",
                    language = "Java",
                    description = "Every Java program starts with a main method inside a class."
                ),
                problems = listOf(
                    Problem(
                        id = "java1_p1",
                        title = "Print Message",
                        description = "Write a Java program to print 'Learning Java'",
                        difficulty = Difficulty.EASY,
                        language = "Java",
                        starterCode = "public class Main {\n    public static void main(String[] args) {\n        // Your code here\n    }\n}",
                        testCases = listOf(
                            TestCase("", "Learning Java")
                        )
                    )
                ),
                videoUrl = null
            )
        )
    }
    
    private fun getJavaScriptLevels(): List<Level> {
        return listOf(
            Level(
                id = 1,
                title = "Introduction to JavaScript",
                language = "JavaScript",
                mcqQuestions = listOf(
                    MCQQuestion(
                        id = "js1_q1",
                        question = "Which keyword is used to declare a variable in modern JavaScript?",
                        options = listOf("var", "let", "const", "Both let and const"),
                        correctAnswerIndex = 3,
                        hint = "Modern JavaScript has two main ways to declare variables",
                        explanation = "Both 'let' and 'const' are used in modern JavaScript. 'let' for variables that change, 'const' for constants."
                    )
                ),
                codeExample = CodeExample(
                    title = "Variables in JavaScript",
                    code = "// Using let for variables that change\nlet name = \"John\";\nname = \"Jane\";\n\n// Using const for constants\nconst age = 25;\n\nconsole.log(name);\nconsole.log(age);",
                    language = "JavaScript",
                    description = "JavaScript uses let and const to declare variables."
                ),
                problems = listOf(
                    Problem(
                        id = "js1_p1",
                        title = "Declare and Log",
                        description = "Create a variable with your favorite number and log it",
                        difficulty = Difficulty.EASY,
                        language = "JavaScript",
                        starterCode = "// Declare your variable here\nconst favoriteNumber = 7;\n// Log it to console",
                        testCases = listOf(
                            TestCase("", "7")
                        )
                    )
                ),
                videoUrl = null
            )
        )
    }
    
    private fun getKotlinLevels(): List<Level> {
        return listOf(
            Level(
                id = 1,
                title = "Introduction to Kotlin",
                language = "Kotlin",
                mcqQuestions = listOf(
                    MCQQuestion(
                        id = "kt1_q1",
                        question = "Which keyword is used to declare a read-only variable in Kotlin?",
                        options = listOf("var", "val", "const", "let"),
                        correctAnswerIndex = 1,
                        hint = "It's short for 'value'",
                        explanation = "'val' declares a read-only variable in Kotlin, while 'var' is for mutable variables."
                    )
                ),
                codeExample = CodeExample(
                    title = "Hello Kotlin",
                    code = "fun main() {\n    val message = \"Hello, Kotlin!\"\n    println(message)\n}",
                    language = "Kotlin",
                    description = "Kotlin programs start with a main function."
                ),
                problems = listOf(
                    Problem(
                        id = "kt1_p1",
                        title = "Print Greeting",
                        description = "Print a greeting message",
                        difficulty = Difficulty.EASY,
                        language = "Kotlin",
                        starterCode = "fun main() {\n    // Your code here\n}",
                        testCases = listOf(
                            TestCase("", "Hello, Kotlin!")
                        )
                    )
                ),
                videoUrl = null
            )
        )
    }
    
    private fun getCppLevels(): List<Level> {
        return listOf(
            Level(
                id = 1,
                title = "Introduction to C++",
                language = "C++",
                mcqQuestions = listOf(
                    MCQQuestion(
                        id = "cpp1_q1",
                        question = "Which header file is needed for cout in C++?",
                        options = listOf("stdio.h", "iostream", "conio.h", "stdlib.h"),
                        correctAnswerIndex = 1,
                        hint = "It's related to input/output streams",
                        explanation = "iostream is the header for input/output streams, including cout and cin."
                    )
                ),
                codeExample = CodeExample(
                    title = "Hello C++",
                    code = "#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"Hello, C++!\" << endl;\n    return 0;\n}",
                    language = "C++",
                    description = "A basic C++ program with main function and cout statement."
                ),
                problems = listOf(
                    Problem(
                        id = "cpp1_p1",
                        title = "Print Message",
                        description = "Print 'Welcome to C++' to the console",
                        difficulty = Difficulty.EASY,
                        language = "C++",
                        starterCode = "#include <iostream>\nusing namespace std;\n\nint main() {\n    // Your code here\n    return 0;\n}",
                        testCases = listOf(
                            TestCase("", "Welcome to C++")
                        )
                    )
                ),
                videoUrl = null
            )
        )
    }
}
