package com.codinglearning.app.ui.trycode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.data.model.CodeExample
import com.codinglearning.app.data.repository.LevelRepository
import com.codinglearning.app.network.CodeExecutionRequest
import com.codinglearning.app.network.RetrofitClient
import com.codinglearning.app.ui.levels.LevelsFragment
import kotlinx.coroutines.launch

class TryCodeFragment : Fragment() {
    
    private var levelId = 1
    private var hasRunCode = false
    private lateinit var prefsManager: PreferencesManager
    private lateinit var levelRepository: LevelRepository
    private lateinit var codeExample: CodeExample
    
    companion object {
        private const val ARG_LEVEL_ID = "level_id"
        
        fun newInstance(levelId: Int): TryCodeFragment {
            return TryCodeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_LEVEL_ID, levelId)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        levelId = arguments?.getInt(ARG_LEVEL_ID, 1) ?: 1
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_try_code, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        levelRepository = LevelRepository(prefsManager)
        
        loadCodeExample()
        setupViews(view)
    }
    
    private fun loadCodeExample() {
        val selectedLanguage = prefsManager.getSelectedLanguage()
        val levels = levelRepository.getLevels(selectedLanguage)
        val level = levels.find { it.id == levelId }
        codeExample = level?.codeExample ?: CodeExample("", "", "", "")
    }
    
    private fun setupViews(view: View) {
        view.findViewById<TextView>(R.id.tv_code_title).text = codeExample.title
        view.findViewById<TextView>(R.id.tv_code_description).text = codeExample.description
        view.findViewById<EditText>(R.id.et_code).setText(codeExample.code)
        
        view.findViewById<Button>(R.id.btn_run_code).setOnClickListener {
            runCode(view)
        }
        
        view.findViewById<Button>(R.id.btn_next).setOnClickListener {
            if (hasRunCode) {
                completeLevel()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.must_run_code),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun runCode(view: View) {
        val code = view.findViewById<EditText>(R.id.et_code).text.toString()
        val outputTextView = view.findViewById<TextView>(R.id.tv_output)
        val runButton = view.findViewById<Button>(R.id.btn_run_code)
        
        runButton.isEnabled = false
        outputTextView.text = getString(R.string.loading)
        
        lifecycleScope.launch {
            try {
                // In production, this would call the actual API
                // For now, we'll simulate the output
                val simulatedOutput = simulateCodeExecution(code, codeExample.language)
                outputTextView.text = simulatedOutput
                hasRunCode = true
                
                // Note: In production, uncomment below to use real API
                /*
                val request = CodeExecutionRequest(
                    code = code,
                    language = codeExample.language
                )
                val response = RetrofitClient.compilerApi.executeCode(request)
                outputTextView.text = response.output ?: response.error ?: "No output"
                hasRunCode = true
                */
            } catch (e: Exception) {
                outputTextView.text = "Error: ${e.message}"
            } finally {
                runButton.isEnabled = true
            }
        }
    }
    
    private fun simulateCodeExecution(code: String, language: String): String {
        // Simple simulation for demonstration
        // In production, this would be handled by Firebase Cloud Functions
        return when {
            code.contains("print") && language.lowercase() == "python" -> {
                "Hello, World!\nWelcome to Python programming!"
            }
            code.contains("println") && language.lowercase() == "java" -> {
                "Learning Java"
            }
            code.contains("console.log") && language.lowercase() == "javascript" -> {
                "Jane\n25"
            }
            code.contains("println") && language.lowercase() == "kotlin" -> {
                "Hello, Kotlin!"
            }
            code.contains("cout") && language.lowercase() == "c++" -> {
                "Hello, C++!"
            }
            else -> "Code executed successfully!"
        }
    }
    
    private fun completeLevel() {
        // Mark level as completed and award coins
        prefsManager.completeLevel(levelId)
        
        val selectedLanguage = prefsManager.getSelectedLanguage()
        val levels = levelRepository.getLevels(selectedLanguage)
        val level = levels.find { it.id == levelId }
        level?.let {
            prefsManager.addCoins(it.coinsReward)
        }
        
        Toast.makeText(
            requireContext(),
            getString(R.string.level_complete),
            Toast.LENGTH_SHORT
        ).show()
        
        // Navigate back to levels
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LevelsFragment())
            .commit()
    }
}
