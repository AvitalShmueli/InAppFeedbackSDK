# Getting Started

Easily collect in-app feedback in your Android application in just a few steps.

## What You'll Build
By the end of this guide, you'll have:

- A feedback form created and configured
- Your Android app integrated with the Feedback SDK
- Real-time user feedback displayed in the web portal
- A working, production-ready feedback system

---

## Step 1: Create Your Feedback Form

1. Open the Portal: https://feedback-portal-eight.vercel.app
2. Click "Create Form" on the dashboard
3. Fill in the form details:
   * **Package Name**: `com.yourcompany.yourapp`
   * **Title**: The message shown to users in the feedback dialog
   * **Form Type**: `Rating`, `Free Text`, or `Rating & Text`

   💡 **Important**: Use the actual Android package name (from your build.gradle file)

4. Click "Create"

   ⚠️ **Note**: Only one active form is allowed per package. Creating a new form deactivates the previous one.


Your form is now live! The SDK will automatically fetch and display it in your app.


## Step 2: Android Integration
Add the SDK to your Android app in a few lines of code.

### Installation

1. Add JitPack to your repositories
   In your `settings.gradle.kts`:
   ```kotlin
   dependencyResolutionManagement {
           repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
           repositories {
               mavenCentral()
               maven { url = uri("https://jitpack.io") }
           }
       }
   ```
   
2. Add the SDK dependency - Current tag version: 1.0.0
   **Option A** - Directly in `build.gradle.kts`:
   ```kotlin
   dependencies {
               implementation("com.github.AvitalShmueli:InAppFeedbackSDK:Tag")
       }
   ```

   **Option B** - using libs.versions.toml:
   ```toml
   [versions]
   inappfeedbacksdk = "Tag"
   
   [libraries]
   inappfeedbacksdk = { module = "com.github.AvitalShmueli:InAppFeedbackSDK", version.ref = "inappfeedbacksdk" }
   ``` 
   Then in your module build file:
   ```kotlin
   dependencies {
   implementation(libs.inappfeedbacksdk)
   }
   ```  
   
3. Sync Your Project

4. Add Internet Permission    
   In your `AndroidManifest.xml`:
   ```xml    
    <uses-permission android:name="android.permission.INTERNET" />    
   ```  

### Display the Feedback Form
1. In your `Activity` or `Fragment`, use the following to fetch and show the form:
   ```java
   FeedbackFormManager manager = FeedbackFormManager.getInstance(context);
   
   manager.getActiveFeedbackForm(context, fragmentManager, new FeedbackFormManager.FeedbackFormCallback<FeedbackForm>() {
       @Override
       public void ready(FeedbackForm form) {
           // The feedback dialog is shown automatically
       }
   
       @Override
       public void failed(String errorMsg) {
           Log.e("FeedbackSDK", "Form load failed: " + errorMsg);
       }
   });
   ```

   * **The dialog only appears if there's an active form for your app package in the backend.**

   <br>  

2. Optional: Set a user ID  
   ```java  
   manager.setUserId("user_1234");  
   ```
   Use this to associate feedback with specific users (e.g., logged-in accounts).

   [View full code example](./integration-examples)


## Step 3: Test Your Integration
Let’s test the flow!

### Run Your App
1. Build and launch your app
2. Trigger the feedback dialog (via button, gesture, or automatic call)
3. You should see:
   * Your form title
   * Star rating (if enabled)
   * Text input (if enabled)
   * Submit button

### Submit Test Feedback

1. Rate the app (1-5 stars)
2. Enter a message: `"Testing the feedback system!"`
3. Tap "Submit"
4. You’ll see a success message, and the dialog will close

### View Your Feedback

1. Open the portal: https://feedback-portal-eight.vercel.app
2. Filter by your app’s package name
3. See real-time data including:
   * Rating and message
   * Device information
   * App version
   * Timestamp
   * User id


## Next Steps
Now that your feedback system is live, here are some ways to take it further:
- **Customize when the dialog appears**
  Trigger the feedback dialog after meaningful actions (e.g., purchases, feature use, level completion).

  💡 Ask at natural break points to avoid interrupting users.
  
- **Collect analytics on submission events**
  Track when feedback is shown, skipped, or submitted. Use this data to improve timing and targeting.
  
- **Style the dialog (coming soon)**
  Customize the look and feel to match your app’s branding.
  
- **Monitor trends in the portal**
  Analyze ratings, user messages, and submission frequency over time.

  💡 Don’t just collect — act on insights and close the loop with users where possible.
  
- **Limit over-prompting**
  Prompt users sparingly to prevent fatigue. Once every few sessions is often enough.


## Troubleshooting
- **Form not showing?** Check that your package name matches exactly and that a form is active.
- **Dialog not appearing?** Ensure getActiveFeedbackForm() is called from a UI-safe context.
- **Network error?** Confirm your app has Internet permission and access to https://feedback-portal-eight.vercel.app.
