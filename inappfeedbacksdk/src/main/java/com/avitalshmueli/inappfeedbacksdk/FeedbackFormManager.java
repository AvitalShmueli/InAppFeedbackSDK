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

public class FeedbackFormManager {
    private static FeedbackFormManager instance;
    private Context appContext;
    private String userId;
    private String appVersion;
    private FeedbackForm feedbackForm;

    private static final String PREF_NAME = "in_app_feedback_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final FeedbackController feedbackController = new FeedbackController();

    public interface FeedbackFormCallback<T> {
        void ready(T data);
        void failed(String errorMsg);
    }

    public void setFeedbackForm(FeedbackForm form) {
        this.feedbackForm = form;
    }

    public static FeedbackFormManager getInstance(Context context) {
        if (instance == null) {
            instance = new FeedbackFormManager();
            instance.appContext = context.getApplicationContext();
            instance.appVersion = instance.getAppVersion();
        }
        return instance;
    }

    public static void getActiveFeedbackForm(Context context, FragmentManager fragmentManager, FeedbackFormCallback callback){
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
                        Log.d("ptttt","failed "+errorMessage);
                    }
                });
    }

    /**
     * Host apps can provide a custom user ID (for authenticated users).
     */
    public void setUserId(String userId) {
        if (userId != null && !userId.isEmpty()) {
            this.userId = userId;
            // Optionally store it for consistency
            appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    .edit().putString(KEY_USER_ID, userId).apply();
        }
    }

    private String getOrCreateUserId() {
        SharedPreferences prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String storedId = prefs.getString(KEY_USER_ID, null);
        if (storedId != null) return storedId;

        // No ID stored: generate anonymous UUID and save it
        String generatedId = UUID.randomUUID().toString();
        prefs.edit().putString(KEY_USER_ID, generatedId).apply();
        return generatedId;
    }

    public String getUserId() {
        return userId;
    }

    public Feedback buildFeedback(String message, int rating) {
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

    private String getDeviceInfo() {
        return Build.MANUFACTURER + " " + Build.MODEL + " (" + Build.VERSION.RELEASE + ")";
    }

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

    private static void showFeedbackDialog(FragmentManager fragmentManager, FeedbackForm form) {
        FeedbackDialogFragment dialog = FeedbackDialogFragment.newInstance(form);
        dialog.show(fragmentManager, "FeedbackDialog");
    }

    public void submitFeedback(Feedback feedback, FeedbackSubmitCallback callback) {
        feedbackController.submitFeedback(feedback, callback);
    }

}


