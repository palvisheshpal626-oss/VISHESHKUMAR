package com.codinglearning.app.ui.levels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.data.model.LevelState
import com.codinglearning.app.data.repository.LevelRepository
import com.codinglearning.app.ui.mcq.MCQFragment

class LevelsFragment : Fragment() {
    
    private lateinit var prefsManager: PreferencesManager
    private lateinit var levelRepository: LevelRepository
    private lateinit var adapter: LevelAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_levels, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefsManager = PreferencesManager(requireContext())
        levelRepository = LevelRepository(prefsManager)
        
        setupRecyclerView(view)
        loadLevels()
    }
    
    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_levels)
        adapter = LevelAdapter { levelState ->
            onLevelClicked(levelState)
        }
        
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
    
    private fun loadLevels() {
        val selectedLanguage = prefsManager.getSelectedLanguage()
        val levelStates = levelRepository.getLevelStates(selectedLanguage)
        adapter.submitList(levelStates)
    }
    
    private fun onLevelClicked(levelState: LevelState) {
        if (levelState.isUnlocked) {
            // Navigate to MCQ screen for this level
            val fragment = MCQFragment.newInstance(levelState.level.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            // Show locked message or skip dialog
            // TODO: Implement skip level functionality
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh levels when returning
        loadLevels()
    }
}
