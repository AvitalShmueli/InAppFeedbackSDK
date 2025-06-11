package com.avitalshmueli.inappfeedbacksdk;

import com.avitalshmueli.inappfeedbacksdk.model.Feedback;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedbackFormAPI {
    @GET("forms/{package_name}")
    Call<FeedbackForm> getFormByPackageName(@Path("package_name") String packageName);

    @POST("/feedback")
    Call<Void> submitFeedback(@Body Feedback feedback);
}
