package com.codinglearning.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager

class ProfileFragment : Fragment() {

    private lateinit var prefsManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        
        loadUserStatistics(view)
    }

    private fun loadUserStatistics(view: View) {
        // User info
        val userName = "Learner" // Can be made customizable via PreferencesManager
        val totalCoins = prefsManager.getCoins()
        val selectedLanguage = prefsManager.getSelectedLanguage() ?: "Python"
        
        view.findViewById<TextView>(R.id.tvUserName).text = "User: $userName"
        view.findViewById<TextView>(R.id.tvTotalCoins).text = "💰 $totalCoins Coins"
        view.findViewById<TextView>(R.id.tvSelectedLanguage).text = "📚 Language: $selectedLanguage"
        
        // Calculate overall progress
        val totalStars = prefsManager.getTotalStars()
        val maxPossibleStars = 500 * 3 // 500 levels × 3 stars
        val overallProgress = if (maxPossibleStars > 0) {
            (totalStars * 100) / maxPossibleStars
        } else 0
        
        view.findViewById<TextView>(R.id.tvOverallProgress).text = "Overall Progress: $overallProgress%"
        view.findViewById<TextView>(R.id.tvTotalStars).text = "⭐ Total Stars: $totalStars"
        
        // Language-wise progress
        loadLanguageProgress(view, "Python", R.id.tvPythonProgress)
        loadLanguageProgress(view, "Java", R.id.tvJavaProgress)
        loadLanguageProgress(view, "JavaScript", R.id.tvJavaScriptProgress)
        loadLanguageProgress(view, "Kotlin", R.id.tvKotlinProgress)
        loadLanguageProgress(view, "C++", R.id.tvCppProgress)
        
        // Achievement summary
        loadAchievementSummary(view)
    }

    private fun loadLanguageProgress(view: View, language: String, textViewId: Int) {
        val highestLevel = prefsManager.getHighestCompletedLevel(language)
        val sections = (highestLevel / 10) + if (highestLevel % 10 > 0) 1 else 0
        val percentage = (highestLevel * 100) / 100 // Out of 100 levels
        
        val icon = when(language) {
            "Python" -> "🐍"
            "Java" -> "☕"
            "JavaScript" -> "📜"
            "Kotlin" -> "🟣"
            "C++" -> "⚙️"
            else -> "📝"
        }
        
        val progressText = "$icon $language: $highestLevel/100 levels ($sections/10 sections) - $percentage%"
        view.findViewById<TextView>(textViewId)?.text = progressText
    }

    private fun loadAchievementSummary(view: View) {
        val totalStars = prefsManager.getTotalStars()
        
        // Show total stats
        view.findViewById<TextView>(R.id.tvAchievementSummary)?.text = 
            "Total Achievements: $totalStars stars earned across all levels"
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
