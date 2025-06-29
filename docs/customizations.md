# How to Customize the Feedback Dialog?

By default, the feedback dialog adopts your app’s theme.
However, if you’d like to tailor the dialog’s appearance to match your brand or preferences, the SDK provides several customization options.

You can customize the following elements by calling the appropriate methods on FeedbackFormManager before showing the dialog:

### 1. Title Text Color
Customize the title's text color:

    ```java
    feedbackManager.setDialogTitleTextColor(getColor(R.color.green));
    ```

### 2. Description Text Color
Customize the description’s text color:

    ```java
    feedbackManager.setDialogDescriptionTextColor(getColor(R.color.cyan));
    ```

### 3. Submit Button Background Color
Customize the background color of the **Submit** button:

    ```java
    feedbackManager.setDialogSubmitButtonBackgroundColor(getColor(R.color.green));
    ```

### 4. Submit Button Text Color
Customize the text color of the **Submit** button:

    ```java
    feedbackManager.setDialogSubmitButtonTextColor(getColor(R.color.white));
    ```

### 5. Cancel Button Text Color

Customize the text color of the **Cancel / Not Now** button:
    ```java
    feedbackManager.setDialogCancelButtonTextColor(getColor(R.color.cyan));
    ```

## 💡 Notes
These methods must be called before invoking getActiveFeedbackForm() or displaying the dialog.

All methods accept a resolved color (int) - for example, use ContextCompat.getColor(context, R.color.my_color) if you're not in an Activity.