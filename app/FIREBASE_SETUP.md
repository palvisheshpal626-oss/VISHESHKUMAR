# Firebase Configuration

This file should contain your Firebase `google-services.json` configuration.

## How to get this file:

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Click on "Add app" and select Android
4. Register your app with package name: `com.vishesh.codinglearning`
5. Download the `google-services.json` file
6. Place it in this directory (`app/google-services.json`)

## Important:
- Do NOT commit the actual `google-services.json` to version control
- The file is already excluded in `.gitignore`
- Each developer/environment should have their own Firebase configuration

## Required Firebase Services:
- Firebase Authentication (optional)
- Cloud Firestore (for data storage)
- Cloud Functions (for compiler API)
- Firebase Analytics (optional)

## Sample Structure:
```json
{
  "project_info": {
    "project_number": "YOUR_PROJECT_NUMBER",
    "project_id": "YOUR_PROJECT_ID",
    "storage_bucket": "YOUR_PROJECT_ID.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "YOUR_APP_ID",
        "android_client_info": {
          "package_name": "com.vishesh.codinglearning"
        }
      }
    }
  ]
}
```
