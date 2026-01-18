# Release Checklist - Coding Learning App

This document provides a comprehensive checklist for releasing the Coding Learning app to Google Play Store.

## 📋 Pre-Release Preparation

### 1. Version Management

- [ ] Update `versionCode` in `app/build.gradle.kts` (increment by 1)
- [ ] Update `versionName` in `app/build.gradle.kts` (e.g., 1.0 → 1.1)
- [ ] Document changes in release notes
- [ ] Tag git commit with version number

**Current Version:**
- Version Code: 1
- Version Name: 1.0

### 2. Build Configuration

- [x] Release build type configured in `build.gradle.kts`
- [x] ProGuard enabled (`isMinifyEnabled = true`)
- [x] ProGuard rules optimized in `proguard-rules.pro`
- [ ] **Generate release keystore** (REQUIRED)
  ```bash
  keytool -genkey -v -keystore release-key.jks \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -alias coding-learning-key
  ```
- [ ] Add signing config to `build.gradle.kts`:
  ```kotlin
  signingConfigs {
      release {
          storeFile file("release-key.jks")
          storePassword "YOUR_KEYSTORE_PASSWORD"
          keyAlias "coding-learning-key"
          keyPassword "YOUR_KEY_PASSWORD"
      }
  }
  ```
- [ ] **NEVER commit keystore or passwords to git**
- [ ] Store keystore securely (backup recommended)

### 3. Dependencies & Libraries

- [x] All dependencies up-to-date
- [x] No deprecated APIs used
- [x] Target SDK = 34 (latest requirement)
- [x] Min SDK = 24 (covers 94%+ devices)
- [x] Compile SDK = 34

### 4. Code Quality

- [x] No compiler warnings
- [x] No lint errors (critical/high priority)
- [x] All TODOs addressed or documented
- [x] Code reviewed for security vulnerabilities
- [x] No hardcoded secrets or API keys
- [x] Input validation on all user inputs

---

## 🔐 Security & Compliance

### 5. Privacy & Data Protection

- [x] Privacy Policy created (`PRIVACY_POLICY.md`)
- [ ] Host Privacy Policy online (get public URL)
- [ ] Add Privacy Policy URL to Play Store listing
- [ ] Add Privacy Policy link in app (Settings screen)
- [x] Data collection disclosed in privacy policy
- [x] Third-party services listed (AdMob, Piston API, Firebase)
- [x] COPPA compliant (suitable for children)

### 6. Permissions Audit

- [x] INTERNET - Required for Piston API code execution ✅
- [x] ACCESS_NETWORK_STATE - Required for network checks ✅
- [x] No dangerous permissions requested ✅
- [x] All permissions justified and necessary ✅

### 7. AdMob Configuration

- [x] Test AdMob App ID currently used: `ca-app-pub-3940256099942544~3347511713`
- [ ] **Replace with production AdMob App ID** (REQUIRED)
  1. Create AdMob account at https://admob.google.com
  2. Register app in AdMob console
  3. Get production App ID
  4. Replace in `AndroidManifest.xml`
- [ ] Create ad units for:
  - [ ] Banner ad (MCQ screen)
  - [ ] Rewarded ad (hints)
  - [ ] Interstitial ad (level completion)
- [ ] Test ads on physical device
- [ ] Verify ad placement follows Google policies

### 8. Firebase Configuration (if using)

- [ ] Add production `google-services.json`
- [ ] Set up Firestore security rules
- [ ] Enable Analytics
- [ ] Enable Crashlytics
- [ ] Test Firebase integration

---

## 🧪 Testing

### 9. Functional Testing

- [x] All 5 languages execute code correctly (Python, Java, JS, Kotlin, C++)
- [x] MCQ system works (correct/wrong answers, hints)
- [x] Coin economy functional (earn/spend coins)
- [x] Star system works (0-3 stars per level)
- [x] Section unlocking (EASY → MEDIUM → HARD)
- [x] Problem solving with test cases
- [x] Results screen displays correctly
- [x] Progress persistence (coins, stars, levels)
- [x] Ads display correctly (banner, rewarded, interstitial)

### 10. Device Testing

- [ ] Test on Android 7.0 (API 24 - minimum SDK)
- [ ] Test on Android 14 (API 34 - target SDK)
- [ ] Test on low-end device (2GB RAM)
- [ ] Test on high-end device
- [ ] Test on tablet (if targeting tablets)
- [ ] Test on different screen sizes

### 11. Network Testing

- [x] Code execution works on WiFi
- [ ] Code execution works on mobile data
- [x] Graceful handling of network failures
- [x] Offline mode (local features work)
- [x] Timeout handling (3 seconds for code execution)
- [x] Retry logic for transient failures

### 12. Edge Cases

- [x] Empty code submission handled
- [x] Very long code handled (within limits)
- [x] Rapid button clicks prevented (debouncing)
- [x] Negative stars prevented
- [x] Level replay with lower stars works
- [x] App survives process death (progress saved)

### 13. Performance Testing

- [x] No memory leaks (checked with Android Profiler)
- [x] No ANRs (Application Not Responding)
- [x] Smooth UI (60 FPS target)
- [x] Fast cold start (<3 seconds)
- [x] Efficient battery usage

---

## 🎨 Play Store Assets

### 14. App Icon

- [ ] Create high-res app icon (512x512 PNG)
- [ ] Test icon on different Android versions
- [ ] Ensure icon is recognizable when small
- [ ] Follow Material Design icon guidelines

**Current:** Using default Android launcher icon (must replace)

### 15. Screenshots

- [ ] Minimum 2 screenshots required
- [ ] Recommended: 4-8 screenshots showing key features
- [ ] Screenshot suggestions:
  1. Language selection screen
  2. MCQ learning with coins/stars
  3. Code editor with real execution
  4. Results screen with stars
  5. Level selection (locked/unlocked)
  6. Problem solving screen
- [ ] Size: 1080x1920 or 1080x2340 (portrait)
- [ ] Add localized screenshots if supporting multiple languages

### 16. Play Store Listing

- [ ] **App Title** (max 50 chars):
  ```
  Coding Learning - Learn Programming
  ```
- [ ] **Short Description** (max 80 chars):
  ```
  Learn Python, Java, JS, Kotlin, C++ with real code execution & gamification!
  ```
- [ ] **Full Description** (max 4000 chars) - See template in PHASE6 docs
- [ ] **Feature Graphic** (1024x500 JPG/PNG) - Optional but recommended
- [ ] **Promo Video** (Optional) - YouTube URL

### 17. Categorization & Ratings

- [ ] Select **Category**: Education
- [ ] Complete **Content Rating Questionnaire**:
  - Target Age: Everyone or Everyone 10+
  - Contains Ads: Yes
  - In-App Purchases: No
  - User-Generated Content: No (code is user-written but not shared)
- [ ] Add **Tags**: education, learning, programming, coding, python, java

---

## 📤 Build & Upload

### 18. Build Release APK/AAB

- [ ] Clean project:
  ```bash
  ./gradlew clean
  ```
- [ ] Build release AAB (recommended for Play Store):
  ```bash
  ./gradlew bundleRelease
  ```
- [ ] Build release APK (for testing):
  ```bash
  ./gradlew assembleRelease
  ```
- [ ] Verify release build location:
  - AAB: `app/build/outputs/bundle/release/app-release.aab`
  - APK: `app/build/outputs/apk/release/app-release.apk`
- [ ] Test release build on physical device before upload

### 19. Test Release Build

- [ ] Install release APK on test device:
  ```bash
  adb install app/build/outputs/apk/release/app-release.apk
  ```
- [ ] Verify ProGuard didn't break functionality
- [ ] Check app size (should be smaller than debug)
- [ ] Test all critical features again

---

## 🚀 Play Store Submission

### 20. Google Play Console Setup

- [ ] Create Google Play Developer account ($25 one-time fee)
- [ ] Complete account verification
- [ ] Accept Developer Distribution Agreement
- [ ] Set up merchant account (if planning in-app purchases later)

### 21. Create App in Console

- [ ] Create new app in Play Console
- [ ] Set app name: "Coding Learning"
- [ ] Set default language: English (United States)
- [ ] Select app category: Education
- [ ] Select free/paid: Free

### 22. Upload Release

- [ ] Upload AAB file in Production track (or Internal/Alpha/Beta for testing)
- [ ] Set version name: 1.0
- [ ] Add release notes:
  ```
  Initial release featuring:
  - Learn 5 programming languages (Python, Java, JavaScript, Kotlin, C++)
  - Real code execution with instant feedback
  - MCQ quizzes with coins and stars
  - Problem solving with test cases
  - Progressive difficulty levels
  - Gamified learning experience
  ```

### 23. Store Listing Content

- [ ] Upload app icon (512x512)
- [ ] Upload feature graphic (1024x500) - if available
- [ ] Upload screenshots (min 2)
- [ ] Enter app title
- [ ] Enter short description
- [ ] Enter full description
- [ ] Add Privacy Policy URL (required)
- [ ] Add contact email
- [ ] Add website (optional)

### 24. Final Review

- [ ] Review all Play Console sections (green checkmarks)
- [ ] Preview store listing
- [ ] Verify all information is correct
- [ ] Check pricing & distribution (which countries)
- [ ] Submit for review

---

## 📊 Post-Release

### 25. Monitoring

- [ ] Monitor Play Console for review status
- [ ] Check for crashes in Play Console (if any)
- [ ] Monitor user reviews and ratings
- [ ] Track user acquisition stats
- [ ] Monitor AdMob revenue (if monetizing)

### 26. User Feedback

- [ ] Respond to user reviews (especially negative ones)
- [ ] Create feedback collection mechanism
- [ ] Plan updates based on user requests
- [ ] Monitor crash reports via Play Console

### 27. Updates & Maintenance

- [ ] Plan regular updates (monthly recommended)
- [ ] Increment versionCode for each release
- [ ] Update versionName (1.0 → 1.1 for minor, 2.0 for major)
- [ ] Keep dependencies up-to-date
- [ ] Address security vulnerabilities promptly

---

## ✅ Final Checklist Summary

### Must Complete Before Release:
1. ❗ Generate and configure release keystore
2. ❗ Replace AdMob test IDs with production IDs
3. ❗ Create and upload Play Store assets (icon, screenshots)
4. ❗ Host Privacy Policy online and add URL
5. ❗ Test on physical devices
6. ❗ Build and test release AAB
7. ❗ Create Play Console account and submit

### Nice to Have:
- Feature graphic for Play Store
- Promo video
- Localized content (other languages)
- Tablet optimization

---

## 📞 Support Resources

**Google Play Console**: https://play.google.com/console  
**AdMob Console**: https://admob.google.com  
**Firebase Console**: https://console.firebase.google.com  
**Android Developer Docs**: https://developer.android.com  

**Play Store Review Time**: Typically 3-7 days for first review

---

## 🎉 Congratulations!

Once your app is approved and published on Google Play Store, you'll have successfully launched a complete coding learning platform!

**Next Steps After Launch:**
- Market your app (social media, website)
- Gather user feedback
- Plan feature updates
- Monitor analytics
- Iterate and improve

**Good luck with your launch! 🚀**
