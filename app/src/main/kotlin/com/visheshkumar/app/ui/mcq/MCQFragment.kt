package com.visheshkumar.app.ui.mcq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.visheshkumar.app.R
import com.visheshkumar.app.data.model.MCQ

/**
 * Fragment for displaying and interacting with MCQs.
 * 
 * Phase 3: Shows placeholder MCQs with basic interaction.
 * Future: Will include scoring, timer, hints, and result tracking.
 */
class MCQFragment : Fragment() {
    
    private val viewModel: MCQViewModel by viewModels()
    
    // UI Components
    private lateinit var progressText: TextView
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var questionText: TextView
    private lateinit var optionCards: List<MaterialCardView>
    private lateinit var optionTexts: List<TextView>
    private lateinit var explanationCard: MaterialCardView
    private lateinit var explanationText: TextView
    private lateinit var submitButton: MaterialButton
    private lateinit var nextButton: MaterialButton
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mcq, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize UI components
        initializeViews(view)
        
        // Set up click listeners
        setupClickListeners()
        
        // Observe ViewModel
        observeViewModel()
        
        // Load MCQs for a test level (e.g., kotlin_section_1_level_1)
        // In real implementation, this would come from navigation arguments
        val testLevelId = arguments?.getString(ARG_LEVEL_ID) ?: "kotlin_section_1_level_1"
        viewModel.loadMCQs(testLevelId)
    }
    
    /**
     * Initialize all view references.
     */
    private fun initializeViews(view: View) {
        progressText = view.findViewById(R.id.progressText)
        progressBar = view.findViewById(R.id.progressBar)
        questionText = view.findViewById(R.id.questionText)
        
        val optionA: MaterialCardView = view.findViewById(R.id.optionA)
        val optionB: MaterialCardView = view.findViewById(R.id.optionB)
        val optionC: MaterialCardView = view.findViewById(R.id.optionC)
        val optionD: MaterialCardView = view.findViewById(R.id.optionD)
        optionCards = listOf(optionA, optionB, optionC, optionD)
        
        val optionAText: TextView = view.findViewById(R.id.optionAText)
        val optionBText: TextView = view.findViewById(R.id.optionBText)
        val optionCText: TextView = view.findViewById(R.id.optionCText)
        val optionDText: TextView = view.findViewById(R.id.optionDText)
        optionTexts = listOf(optionAText, optionBText, optionCText, optionDText)
        
        explanationCard = view.findViewById(R.id.explanationCard)
        explanationText = view.findViewById(R.id.explanationText)
        
        submitButton = view.findViewById(R.id.submitButton)
        nextButton = view.findViewById(R.id.nextButton)
    }
    
    /**
     * Set up click listeners for interactive elements.
     */
    private fun setupClickListeners() {
        // Option card clicks
        optionCards.forEachIndexed { index, card ->
            card.setOnClickListener {
                viewModel.selectAnswer(index)
            }
        }
        
        // Submit button
        submitButton.setOnClickListener {
            val isCorrect = viewModel.submitAnswer()
            showAnswerResult(isCorrect)
        }
        
        // Next button
        nextButton.setOnClickListener {
            val hasNext = viewModel.nextQuestion()
            if (!hasNext) {
                // Quiz completed
                showQuizComplete()
            }
        }
    }
    
    /**
     * Observe ViewModel LiveData.
     */
    private fun observeViewModel() {
        // Observe current MCQ
        viewModel.currentMCQ.observe(viewLifecycleOwner) { mcq ->
            mcq?.let { displayMCQ(it) }
        }
        
        // Observe selected answer
        viewModel.selectedAnswerIndex.observe(viewLifecycleOwner) { selectedIndex ->
            updateOptionSelection(selectedIndex)
            submitButton.isEnabled = selectedIndex != null
        }
        
        // Observe answer submission state
        viewModel.isAnswerSubmitted.observe(viewLifecycleOwner) { isSubmitted ->
            optionCards.forEach { it.isEnabled = !isSubmitted }
            submitButton.visibility = if (isSubmitted) View.GONE else View.VISIBLE
            nextButton.visibility = if (isSubmitted) View.VISIBLE else View.GONE
        }
        
        // Observe progress
        viewModel.currentQuestionIndex.observe(viewLifecycleOwner) { index ->
            updateProgress()
        }
    }
    
    /**
     * Display an MCQ on the screen.
     */
    private fun displayMCQ(mcq: MCQ) {
        questionText.text = mcq.questionText
        
        mcq.options.forEachIndexed { index, option ->
            if (index < optionTexts.size) {
                optionTexts[index].text = option
            }
        }
        
        // Reset UI state
        resetUIState()
    }
    
    /**
     * Update option selection visual feedback.
     */
    private fun updateOptionSelection(selectedIndex: Int?) {
        optionCards.forEachIndexed { index, card ->
            if (index == selectedIndex) {
                card.strokeColor = ContextCompat.getColor(requireContext(), R.color.primary)
                card.strokeWidth = 4
            } else {
                card.strokeColor = ContextCompat.getColor(requireContext(), R.color.card_stroke)
                card.strokeWidth = 1
            }
        }
    }
    
    /**
     * Show the result after submitting an answer.
     */
    private fun showAnswerResult(isCorrect: Boolean) {
        val selectedIndex = viewModel.selectedAnswerIndex.value ?: return
        val currentMCQ = viewModel.currentMCQ.value ?: return
        
        // Highlight correct and incorrect answers
        optionCards.forEachIndexed { index, card ->
            when {
                index == currentMCQ.correctAnswerIndex -> {
                    // Correct answer - green
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.success_light)
                    )
                }
                index == selectedIndex && !isCorrect -> {
                    // Wrong answer - red
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.error_light)
                    )
                }
            }
        }
        
        // Show explanation
        explanationCard.visibility = View.VISIBLE
        explanationText.text = currentMCQ.explanation ?: "Explanation will be added later"
    }
    
    /**
     * Update progress indicators.
     */
    private fun updateProgress() {
        val current = viewModel.getCurrentProgress()
        val total = viewModel.getTotalQuestions()
        
        progressText.text = "Question $current of $total"
        
        val progressPercentage = if (total > 0) {
            ((current.toFloat() / total) * 100).toInt()
        } else {
            0
        }
        progressBar.progress = progressPercentage
    }
    
    /**
     * Reset UI state for a new question.
     */
    private fun resetUIState() {
        // Reset option cards
        optionCards.forEach { card ->
            card.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.card_background)
            )
            card.strokeColor = ContextCompat.getColor(requireContext(), R.color.card_stroke)
            card.strokeWidth = 1
            card.isEnabled = true
        }
        
        // Hide explanation
        explanationCard.visibility = View.GONE
        
        // Reset buttons
        submitButton.isEnabled = false
        submitButton.visibility = View.VISIBLE
        nextButton.visibility = View.GONE
    }
    
    /**
     * Show quiz completion screen.
     */
    private fun showQuizComplete() {
        val correctCount = viewModel.correctAnswersCount.value ?: 0
        val totalCount = viewModel.getTotalQuestions()
        
        // In a real implementation, this would navigate to a results screen
        // For now, just show a placeholder message
        questionText.text = "Quiz Complete!\n\nScore: $correctCount / $totalCount"
        
        // Hide options and buttons
        optionCards.forEach { it.visibility = View.GONE }
        submitButton.visibility = View.GONE
        nextButton.visibility = View.GONE
    }
    
    companion object {
        private const val ARG_LEVEL_ID = "level_id"
        
        /**
         * Create a new instance of MCQFragment with a specific level ID.
         * 
         * @param levelId The ID of the level to load MCQs for
         * @return A new MCQFragment instance
         */
        fun newInstance(levelId: String): MCQFragment {
            return MCQFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LEVEL_ID, levelId)
                }
            }
        }
    }
}
