 # InAppFeedbackSDK
![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)
[![](https://jitpack.io/v/AvitalShmueli/InAppFeedbackSDK.svg)](https://jitpack.io/#AvitalShmueli/InAppFeedbackSDK)

The **InAppFeedbackSDK** library is a lightweight Android SDK to **collect in-app user feedback** with a customizable dialog and automatic device/app metadata capture. Built for easy integration and seamless UX.


## Features

- Easily fetch and display feedback forms
- Collect user messages and optional star ratings
- Includes app version, device info, and timestamp automatically
- Supports anonymous or authenticated user IDs
- Clean and native UI dialog using `FragmentManager`
- Retrofit-powered submission with callback support


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
```kotlin
dependencies {
	        implementation("com.github.AvitalShmueli:InAppFeedbackSDK:Tag")
	}
```
convert dependency to kotlin syntax using gradle/libs.versions.toml file.

# How to Start?
1. Initialize the SDK and fetch the active form:
```java
FeedbackFormManager manager = FeedbackFormManager.getInstance(context);

manager.getActiveFeedbackForm(context, fragmentManager, new FeedbackFormManager.FeedbackFormCallback<FeedbackForm>() {
    @Override
    public void ready(FeedbackForm form) {
        // The feedback dialog will be shown automatically
    }

    @Override
    public void failed(String errorMsg) {
        Log.e("FeedbackSDK", "Form load failed: " + errorMsg);
    }
});
```
2. Optionally set a user ID (e.g., logged-in user):
```java
manager.setUserId("user_1234");
```

# License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0



