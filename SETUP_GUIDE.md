# 🚀 Complete Setup Guide - Coding Learning App

This guide will walk you through setting up the complete Coding Learning App from scratch.

## Prerequisites

- ✅ Android Studio Hedgehog (2023.1.1) or later
- ✅ JDK 17 or higher
- ✅ Android SDK 34
- ✅ Gradle 8.2+
- ✅ Git installed
- ✅ Google account (for Firebase and AdMob)

## Step 1: Clone the Repository

```bash
git clone https://github.com/palvisheshpal626-oss/VISHESHKUMAR.git
cd VISHESHKUMAR
```

## Step 2: Open in Android Studio

1. Launch Android Studio
2. Click "Open an Existing Project"
3. Navigate to the cloned repository
4. Select the root folder
5. Click "OK"
6. Wait for Gradle sync to complete

## Step 3: Firebase Setup

### 3.1 Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project"
3. Enter project name: `coding-learning-app` (or your choice)
4. Disable Google Analytics (optional)
5. Click "Create project"

### 3.2 Add Android App to Firebase

1. In Firebase Console, click "Add app" → Android icon
2. Register app with these details:
   - **Package name:** `com.vishesh.codinglearning`
   - **App nickname:** Coding Learning App
   - **Debug signing certificate SHA-1:** (optional for now)
3. Click "Register app"

### 3.3 Download google-services.json

1. Download the `google-services.json` file
2. Place it in: `app/google-services.json`
3. Verify placement:
   ```
   VISHESHKUMAR/
   └── app/
       └── google-services.json  ← Should be here
   ```

### 3.4 Enable Firebase Services

In Firebase Console, enable these services:

#### Firestore Database
1. Go to "Firestore Database"
2. Click "Create database"
3. Choose "Start in test mode" (for development)
4. Select location closest to your users
5. Click "Enable"

#### Cloud Functions (Optional - for real compiler)
1. Go to "Functions"
2. Click "Get started"
3. Upgrade to Blaze plan (pay-as-you-go)
   - Note: Has free tier, only pay if you exceed limits

## Step 4: Google AdMob Setup

### 4.1 Create AdMob Account

1. Go to [Google AdMob](https://apps.admob.com/)
2. Sign in with Google account
3. Click "Get Started"
4. Accept terms and conditions

### 4.2 Create App in AdMob

1. Click "Apps" → "Add app"
2. Select "No" (app not published yet)
3. Enter app name: "Coding Learning"
4. Select platform: Android
5. Click "Add"
6. Note down your **AdMob App ID** (looks like: `ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy`)

### 4.3 Create Ad Units

Create the following ad units:

#### 1. Banner Ad (Home Screen)
- Ad format: Banner
- Ad unit name: "Home Banner"
- Note the **Ad Unit ID**

#### 2. Banner Ad (Language Selection)
- Ad format: Banner
- Ad unit name: "Language Banner"
- Note the **Ad Unit ID**

#### 3. Interstitial Ad (Result Screen)
- Ad format: Interstitial
- Ad unit name: "Result Interstitial"
- Note the **Ad Unit ID**

#### 4. Interstitial Ad (Problem Success)
- Ad format: Interstitial
- Ad unit name: "Problem Success Interstitial"
- Note the **Ad Unit ID**

#### 5. Rewarded Ad (Coins/Hints)
- Ad format: Rewarded
- Ad unit name: "Rewarded Ad"
- Note the **Ad Unit ID**

### 4.4 Update AdMob App ID

Open `app/src/main/AndroidManifest.xml` and replace:

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"/>  <!-- Replace this -->
```

With your actual AdMob App ID:

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="YOUR_ADMOB_APP_ID"/>  <!-- Your actual ID -->
```

### 4.5 Testing Ads During Development

For testing, you can use Google's test ad unit IDs:

- Banner: `ca-app-pub-3940256099942544/6300978111`
- Interstitial: `ca-app-pub-3940256099942544/1033173712`
- Rewarded: `ca-app-pub-3940256099942544/5224854917`

**Important:** Replace test IDs with your real ad unit IDs before publishing!

## Step 5: Compiler API Setup (Optional)

The app includes a **mock compiler** for demo purposes. For real code execution:

### Option A: Use Judge0 API (Recommended)

1. Sign up at [RapidAPI Judge0](https://rapidapi.com/judge0-official/api/judge0-ce)
2. Subscribe to a plan (free tier available)
3. Get your API key

### Option B: Use Piston API (Free)

1. Use [Piston API](https://github.com/engineer-man/piston) - free and open source
2. Self-host or use public endpoint (rate limited)

### Setup Firebase Cloud Function

Create a Cloud Function to call the compiler API:

```javascript
// functions/index.js
const functions = require('firebase-functions');
const axios = require('axios');

exports.executeCode = functions.https.onCall(async (data, context) => {
  const { code, language, input } = data;
  
  // Map language to Judge0 language ID
  const languageMap = {
    'PYTHON': 71,
    'JAVA': 62,
    'C': 50,
    'CPP': 54
  };
  
  try {
    const response = await axios.post(
      'https://judge0-ce.p.rapidapi.com/submissions',
      {
        source_code: code,
        language_id: languageMap[language],
        stdin: input
      },
      {
        headers: {
          'X-RapidAPI-Key': functions.config().judge0.apikey,
          'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
        }
      }
    );
    
    // Poll for result
    const token = response.data.token;
    const resultResponse = await axios.get(
      `https://judge0-ce.p.rapidapi.com/submissions/${token}`,
      {
        headers: {
          'X-RapidAPI-Key': functions.config().judge0.apikey,
          'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
        }
      }
    );
    
    return resultResponse.data;
  } catch (error) {
    throw new functions.https.HttpsError('internal', error.message);
  }
});
```

Deploy:
```bash
# Set API key
firebase functions:config:set judge0.apikey="YOUR_API_KEY"

# Deploy function
firebase deploy --only functions
```

## Step 6: Build and Run

### 6.1 Sync Project

1. In Android Studio, click "File" → "Sync Project with Gradle Files"
2. Wait for sync to complete
3. Fix any errors that appear

### 6.2 Build APK

```bash
./gradlew assembleDebug
```

Or in Android Studio:
1. Click "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
2. Wait for build to complete

### 6.3 Run on Emulator

1. Create an AVD (Android Virtual Device):
   - Tools → Device Manager → Create Device
   - Select device (e.g., Pixel 6)
   - Select system image (API 34 recommended)
2. Click "Run" (green play button)
3. Select your emulator

### 6.4 Run on Physical Device

1. Enable Developer Options on your Android device
2. Enable USB Debugging
3. Connect device via USB
4. Click "Run"
5. Select your device

## Step 7: Testing

### Test Features

1. **Home Screen**
   - Verify coin display
   - Test "Start Learning" button
   - Test "Get Free Coins" (will show test rewarded ad)

2. **Language Selection**
   - Select each language
   - Verify selection is saved

3. **Level Selection**
   - Check level 1 is unlocked
   - Check other levels are locked
   - Test level skip feature

4. **MCQ Screen**
   - Answer questions
   - Test hint feature
   - Check coin rewards

5. **Result Screen**
   - Verify accuracy calculation
   - Check coins earned

6. **Try Code**
   - Edit code
   - Run code
   - Check output

7. **Problem Solving**
   - View problem list
   - Attempt a problem
   - Submit solution

## Step 8: Production Checklist

Before publishing to Play Store:

- [ ] Replace all test AdMob IDs with real ad unit IDs
- [ ] Setup Firebase in production mode
- [ ] Implement real compiler API (not mock)
- [ ] Add proper error handling
- [ ] Add analytics tracking
- [ ] Test on multiple devices
- [ ] Optimize performance
- [ ] Add proper app icons (all densities)
- [ ] Create screenshots for Play Store
- [ ] Write privacy policy
- [ ] Generate signed release APK
- [ ] Test release build thoroughly

## Common Issues & Solutions

### Issue: "google-services.json not found"
**Solution:** Make sure you downloaded and placed the file in `app/` directory

### Issue: "Failed to resolve: com.google.firebase:firebase-bom"
**Solution:** Check internet connection and sync Gradle again

### Issue: "AAPT: error: resource android:attr/lStar not found"
**Solution:** Update compileSdk to 33 or higher

### Issue: Ads not showing
**Solution:** 
- Use test ad unit IDs during development
- Check AdMob account is active
- Ensure AdMob App ID is correct in manifest

### Issue: Build fails with "Duplicate class"
**Solution:** Clean and rebuild:
```bash
./gradlew clean
./gradlew build
```

## Getting Help

If you encounter issues:

1. Check [GitHub Issues](https://github.com/palvisheshpal626-oss/VISHESHKUMAR/issues)
2. Read [Android Documentation](https://developer.android.com/docs)
3. Visit [Firebase Documentation](https://firebase.google.com/docs)
4. Join [Android Developers Discord](https://discord.gg/android-developers)

## Next Steps

Once setup is complete:

1. Customize the app (colors, strings, content)
2. Add more MCQ questions
3. Add more coding problems
4. Implement animated learning videos
5. Add profile and settings screens
6. Implement social features
7. Add achievements and badges

## Contributing

Want to contribute? Check out [CONTRIBUTING.md](CONTRIBUTING.md) (to be added)

## License

MIT License - See [LICENSE](LICENSE) file

---

**Happy Coding! 🚀**

For questions: palvisheshpal626@gmail.com
