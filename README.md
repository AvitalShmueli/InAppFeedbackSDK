 # InAppFeedbackSDK
![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)
[![](https://jitpack.io/v/AvitalShmueli/InAppFeedbackSDK.svg)](https://jitpack.io/#AvitalShmueli/InAppFeedbackSDK)

The **InAppFeedbackSDK** library is a lightweight Android SDK to **collect in-app user feedback** with a customizable dialog and automatic device/app metadata capture. Built for easy integration and seamless UX.

<p align="center">
<img src="https://github.com/user-attachments/assets/9fb928d5-5f69-430a-a662-fd10f9ffb947" alt="demo_app"
style="height:400px;"/>
</p>

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

# License

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0



