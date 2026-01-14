package com.codinglearning.app.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.ui.trycode.TryCodeFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class ResultFragment : Fragment() {
    
    private var totalQuestions = 0
    private var correctAnswers = 0
    private var coinsEarned = 0
    private var levelId = 1
    private var starsEarned = 0
    private var hintsUsed = 0
    private var timeSeconds: Long = 0
    private var interstitialAd: InterstitialAd? = null
    private lateinit var prefsManager: PreferencesManager
    
    companion object {
        private const val ARG_TOTAL = "total_questions"
        private const val ARG_CORRECT = "correct_answers"
        private const val ARG_COINS = "coins_earned"
        private const val ARG_LEVEL_ID = "level_id"
        private const val ARG_STARS = "stars_earned"
        private const val ARG_HINTS = "hints_used"
        private const val ARG_TIME = "time_seconds"
        
        fun newInstance(
            totalQuestions: Int,
            correctAnswers: Int,
            coinsEarned: Int,
            levelId: Int,
            starsEarned: Int = 0,
            hintsUsed: Int = 0,
            timeSeconds: Long = 0
        ): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TOTAL, totalQuestions)
                    putInt(ARG_CORRECT, correctAnswers)
                    putInt(ARG_COINS, coinsEarned)
                    putInt(ARG_LEVEL_ID, levelId)
                    putInt(ARG_STARS, starsEarned)
                    putInt(ARG_HINTS, hintsUsed)
                    putLong(ARG_TIME, timeSeconds)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalQuestions = it.getInt(ARG_TOTAL, 0)
            correctAnswers = it.getInt(ARG_CORRECT, 0)
            coinsEarned = it.getInt(ARG_COINS, 0)
            levelId = it.getInt(ARG_LEVEL_ID, 1)
            starsEarned = it.getInt(ARG_STARS, 0)
            hintsUsed = it.getInt(ARG_HINTS, 0)
            timeSeconds = it.getLong(ARG_TIME, 0)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        
        // Save stars to preferences (only if better than previous)
        if (starsEarned > 0) {
            prefsManager.completeLevel(levelId, starsEarned)
        }
        
        displayResults(view)
        loadInterstitialAd()
        
        view.findViewById<Button>(R.id.btn_continue).setOnClickListener {
            showAdAndContinue()
        }
    }
    
    private fun displayResults(view: View) {
        val accuracy = if (totalQuestions > 0) {
            (correctAnswers * 100) / totalQuestions
        } else 0
        
        view.findViewById<TextView>(R.id.tv_total_questions).text =
            getString(R.string.total_questions, totalQuestions)
        
        view.findViewById<TextView>(R.id.tv_correct_answers).text =
            getString(R.string.correct_answers, correctAnswers)
        
        view.findViewById<TextView>(R.id.tv_accuracy).text =
            getString(R.string.accuracy, accuracy)
        
        view.findViewById<TextView>(R.id.tv_coins_earned).text =
            getString(R.string.coins_earned, coinsEarned)
        
        // Display stars with visual representation
        val starsText = "★".repeat(starsEarned) + "☆".repeat(3 - starsEarned)
        view.findViewById<TextView>(R.id.tv_stars_earned)?.text = 
            "Stars: $starsText ($starsEarned/3)"
        
        // Display performance info
        val minutes = timeSeconds / 60
        val seconds = timeSeconds % 60
        view.findViewById<TextView>(R.id.tv_performance_info)?.text =
            "Time: ${minutes}m ${seconds}s | Hints: $hintsUsed"
    }
    
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/1033173712", // Test interstitial ad unit
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
                
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }
    
    private fun showAdAndContinue() {
        if (interstitialAd != null) {
            interstitialAd?.show(requireActivity())
            interstitialAd = null
            continueToNextScreen()
        } else {
            continueToNextScreen()
        }
    }
    
    private fun continueToNextScreen() {
        // Navigate to Try Code screen
        val fragment = TryCodeFragment.newInstance(levelId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
