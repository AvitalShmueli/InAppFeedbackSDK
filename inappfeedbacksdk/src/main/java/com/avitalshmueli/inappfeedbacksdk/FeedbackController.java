package com.avitalshmueli.inappfeedbacksdk;

import com.avitalshmueli.inappfeedbacksdk.interfaces.FormLoadedCallback;
import com.avitalshmueli.inappfeedbacksdk.interfaces.FeedbackSubmitCallback;
import com.avitalshmueli.inappfeedbacksdk.model.Feedback;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackController {
    static final String BASE_URL = "http://192.168.1.103:5000/";  // TODO: change url of server

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
        call.enqueue(new Callback<FeedbackForm>() {
            @Override
            public void onResponse(Call<FeedbackForm> call, Response<FeedbackForm> response) {
                if (response.isSuccessful() && response.body() != null) {
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
        Call<Void> call = getAPI().submitFeedback(feedback);
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
}
