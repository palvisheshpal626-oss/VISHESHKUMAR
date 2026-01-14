package com.codinglearning.app.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CompilerApiService {
    
    // Piston API endpoints
    @POST("execute")
    suspend fun executePiston(
        @Body request: PistonExecuteRequest
    ): PistonExecuteResponse
    
    @GET("runtimes")
    suspend fun getRuntimes(): List<PistonRuntime>
}
