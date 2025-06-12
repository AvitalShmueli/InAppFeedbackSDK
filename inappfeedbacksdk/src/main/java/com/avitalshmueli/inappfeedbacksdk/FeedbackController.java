package com.avitalshmueli.inappfeedbacksdk;

import androidx.annotation.NonNull;

import com.avitalshmueli.inappfeedbacksdk.interfaces.FormLoadedCallback;
import com.avitalshmueli.inappfeedbacksdk.interfaces.FeedbackSubmitCallback;
import com.avitalshmueli.inappfeedbacksdk.model.Feedback;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackController {
    static final String BASE_URL = "https://feedback-backend-one.vercel.app/";

    private FeedbackFormAPI getAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .setLenient()
                                        .create()
                        )
                )
                .build();

        return retrofit.create(FeedbackFormAPI.class);
    }


    public void getFormByPackageName(String packageName, FormLoadedCallback callback) {
        Call<FeedbackForm> call = getAPI().getFormByPackageName(packageName);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<FeedbackForm> call, @NonNull Response<FeedbackForm> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FeedbackForm> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void submitFeedback(Feedback feedback, FeedbackSubmitCallback callback) {
        Call<Void> call = getAPI().submitFeedback(feedback);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String errorMsg = "Submission failed: HTTP " + response.code();
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            errorMsg += " - " + errorBody.string(); // or parse JSON if needed
                        }
                    } catch (Exception e) {
                        errorMsg += " - Unable to parse error";
                    }
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());
            }
        });
    }
}
