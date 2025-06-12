package com.avitalshmueli.inappfeedbacksdk.interfaces;

public interface FeedbackSubmitCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}