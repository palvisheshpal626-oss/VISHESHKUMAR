package com.vishesh.codinglearning.domain.usecase

import com.vishesh.codinglearning.data.api.CompilerRequest
import com.vishesh.codinglearning.data.api.CompilerResponse
import com.vishesh.codinglearning.data.repository.CompilerRepository

class RunCodeUseCase(
    private val compilerRepository: CompilerRepository
) {
    suspend operator fun invoke(request: CompilerRequest): Result<CompilerResponse> {
        return compilerRepository.runCode(request)
    }
}
