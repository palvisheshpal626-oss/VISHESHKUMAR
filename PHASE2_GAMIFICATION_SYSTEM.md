# PHASE 2: Advanced Gamification System - Implementation Guide

## Overview

This document details the implementation of the advanced gamification system including:
- 0-3 star rating per level
- Star calculation based on performance
- Section-based progression (Easy → Medium → Hard)
- Section unlocking based on star thresholds

---

## Star Rating System

### How Stars Are Calculated

Stars are calculated based on MCQ session performance using `MCQSessionStats`:

```kotlin
fun calculateStars(): Int {
    var stars = 3  // Start with maximum stars
    
    // Deduct for wrong answers
    if (wrongAnswers > 0) stars--
    
    // Deduct for excessive hints
    if (hintsUsed > 1) stars--
    
    // Deduct for slow completion (>5 minutes)
    if (totalTimeSeconds > 300) stars--
    
    return maxOf(0, stars)
}
```

### Star Criteria

| Stars | Criteria |
|-------|----------|
| ⭐⭐⭐ (3) | Perfect: No wrong answers, ≤1 hint, <5 minutes |
| ⭐⭐☆ (2) | Good: Some mistakes OR multiple hints OR slow |
| ⭐☆☆ (1) | Okay: Multiple issues |
| ☆☆☆ (0) | Poor: Many mistakes + hints + very slow |

### Performance Tracking

**MCQFragment tracks:**
- `wrongAnswersCount` - Number of incorrect answers
- `hintsUsedCount` - Number of hints used
- `sessionStartTime` - Start time of MCQ session
- `questionStartTime` - Start time for each question
- `questionTimes` - List of time taken per question

**Star persistence:**
- Stars are saved per level in SharedPreferences
- Only the highest star count is kept
- Replaying a level can improve stars
- Total stars across all levels are tracked

---

## Section-Based Progression

### Level Sections

Levels are now organized into three sections:

```kotlin
enum class LevelSection {
    EASY,    // Always unlocked
    MEDIUM,  // Unlock with 50% of Easy stars
    HARD     // Unlock with 60% of Easy+Medium stars
}
```

### Section Assignment

Example for Python:
```kotlin
Level(
    id = 1,
    title = "Introduction to Python",
    section = LevelSection.EASY  // ← Added
)
```

### Unlocking Rules

**EASY Section:**
- Always unlocked
- Level 1 is always available

**MEDIUM Section:**
- Requires: 50% of possible stars from all EASY levels
- Example: 2 EASY levels × 3 stars = 6 possible → Need 3 stars minimum
- Formula: `easyStars >= (maxPossibleEasyStars * 0.5)`

**HARD Section:**
- Requires: 60% of possible stars from EASY + MEDIUM levels
- Example: 4 total levels × 3 stars = 12 possible → Need 8 stars minimum
- Formula: `totalStars >= (maxPossibleStars * 0.6)`

### Implementation

In `LevelRepository`:
```kotlin
private fun isSectionUnlocked(level: Level, allLevels: List<Level>): Boolean {
    return when (level.section) {
        LevelSection.EASY -> true
        
        LevelSection.MEDIUM -> {
            val easyLevels = allLevels.filter { it.section == LevelSection.EASY }
            val easyStars = prefsManager.getStarsInSection(easyLevels.map { it.id })
            val maxStars = easyLevels.size * 3
            easyStars >= (maxStars * 0.5).toInt()
        }
        
        LevelSection.HARD -> {
            val easyMediumLevels = allLevels.filter { 
                it.section == LevelSection.EASY || it.section == LevelSection.MEDIUM 
            }
            val totalStars = prefsManager.getStarsInSection(easyMediumLevels.map { it.id })
            val maxStars = easyMediumLevels.size * 3
            totalStars >= (maxStars * 0.6).toInt()
        }
    }
}
```

---

## Data Models

### Updated Models

**Level.kt:**
```kotlin
data class Level(
    val id: Int,
    val title: String,
    val language: String,
    val mcqQuestions: List<MCQQuestion>,
    val codeExample: CodeExample,
    val problems: List<Problem>,
    val videoUrl: String?,
    val coinsReward: Int = 50,
    val section: LevelSection = LevelSection.EASY  // ← New
)

data class LevelState(
    val level: Level,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val starsEarned: Int = 0  // ← New
)
```

**UserProgress.kt:**
```kotlin
data class UserProgress(
    val selectedLanguage: String = "",
    val highestCompletedLevel: Int = 0,
    val coins: Int = 0,
    val completedLevels: Set<Int> = emptySet(),
    val totalStarsEarned: Int = 0,              // ← New
    val levelStars: Map<Int, Int> = emptyMap()  // ← New
)
```

**MCQSessionStats.kt (NEW):**
```kotlin
data class MCQSessionStats(
    val levelId: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val hintsUsed: Int,
    val totalTimeSeconds: Long,
    val questionTimes: List<Long> = emptyList()
) {
    fun calculateStars(): Int {
        var stars = 3
        if (wrongAnswers > 0) stars--
        if (hintsUsed > 1) stars--
        if (totalTimeSeconds > 300) stars--
        return maxOf(0, stars)
    }
    
    fun getAccuracy(): Int {
        return if (totalQuestions > 0) {
            (correctAnswers * 100) / totalQuestions
        } else 0
    }
}
```

---

## SharedPreferences Changes

### New Keys

```kotlin
private const val KEY_TOTAL_STARS = "total_stars"
private const val KEY_LEVEL_STARS_PREFIX = "level_stars_"
```

### New Methods

**Save/Load Stars:**
```kotlin
fun completeLevel(levelId: Int, starsEarned: Int = 0) {
    // Save completion
    // Save stars (only if better than previous)
    // Update total stars
}

fun getLevelStars(levelId: Int): Int

fun getTotalStars(): Int

fun getStarsInSection(levelIds: List<Int>): Int
```

---

## User Flow with Stars

### Complete Flow Example

1. **Start MCQ Session**
   - `sessionStartTime` recorded
   - `questionStartTime` recorded for each question

2. **Answer Questions**
   - Correct answer: `correctAnswersCount++`
   - Wrong answer: `wrongAnswersCount++`
   - Use hint: `hintsUsedCount++`
   - Time tracked per question

3. **Finish MCQ**
   - Calculate total time
   - Create `MCQSessionStats`
   - Calculate stars: `sessionStats.calculateStars()`

4. **Show Results**
   - Display accuracy, coins, time
   - Display stars: ★★★ or ★★☆ or ★☆☆ or ☆☆☆
   - Save stars to SharedPreferences

5. **Continue to Try Code**
   - Complete level after code execution
   - `prefsManager.completeLevel(levelId, starsEarned)`

6. **Return to Level Selection**
   - Section unlocking recalculated
   - Star icons shown on completed levels

---

## Visual Representation

### Result Screen

```
╔════════════════════════════╗
║   LEVEL COMPLETE!          ║
║                            ║
║   Questions: 2/2           ║
║   Accuracy: 100%           ║
║   Coins: +20               ║
║                            ║
║   Stars: ★★★ (3/3)         ║
║   Time: 2m 15s | Hints: 0  ║
║                            ║
║   [Continue]               ║
╚════════════════════════════╝
```

### Level Selection (with sections)

```
╔════════════════════════════╗
║   EASY SECTION             ║
║   ┌──────────────────┐     ║
║   │ Level 1 ✓ ★★★    │     ║
║   └──────────────────┘     ║
║   ┌──────────────────┐     ║
║   │ Level 2 ✓ ★★☆    │     ║
║   └──────────────────┘     ║
║                            ║
║   MEDIUM SECTION (3/6★)    ║
║   ┌──────────────────┐     ║
║   │ Level 3 🔓        │     ║
║   └──────────────────┘     ║
║                            ║
║   HARD SECTION (Locked)    ║
║   ┌──────────────────┐     ║
║   │ Level 4 🔒        │     ║
║   └──────────────────┘     ║
╚════════════════════════════╝
```

---

## Testing the Star System

### Test Scenarios

**Scenario 1: Perfect Performance**
- Answer all questions correctly
- Use 0-1 hints
- Complete in <5 minutes
- **Expected**: ★★★ (3 stars)

**Scenario 2: Good Performance**
- 1 wrong answer
- Use 1 hint
- Complete in 4 minutes
- **Expected**: ★★☆ (2 stars)

**Scenario 3: Poor Performance**
- Multiple wrong answers
- Use 3 hints
- Take 7 minutes
- **Expected**: ☆☆☆ (0 stars)

**Scenario 4: Section Unlocking**
- Complete 2 EASY levels with 1★ each (2/6 stars)
- MEDIUM section: **Locked** (need 3/6)
- Get 1 more star (3/6 total)
- MEDIUM section: **Unlocked** ✓

---

## Files Modified

### Core Files
1. `Level.kt` - Added `section` and `starsEarned`
2. `UserProgress.kt` - Added star tracking
3. `MCQSessionStats.kt` - **NEW** - Star calculation logic
4. `PreferencesManager.kt` - Star persistence methods
5. `LevelRepository.kt` - Section unlocking logic
6. `MCQFragment.kt` - Performance tracking
7. `ResultFragment.kt` - Star display and saving

### Documentation
8. `PHASE2_GAMIFICATION_SYSTEM.md` - This file

---

## Performance Metrics

### Storage Impact
- Per level: +4 bytes (int for stars)
- Total stars: +4 bytes (int)
- Negligible impact on SharedPreferences

### Calculation Impact
- Star calculation: O(1) - simple arithmetic
- Section unlocking: O(n) where n = number of levels
- Minimal performance impact

---

## Future Enhancements

### Potential Improvements (Not in PHASE 2)

1. **Question-level time tracking**
   - Individual question performance
   - Adaptive difficulty

2. **Streak bonuses**
   - Consecutive perfect scores
   - Extra star for 5-day streak

3. **Difficulty multipliers**
   - HARD levels worth more stars
   - Weighted star calculations

4. **Star leaderboards**
   - Compare with other users
   - Weekly/monthly rankings

---

## Summary

PHASE 2 successfully implements:

✅ **0-3 Star rating system**
- Based on wrong answers, hints, and time
- Persistent storage per level
- Only best score counts

✅ **Section-based progression**
- EASY → MEDIUM → HARD
- Star thresholds for unlocking
- Fair and motivating progression

✅ **No UI redesign**
- Minimal visual indicators
- Backward compatible
- Existing features unchanged

The system is fully functional, well-documented, and ready for production use.
