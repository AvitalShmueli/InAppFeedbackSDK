# InAppFeedbackSDK
![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)
[![](https://jitpack.io/v/AvitalShmueli/InAppFeedbackSDK.svg)](https://jitpack.io/#AvitalShmueli/InAppFeedbackSDK)

The **InAppFeedbackSDK** library is a lightweight Android SDK to **collect in-app user feedback** with a customizable dialog and automatic device/app metadata capture. Built for easy integration and seamless UX.

<p align="center">
<img src="https://github.com/user-attachments/assets/9fb928d5-5f69-430a-a662-fd10f9ffb947" alt="demo_app"
style="height:400px;"/>
</p>

---

## Project Architecture
The InAppFeedbackSDK is a feedback collection system with multiple components that work together to capture user feedback in Android applications.
### System Overview
The project follows a client-server architecture with three main layers:
**Client Layer**: Android applications integrate the lightweight SDK library to display feedback forms and collect user input through native dialogs. 
Additionally, a web-based administration portal provides administrators with a dashboard to manage feedback data and system configurations.
**Server Layer**: A Flask-based REST API service handles all backend operations, processing feedback submissions, managing form configurations, and serving data to client applications. The server is deployed on Vercel for reliability and scalability.
**Data Layer**: MongoDB Atlas serves as the cloud database, storing feedback submissions, user data, application configurations, and form templates. MongoDB's document-based structure perfectly handles the varied feedback data formats and metadata.

---

## Key Components

1. **Backend API Service**
   The backend service acts as the central hub for all feedback operations, providing a robust and scalable RESTful API.
   Technology Stack:
   - Framework: Flask (RESTful API)
   - Database: MongoDB Atlas (Cloud-hosted)
   - Deployment: Cloud service provider - Vercel
   
   [Link to the backend's GitHab repository](https://github.com/AvitalShmueli/feedback-backend) 

2. **Android SDK Library**
   The Android library gives developers an easy way to add feedback form to their apps to collect feedback from users.
   Published on JitPack for easy integration 
   Key Features:
   - **FeedbackFormManager**: Central class managing all SDK operations
   - **Automatic Metadata Collection**: Captures device info, app version, and timestamps
   - **Customizable UI**: Native Android dialog with clean, intuitive design
   - **Network Layer**: Handles API communication with retry logic and error handling
   - **Callback System**: Provides success/error feedback to host applications

   Main Classes:
   - FeedbackFormManager: Singleton manager for SDK operations
   - FeedbackDialogFragment: Custom dialog fragment for user interaction
   - FeedbackForm: Data model representing feedback forms
   - Feedback: Data model representing user's feedback
   - FeedbackController: Network communication handler

3. **Example Android Application**
   A simple demo app showing how to integrate and use the SDK.

4. **Administration Portal**
   A web-based interface for managing feedback data and system configuration.
   [Link to the portal's GitHab repository](https://github.com/AvitalShmueli/feedback-portal)

---

## Features

- Display feedback forms with minimal code
- Collect user messages and optional star ratings
- Automatically includes app version, device info, and timestamp
- Supports anonymous or authenticated user IDs
- Clean and native UI dialog using `FragmentManager`
- Callback support for handling success and errors

---

## Installation

1. Add the JitPack repository to your build file. Add it in your `settings.gradle.kts` at the end of repositories:
    ```kotlin
    dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                mavenCentral()
                maven { url = uri("https://jitpack.io") }
            }
        }
    ```

2. Add the library dependency to your app-level build.gradle:
    Using direct declaration:
    ```kotlin
    dependencies {
                implementation("com.github.AvitalShmueli:InAppFeedbackSDK:Tag")
        }
    ```
    
    Or using libs.versions.toml:
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

3.  Sync Your Project

---

# How to Start?
1. Fetch and Display the Feedback Form
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

* **The feedback dialog is shown automatically only if there is an active form for the application package in the DB.**

<br>
2. Optionally set a user ID (e.g., logged-in user):
```java
manager.setUserId("user_1234");
```

---

## Documentation
For detailed documentation, API references, and advanced usage examples, visit the [documentation site](docs/index.md).


# License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0



