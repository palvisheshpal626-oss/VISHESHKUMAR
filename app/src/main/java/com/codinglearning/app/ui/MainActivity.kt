package com.codinglearning.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codinglearning.app.R
import com.codinglearning.app.data.local.PreferencesManager
import com.codinglearning.app.ui.home.HomeFragment
import com.codinglearning.app.ui.language.LanguageSelectionFragment
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class MainActivity : AppCompatActivity() {
    
    private lateinit var prefsManager: PreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        prefsManager = PreferencesManager(this)
        
        // Initialize Mobile Ads SDK
        initializeAds()
        
        if (savedInstanceState == null) {
            navigateToInitialScreen()
        }
    }
    
    private fun initializeAds() {
        MobileAds.initialize(this) {}
        
        // Set test device for development
        val testDeviceIds = listOf("YOUR_TEST_DEVICE_ID")
        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(testDeviceIds)
            .build()
        MobileAds.setRequestConfiguration(configuration)
    }
    
    private fun navigateToInitialScreen() {
        val selectedLanguage = prefsManager.getSelectedLanguage()
        
        val fragment = if (selectedLanguage.isEmpty()) {
            // No language selected, show language selection
            LanguageSelectionFragment()
        } else {
            // Language already selected, show home
            HomeFragment()
        }
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
