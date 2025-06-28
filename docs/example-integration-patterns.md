# Example Integration Patterns

## Pattern 1: Post-Action Feedback

```java
// After user completes an important action
private void onImportantActionCompleted() {
// Perform the action
completeUserAction();

    // Show feedback after 2 seconds
    new Handler(Looper.getMainLooper()).postDelayed(() -> {
        showFeedbackDialog();
    }, 2000);
}
```

## Pattern 2: Menu-Based Feedback
```java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
if (item.getItemId() == R.id.menu_feedback) {
    showFeedbackDialog();
return true;
}
return super.onOptionsItemSelected(item);
}
```

## Pattern 3: Periodic Feedback
```java
// Ask for feedback every 7 days
private void checkForPeriodicFeedback() {
SharedPreferences prefs = getSharedPreferences("feedback", MODE_PRIVATE);
long lastAsked = prefs.getLong("last_feedback_request", 0);
long now = System.currentTimeMillis();

    if (now - lastAsked > TimeUnit.DAYS.toMillis(7)) {
        showFeedbackDialog();
        prefs.edit().putLong("last_feedback_request", now).apply();
    }
}
```

