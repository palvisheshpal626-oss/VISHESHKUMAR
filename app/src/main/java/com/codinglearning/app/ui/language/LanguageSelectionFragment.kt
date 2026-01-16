package com.codinglearning.app.ui.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.ui.home.HomeFragment

class LanguageSelectionFragment : Fragment() {
    
    private lateinit var prefsManager: PreferencesManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_language_selection, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        
        setupLanguageCards(view)
    }
    
    private fun setupLanguageCards(view: View) {
        view.findViewById<CardView>(R.id.card_python).setOnClickListener {
            selectLanguage("Python")
        }
        
        view.findViewById<CardView>(R.id.card_java).setOnClickListener {
            selectLanguage("Java")
        }
        
        view.findViewById<CardView>(R.id.card_javascript).setOnClickListener {
            selectLanguage("JavaScript")
        }
        
        view.findViewById<CardView>(R.id.card_kotlin).setOnClickListener {
            selectLanguage("Kotlin")
        }
        
        view.findViewById<CardView>(R.id.card_cpp).setOnClickListener {
            selectLanguage("C++")
        }
    }
    
    private fun selectLanguage(language: String) {
        prefsManager.setSelectedLanguage(language)
        
        // Navigate to Home
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }
}
