# Build Configuration Guide

## Issue Resolution

The build failure with error "Cannot resolve external dependency ... because no repositories are defined" has been fixed.

## What Was Wrong

The previous configuration was missing the `dependencyResolutionManagement` block in `settings.gradle.kts`, which is required in modern Gradle versions (7.x+) to define repositories for dependency resolution.

## What Was Fixed

### settings.gradle.kts
Added `dependencyResolutionManagement` block:
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

### build.gradle.kts
Removed the deprecated `allprojects` block that was conflicting with the modern approach.

## Current Configuration

**settings.gradle.kts:**
- `pluginManagement`: Defines repositories for Gradle plugins
- `dependencyResolutionManagement`: Defines repositories for project dependencies
- Repository order: gradlePluginPortal, google, mavenCentral

**build.gradle.kts (root):**
- `buildscript`: Defines buildscript dependencies (AGP, Kotlin plugin, Google Services)
- Repositories: google, mavenCentral

**app/build.gradle.kts:**
- Applies plugins: Android application, Kotlin Android, Google Services
- Defines dependencies with proper versions

## How to Build

### Using Android Studio (Recommended)
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Build → Make Project (or Ctrl+F9)
4. Run → Run 'app' (or Shift+F10)

### Using Command Line
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build and install on connected device
./gradlew installDebug

# Run tests
./gradlew test
```

## Common Build Issues

### Issue: "Cannot resolve dependency"
**Solution:** Ensure you have internet connection and can access:
- https://dl.google.com (Google Maven Repository)
- https://repo1.maven.org (Maven Central)
- https://plugins.gradle.org (Gradle Plugin Portal)

### Issue: "SDK location not found"
**Solution:** Create `local.properties` file with:
```properties
sdk.dir=/path/to/android/sdk
```

### Issue: "Gradle sync failed"
**Solution:**
1. File → Invalidate Caches / Restart
2. Delete `.gradle` and `build` directories
3. Sync again

### Issue: "Firebase configuration missing"
**Solution:** Download `google-services.json` from Firebase Console and place in `app/` directory.

## Dependency Versions

| Dependency | Version | Purpose |
|------------|---------|---------|
| Android Gradle Plugin | 7.4.2 | Build tooling |
| Kotlin | 1.8.22 | Language support |
| Gradle | 7.5 | Build system |
| compileSdk | 34 | API level for compilation |
| minSdk | 24 | Android 7.0 (Nougat) |
| targetSdk | 34 | Android 14 |

## Repository Access

The build requires access to:
1. **Google Maven Repository** - For Android and Google libraries
2. **Maven Central** - For third-party libraries
3. **Gradle Plugin Portal** - For Gradle plugins

All are configured in `settings.gradle.kts`.

## Troubleshooting Network Issues

If you're behind a corporate proxy or firewall:

1. Configure proxy in `gradle.properties`:
```properties
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=proxy.company.com
systemProp.https.proxyPort=8080
```

2. Or use environment variables:
```bash
export GRADLE_OPTS="-Dhttp.proxyHost=proxy.company.com -Dhttp.proxyPort=8080"
```

## Build Output

Successful build produces:
- `app/build/outputs/apk/debug/app-debug.apk` - Debug APK
- `app/build/outputs/apk/release/app-release.apk` - Release APK (after signing)

## Next Steps After Successful Build

1. Test on emulator or physical device
2. Verify all features work correctly
3. Configure Firebase for production
4. Replace test ad units with production ad units
5. Generate signed APK for release
6. Test on multiple devices and Android versions

## Resources

- [Android Gradle Plugin Documentation](https://developer.android.com/studio/releases/gradle-plugin)
- [Gradle Build Configuration](https://docs.gradle.org/current/userguide/userguide.html)
- [Dependency Management in Gradle](https://docs.gradle.org/current/userguide/dependency_management.html)
