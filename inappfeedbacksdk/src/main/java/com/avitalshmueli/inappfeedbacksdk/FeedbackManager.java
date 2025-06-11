package com.avitalshmueli.inappfeedbacksdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.avitalshmueli.inappfeedbacksdk.interfaces.FormLoadedCallback;
import com.avitalshmueli.inappfeedbacksdk.interfaces.FeedbackSubmitCallback;
import com.avitalshmueli.inappfeedbacksdk.model.Feedback;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackManager {
    private static FeedbackManager instance;
    private final Context appContext;
    private String userId;
    private String appVersion;
    private FeedbackFormAPI api;
    private FeedbackForm feedbackForm;

    static final String BASE_URL = "http://192.168.1.103:5000/";  // TODO: change url of server
    private static final String PREF_NAME = "in_app_feedback_prefs";
    private static final String KEY_USER_ID = "user_id";

    private FeedbackManager(Context context) {
        this.appContext = context.getApplicationContext();
        this.appVersion = getAppVersion();
        this.userId = getOrCreateUserId();
    }

    private void initApi() {
        if (api == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Use this for Android emulator
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            api = retrofit.create(FeedbackFormAPI.class);
        }
    }

    public static synchronized FeedbackManager getInstance(Context context) {
        if (instance == null) {
            instance = new FeedbackManager(context);
        }
        return instance;
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

    public void getFormByPackageName(String packageName, FormLoadedCallback callback) {
        initApi();
        Call<FeedbackForm> call = api.getFormByPackageName(packageName);
        call.enqueue(new Callback<FeedbackForm>() {
            @Override
            public void onResponse(Call<FeedbackForm> call, Response<FeedbackForm> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ptttt", new Gson().toJson(response.body()));
                    feedbackForm = response.body();
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<FeedbackForm> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void submitFeedback(Feedback feedback, FeedbackSubmitCallback callback) {
        initApi();
        Call<Void> call = api.submitFeedback(feedback);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String errorMsg = "Submission failed: HTTP " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            //errorMsg += " - " + response.errorBody().string();
                            errorMsg += " - " + response.message();
                        }
                    } catch (Exception e) {
                        errorMsg += " - Unable to parse error";
                    }
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }

    public void showFeedbackDialog(FragmentManager fragmentManager, FeedbackForm form) {
        FeedbackDialogFragment dialog = FeedbackDialogFragment.newInstance(form);
        dialog.show(fragmentManager, "FeedbackDialog");
    }

    public interface MyCallBack<T> {
        void ready(T data);
        void failed(Throwable throwable);
    }
}
