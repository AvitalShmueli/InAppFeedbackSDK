# Feedback Portal Guide
The Feedback Portal is a web-based management interface for developers to configure and monitor their in-app feedback systems.
Use it to create feedback forms, track user responses, and analyze feedback data from your Android applications.

## Portal Access
URL: https://feedback-portal-eight.vercel.app

Access the portal through any web browser. No authentication is required for basic usage.

---

## Creating Feedback Forms

### Form Creation Process
1. Navigate to the portal and click "Create Form"
2. Configure your form:
    - **Package Name**: Enter your Android app's package name exactly as it appears in your build.gradle file
    - **Title**: Set the message users will see in the feedback dialog
    - **Form Type**: Choose the input method for your users:
      - Rating - 5-star rating only
      - Free Text - Text input field only
      - Rating & Text - Both rating and text input
3. Deploy the form by clicking "Create"

### Important Constraints
- **One Active Form Per Package**: Creating a new form automatically deactivates any existing form for that package
- **Package Name Validation**: The package name must match your Android app exactly (case-sensitive)
- **Immediate Deployment**: Forms are live immediately after creation

**Form Configuration Example**
```
Package Name: com.mycompany.myapp
Title: "How would you rate your experience?"
Form Type: Rating & Text
```

This configuration will display a dialog with the specified title, star rating, and text input field.

---

## Managing Forms

### Form States
- **Active**: The form is live and will be served to your app
- **Inactive**: The form exists but won't be displayed to users

### Form Operations
- **Enable/Disable**: Toggle form availability without deleting configuration
- **View Details**: Check form configuration and status
- **Track Submissions**: Track submission counts and activity

---

## Viewing Feedback Data

### Data Available
For each feedback submission, you'll see:
- **Rating**: Star rating (1-5) if rating is enabled
- **Message**: User's text feedback if text input is enabled
- **Device Information**: Device model and OS version
- **App Version**: Version of your app that submitted the feedback
- **Timestamp**: When the feedback was submitted
- **User ID**: If you've set a user ID in your app code

### Filtering Options
Filter feedback data by:
- **Package Name**: View feedback for specific apps
- **Status**: Filter by form status (active/inactive)
- **Type**: Filter by form type (Rating, Free Text, Rating & Text)
- **Form Title**: Search by form title text

---

## Integration Workflow

### Development Flow
1. **Create Form**: Use the portal to create and configure your feedback form
2. **Integrate SDK**: Add the SDK to your Android app following the [Getting Started guide](./getting-started.md)
3. **Test Integration**: Verify the form appears correctly in your app
4. **Monitor Data**: Use the portal to view incoming feedback

---

### Testing Your Setup
1. **Create a test form** with your app's package name
2. **Run your app** and trigger the feedback dialog
3. **Submit test feedback** from the app
4. **Verify in portal** that the feedback appears in the dashboard
