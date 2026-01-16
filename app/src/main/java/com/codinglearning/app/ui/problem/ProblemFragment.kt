package com.codinglearning.app.ui.problem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.data.model.Problem
import com.codinglearning.app.data.model.TestCase
import com.codinglearning.app.network.CompilerApiService
import com.codinglearning.app.network.PistonExecuteRequest
import com.codinglearning.app.network.PistonFile
import com.codinglearning.app.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProblemFragment : Fragment() {

    private lateinit var prefsManager: PreferencesManager
    private lateinit var compilerService: CompilerApiService
    private lateinit var problem: Problem
    
    private lateinit var titleTextView: TextView
    private lateinit var difficultyBadge: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var testCasesTextView: TextView
    private lateinit var codeEditor: EditText
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var resultContainer: LinearLayout
    private lateinit var resultTextView: TextView
    
    private var problemStartTime: Long = 0

    companion object {
        private const val ARG_PROBLEM = "problem"

        fun newInstance(problem: Problem): ProblemFragment {
            val fragment = ProblemFragment()
            val args = Bundle()
            // In production, pass problem ID and fetch from repository
            // For now, we'll need to reconstruct the problem
            args.putString("problem_id", problem.id)
            args.putString("title", problem.title)
            args.putString("description", problem.description)
            args.putString("difficulty", problem.difficulty.name)
            args.putString("language", problem.language)
            args.putString("starter_code", problem.starterCode)
            args.putInt("coins_reward", problem.coinsReward)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_problem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        compilerService = RetrofitClient.compilerService
        
        // Reconstruct problem from arguments
        problem = reconstructProblemFromArgs()
        
        // Start timing when problem screen is displayed
        problemStartTime = System.currentTimeMillis()
        
        initializeViews(view)
        displayProblemDetails()
        setupSubmitButton()
    }

    private fun reconstructProblemFromArgs(): Problem {
        val args = requireArguments()
        // Note: Test cases would need to be passed or fetched from repository
        // For this implementation, we'll use sample test cases based on problem
        val testCases = getSampleTestCases(args.getString("problem_id", ""))
        
        return Problem(
            id = args.getString("problem_id", ""),
            title = args.getString("title", ""),
            description = args.getString("description", ""),
            difficulty = com.codinglearning.app.data.model.Difficulty.valueOf(
                args.getString("difficulty", "EASY")
            ),
            language = args.getString("language", ""),
            starterCode = args.getString("starter_code", ""),
            testCases = testCases,
            coinsReward = args.getInt("coins_reward", 30)
        )
    }

    private fun getSampleTestCases(problemId: String): List<TestCase> {
        // Sample test cases - in production these would come from the repository
        return when (problemId) {
            "py_1_print_name" -> listOf(
                TestCase(input = "", expectedOutput = "Your Name")
            )
            "py_2_calculate_sum" -> listOf(
                TestCase(input = "", expectedOutput = "8")
            )
            else -> listOf(
                TestCase(input = "", expectedOutput = "Output")
            )
        }
    }

    private fun initializeViews(view: View) {
        titleTextView = view.findViewById(R.id.problemTitle)
        difficultyBadge = view.findViewById(R.id.difficultyBadge)
        descriptionTextView = view.findViewById(R.id.problemDescription)
        testCasesTextView = view.findViewById(R.id.testCasesText)
        codeEditor = view.findViewById(R.id.codeEditor)
        submitButton = view.findViewById(R.id.submitButton)
        progressBar = view.findViewById(R.id.progressBar)
        resultContainer = view.findViewById(R.id.resultContainer)
        resultTextView = view.findViewById(R.id.resultText)
    }

    private fun displayProblemDetails() {
        titleTextView.text = problem.title
        difficultyBadge.text = problem.difficulty.name
        descriptionTextView.text = problem.description
        
        // Display test cases
        val testCasesText = buildString {
            append("Test Cases:\n\n")
            problem.testCases.forEachIndexed { index, testCase ->
                append("Test ${index + 1}:\n")
                append("Input: ${testCase.input.ifEmpty { "(none)" }}\n")
                append("Expected Output: ${testCase.expectedOutput}\n\n")
            }
        }
        testCasesTextView.text = testCasesText
        
        // Pre-fill editor with starter code
        codeEditor.setText(problem.starterCode)
        
        // Check if already completed
        if (prefsManager.isProblemCompleted(problem.id)) {
            submitButton.text = "Completed ✓ (Retry)"
        }
    }

    private fun setupSubmitButton() {
        submitButton.setOnClickListener {
            val code = codeEditor.text.toString()
            
            if (code.isBlank()) {
                Toast.makeText(requireContext(), "Please write some code!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            submitSolution(code)
        }
    }

    private fun submitSolution(code: String) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                
                val validationResult = validateSolution(code)
                
                showLoading(false)
                
                if (validationResult.allPassed) {
                    handleSuccess()
                } else {
                    handleFailure(validationResult)
                }
                
            } catch (e: Exception) {
                showLoading(false)
                showError("Error: ${e.message}")
            }
        }
    }

    private suspend fun validateSolution(code: String): ValidationResult = withContext(Dispatchers.IO) {
        val passedTests = mutableListOf<Int>()
        val failedTests = mutableListOf<FailedTest>()
        
        problem.testCases.forEachIndexed { index, testCase ->
            try {
                val output = executeCode(code, problem.language)
                val actualOutput = output.trim()
                val expectedOutput = testCase.expectedOutput.trim()
                
                if (actualOutput == expectedOutput) {
                    passedTests.add(index)
                } else {
                    failedTests.add(FailedTest(
                        index = index,
                        input = testCase.input,
                        expected = expectedOutput,
                        actual = actualOutput
                    ))
                }
            } catch (e: Exception) {
                failedTests.add(FailedTest(
                    index = index,
                    input = testCase.input,
                    expected = testCase.expectedOutput,
                    actual = "Error: ${e.message}"
                ))
            }
        }
        
        ValidationResult(
            allPassed = failedTests.isEmpty(),
            passedCount = passedTests.size,
            totalCount = problem.testCases.size,
            failedTests = failedTests
        )
    }

    private suspend fun executeCode(code: String, language: String): String {
        val languageVersion = RetrofitClient.getLanguageVersion(language)
        val fileName = RetrofitClient.getFileName(language)
        
        val request = PistonExecuteRequest(
            language = language.lowercase(),
            version = languageVersion,
            files = listOf(PistonFile(name = fileName, content = code)),
            stdin = "",
            args = emptyList(),
            compile_timeout = 10000,
            run_timeout = 3000,
            compile_memory_limit = -1,
            run_memory_limit = -1
        )
        
        val response = compilerService.execute(request)
        
        return when {
            response.compile?.code != 0 && response.compile?.code != null -> {
                "Compilation Error:\n${response.compile.stderr}"
            }
            response.run.code != 0 -> {
                "Runtime Error:\n${response.run.stderr}"
            }
            else -> {
                response.run.stdout
            }
        }
    }

    private fun handleSuccess() {
        val wasAlreadyCompleted = prefsManager.isProblemCompleted(problem.id)
        
        // Calculate time-based coin reward
        val timeElapsedSeconds = (System.currentTimeMillis() - problemStartTime) / 1000
        val coinsEarned = if (!wasAlreadyCompleted && timeElapsedSeconds <= 60) 10 else 0
        
        if (!wasAlreadyCompleted) {
            prefsManager.completeProblem(problem.id)
            if (coinsEarned > 0) {
                prefsManager.addCoins(coinsEarned)
            }
        }
        
        val message = buildString {
            append("✓ SUCCESS!\n\n")
            append("All test cases passed!\n")
            append("Passed: ${problem.testCases.size}/${problem.testCases.size}\n\n")
            append("Time: ${timeElapsedSeconds}s\n\n")
            
            if (!wasAlreadyCompleted) {
                if (coinsEarned > 0) {
                    append("Coins earned: +${coinsEarned}\n")
                    append("(Solved within 1 minute!)")
                } else {
                    append("Time exceeded 1 minute\n")
                    append("No coins awarded")
                }
            } else {
                append("(Already completed - no additional coins)")
            }
        }
        
        resultTextView.text = message
        resultContainer.visibility = View.VISIBLE
        
        if (!wasAlreadyCompleted) {
            submitButton.text = "Completed ✓ (Retry)"
        }
        
        val toastMessage = if (!wasAlreadyCompleted) {
            if (coinsEarned > 0) "Problem solved! +${coinsEarned} coins (solved in ${timeElapsedSeconds}s)" 
            else "Problem solved! (no coins - time exceeded 1 minute)"
        } else {
            "Problem completed again!"
        }
        
        Toast.makeText(
            requireContext(),
            toastMessage,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun handleFailure(result: ValidationResult) {
        val message = buildString {
            append("✗ Some tests failed\n\n")
            append("Passed: ${result.passedCount}/${result.totalCount}\n\n")
            
            result.failedTests.forEach { failed ->
                append("Test ${failed.index + 1} Failed:\n")
                append("Input: ${failed.input.ifEmpty { "(none)" }}\n")
                append("Expected: ${failed.expected}\n")
                append("Your Output: ${failed.actual}\n\n")
            }
            
            append("No coins deducted. Try again!")
        }
        
        resultTextView.text = message
        resultContainer.visibility = View.VISIBLE
        
        Toast.makeText(requireContext(), "Some tests failed. Try again!", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        resultTextView.text = message
        resultContainer.visibility = View.VISIBLE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        submitButton.isEnabled = !show
        submitButton.text = if (show) "Validating..." else {
            if (prefsManager.isProblemCompleted(problem.id)) "Completed ✓ (Retry)" else "Submit Solution"
        }
    }

    data class ValidationResult(
        val allPassed: Boolean,
        val passedCount: Int,
        val totalCount: Int,
        val failedTests: List<FailedTest>
    )

    data class FailedTest(
        val index: Int,
        val input: String,
        val expected: String,
        val actual: String
    )
}
