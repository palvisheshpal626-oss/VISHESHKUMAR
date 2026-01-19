package com.visheshkumar.app.ui.problem

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.visheshkumar.app.data.local.PreferencesManager
import com.visheshkumar.app.data.model.Problem
import com.visheshkumar.app.data.repository.CompilerRepository
import com.visheshkumar.app.data.source.ProblemDataSource

/**
 * Fragment for solving coding problems with time-based rewards.
 * 
 * Reward Rules:
 * - First solve within 1 minute: +10 coins
 * - First solve after 1 minute: 0 coins
 * - Replay/re-solve: 0 coins (no matter the time)
 * 
 * This encourages users to:
 * 1. Think carefully before coding
 * 2. Solve problems efficiently
 * 3. Move on to new challenges rather than replaying old ones
 */
class ProblemFragment : Fragment() {
    
    private lateinit var viewModel: ProblemViewModel
    private lateinit var preferencesManager: PreferencesManager
    
    // UI Components
    private lateinit var problemTitleText: TextView
    private lateinit var problemDescriptionText: TextView
    private lateinit var difficultyBadge: TextView
    private lateinit var timerText: TextView
    private lateinit var coinDisplay: TextView
    private lateinit var codeEditor: EditText
    private lateinit var outputText: TextView
    private lateinit var runButton: Button
    private lateinit var submitButton: Button
    private lateinit var resetButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var testCasesLayout: LinearLayout
    
    // Timer tracking
    private var startTime: Long = 0L
    private var isTimerRunning = false
    private var currentProblem: Problem? = null
    
    companion object {
        private const val ARG_LEVEL_ID = "level_id"
        private const val ARG_PROBLEM_NUMBER = "problem_number"
        private const val TIME_LIMIT_MS = 60_000L // 1 minute in milliseconds
        
        fun newInstance(levelId: String, problemNumber: Int = 1): ProblemFragment {
            val fragment = ProblemFragment()
            val args = Bundle()
            args.putString(ARG_LEVEL_ID, levelId)
            args.putInt(ARG_PROBLEM_NUMBER, problemNumber)
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())
        viewModel = ViewModelProvider(this)[ProblemViewModel::class.java]
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Note: This would normally inflate from fragment_problem.xml
        // For now, creating a simple programmatic UI
        val context = requireContext()
        val rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
        
        // Initialize UI components programmatically
        problemTitleText = TextView(context).apply {
            textSize = 20f
            setPadding(0, 0, 0, 8)
        }
        
        difficultyBadge = TextView(context).apply {
            textSize = 14f
            setPadding(8, 4, 8, 4)
        }
        
        timerText = TextView(context).apply {
            textSize = 16f
            setPadding(0, 8, 0, 8)
        }
        
        coinDisplay = TextView(context).apply {
            textSize = 16f
            setPadding(0, 0, 0, 8)
        }
        
        problemDescriptionText = TextView(context).apply {
            textSize = 14f
            setPadding(0, 8, 0, 16)
        }
        
        codeEditor = EditText(context).apply {
            hint = "Write your code here..."
            minLines = 10
            setPadding(8, 8, 8, 8)
        }
        
        runButton = Button(context).apply {
            text = "Run Code"
        }
        
        submitButton = Button(context).apply {
            text = "Submit Solution"
        }
        
        resetButton = Button(context).apply {
            text = "Reset"
        }
        
        outputText = TextView(context).apply {
            textSize = 14f
            setPadding(8, 8, 8, 8)
        }
        
        progressBar = ProgressBar(context).apply {
            visibility = View.GONE
        }
        
        testCasesLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        
        // Add all views to root layout
        rootLayout.addView(problemTitleText)
        rootLayout.addView(difficultyBadge)
        rootLayout.addView(timerText)
        rootLayout.addView(coinDisplay)
        rootLayout.addView(problemDescriptionText)
        rootLayout.addView(codeEditor)
        rootLayout.addView(runButton)
        rootLayout.addView(submitButton)
        rootLayout.addView(resetButton)
        rootLayout.addView(progressBar)
        rootLayout.addView(outputText)
        rootLayout.addView(testCasesLayout)
        
        return rootLayout
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        loadProblem()
    }
    
    private fun setupUI() {
        updateCoinDisplay()
        
        runButton.setOnClickListener {
            runCode()
        }
        
        submitButton.setOnClickListener {
            submitSolution()
        }
        
        resetButton.setOnClickListener {
            resetCode()
        }
    }
    
    private fun loadProblem() {
        val levelId = arguments?.getString(ARG_LEVEL_ID) ?: return
        val problemNumber = arguments?.getInt(ARG_PROBLEM_NUMBER, 1) ?: 1
        
        val problemId = "${levelId}_problem_$problemNumber"
        val problem = ProblemDataSource.getProblemById(problemId) ?: return
        
        currentProblem = problem
        displayProblem(problem)
        startTimer()
    }
    
    private fun displayProblem(problem: Problem) {
        problemTitleText.text = problem.title
        problemDescriptionText.text = problem.description
        difficultyBadge.text = problem.difficulty.uppercase()
        codeEditor.setText(problem.starterCode)
        
        // Show if this is a replay
        if (problem.hasBeenSolved) {
            Toast.makeText(
                requireContext(),
                "⚠️ Replay: No coins will be awarded",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    private fun startTimer() {
        startTime = SystemClock.elapsedRealtime()
        isTimerRunning = true
        updateTimer()
    }
    
    private fun updateTimer() {
        if (!isTimerRunning) return
        
        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        val seconds = (elapsedTime / 1000).toInt()
        val minutes = seconds / 60
        val secs = seconds % 60
        
        timerText.text = String.format("⏱️ Time: %02d:%02d", minutes, secs)
        
        // Update every second
        timerText.postDelayed({ updateTimer() }, 1000)
    }
    
    private fun stopTimer(): Long {
        isTimerRunning = false
        return SystemClock.elapsedRealtime() - startTime
    }
    
    private fun runCode() {
        val code = codeEditor.text.toString()
        if (code.isBlank()) {
            Toast.makeText(requireContext(), "Please write some code first", Toast.LENGTH_SHORT).show()
            return
        }
        
        progressBar.visibility = View.VISIBLE
        outputText.text = "Running code..."
        
        // Simulate code execution (in real app, would use CompilerRepository)
        outputText.postDelayed({
            progressBar.visibility = View.GONE
            outputText.text = "Output:\nplaceholder\n\n(This is placeholder execution)"
        }, 1000)
    }
    
    private fun submitSolution() {
        val problem = currentProblem ?: return
        val elapsedTime = stopTimer()
        
        val code = codeEditor.text.toString()
        if (code.isBlank() || code == problem.starterCode) {
            Toast.makeText(
                requireContext(),
                "Please write a solution first",
                Toast.LENGTH_SHORT
            ).show()
            startTimer() // Resume timer
            return
        }
        
        progressBar.visibility = View.VISIBLE
        outputText.text = "Checking solution..."
        
        // Simulate solution check
        outputText.postDelayed({
            progressBar.visibility = View.GONE
            checkSolutionAndAwardCoins(problem, elapsedTime)
        }, 1500)
    }
    
    private fun checkSolutionAndAwardCoins(problem: Problem, solveTime: Long) {
        // For placeholder: assume solution is correct
        val isCorrect = true
        
        if (!isCorrect) {
            outputText.text = "❌ Incorrect solution. Try again!"
            Toast.makeText(
                requireContext(),
                "Solution failed test cases",
                Toast.LENGTH_SHORT
            ).show()
            startTimer() // Resume timer
            return
        }
        
        // Solution is correct - apply time-based reward rules
        val coinsEarned = calculateCoinsForSolve(problem, solveTime)
        
        if (coinsEarned > 0) {
            val newBalance = preferencesManager.addCoins(coinsEarned)
            updateCoinDisplay()
            
            val timeSeconds = (solveTime / 1000.0)
            Toast.makeText(
                requireContext(),
                "✅ Correct! Solved in %.1fs → +%d coins".format(timeSeconds, coinsEarned),
                Toast.LENGTH_LONG
            ).show()
            
            outputText.text = "✅ Solution Accepted!\n\n" +
                    "Time: %.1f seconds\n".format(timeSeconds) +
                    "Coins earned: +$coinsEarned\n" +
                    "New balance: 💰 $newBalance coins"
        } else {
            val timeSeconds = (solveTime / 1000.0)
            val reason = when {
                problem.hasBeenSolved -> "Replay"
                solveTime > TIME_LIMIT_MS -> "Time limit exceeded"
                else -> "Unknown"
            }
            
            Toast.makeText(
                requireContext(),
                "✅ Correct! ($reason → 0 coins)",
                Toast.LENGTH_LONG
            ).show()
            
            outputText.text = "✅ Solution Accepted!\n\n" +
                    "Time: %.1f seconds\n".format(timeSeconds) +
                    "Coins earned: 0 ($reason)\n" +
                    "Current balance: 💰 ${preferencesManager.getCoins()} coins"
        }
        
        // Mark problem as solved
        // In a real app, this would be persisted to database
    }
    
    /**
     * Calculate coins earned based on solve time and replay status.
     * 
     * Rules:
     * - First solve ≤ 1 minute: +10 coins
     * - First solve > 1 minute: 0 coins
     * - Replay: 0 coins
     */
    private fun calculateCoinsForSolve(problem: Problem, solveTime: Long): Int {
        // Rule 1: Replays get 0 coins
        if (problem.hasBeenSolved) {
            return 0
        }
        
        // Rule 2: First solve within time limit gets 10 coins
        if (solveTime <= TIME_LIMIT_MS) {
            return 10
        }
        
        // Rule 3: First solve after time limit gets 0 coins
        return 0
    }
    
    private fun resetCode() {
        val problem = currentProblem ?: return
        codeEditor.setText(problem.starterCode)
        outputText.text = ""
        Toast.makeText(requireContext(), "Code reset to starter template", Toast.LENGTH_SHORT).show()
    }
    
    private fun updateCoinDisplay() {
        val coins = preferencesManager.getCoins()
        coinDisplay.text = "💰 $coins coins"
    }
}
