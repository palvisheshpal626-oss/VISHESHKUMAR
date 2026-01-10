package com.codinglearning.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.ui.levels.LevelsFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class HomeFragment : Fragment() {
    
    private lateinit var prefsManager: PreferencesManager
    private var adView: AdView? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        
        setupViews(view)
        loadBannerAd(view)
    }
    
    private fun setupViews(view: View) {
        val coinsTextView = view.findViewById<TextView>(R.id.tv_coins)
        val languageTextView = view.findViewById<TextView>(R.id.tv_selected_language)
        val startButton = view.findViewById<Button>(R.id.btn_start_learning)
        
        // Update UI with user data
        val userProgress = prefsManager.getUserProgress()
        coinsTextView.text = userProgress.coins.toString()
        languageTextView.text = userProgress.selectedLanguage
        
        startButton.setOnClickListener {
            navigateToLevels()
        }
    }
    
    private fun loadBannerAd(view: View) {
        val adContainer = requireActivity().findViewById<ViewGroup>(R.id.ad_container)
        adContainer?.removeAllViews()
        
        adView = AdView(requireContext()).apply {
            adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test banner ad unit
            setAdSize(AdSize.BANNER)
        }
        
        adContainer?.addView(adView)
        
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }
    
    private fun navigateToLevels() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LevelsFragment())
            .addToBackStack(null)
            .commit()
    }
    
    override fun onResume() {
        super.onResume()
        adView?.resume()
        
        // Refresh coin count when returning to home
        view?.findViewById<TextView>(R.id.tv_coins)?.text = prefsManager.getCoins().toString()
    }
    
    override fun onPause() {
        adView?.pause()
        super.onPause()
    }
    
    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }
}
