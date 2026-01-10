package com.vishesh.codinglearning.data.repository

import com.vishesh.codinglearning.data.api.CompilerApi
import com.vishesh.codinglearning.data.api.CompilerRequest
import com.vishesh.codinglearning.data.api.CompilerResponse

class CompilerRepositoryImpl(
    private val compilerApi: CompilerApi
) : CompilerRepository {
    
    override suspend fun runCode(request: CompilerRequest): Result<CompilerResponse> {
        return compilerApi.executeCode(request)
    }
}
