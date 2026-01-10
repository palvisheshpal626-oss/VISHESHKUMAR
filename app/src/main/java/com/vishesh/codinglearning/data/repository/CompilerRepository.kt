package com.vishesh.codinglearning.data.repository

import com.vishesh.codinglearning.data.api.CompilerApi
import com.vishesh.codinglearning.data.api.CompilerRequest
import com.vishesh.codinglearning.data.api.CompilerResponse

interface CompilerRepository {
    suspend fun runCode(request: CompilerRequest): Result<CompilerResponse>
}
