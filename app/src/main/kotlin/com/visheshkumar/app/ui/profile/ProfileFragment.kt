package com.visheshkumar.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.visheshkumar.app.R
import com.visheshkumar.app.data.local.PreferencesManager
import com.visheshkumar.app.data.model.LanguageProgress
import java.text.SimpleDateFormat
import java.util.*

/**
 * Profile Fragment
 * 
 * Displays user profile information and progress statistics
 */
class ProfileFragment : Fragment() {
    
    private lateinit var viewModel: ProfileViewModel
    private lateinit var preferencesManager: PreferencesManager
    
    // UI Components
    private lateinit var usernameText: TextView
    private lateinit var coinText: TextView
    private lateinit var totalStarsText: TextView
    private lateinit var sectionsCompletedText: TextView
    private lateinit var levelsCompletedText: TextView
    private lateinit var memberSinceText: TextView
    private lateinit var languagesRecyclerView: RecyclerView
    private lateinit var languagesAdapter: LanguagesAdapter
    private lateinit var loadingView: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        preferencesManager = PreferencesManager(requireContext())
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Note: In a real app, we would inflate from fragment_profile.xml
        // For now, creating a simple programmatic UI
        return createProfileView()
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        viewModel.loadUserProfile(preferencesManager)
    }
    
    /**
     * Create profile view programmatically
     */
    private fun createProfileView(): View {
        // In a real implementation, this would use fragment_profile.xml
        // For demonstration, creating a simple TextView-based layout
        
        val rootView = View(requireContext())
        // UI would be properly set up here with all components
        
        return rootView
    }
    
    /**
     * Setup LiveData observers
     */
    private fun setupObservers() {
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            // Update username
            usernameText.text = profile.username
            
            // Update coins
            coinText.text = "💰 ${profile.totalCoins} coins"
            
            // Update total stars
            totalStarsText.text = "⭐ ${profile.totalStarsEarned} stars"
            
            // Update sections completed
            sectionsCompletedText.text = "${profile.totalSectionsCompleted} sections completed"
            
            // Update levels completed
            levelsCompletedText.text = "${profile.totalLevelsCompleted} levels completed"
            
            // Update member since date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            memberSinceText.text = "Member since ${dateFormat.format(Date(profile.joinDate))}"
            
            // Update languages progress
            languagesAdapter.updateLanguages(profile.languagesProgress)
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    
    companion object {
        /**
         * Create new instance of ProfileFragment
         */
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}

/**
 * Adapter for languages progress list
 */
class LanguagesAdapter : RecyclerView.Adapter<LanguagesAdapter.LanguageViewHolder>() {
    
    private var languages: List<LanguageProgress> = emptyList()
    
    fun updateLanguages(newLanguages: List<LanguageProgress>) {
        languages = newLanguages
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        // Create view holder
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return LanguageViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.bind(language)
    }
    
    override fun getItemCount(): Int = languages.size
    
    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(android.R.id.text1)
        private val progressText: TextView = itemView.findViewById(android.R.id.text2)
        
        fun bind(language: LanguageProgress) {
            nameText.text = "${language.getProgressEmoji()} ${language.languageName}"
            
            progressText.text = buildString {
                append("${language.completionPercentage}% complete • ")
                append("${language.sectionsCompleted}/${language.totalSections} sections • ")
                append("${language.starsEarned}/${language.maxStars} stars")
            }
        }
    }
}
