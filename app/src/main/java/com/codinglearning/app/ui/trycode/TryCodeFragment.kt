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
        
        if (code.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Please write some code first",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        
        runButton.isEnabled = false
        outputTextView.text = "Running code..."
        
        lifecycleScope.launch {
            try {
                val result = executeCodeWithPiston(code, codeExample.language)
                outputTextView.text = result
                hasRunCode = true
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("Unable to resolve host") == true -> 
                        "Network error: Please check your internet connection"
                    e.message?.contains("timeout") == true -> 
                        "Request timeout: The code took too long to execute"
                    else -> "Error: ${e.message}"
                }
                outputTextView.text = errorMessage
            } finally {
                runButton.isEnabled = true
            }
        }
    }
    
    private suspend fun executeCodeWithPiston(code: String, language: String): String {
        val languageId = RetrofitClient.getPistonLanguageId(language)
        val version = RetrofitClient.getPistonLanguageVersion(language)
        val fileName = RetrofitClient.getFileName(language)
        
        val request = com.codinglearning.app.network.PistonExecuteRequest(
            language = languageId,
            version = version,
            files = listOf(
                com.codinglearning.app.network.PistonFile(
                    name = fileName,
                    content = code
                )
            ),
            stdin = "",
            args = emptyList(),
            compile_timeout = 10000,
            run_timeout = 3000
        )
        
        val response = RetrofitClient.compilerApi.executePiston(request)
        
        return buildOutputString(response)
    }
    
    private fun buildOutputString(response: com.codinglearning.app.network.PistonExecuteResponse): String {
        val output = StringBuilder()
        
        // Check for compilation errors first
        response.compile?.let { compile ->
            if (compile.code != 0 || compile.stderr.isNotEmpty()) {
                output.append("Compilation Error:\n")
                output.append(compile.stderr.ifEmpty { compile.output })
                return output.toString()
            }
        }
        
        // Check runtime output
        val runOutput = response.run
        
        when {
            runOutput.code != 0 && runOutput.stderr.isNotEmpty() -> {
                output.append("Runtime Error:\n")
                output.append(runOutput.stderr)
            }
            runOutput.stdout.isNotEmpty() -> {
                output.append(runOutput.stdout)
            }
            runOutput.output.isNotEmpty() -> {
                output.append(runOutput.output)
            }
            else -> {
                output.append("Code executed successfully with no output.")
            }
        }
        
        return output.toString().trim()
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
