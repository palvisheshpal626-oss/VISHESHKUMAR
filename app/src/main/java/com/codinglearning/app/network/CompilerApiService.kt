package com.codinglearning.app.network

import retrofit2.http.Body
import retrofit2.http.POST

interface CompilerApiService {
    
    @POST("execute")
    suspend fun executeCode(
        @Body request: CodeExecutionRequest
    ): CodeExecutionResponse
}
