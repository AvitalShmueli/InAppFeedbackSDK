package com.avitalshmueli.inappfeedbacksdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.avitalshmueli.inappfeedbacksdk.interfaces.FeedbackSubmitCallback;
import com.avitalshmueli.inappfeedbacksdk.interfaces.FormLoadedCallback;
import com.avitalshmueli.inappfeedbacksdk.model.Feedback;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;

import java.util.Date;
import java.util.UUID;

/**
 * Manager class for handling feedback form operations in the host app.
 * Implements singleton pattern to ensure a single instance is used.
 */
public class FeedbackFormManager {
    private static FeedbackFormManager instance;
    private Context appContext;
    private String userId;
    private String appVersion;
    private FeedbackForm feedbackForm;

    private static final String PREF_NAME = "in_app_feedback_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final FeedbackController feedbackController = new FeedbackController();

    private Integer dialogTitleTextColor;
    private Integer dialogDescriptionTextColor;
    private Integer dialogSubmitButtonBackgroundColor;
    private Integer dialogSubmitButtonTextColor;
    private Integer dialogCancelButtonTextColor;

    /**
     * Callback interface for delivering feedback form loading results.
     * @param <T> The type of data returned in the callback.
     */
    public interface FeedbackFormCallback<T> {
        void ready(T data);
        void failed(String errorMsg);
    }

    /**
     * Returns the singleton instance of FeedbackFormManager.
     * Initializes the instance if not already created.
     *
     * @param context Application context used for initialization.
     * @return The singleton instance of FeedbackFormManager.
     */
    public static FeedbackFormManager getInstance(Context context) {
        if (instance == null) {
            instance = new FeedbackFormManager();
            instance.appContext = context.getApplicationContext();
            instance.appVersion = instance.getAppVersion();

        }
        return instance;
    }

    /**
     * Fetches the active feedback form from the backend using the package name.
     * If successful, shows the feedback dialog.
     *
     * @param context The application context.
     * @param fragmentManager The FragmentManager to show the dialog.
     * @param callback Callback to notify if loading failed.
     */
    public void getActiveFeedbackForm(Context context, FragmentManager fragmentManager, FeedbackFormCallback<FeedbackForm> callback){
        if (callback == null) {
            return;
        }

        feedbackController.getFormByPackageName(context.getPackageName(),
                new FormLoadedCallback() {
                    @Override
                    public void onSuccess(FeedbackForm form) {
                        FeedbackFormManager manager = getInstance(context);
                        manager.setFeedbackForm(form); // save it
                        showFeedbackDialog(fragmentManager, form);
                        callback.ready(form);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        callback.failed(errorMessage);
                        Log.d("FeedbackFormManager","failed "+errorMessage);
                    }
                });
    }

    /**
     * Sets a custom user ID, typically for authenticated users.
     * Stores it in shared preferences.
     *
     * @param userId The user ID to associate with feedback.
     */
    public void setUserId(String userId) {
        if (userId != null && !userId.isEmpty()) {
            this.userId = userId;
            appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    .edit().putString(KEY_USER_ID, userId).apply();
        }
    }


    /**
     * Returns the stored user ID, or creates one if not found.
     * Stores the new ID in shared preferences.
     *
     * @return Existing or newly created user ID.
     */
    private String getOrCreateUserId() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String storedId = prefs.getString(KEY_USER_ID, null);
        if (storedId != null) return storedId;

        // No ID stored: generate anonymous UUID and save it
        String generatedId = UUID.randomUUID().toString();
        prefs.edit().putString(KEY_USER_ID, generatedId).apply();
        return generatedId;
    }

    /**
     * Returns the currently set user ID.
     *
     * @return The user ID or null if not set.
     */
    String getUserId() {
        return userId;
    }

    /**
     * Assigns a feedback form to be used for submission.
     *
     * @param form The FeedbackForm object to store.
     */
    void setFeedbackForm(FeedbackForm form) {
        this.feedbackForm = form;
    }

    /**
     * Builds a Feedback object with the provided message and optional rating.
     *
     * @param message The user’s feedback message.
     * @param rating The user’s rating, or 0 if not provided.
     * @return A Feedback object ready for submission.
     */
    Feedback buildFeedback(String message, int rating) {
        if (rating == 0){
            return new Feedback(
                    appContext.getPackageName(),
                    feedbackForm.getId(),
                    getOrCreateUserId(),
                    message,
                    null,
                    appVersion,
                    getDeviceInfo(),
                    new Date()
            );
        }
        return new Feedback(
                appContext.getPackageName(),
                feedbackForm.getId(),
                getOrCreateUserId(),
                message,
                rating,
                appVersion,
                getDeviceInfo(),
                new Date()
        );
    }

    /**
     * Returns device manufacturer, model, and OS version as a string.
     *
     * @return Device information string.
     */
    private String getDeviceInfo() {
        return Build.MANUFACTURER + " " + Build.MODEL + " (" + Build.VERSION.RELEASE + ")";
    }

    /**
     * Gets the app version name from the package info.
     *
     * @return App version name, or "unknown" if unavailable.
     */
    private String getAppVersion() {
        try {
            return appContext
                    .getPackageManager()
                    .getPackageInfo(appContext.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
            return "unknown";
        }
    }

    public Integer getDialogTitleTextColor() {
        return dialogTitleTextColor;
    }

    public FeedbackFormManager setDialogTitleTextColor(Integer dialogTitleTextColor) {
        this.dialogTitleTextColor = dialogTitleTextColor;
        return this;
    }

    public Integer getDialogDescriptionTextColor() {
        return dialogDescriptionTextColor;
    }

    public FeedbackFormManager setDialogDescriptionTextColor(Integer dialogDescriptionTextColor) {
        this.dialogDescriptionTextColor = dialogDescriptionTextColor;
        return this;
    }

    public Integer getDialogSubmitButtonBackgroundColor() {
        return dialogSubmitButtonBackgroundColor;
    }

    public FeedbackFormManager setDialogSubmitButtonBackgroundColor(Integer dialogSubmitButtonBackgroundColor) {
        this.dialogSubmitButtonBackgroundColor = dialogSubmitButtonBackgroundColor;
        return this;
    }

    public Integer getDialogSubmitButtonTextColor() {
        return dialogSubmitButtonTextColor;
    }

    public FeedbackFormManager setDialogSubmitButtonTextColor(Integer dialogSubmitButtonTextColor) {
        this.dialogSubmitButtonTextColor = dialogSubmitButtonTextColor;
        return this;
    }

    public Integer getDialogCancelButtonTextColor() {
        return dialogCancelButtonTextColor;
    }

    public FeedbackFormManager setDialogCancelButtonTextColor(Integer dialogCancelButtonTextColor) {
        this.dialogCancelButtonTextColor = dialogCancelButtonTextColor;
        return this;
    }

    /**
     * Shows the feedback dialog fragment with the given feedback form.
     *
     * @param fragmentManager FragmentManager to manage the dialog.
     * @param form The FeedbackForm to display.
     */
    private void showFeedbackDialog(FragmentManager fragmentManager, FeedbackForm form) {
        FeedbackDialogFragment dialog = FeedbackDialogFragment.newInstance(form);
        dialog.show(fragmentManager, "FeedbackDialog");
    }

    /**
     * Submits the given Feedback object to the backend.
     * @param feedback The Feedback object to be submitted.
     * @param callback Callback to notify on success or failure.
     */
    void submitFeedback(Feedback feedback, FeedbackSubmitCallback callback) {
        feedbackController.submitFeedback(feedback, callback);
    }

}


