package com.visheshkumar.app.data.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages user preferences and coin system using SharedPreferences.
 * 
 * Phase 4: Implements strict coin rules to prevent guessing:
 * - Correct MCQ: +10 coins
 * - Wrong MCQ: -20 coins
 * - Coins never go below 0
 * - If coins < 20: 1 rewarded ad required
 * - Hint cost: 25 coins
 */
class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "visheshkumar_prefs"
        private const val KEY_COINS = "user_coins"
        private const val KEY_AD_WATCHED = "ad_watched"
        
        // Coin rules
        const val COINS_FOR_CORRECT_ANSWER = 10
        const val COINS_DEDUCTED_FOR_WRONG_ANSWER = 20
        const val MINIMUM_COINS_TO_CONTINUE = 20
        const val HINT_COST = 25
        const val INITIAL_COINS = 100 // Starting coins for new users
    }
    
    /**
     * Get current coin balance.
     * @return Current number of coins
     */
    fun getCoins(): Int {
        return prefs.getInt(KEY_COINS, INITIAL_COINS)
    }
    
    /**
     * Set coin balance (private - use specific methods instead).
     * Ensures coins never go below 0.
     */
    private fun setCoins(coins: Int) {
        val safeCoins = maxOf(0, coins) // Never allow negative coins
        prefs.edit().putInt(KEY_COINS, safeCoins).apply()
    }
    
    /**
     * Add coins for correct answer.
     * @return New coin balance
     */
    fun addCoinsForCorrectAnswer(): Int {
        val currentCoins = getCoins()
        val newCoins = currentCoins + COINS_FOR_CORRECT_ANSWER
        setCoins(newCoins)
        return newCoins
    }
    
    /**
     * Deduct coins for wrong answer.
     * @return New coin balance (never goes below 0)
     */
    fun deductCoinsForWrongAnswer(): Int {
        val currentCoins = getCoins()
        val newCoins = currentCoins - COINS_DEDUCTED_FOR_WRONG_ANSWER
        setCoins(newCoins)
        return maxOf(0, newCoins)
    }
    
    /**
     * Check if user has enough coins to continue (minimum 20).
     * @return True if coins >= 20, false otherwise
     */
    fun hasEnoughCoinsToC ontinue(): Boolean {
        return getCoins() >= MINIMUM_COINS_TO_CONTINUE
    }
    
    /**
     * Check if user has enough coins to use hint (25 coins).
     * @return True if coins >= 25, false otherwise
     */
    fun hasEnoughCoinsForHint(): Boolean {
        return getCoins() >= HINT_COST
    }
    
    /**
     * Deduct coins for using a hint.
     * @return New coin balance, or -1 if not enough coins
     */
    fun useHint(): Int {
        if (!hasEnoughCoinsForHint()) {
            return -1 // Not enough coins
        }
        
        val currentCoins = getCoins()
        val newCoins = currentCoins - HINT_COST
        setCoins(newCoins)
        return newCoins
    }
    
    /**
     * Mark that user has watched a rewarded ad.
     * This allows them to continue if they had less than 20 coins.
     */
    fun markAdWatched() {
        prefs.edit().putBoolean(KEY_AD_WATCHED, true).apply()
    }
    
    /**
     * Check if user has watched ad (allows bypass of coin requirement).
     * @return True if ad was watched, false otherwise
     */
    fun hasWatchedAd(): Boolean {
        return prefs.getBoolean(KEY_AD_WATCHED, false)
    }
    
    /**
     * Reset ad watched status (e.g., after user gets more coins).
     */
    fun resetAdWatched() {
        prefs.edit().putBoolean(KEY_AD_WATCHED, false).apply()
    }
    
    /**
     * Check if user needs to watch an ad to continue.
     * Required when coins < 20 and ad not yet watched.
     */
    fun needsToWatchAd(): Boolean {
        return !hasEnoughCoinsToC ontinue() && !hasWatchedAd()
    }
    
    /**
     * Add coins manually (e.g., for testing or bonus rewards).
     * @param amount Number of coins to add
     * @return New coin balance
     */
    fun addCoins(amount: Int): Int {
        val currentCoins = getCoins()
        val newCoins = currentCoins + amount
        setCoins(newCoins)
        return newCoins
    }
    
    /**
     * Reset all coin-related data (for testing or reset feature).
     */
    fun resetCoins() {
        setCoins(INITIAL_COINS)
        resetAdWatched()
    }
    
    /**
     * Get formatted coin display string.
     * @return String like "50 coins" or "1 coin"
     */
    fun getCoinsDisplayString(): String {
        val coins = getCoins()
        return if (coins == 1) "$coins coin" else "$coins coins"
    }
}
