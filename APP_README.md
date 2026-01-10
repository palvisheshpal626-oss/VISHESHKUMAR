# Coding Learning App - Android

A gamified coding learning platform built with Kotlin for Android.

## Features

### Core Functionality
- **Multi-language Support**: Python, Java, JavaScript, Kotlin, C++
- **Gamification**: Coin system, level progression, rewards
- **Learning Modules**:
  - MCQ Questions with hints
  - Interactive code examples
  - Problem solving challenges
  - Video tutorials
- **Progress Tracking**: Persistent user progress using SharedPreferences
- **Monetization**: Google AdMob integration (banner, interstitial, rewarded ads)

### UI/UX
- Dark theme by default
- Material Design 3
- Gradient and glow effects
- Smooth animations
- Professional, game-like interface

## Architecture

The app follows MVVM architecture with the following structure:

```
app/
├── data/
│   ├── model/          # Data models (Level, Question, Problem, etc.)
│   ├── local/          # SharedPreferences manager
│   └── repository/     # Data repository layer
├── network/            # Retrofit API service for code execution
├── ui/                 # UI components (Fragments)
│   ├── home/
│   ├── language/
│   ├── levels/
│   ├── mcq/
│   ├── result/
│   └── trycode/
└── utils/              # Utility classes
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+
- Google AdMob account
- Firebase project

### Firebase Setup

1. **Create a Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project
   - Add an Android app with package name: `com.codinglearning.app`

2. **Download Configuration**
   - Download `google-services.json` from Firebase Console
   - Replace the placeholder file at `app/google-services.json`

3. **Enable Services**
   - Enable Firestore Database
   - Enable Cloud Functions
   - Enable Analytics (optional)

### Code Execution Backend (Firebase Cloud Functions)

The app requires a backend for secure code execution. API keys must NEVER be in the Android app.

1. **Deploy Cloud Function**:

```javascript
// functions/index.js
const functions = require('firebase-functions');
const axios = require('axios');

exports.executeCode = functions.https.onCall(async (data, context) => {
  const { code, language, input } = data;
  
  // Call your compiler API (e.g., JDoodle, Judge0, etc.)
  // API key is stored in Firebase environment, not in the app
  const apiKey = functions.config().compiler.apikey;
  
  try {
    const response = await axios.post('COMPILER_API_URL', {
      code: code,
      language: language,
      stdin: input
    }, {
      headers: {
        'Authorization': `Bearer ${apiKey}`
      }
    });
    
    return {
      output: response.data.output,
      error: response.data.error,
      executionTime: response.data.executionTime
    };
  } catch (error) {
    throw new functions.https.HttpsError('internal', error.message);
  }
});
```

2. **Set Environment Variables**:
```bash
firebase functions:config:set compiler.apikey="YOUR_API_KEY"
```

3. **Deploy**:
```bash
firebase deploy --only functions
```

4. **Update RetrofitClient.kt**:
   - Replace `BASE_URL` with your Firebase Function URL

### AdMob Setup

1. **Create AdMob Account**
   - Go to [AdMob Console](https://apps.admob.com/)
   - Create an app
   - Generate ad unit IDs

2. **Update AndroidManifest.xml**:
   - Replace `ca-app-pub-3940256099942544~3347511713` with your AdMob App ID

3. **Update Ad Unit IDs** in:
   - `HomeFragment.kt` (banner)
   - `MCQFragment.kt` (rewarded)
   - `ResultFragment.kt` (interstitial)

### Build and Run

1. **Clone the repository**
```bash
git clone https://github.com/palvisheshpal626-oss/VISHESHKUMAR.git
cd VISHESHKUMAR
```

2. **Open in Android Studio**
   - File > Open > Select project directory

3. **Sync Gradle**
   - Let Android Studio download dependencies

4. **Run the app**
   - Connect device or start emulator
   - Click Run button

## Security Best Practices

✅ **Implemented**:
- API keys stored in Firebase Cloud Functions (not in app)
- Secure code execution via backend
- HTTPS only communication
- No sensitive data in SharedPreferences

⚠️ **Important**:
- Never commit `google-services.json` with real credentials to public repos
- Use Firebase App Check for production
- Implement rate limiting on Cloud Functions
- Use ProGuard/R8 for release builds

## Level Progression Logic

The app uses a bug-free level system:

```kotlin
// Only store highest completed level
val highestCompleted = prefsManager.getHighestCompletedLevel()

// Levels are unlocked progressively
val isUnlocked = levelId <= highestCompleted + 1

// This prevents:
// - Accidental progress loss
// - Random locking
// - Data corruption
```

## Coin System

Coins are the global currency:
- Earned by completing MCQs, problems, levels
- Used for hints, skipping levels
- Persistent across app restarts
- Never reset accidentally

## Ad Strategy

- **Banner ads**: Bottom of screen (non-intrusive)
- **Rewarded ads**: For hints and coins (user choice)
- **Interstitial ads**: After level completion only
- **No ads**: On app launch or failure

## Testing

### Test Accounts
- Use Firebase Test Lab for comprehensive testing
- Test ad units are included for development

### Manual Testing Checklist
- [ ] Language selection persists
- [ ] Coin balance updates correctly
- [ ] Level progression works
- [ ] MCQ answers validate properly
- [ ] Code execution simulates output
- [ ] Ads load and display
- [ ] Back navigation works

## Future Enhancements

- Real-time code execution via Firebase
- Leaderboards and achievements
- Social features (share progress)
- More programming languages
- Advanced problem sets
- Video tutorial integration
- Offline mode support

## License

This project is for educational purposes.

## Contact

Developer: Vishesh Kumar
Email: palvisheshpal626@gmail.com
GitHub: [@palvisheshpal626-oss](https://github.com/palvisheshpal626-oss)
