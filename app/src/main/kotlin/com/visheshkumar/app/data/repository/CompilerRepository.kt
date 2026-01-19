package com.visheshkumar.app.data.repository

import com.visheshkumar.app.data.model.CodeExecutionRequest
import com.visheshkumar.app.data.model.CodeExecutionResponse
import com.visheshkumar.app.data.model.PistonLanguage
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for code compilation and execution using Piston API
 * 
 * Piston API: https://github.com/engineer-man/piston
 * Public instance: https://emkc.org/api/v2/piston/execute
 * 
 * No API key required for public instance
 */
class CompilerRepository {
    
    companion object {
        private const val PISTON_API_URL = "https://emkc.org/api/v2/piston/execute"
        private const val TIMEOUT_MS = 30000 // 30 seconds
    }
    
    /**
     * Execute code using Piston API
     * 
     * @param languageId The language ID (e.g., "kotlin", "python")
     * @param code The source code to execute
     * @param stdin Optional standard input
     * @return CodeExecutionResponse with execution results
     */
    suspend fun executeCode(
        languageId: String,
        code: String,
        stdin: String = ""
    ): Result<CodeExecutionResponse> = withContext(Dispatchers.IO) {
        try {
            // Get language configuration
            val pistonLang = PistonLanguage.fromLanguageId(languageId)
                ?: return@withContext Result.failure(
                    IllegalArgumentException("Unsupported language: $languageId")
                )
            
            // Check if language supports real execution
            if (!pistonLang.supportsRealExecution) {
                return@withContext Result.failure(
                    UnsupportedOperationException(
                        "$languageId does not support code execution. Use learning mode instead."
                    )
                )
            }
            
            // Create request
            val request = CodeExecutionRequest(
                language = pistonLang.pistonName,
                version = pistonLang.version,
                files = listOf(
                    CodeExecutionRequest.SourceFile(
                        name = "main.${pistonLang.extension}",
                        content = code
                    )
                ),
                stdin = stdin
            )
            
            // Execute API call
            val response = executePistonRequest(request)
            Result.success(response)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Execute the HTTP request to Piston API
     */
    private fun executePistonRequest(request: CodeExecutionRequest): CodeExecutionResponse {
        val url = URL(PISTON_API_URL)
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            // Configure connection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true
            connection.doInput = true
            connection.connectTimeout = TIMEOUT_MS
            connection.readTimeout = TIMEOUT_MS
            
            // Write request body
            val requestJson = buildRequestJson(request)
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(requestJson)
                writer.flush()
            }
            
            // Read response
            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw Exception("HTTP error code: $responseCode")
            }
            
            val responseJson = BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                reader.readText()
            }
            
            // Parse response
            return parseResponse(responseJson)
            
        } finally {
            connection.disconnect()
        }
    }
    
    /**
     * Build JSON request for Piston API
     */
    private fun buildRequestJson(request: CodeExecutionRequest): String {
        val json = JSONObject()
        json.put("language", request.language)
        json.put("version", request.version)
        
        val filesArray = JSONArray()
        request.files.forEach { file ->
            val fileObj = JSONObject()
            file.name?.let { fileObj.put("name", it) }
            fileObj.put("content", file.content)
            filesArray.put(fileObj)
        }
        json.put("files", filesArray)
        
        if (request.stdin.isNotEmpty()) {
            json.put("stdin", request.stdin)
        }
        
        if (request.args.isNotEmpty()) {
            val argsArray = JSONArray()
            request.args.forEach { argsArray.put(it) }
            json.put("args", argsArray)
        }
        
        json.put("compile_timeout", request.compile_timeout)
        json.put("run_timeout", request.run_timeout)
        
        if (request.compile_memory_limit > 0) {
            json.put("compile_memory_limit", request.compile_memory_limit)
        }
        
        if (request.run_memory_limit > 0) {
            json.put("run_memory_limit", request.run_memory_limit)
        }
        
        return json.toString()
    }
    
    /**
     * Parse Piston API response JSON
     */
    private fun parseResponse(jsonString: String): CodeExecutionResponse {
        val json = JSONObject(jsonString)
        
        val language = json.getString("language")
        val version = json.getString("version")
        
        val runObj = json.getJSONObject("run")
        val run = CodeExecutionResponse.ExecutionResult(
            stdout = runObj.optString("stdout", ""),
            stderr = runObj.optString("stderr", ""),
            code = runObj.optInt("code", -1),
            signal = runObj.optString("signal", null),
            output = runObj.optString("output", "")
        )
        
        val compile = if (json.has("compile")) {
            val compileObj = json.getJSONObject("compile")
            CodeExecutionResponse.ExecutionResult(
                stdout = compileObj.optString("stdout", ""),
                stderr = compileObj.optString("stderr", ""),
                code = compileObj.optInt("code", -1),
                signal = compileObj.optString("signal", null),
                output = compileObj.optString("output", "")
            )
        } else {
            null
        }
        
        return CodeExecutionResponse(
            language = language,
            version = version,
            run = run,
            compile = compile
        )
    }
    
    /**
     * Check if a language supports real execution
     */
    fun supportsExecution(languageId: String): Boolean {
        return PistonLanguage.supportsExecution(languageId)
    }
    
    /**
     * Get default code template for a language
     */
    fun getDefaultCodeTemplate(languageId: String): String {
        return when (languageId.lowercase()) {
            "kotlin" -> """
                fun main() {
                    println("Hello, Kotlin!")
                }
            """.trimIndent()
            
            "java" -> """
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("Hello, Java!");
                    }
                }
            """.trimIndent()
            
            "python" -> """
                print("Hello, Python!")
            """.trimIndent()
            
            "javascript" -> """
                console.log("Hello, JavaScript!");
            """.trimIndent()
            
            "typescript" -> """
                console.log("Hello, TypeScript!");
            """.trimIndent()
            
            "cpp", "c++" -> """
                #include <iostream>
                
                int main() {
                    std::cout << "Hello, C++!" << std::endl;
                    return 0;
                }
            """.trimIndent()
            
            "c" -> """
                #include <stdio.h>
                
                int main() {
                    printf("Hello, C!\n");
                    return 0;
                }
            """.trimIndent()
            
            "csharp", "c#" -> """
                using System;
                
                class Program {
                    static void Main() {
                        Console.WriteLine("Hello, C#!");
                    }
                }
            """.trimIndent()
            
            "swift" -> """
                print("Hello, Swift!")
            """.trimIndent()
            
            "go" -> """
                package main
                
                import "fmt"
                
                func main() {
                    fmt.Println("Hello, Go!")
                }
            """.trimIndent()
            
            "rust" -> """
                fn main() {
                    println!("Hello, Rust!");
                }
            """.trimIndent()
            
            "php" -> """
                <?php
                echo "Hello, PHP!";
                ?>
            """.trimIndent()
            
            "ruby" -> """
                puts "Hello, Ruby!"
            """.trimIndent()
            
            "dart" -> """
                void main() {
                    print("Hello, Dart!");
                }
            """.trimIndent()
            
            "r" -> """
                print("Hello, R!")
            """.trimIndent()
            
            "scala" -> """
                object Main extends App {
                    println("Hello, Scala!")
                }
            """.trimIndent()
            
            "bash", "sh" -> """
                echo "Hello, Bash!"
            """.trimIndent()
            
            else -> "// Write your code here"
        }
    }
}
