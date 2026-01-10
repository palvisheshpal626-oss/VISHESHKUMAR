# Coding Learning App 📚💻

A gamified Android learning app that teaches programming concepts through interactive lessons, MCQs, code practice, and problem-solving challenges. Built with Kotlin, Jetpack Compose, and modern Android architecture.

## 🌟 Features

### Learning Flow
The app provides a structured learning journey:
1. **Animated Learning Videos** - Visual explanations of concepts
2. **MCQ Questions** - Test understanding with multiple-choice questions
3. **Result Screen** - View performance and earn coins
4. **Try Code** - Practice with editable code examples
5. **Compiler** - Run code in multiple languages
6. **Problem Solving** - HackerRank-style coding challenges
7. **Level Completion** - Unlock new levels as you progress

### Core Features

#### 🏠 Home Screen
- App entry point with attractive UI
- Display total coins earned
- Daily challenge for bonus coins
- "Get Free Coins" button (watch rewarded ad for +3 coins)
- Banner ad at bottom (no interstitial on launch)

#### 🔤 Language Selection
- Choose from C, C++, Java, or Python
- Beautiful gradient cards for each language
- Language selection persisted across sessions
- Banner ad at bottom (no interstitial on selection)

#### 📊 Level Selection
- 20 levels with lock/unlock mechanism
- Level 1 unlocked by default
- Visual indicators for locked/unlocked/completed levels
- **Level Skip Feature:**
  - If coins >= 5: Pay 5 coins + watch 1 rewarded ad
  - If coins < 5: Watch 2 rewarded ads
- **Bug Fix:** Levels unlock based on highest completed level + 1
- No banner or interstitial ads on this screen

#### ❓ MCQ Learning
- One question at a time with 4 options
- **Hint System:**
  - Cost: 2 coins OR watch rewarded ad
  - Shows explanation or removes wrong options
- **Rewards:**
  - +1 coin for correct answer
  - No penalty for wrong answer
  - NO ads on wrong answers
- No banner or interstitial ads (only rewarded ads for hints)

#### 📈 Result Screen
- Shows total questions, correct answers, accuracy
- Displays coins earned
- Motivational messages based on performance
- **One interstitial ad allowed** after showing results

#### 💻 Try Code Screen
- Editable code examples
- Run button to execute code
- Output display
- Optional rewarded ad for extra code runs
- No banner or interstitial ads

#### ⚙️ Compiler System
- **Multi-language support:** C, C++, Java, Python
- **API-based execution** (Firebase Cloud Function + Online Compiler API)
- Secure - no code execution on device
- Mock implementation included for demo

#### 🏆 Problem Solving
- HackerRank-style problems
- Filter by difficulty (Easy/Medium/Hard)
- Problem statement, input/output format, examples
- Code editor with template code
- Test case validation
- **One interstitial ad** after successful submission

### 💰 Coin System
- Earn coins by answering MCQs correctly
- Earn coins by solving problems
- Spend coins on hints and level skips
- Get free coins by watching rewarded ads
- Coin balance synced across app using DataStore

### 🎯 Ad Integration Strategy

**Banner Ads** (Only 2 places):
- Home Screen (bottom)
- Language Selection Screen (bottom)

**Interstitial Ads** (Only 2 places):
- Result Screen (after showing results) - 1 ad
- Problem Submission (after successful solution) - 1 ad

**Rewarded Ads** (User-initiated):
- Get Free Coins (+3 coins)
- Get Hint (when coins < 2)
- Skip Level (5 coins + 1 ad OR 2 ads)
- Try Code extra runs (optional)

**❌ NO ADS:**
- App launch
- Wrong MCQ answers
- Navigation between screens
- Learning videos
- During code editing

## 🏗️ Architecture

### Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose (100% Compose UI)
- **Architecture:** MVVM + Clean Architecture
- **Navigation:** Jetpack Navigation Compose
- **State Management:** StateFlow, Kotlin Coroutines
- **Data Persistence:** DataStore Preferences
- **Dependency Injection:** Manual (can be upgraded to Hilt/Koin)
- **Ads:** Google AdMob
- **Backend:** Firebase (Cloud Functions, Firestore)

### Project Structure
```
app/src/main/java/com/vishesh/codinglearning/
├── data/
│   ├── api/                 # Compiler API interface & mock
│   ├── model/               # Data models
│   ├── repository/          # Repository implementations
│   └── UserPreferencesRepository.kt
├── domain/
│   └── usecase/             # Business logic use cases
├── ui/
│   ├── home/                # Home screen
│   ├── language/            # Language selection
│   ├── level/               # Level selection
│   ├── mcq/                 # MCQ questions
│   ├── result/              # Result display
│   ├── trycode/             # Code practice
│   ├── problem/             # Problem solving
│   └── theme/               # App theme
└── MainActivity.kt
```

### Key Design Patterns
- **Repository Pattern** - Data abstraction
- **Use Case Pattern** - Business logic separation
- **State Pattern** - UI state management
- **Observer Pattern** - Reactive data flow

## 🚀 Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Gradle 8.2

### 1. Clone the Repository
```bash
git clone https://github.com/palvisheshpal626-oss/VISHESHKUMAR.git
cd VISHESHKUMAR
```

### 2. Firebase Setup
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app with package name: `com.vishesh.codinglearning`
3. Download `google-services.json`
4. Place it in `app/` directory
5. Enable Firebase Authentication, Firestore, and Cloud Functions

### 3. AdMob Setup
1. Create an AdMob account at [AdMob Console](https://apps.admob.com/)
2. Create an Android app
3. Get your AdMob App ID
4. Replace in `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_ADMOB_APP_ID"/>
```
5. Create ad units for:
   - Banner Ads (2 units)
   - Interstitial Ads (2 units)
   - Rewarded Ads (1 unit)

### 4. Compiler API Setup (Optional)

The app includes a mock compiler for demo purposes. For production:

1. Create a Firebase Cloud Function:
```javascript
const functions = require('firebase-functions');
const axios = require('axios');

exports.executeCode = functions.https.onCall(async (data, context) => {
  // Call Judge0 or similar online compiler API
  // Never expose API keys to client
  const response = await axios.post('COMPILER_API_URL', {
    source_code: data.code,
    language_id: data.languageId,
    stdin: data.input
  }, {
    headers: {
      'X-RapidAPI-Key': functions.config().compiler.apikey
    }
  });
  
  return response.data;
});
```

2. Deploy the function
3. Update `CompilerApi.kt` to call the Firebase function

### 5. Build and Run
```bash
./gradlew assembleDebug
```

Or use Android Studio:
1. Open project in Android Studio
2. Sync Gradle
3. Run on emulator or device

## 📝 Important Notes

### Security
- ✅ API keys stored in Firebase Cloud Functions (backend)
- ✅ No sensitive data in client code
- ✅ All code execution happens server-side
- ❌ Never commit `google-services.json` to public repos

### Ad Policy Compliance
- All ads follow Google AdMob policies
- No excessive ads (minimal ad load)
- Rewarded ads are user-initiated
- Clear value proposition for ads

### Level Completion Logic
A level is marked complete only when:
1. ✅ All MCQs completed
2. ✅ At least one code run in Try Code
3. ✅ At least one problem solved

### Data Persistence
- Coins, level progress, and language selection persist using DataStore
- No user authentication required (local storage)
- Can be extended to Firebase for multi-device sync

## 🎨 Customization

### Colors
Edit `app/src/main/res/values/colors.xml`

### Strings
Edit `app/src/main/res/values/strings.xml`

### Icons
Replace drawable XML files in `app/src/main/res/drawable/`

### Sample Content
- MCQ questions: `McqRepositoryImpl.kt`
- Problems: `ProblemRepositoryImpl.kt`
- Example code: `TryCodeViewModel.kt`

## 📱 Screenshots

(Add screenshots here after building the app)

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Vishesh Kumar**
- GitHub: [@palvisheshpal626-oss](https://github.com/palvisheshpal626-oss)
- Email: palvisheshpal626@gmail.com

## 🙏 Acknowledgments

- Jetpack Compose for modern Android UI
- Firebase for backend services
- Google AdMob for monetization
- Online compiler APIs (Judge0, etc.)

---

**Build Quietly • Think Clearly • Execute Consistently**
