package com.visheshkumar.app.ui.compiler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.card.MaterialCardView

/**
 * Fragment for trying code with real execution
 * 
 * Features:
 * - Code editor (multiline EditText)
 * - Optional stdin input
 * - Run button to execute code
 * - Output display (stdout, stderr, errors)
 * - Support for 17 languages via Piston API
 * - Learning mode for HTML, CSS, SQL
 */
class TryCodeFragment : Fragment() {
    
    private val viewModel: CompilerViewModel by viewModels()
    
    // UI Components
    private lateinit var languageText: TextView
    private lateinit var codeEditor: EditText
    private lateinit var stdinEditor: EditText
    private lateinit var runButton: Button
    private lateinit var clearButton: Button
    private lateinit var resetButton: Button
    private lateinit var outputText: TextView
    private lateinit var outputCard: MaterialCardView
    private lateinit var progressBar: ProgressBar
    private lateinit var executionStatusText: TextView
    
    companion object {
        private const val ARG_LANGUAGE_ID = "language_id"
        
        /**
         * Create new instance with language ID
         */
        fun newInstance(languageId: String): TryCodeFragment {
            val fragment = TryCodeFragment()
            val args = Bundle()
            args.putString(ARG_LANGUAGE_ID, languageId)
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            requireContext().resources.getIdentifier(
                "fragment_try_code",
                "layout",
                requireContext().packageName
            ),
            container,
            false
        )
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize UI components
        initializeViews(view)
        
        // Get language ID from arguments
        val languageId = arguments?.getString(ARG_LANGUAGE_ID) ?: "kotlin"
        
        // Set language name in header
        languageText.text = "Try ${languageId.replaceFirstChar { it.uppercase() }}"
        
        // Initialize ViewModel with language
        viewModel.setLanguage(languageId)
        
        // Setup observers
        setupObservers()
        
        // Setup click listeners
        setupClickListeners()
        
        // Load default template
        loadDefaultTemplate()
    }
    
    private fun initializeViews(view: View) {
        languageText = view.findViewById(
            view.context.resources.getIdentifier("languageText", "id", view.context.packageName)
        )
        codeEditor = view.findViewById(
            view.context.resources.getIdentifier("codeEditor", "id", view.context.packageName)
        )
        stdinEditor = view.findViewById(
            view.context.resources.getIdentifier("stdinEditor", "id", view.context.packageName)
        )
        runButton = view.findViewById(
            view.context.resources.getIdentifier("runButton", "id", view.context.packageName)
        )
        clearButton = view.findViewById(
            view.context.resources.getIdentifier("clearButton", "id", view.context.packageName)
        )
        resetButton = view.findViewById(
            view.context.resources.getIdentifier("resetButton", "id", view.context.packageName)
        )
        outputText = view.findViewById(
            view.context.resources.getIdentifier("outputText", "id", view.context.packageName)
        )
        outputCard = view.findViewById(
            view.context.resources.getIdentifier("outputCard", "id", view.context.packageName)
        )
        progressBar = view.findViewById(
            view.context.resources.getIdentifier("progressBar", "id", view.context.packageName)
        )
        executionStatusText = view.findViewById(
            view.context.resources.getIdentifier("executionStatusText", "id", view.context.packageName)
        )
    }
    
    private fun setupObservers() {
        // Execution state
        viewModel.isExecuting.observe(viewLifecycleOwner) { isExecuting ->
            progressBar.visibility = if (isExecuting) View.VISIBLE else View.GONE
            runButton.isEnabled = !isExecuting
            runButton.text = if (isExecuting) "Running..." else "▶ Run Code"
        }
        
        // Execution result
        viewModel.executionResult.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                outputText.text = result
                outputCard.visibility = View.VISIBLE
            }
        }
        
        // Error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        
        // Execution support
        viewModel.supportsExecution.observe(viewLifecycleOwner) { supportsExecution ->
            if (supportsExecution) {
                executionStatusText.text = "✓ Real execution supported"
                executionStatusText.setTextColor(
                    resources.getColor(
                        resources.getIdentifier("success", "color", requireContext().packageName),
                        null
                    )
                )
            } else {
                executionStatusText.text = "Learning mode only"
                executionStatusText.setTextColor(
                    resources.getColor(
                        resources.getIdentifier("warning", "color", requireContext().packageName),
                        null
                    )
                )
            }
            
            runButton.isEnabled = supportsExecution
        }
    }
    
    private fun setupClickListeners() {
        runButton.setOnClickListener {
            val code = codeEditor.text.toString()
            val stdin = stdinEditor.text.toString()
            
            viewModel.updateCode(code)
            viewModel.updateStdin(stdin)
            viewModel.executeCode()
        }
        
        clearButton.setOnClickListener {
            viewModel.clearOutput()
            outputCard.visibility = View.GONE
        }
        
        resetButton.setOnClickListener {
            loadDefaultTemplate()
            viewModel.resetCode()
        }
    }
    
    private fun loadDefaultTemplate() {
        codeEditor.setText(viewModel.getDefaultCodeTemplate())
    }
}
