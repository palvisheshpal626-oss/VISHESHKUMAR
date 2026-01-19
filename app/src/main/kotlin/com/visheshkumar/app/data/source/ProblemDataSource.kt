package com.visheshkumar.app.data.source

import com.visheshkumar.app.data.model.Problem
import com.visheshkumar.app.data.model.TestCase

/**
 * Data source for Problem Solving practice.
 * 
 * Provides placeholder problems for all levels across all languages.
 * Each level has 3 problems of increasing difficulty.
 */
object ProblemDataSource {
    
    private const val PROBLEMS_PER_LEVEL = 3
    
    /**
     * Get all problems for a specific level
     */
    fun getProblemsForLevel(levelId: String): List<Problem> {
        return (1..PROBLEMS_PER_LEVEL).map { problemNumber ->
            createPlaceholderProblem(levelId, problemNumber)
        }
    }
    
    /**
     * Get a specific problem by ID
     */
    fun getProblemById(problemId: String): Problem? {
        val parts = problemId.split("_")
        if (parts.size != 8 || parts[6] != "problem") return null
        
        val levelId = parts.subList(0, 6).joinToString("_")
        val problemNumber = parts[7].toIntOrNull() ?: return null
        
        return createPlaceholderProblem(levelId, problemNumber)
    }
    
    /**
     * Check if problems are available for a level
     */
    fun hasProblemsForLevel(levelId: String): Boolean {
        return levelId.matches(Regex("^[a-z]+_section_\\d+_level_\\d+$"))
    }
    
    /**
     * Get all problems across all levels (useful for testing)
     */
    fun getAllProblems(): List<Problem> {
        val allProblems = mutableListOf<Problem>()
        val languages = listOf(
            "kotlin", "java", "python", "javascript", "typescript",
            "cpp", "c", "csharp", "swift", "go", "rust",
            "php", "ruby", "dart", "r", "scala", "sql", "html", "css", "bash"
        )
        
        languages.forEach { languageId ->
            (1..10).forEach { sectionNumber ->
                (1..10).forEach { levelNumber ->
                    val levelId = "${languageId}_section_${sectionNumber}_level_$levelNumber"
                    allProblems.addAll(getProblemsForLevel(levelId))
                }
            }
        }
        
        return allProblems
    }
    
    /**
     * Create a placeholder problem for a specific level
     */
    private fun createPlaceholderProblem(levelId: String, problemNumber: Int): Problem {
        val parts = levelId.split("_")
        val languageId = parts[0]
        val sectionNumber = parts[2].toIntOrNull() ?: 1
        val levelNumber = parts[4].toIntOrNull() ?: 1
        
        val difficulty = when (problemNumber) {
            1 -> "easy"
            2 -> "medium"
            else -> "hard"
        }
        
        val problemId = "${levelId}_problem_$problemNumber"
        
        return Problem(
            id = problemId,
            levelId = levelId,
            problemNumber = problemNumber,
            title = "Problem will be added later",
            description = "Problem description will be added later.\n\n" +
                    "This is a placeholder for a $difficulty problem in ${languageId.capitalize()}.\n\n" +
                    "Future content will include:\n" +
                    "- Clear problem statement\n" +
                    "- Input/output examples\n" +
                    "- Constraints and edge cases\n" +
                    "- Sample test cases",
            difficulty = difficulty,
            languageId = languageId,
            starterCode = getStarterCodeForLanguage(languageId),
            testCases = listOf(
                TestCase(
                    input = "placeholder",
                    expectedOutput = "placeholder",
                    isHidden = false
                ),
                TestCase(
                    input = "hidden_test",
                    expectedOutput = "hidden_result",
                    isHidden = true
                )
            ),
            timeLimit = 60_000, // 1 minute
            isPlaceholder = true,
            hasBeenSolved = false,
            bestSolveTime = 0L
        )
    }
    
    /**
     * Get starter code template for each language
     */
    private fun getStarterCodeForLanguage(languageId: String): String {
        return when (languageId) {
            "kotlin" -> """
                fun solve(input: String): String {
                    // Write your solution here
                    return "placeholder"
                }
            """.trimIndent()
            
            "java" -> """
                public class Solution {
                    public static String solve(String input) {
                        // Write your solution here
                        return "placeholder";
                    }
                }
            """.trimIndent()
            
            "python" -> """
                def solve(input_data):
                    # Write your solution here
                    return "placeholder"
            """.trimIndent()
            
            "javascript" -> """
                function solve(input) {
                    // Write your solution here
                    return "placeholder";
                }
            """.trimIndent()
            
            "typescript" -> """
                function solve(input: string): string {
                    // Write your solution here
                    return "placeholder";
                }
            """.trimIndent()
            
            "cpp" -> """
                #include <iostream>
                #include <string>
                
                std::string solve(std::string input) {
                    // Write your solution here
                    return "placeholder";
                }
            """.trimIndent()
            
            "c" -> """
                #include <stdio.h>
                #include <string.h>
                
                char* solve(char* input) {
                    // Write your solution here
                    return "placeholder";
                }
            """.trimIndent()
            
            "csharp" -> """
                public class Solution {
                    public static string Solve(string input) {
                        // Write your solution here
                        return "placeholder";
                    }
                }
            """.trimIndent()
            
            "swift" -> """
                func solve(input: String) -> String {
                    // Write your solution here
                    return "placeholder"
                }
            """.trimIndent()
            
            "go" -> """
                package main
                
                func solve(input string) string {
                    // Write your solution here
                    return "placeholder"
                }
            """.trimIndent()
            
            "rust" -> """
                fn solve(input: &str) -> String {
                    // Write your solution here
                    String::from("placeholder")
                }
            """.trimIndent()
            
            "php" -> """
                <?php
                function solve(${'$'}input) {
                    // Write your solution here
                    return "placeholder";
                }
                ?>
            """.trimIndent()
            
            "ruby" -> """
                def solve(input)
                  # Write your solution here
                  "placeholder"
                end
            """.trimIndent()
            
            "dart" -> """
                String solve(String input) {
                  // Write your solution here
                  return "placeholder";
                }
            """.trimIndent()
            
            "r" -> """
                solve <- function(input) {
                  # Write your solution here
                  return("placeholder")
                }
            """.trimIndent()
            
            "scala" -> """
                object Solution {
                  def solve(input: String): String = {
                    // Write your solution here
                    "placeholder"
                  }
                }
            """.trimIndent()
            
            "sql" -> """
                -- SQL problems will have specific schema
                SELECT 'placeholder' AS result;
            """.trimIndent()
            
            "html" -> """
                <!-- HTML problem starter code -->
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Solution</title>
                </head>
                <body>
                    <!-- Write your solution here -->
                </body>
                </html>
            """.trimIndent()
            
            "css" -> """
                /* CSS problem starter code */
                .solution {
                    /* Write your solution here */
                }
            """.trimIndent()
            
            "bash" -> """
                #!/bin/bash
                # Write your solution here
                echo "placeholder"
            """.trimIndent()
            
            else -> "// Starter code will be provided"
        }
    }
    
    /**
     * Capitalize first letter of language name
     */
    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
