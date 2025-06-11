package com.avitalshmueli.inappfeedbacksdk.interfaces;

import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;

public interface FormLoadedCallback {
    void onSuccess(FeedbackForm form);
    void onFailure(String errorMessage);
}