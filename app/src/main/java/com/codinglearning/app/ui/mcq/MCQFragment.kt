package com.codinglearning.app.ui.mcq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.data.model.MCQQuestion
import com.codinglearning.app.data.model.MCQSessionStats
import com.codinglearning.app.data.repository.LevelRepository
import com.codinglearning.app.ui.result.ResultFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MCQFragment : Fragment() {
    
    private lateinit var prefsManager: PreferencesManager
    private lateinit var levelRepository: LevelRepository
    private var levelId: Int = 1
    private var questions: List<MCQQuestion> = emptyList()
    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0
    private var wrongAnswersCount = 0
    private var hintsUsedCount = 0
    private var totalCoinsEarned = 0
    private var sessionStartTime: Long = 0
    private var questionStartTime: Long = 0
    private val questionTimes = mutableListOf<Long>()
    private var rewardedAd: RewardedAd? = null
    private var bannerAdView: AdView? = null
    
    companion object {
        private const val ARG_LEVEL_ID = "level_id"
        
        fun newInstance(levelId: Int): MCQFragment {
            return MCQFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_LEVEL_ID, levelId)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        levelId = arguments?.getInt(ARG_LEVEL_ID, 1) ?: 1
        sessionStartTime = System.currentTimeMillis()
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mcq, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        levelRepository = LevelRepository(prefsManager)
        
        loadQuestions()
        loadRewardedAd()
        loadBannerAd()
        setupViews(view)
        displayQuestion()
    }
    
    private fun loadQuestions() {
        val selectedLanguage = prefsManager.getSelectedLanguage()
        val levels = levelRepository.getLevels(selectedLanguage)
        val level = levels.find { it.id == levelId }
        questions = level?.mcqQuestions ?: emptyList()
    }
    
    private fun setupViews(view: View) {
        view.findViewById<Button>(R.id.btn_submit_answer).setOnClickListener {
            checkAnswer(view)
        }
        
        view.findViewById<Button>(R.id.btn_get_hint).setOnClickListener {
            getHint(view)
        }
    }
    
    private fun displayQuestion() {
        if (currentQuestionIndex >= questions.size) {
            showResults()
            return
        }
        
        // Start timing for this question
        questionStartTime = System.currentTimeMillis()
        
        val question = questions[currentQuestionIndex]
        view?.let { v ->
            v.findViewById<TextView>(R.id.tv_question_number).text =
                getString(R.string.question_number, currentQuestionIndex + 1, questions.size)
            
            v.findViewById<TextView>(R.id.tv_question).text = question.question
            
            val radioGroup = v.findViewById<RadioGroup>(R.id.rg_options)
            radioGroup.removeAllViews()
            radioGroup.clearCheck()
            
            question.options.forEachIndexed { index, option ->
                val radioButton = RadioButton(requireContext()).apply {
                    id = View.generateViewId()
                    text = option
                    textSize = 16f
                    setPadding(16, 16, 16, 16)
                    setTextColor(resources.getColor(R.color.text_primary, null))
                }
                radioGroup.addView(radioButton)
            }
        }
    }
    
    private fun checkAnswer(view: View) {
        val radioGroup = view.findViewById<RadioGroup>(R.id.rg_options)
        val selectedId = radioGroup.checkedRadioButtonId
        
        if (selectedId == -1) {
            Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Record time taken for this question
        val questionTime = (System.currentTimeMillis() - questionStartTime) / 1000
        questionTimes.add(questionTime)
        
        val selectedIndex = radioGroup.indexOfChild(radioGroup.findViewById(selectedId))
        val question = questions[currentQuestionIndex]
        
        if (selectedIndex == question.correctAnswerIndex) {
            // Correct answer
            correctAnswersCount++
            totalCoinsEarned += question.coinsReward
            prefsManager.addCoins(question.coinsReward)
            
            Toast.makeText(
                requireContext(),
                getString(R.string.correct_answer, question.coinsReward),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Wrong answer
            wrongAnswersCount++
            Toast.makeText(
                requireContext(),
                getString(R.string.wrong_answer),
                Toast.LENGTH_SHORT
            ).show()
        }
        
        // Move to next question
        currentQuestionIndex++
        displayQuestion()
    }
    
    private fun getHint(view: View) {
        val hintCost = 25
        
        if (prefsManager.deductCoins(hintCost)) {
            hintsUsedCount++
            showHint()
        } else {
            // Not enough coins, show rewarded ad
            if (rewardedAd != null) {
                rewardedAd?.show(requireActivity()) { rewardItem ->
                    prefsManager.addCoins(hintCost)
                    hintsUsedCount++
                    showHint()
                    loadRewardedAd() // Load next ad
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.ad_not_ready),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun showHint() {
        val question = questions[currentQuestionIndex]
        Toast.makeText(requireContext(), "Hint: ${question.hint}", Toast.LENGTH_LONG).show()
    }
    
    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/5224354917", // Test rewarded ad unit
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
                
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }
    
    private fun loadBannerAd() {
        val adContainer = requireActivity().findViewById<ViewGroup>(R.id.ad_container)
        adContainer?.removeAllViews()
        
        bannerAdView = AdView(requireContext()).apply {
            adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test banner ad unit
            setAdSize(AdSize.BANNER)
        }
        
        adContainer?.addView(bannerAdView)
        
        val adRequest = AdRequest.Builder().build()
        bannerAdView?.loadAd(adRequest)
    }
    
    private fun showResults() {
        val totalTimeSeconds = (System.currentTimeMillis() - sessionStartTime) / 1000
        
        val sessionStats = MCQSessionStats(
            levelId = levelId,
            totalQuestions = questions.size,
            correctAnswers = correctAnswersCount,
            wrongAnswers = wrongAnswersCount,
            hintsUsed = hintsUsedCount,
            totalTimeSeconds = totalTimeSeconds,
            questionTimes = questionTimes
        )
        
        val starsEarned = sessionStats.calculateStars()
        
        val fragment = ResultFragment.newInstance(
            totalQuestions = questions.size,
            correctAnswers = correctAnswersCount,
            coinsEarned = totalCoinsEarned,
            levelId = levelId,
            starsEarned = starsEarned,
            hintsUsed = hintsUsedCount,
            timeSeconds = totalTimeSeconds
        )
        
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    override fun onResume() {
        super.onResume()
        bannerAdView?.resume()
    }
    
    override fun onPause() {
        bannerAdView?.pause()
        super.onPause()
    }
    
    override fun onDestroy() {
        bannerAdView?.destroy()
        super.onDestroy()
    }
}
