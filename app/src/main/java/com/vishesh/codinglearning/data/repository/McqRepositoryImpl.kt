package com.vishesh.codinglearning.data.repository

import com.vishesh.codinglearning.data.model.McqQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class McqRepositoryImpl : McqRepository {
    
    override fun getQuestionsForLevel(levelId: Int): Flow<List<McqQuestion>> {
        return flowOf(getSampleQuestions(levelId))
    }
    
    private fun getSampleQuestions(levelId: Int): List<McqQuestion> {
        return listOf(
            McqQuestion(
                id = 1,
                levelId = levelId,
                question = "What is the output of print('Hello' + 'World')?",
                options = listOf("Hello World", "HelloWorld", "Error", "None"),
                correctAnswerIndex = 1,
                hint = "String concatenation in Python doesn't add spaces automatically",
                explanation = "The + operator concatenates strings without adding spaces"
            ),
            McqQuestion(
                id = 2,
                levelId = levelId,
                question = "Which keyword is used to define a function in Python?",
                options = listOf("function", "def", "func", "define"),
                correctAnswerIndex = 1,
                hint = "It's a three-letter keyword",
                explanation = "'def' is the keyword used to define functions in Python"
            ),
            McqQuestion(
                id = 3,
                levelId = levelId,
                question = "What is the correct syntax for a for loop in Python?",
                options = listOf(
                    "for i in range(10):",
                    "for (i=0; i<10; i++)",
                    "for i to 10:",
                    "loop i in 10:"
                ),
                correctAnswerIndex = 0,
                hint = "Python uses 'in' keyword with range",
                explanation = "Python for loops use 'for variable in iterable:' syntax"
            )
        )
    }
}
