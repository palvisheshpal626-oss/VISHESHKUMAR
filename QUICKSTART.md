# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Prerequisites
```bash
✅ Android Studio installed
✅ JDK 11+ installed
✅ Git installed
```

### Step 2: Clone Repository
```bash
git clone https://github.com/palvisheshpal626-oss/VISHESHKUMAR.git
cd VISHESHKUMAR
```

### Step 3: Open in Android Studio
1. Launch Android Studio
2. File → Open
3. Select the VISHESHKUMAR directory
4. Wait for Gradle sync to complete

### Step 4: Run the App
1. Connect Android device OR start emulator
2. Click the green "Run" button
3. App launches with language selection screen

## 🎮 Try It Out

### First Launch Flow:
1. **Language Selection**: Choose Python, Java, JavaScript, Kotlin, or C++
2. **Home Screen**: See your 0 coins, tap "Start Learning"
3. **Levels Screen**: Level 1 is unlocked (green), others locked (gray)
4. **MCQ Screen**: Answer questions, earn coins
5. **Result Screen**: See your score and coins earned
6. **Try Code**: Edit and run code, must run at least once
7. **Back to Levels**: Level 2 is now unlocked!

### Test the Coin System:
- Answer MCQ correctly: +10 coins
- Complete a level: +50 coins
- Use a hint: -20 coins (or watch ad)
- Check Home screen to see updated balance

### Test Level Progression:
- Complete Level 1
- See Level 2 unlock
- Try clicking Level 3 (locked, can't open)
- Complete Level 2
- See Level 3 unlock
- Return to Home, restart app
- Progress is saved!

## ⚙️ Configuration (Optional)

### For Testing Ads:
The app uses test ad units by default. To use real ads:

1. Create AdMob account: https://apps.admob.com/
2. Create app in AdMob
3. Generate ad unit IDs
4. Update in code:
   - `AndroidManifest.xml`: Replace App ID
   - `HomeFragment.kt`: Replace banner ad unit
   - `MCQFragment.kt`: Replace rewarded ad unit
   - `ResultFragment.kt`: Replace interstitial ad unit

### For Code Execution:
Currently uses simulated execution. To enable real execution:

1. Create Firebase project: https://console.firebase.google.com/
2. Download `google-services.json`
3. Replace `app/google-services.json`
4. Deploy Cloud Function (see APP_README.md)
5. Update `RetrofitClient.kt` BASE_URL
6. Uncomment real API call in `TryCodeFragment.kt`

## 📱 Supported Features

### ✅ Working Out of the Box:
- Language selection
- Home screen
- Level listing
- MCQ questions
- Hint system
- Coin earning/spending
- Results display
- Code editor
- Simulated code execution
- Level progression
- Data persistence

### ⚙️ Requires Setup:
- Real code execution (needs Firebase)
- Real ads (needs AdMob account)
- Video learning (needs implementation)
- Problem solving (needs implementation)

## 🐛 Troubleshooting

### Gradle Sync Failed
```bash
# Solution 1: Clean and rebuild
./gradlew clean
./gradlew build

# Solution 2: Delete cache
rm -rf .gradle
rm -rf build
rm -rf app/build
# Then sync again in Android Studio
```

### App Crashes on Launch
```bash
# Check logcat for errors
adb logcat | grep -i "error\|exception"

# Common issues:
- Missing google-services.json (use placeholder)
- Wrong AdMob App ID (use test ID)
- API level mismatch (requires minSdk 24)
```

### Ads Not Loading
```bash
# Expected behavior with test ads:
- Banner: Shows test ad banner
- Interstitial: Shows test interstitial
- Rewarded: Shows test rewarded video

# If not showing:
- Check internet connection
- Verify test device ID in MainActivity
- Wait a few seconds for ad to load
```

## 📚 Learn More

- **Architecture**: See `APP_README.md` for MVVM pattern details
- **Security**: See `PROJECT_SUMMARY.md` for security implementation
- **Code Structure**: Browse `app/src/main/java/com/codinglearning/app/`

## 🎯 Next Steps

### For Learning:
1. Read through the code in each fragment
2. Understand the data flow (Model → Repository → Fragment)
3. Modify sample questions in `LevelRepository.kt`
4. Add your own programming language
5. Customize the UI theme colors

### For Development:
1. Implement Problem Solving screen
2. Add Video Learning integration
3. Create more levels and questions
4. Add achievements system
5. Implement skip level feature
6. Add user analytics

### For Deployment:
1. Create Firebase project
2. Set up Cloud Functions for code execution
3. Configure AdMob for monetization
4. Create app screenshots
5. Write Play Store description
6. Generate signed APK
7. Publish to Play Store

## 🔗 Useful Links

- **Firebase Console**: https://console.firebase.google.com/
- **AdMob Console**: https://apps.admob.com/
- **Android Developers**: https://developer.android.com/
- **Material Design**: https://m3.material.io/
- **Kotlin Docs**: https://kotlinlang.org/docs/

## 💡 Tips

1. **Test on Real Device**: Emulator is slower for testing
2. **Enable Instant Run**: Faster development iterations
3. **Use Logcat**: Debug issues with logging
4. **Check TODO Comments**: See what needs implementation
5. **Read Inline Comments**: Understand code logic

## ✨ You're Ready!

The app is fully functional and ready to use. Start exploring, learning, and customizing!

For questions or issues, refer to:
- `APP_README.md` - Detailed documentation
- `PROJECT_SUMMARY.md` - Complete feature list
- Inline code comments - Implementation details

Happy Coding! 🚀
