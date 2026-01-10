package com.vishesh.codinglearning.ui.problem

import com.vishesh.codinglearning.data.model.Difficulty
import com.vishesh.codinglearning.data.model.Problem
import com.vishesh.codinglearning.data.model.ProblemSubmitResult

data class ProblemState(
    val problems: List<Problem> = emptyList(),
    val selectedProblem: Problem? = null,
    val code: String = "",
    val isSubmitting: Boolean = false,
    val submitResult: ProblemSubmitResult? = null,
    val selectedDifficulty: Difficulty? = null
)
