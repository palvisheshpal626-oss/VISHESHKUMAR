package com.vishesh.codinglearning.data.api

import com.vishesh.codinglearning.data.model.ProgrammingLanguage
import kotlinx.coroutines.delay

/**
 * Mock implementation of CompilerApi for demo purposes.
 * In production, this should call Firebase Cloud Function which calls Judge0 or similar online compiler API.
 * 
 * IMPORTANT: Never store API keys in the Android app.
 * All compiler API calls should go through Firebase Cloud Functions for security.
 */
class MockCompilerApi : CompilerApi {
    
    override suspend fun executeCode(request: CompilerRequest): Result<CompilerResponse> {
        return try {
            // Simulate network delay
            delay(1000)
            
            // Mock execution based on language
            val output = when (request.language) {
                ProgrammingLanguage.PYTHON -> executePythonMock(request.code, request.input)
                ProgrammingLanguage.JAVA -> "Java execution (mock): Hello from Java"
                ProgrammingLanguage.C -> "C execution (mock): Hello from C"
                ProgrammingLanguage.CPP -> "C++ execution (mock): Hello from C++"
            }
            
            Result.success(
                CompilerResponse(
                    output = output,
                    error = null,
                    executionTime = 150
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun executePythonMock(code: String, input: String): String {
        // Very basic mock - just check if code contains print
        return if (code.contains("print")) {
            "Hello World\n(This is a mock output. Real execution requires backend setup)"
        } else {
            "(Mock output - setup Firebase Cloud Function for real execution)"
        }
    }
}
