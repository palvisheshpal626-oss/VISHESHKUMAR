# VISHESHKUMAR - Phase 4: Coin & Hint System

## Overview
Phase 4 implements a strict coin system to prevent random guessing and encourage learning. The system rewards correct answers and penalizes wrong ones, with a hint feature to help users.

## Coin Rules

### Earning & Losing Coins
- **Correct Answer**: +10 coins
- **Wrong Answer**: -20 coins
- **Minimum Coins**: 0 (never goes negative)
- **Continue Requirement**: Minimum 20 coins
- **Hint Cost**: 25 coins

### Key Principles
1. **Prevents Guessing**: Wrong answers cost more (-20) than correct answers earn (+10)
2. **Never Negative**: Coins are clamped to 0 minimum
3. **Ad Gate**: Users with < 20 coins must watch a rewarded ad to continue
4. **Hint System**: Costs 25 coins, eliminates one wrong answer

## Implementation

### 1. PreferencesManager
**File**: `app/src/main/kotlin/com/visheshkumar/app/data/local/PreferencesManager.kt`

**Purpose**: Manages coin balance and ad state using SharedPreferences.

**Key Methods**:
```kotlin
// Get current coins
fun getCoins(): Int

// Reward correct answer
fun addCoinsForCorrectAnswer(): Int  // +10 coins

// Penalize wrong answer
fun deductCoinsForWrongAnswer(): Int  // -20 coins, min 0

// Check if user can continue
fun hasEnoughCoinsToC ontinue(): Boolean  // coins >= 20

// Check if user can afford hint
fun hasEnoughCoinsForHint(): Boolean  // coins >= 25

// Use hint (deduct 25 coins)
fun useHint(): Int  // Returns -1 if insufficient

// Check if ad is required
fun needsToWatchAd(): Boolean  // !hasEnoughCoins && !hasWatchedAd

// Mark ad as watched
fun markAdWatched()

// Reset coins (testing/bonus)
fun resetCoins()
```

**Constants**:
```kotlin
COINS_FOR_CORRECT_ANSWER = 10
COINS_DEDUCTED_FOR_WRONG_ANSWER = 20
MINIMUM_COINS_TO_CONTINUE = 20
HINT_COST = 25
INITIAL_COINS = 100  // Starting balance
```

### 2. MCQFragment Updates
**File**: `app/src/main/kotlin/com/visheshkumar/app/ui/mcq/MCQFragment.kt`

**Changes**:

#### Added Fields
```kotlin
private lateinit var preferencesManager: PreferencesManager
private lateinit var coinText: TextView
private lateinit var hintButton: MaterialButton
```

#### New Methods
```kotlin
// Handle answer with coin logic
private fun handleAnswerSubmission(isCorrect: Boolean)

// Update coin display
private fun updateCoinDisplay()

// Check coin requirement before quiz
private fun checkCoinRequirement()

// Show warning when low on coins
private fun showLowCoinsWarning()

// Show ad required dialog
private fun showAdRequiredDialog()

// Use hint (eliminate wrong answer)
private fun useHint()

// Show toast messages
private fun showMessage(message: String)
```

#### Flow
1. **onCreate**: Initialize PreferencesManager, check coin requirement
2. **Answer Submission**: Apply coin rules, show messages, update display
3. **Hint Usage**: Check coins, deduct 25, eliminate wrong option
4. **Low Coins**: Show warning, prompt for ad if < 20

### 3. Layout Updates
**File**: `app/src/main/res/layout/fragment_mcq.xml`

**Added Components**:

#### Coin Display
```xml
<TextView
    android:id="@+id/coinText"
    android:text="💰 100 coins"
    android:textColor="@color/primary"
    app:layout_constraintTop_toTopOf="parent"
    app:layout_constraintEnd_toEndOf="parent"/>
```

#### Hint Button
```xml
<MaterialButton
    android:id="@+id/hintButton"
    style="@style/Widget.VISHESHKUMAR.Button.Outlined"
    android:text="💡 Hint (25)"
    android:textSize="12sp"/>
```

**Layout Changes**:
- Coin display positioned at top right
- Progress text at top left
- Hint button at bottom left
- Submit/Next buttons at bottom right

## User Experience

### Coin Feedback Messages
- **Correct**: "Correct! +10 coins"
- **Wrong**: "Wrong! -20 coins"
- **Hint Used**: "Hint used! -25 coins. One wrong answer eliminated."
- **Not Enough Coins (Hint)**: "Not enough coins! Hint costs 25 coins."
- **Low Coins Warning**: "Warning: You need at least 20 coins to continue. Watch an ad to proceed!"
- **Ad Required**: "You need at least 20 coins to continue. Please watch a rewarded ad."

### Visual Feedback
- **Coin Display**: Updates in real-time after each action
- **Hint Button**: 
  - Enabled when coins >= 25
  - Disabled (50% opacity) when coins < 25
- **Eliminated Option**: 
  - 30% opacity
  - Not clickable

## Example Scenarios

### Scenario 1: Normal Gameplay
```
Start: 100 coins
Answer correctly: 100 + 10 = 110 coins
Answer correctly: 110 + 10 = 120 coins
Use hint: 120 - 25 = 95 coins
Answer correctly: 95 + 10 = 105 coins
```

### Scenario 2: Incorrect Answers
```
Start: 100 coins
Answer wrong: 100 - 20 = 80 coins
Answer wrong: 80 - 20 = 60 coins
Answer wrong: 60 - 20 = 40 coins
Answer wrong: 40 - 20 = 20 coins (can continue)
Answer wrong: 20 - 20 = 0 coins (ad required!)
```

### Scenario 3: Strategic Hint Usage
```
Start: 100 coins
Use hint: 100 - 25 = 75 coins
Answer correctly: 75 + 10 = 85 coins
Use hint: 85 - 25 = 60 coins
Answer correctly: 60 + 10 = 70 coins
```

### Scenario 4: Coins Never Go Negative
```
Start: 100 coins
Answer wrong: 100 - 20 = 80 coins
Answer wrong: 80 - 20 = 60 coins
...
Answer wrong: 10 - 20 = -10 → Clamped to 0 coins
Next attempt requires ad!
```

## Files Created/Modified

### New Files (1)
1. `app/src/main/kotlin/com/visheshkumar/app/data/local/PreferencesManager.kt`

### Modified Files (2)
2. `app/src/main/kotlin/com/visheshkumar/app/ui/mcq/MCQFragment.kt`
3. `app/src/main/res/layout/fragment_mcq.xml`

### Documentation/Testing (1)
4. `app/src/test/kotlin/com/visheshkumar/app/test/CoinSystemTest.kt`
5. `PHASE4_COIN_SYSTEM.md` (this file)

## Data Persistence

### SharedPreferences Keys
- `user_coins`: Current coin balance (Int)
- `ad_watched`: Whether user has watched ad (Boolean)

### Storage Location
- Name: `"visheshkumar_prefs"`
- Mode: `Context.MODE_PRIVATE`

### Data Lifecycle
- **First Launch**: 100 coins (INITIAL_COINS)
- **Persistent**: Survives app restarts
- **Reset**: Call `resetCoins()` to restore to 100

## Future Enhancements

### Possible Additions
1. **Rewarded Ads Integration**
   - Integrate Google AdMob
   - Actually show rewarded video ads
   - Grant 50 bonus coins after ad

2. **Bonus Coins**
   - Daily login bonus
   - Streak rewards
   - Achievement coins

3. **Coin Shop**
   - Buy hints in bulk
   - Unlock premium features
   - Customize avatar

4. **Analytics**
   - Track coin earnings/spending
   - User behavior insights
   - Difficulty balancing

5. **Social Features**
   - Leaderboard based on coins
   - Gift coins to friends
   - Coin challenges

## Testing

### Manual Test Cases

**Test 1: Correct Answer Coins**
1. Start quiz (100 coins)
2. Answer correctly
3. Verify: +10 coins (110 total)
4. Verify: Toast "Correct! +10 coins"

**Test 2: Wrong Answer Coins**
1. Start quiz (100 coins)
2. Answer incorrectly
3. Verify: -20 coins (80 total)
4. Verify: Toast "Wrong! -20 coins"

**Test 3: Coins Never Negative**
1. Set coins to 10
2. Answer incorrectly
3. Verify: Coins = 0 (not -10)
4. Verify: Ad required message

**Test 4: Hint Usage**
1. Start quiz (100 coins)
2. Press Hint button
3. Verify: -25 coins (75 total)
4. Verify: One option disabled
5. Verify: Toast "Hint used! -25 coins..."

**Test 5: Insufficient Coins for Hint**
1. Set coins to 20
2. Press Hint button
3. Verify: Coins unchanged
4. Verify: Toast "Not enough coins..."

**Test 6: Low Coins Warning**
1. Set coins to 25
2. Answer incorrectly (25-20=5)
3. Verify: Warning toast shown
4. Verify: Ad required for next question

### Automated Tests
See `CoinSystemTest.kt` for logical verification of:
- Coin addition/subtraction
- Minimum clamp to 0
- Hint availability
- Continue requirements

## Design Integration

### Material Design 3
- Coin display uses primary color
- Hint button uses outlined style
- Toast messages for feedback
- Consistent with Phase 2 design system

### Accessibility
- Clear numeric coin display
- Icon (💰) for visual recognition
- Toast messages for screen readers
- Button states (enabled/disabled)

## Security Considerations

### Local Storage
- SharedPreferences is not secure
- Users can modify values with root access
- For production: Consider server-side validation

### Recommendations for Production
1. **Server Verification**
   - Validate coin changes on backend
   - Prevent client-side manipulation

2. **Encryption**
   - Encrypt SharedPreferences
   - Use Android Keystore

3. **Anti-Cheat**
   - Detect rooted devices
   - Monitor unusual coin patterns
   - Rate limit actions

## Summary

Phase 4 successfully implements:
✅ Strict coin system (+10 correct, -20 wrong)
✅ Coins never go below 0
✅ Continue requirement (20 coins minimum)
✅ Hint feature (25 coins, eliminates wrong answer)
✅ Ad requirement when low on coins
✅ Clear user messages via Toasts
✅ Persistent storage via SharedPreferences
✅ Updated UI with coin display and hint button

**Status**: ✅ COMPLETE
**Ready for**: Phase 5 (Screen Navigation & Integration)
