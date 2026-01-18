package com.codinglearning.app.network

import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    // Piston API - Free online code execution service
    // https://github.com/engineer-man/piston
    private const val PISTON_BASE_URL = "https://emkc.org/api/v2/piston/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(PISTON_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val compilerApi: CompilerApiService = retrofit.create(CompilerApiService::class.java)
    
    // Alias for backward compatibility
    val compilerService: CompilerApiService = compilerApi
    
    // Language version mapping for Piston API
    // These are stable versions that work reliably
    fun getPistonLanguageVersion(language: String): String {
        return when (language.lowercase()) {
            "python" -> "3.10.0"
            "java" -> "15.0.2"
            "javascript" -> "18.15.0"
            "kotlin" -> "1.8.20"
            "c++" -> "10.2.0"
            else -> "latest"
        }
    }
    
    // Get proper language identifier for Piston API
    fun getPistonLanguageId(language: String): String {
        return when (language.lowercase()) {
            "python" -> "python"
            "java" -> "java"
            "javascript" -> "javascript"
            "kotlin" -> "kotlin"
            "c++" -> "c++"
            else -> language.lowercase()
        }
    }
    
    // Get appropriate file name for the language
    fun getFileName(language: String): String {
        return when (language.lowercase()) {
            "python" -> "main.py"
            "java" -> "Main.java"
            "javascript" -> "main.js"
            "kotlin" -> "Main.kt"
            "c++" -> "main.cpp"
            else -> "main.txt"
        }
    }
}
