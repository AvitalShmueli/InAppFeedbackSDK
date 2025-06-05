package com.avitalshmueli.inappfeedbacksdk;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.avitalshmueli.inappfeedbacksdk.databinding.ActivityFeedbackBinding;
import com.avitalshmueli.inappfeedbacksdk.model.Feedback;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class FeedbackActivity extends AppCompatActivity {
    private String formId;
    private String appVersion;
    private String deviceInfo;

    private ActivityFeedbackBinding binding;
    private TextInputLayout feedback_TIL_message;
    private TextInputEditText feedback_TXT_message;
    private MaterialTextView feedback_LBL_description;
    private RatingBar feedback_BAR_rating;
    private MaterialButton feedback_BTN_submit;
    private MaterialButton feedback_BTN_notNow;
    private  FeedbackManager feedbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        createBinding();

        feedbackManager = FeedbackManager.getInstance(this);
        //feedbackManager.setUserId("user_abc123"); // Will override and persist - example for Authenticated Apps

        loadForm();

        /*
        Intent previousIntent = getIntent();
        formId = previousIntent.getStringExtra("form_id");
        userId = previousIntent.getStringExtra("user_id");
        appVersion = previousIntent.getStringExtra("app_version");
        deviceInfo = previousIntent.getStringExtra("device_info");
        */

        feedback_BTN_submit.setOnClickListener(v -> submitFeedback());
        feedback_BTN_notNow.setOnClickListener(v -> finish());
    }

    private void createBinding(){
        feedback_LBL_description = binding.feedbackLBLDescription;
        feedback_TIL_message = binding.feedbackTILMessage;
        feedback_TXT_message = binding.feedbackTXTMessage;
        feedback_BAR_rating = binding.feedbackBARRating;
        feedback_BTN_submit = binding.feedbackBTNSubmit;
        feedback_BTN_notNow = binding.feedbackBTNNotNow;
    }

    private void loadForm() {
        String packageName = getPackageName();
        feedbackManager.getFormByPackageName(packageName, new FeedbackManager.FeedbackCallback() {
            @Override
            public void onFormLoaded(FeedbackForm form) {
                formId = form.getId();
                applyFormSettings(form);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(FeedbackActivity.this, "Failed to load form", Toast.LENGTH_SHORT).show();
                Log.e("pttt","Failed to load form: " + errorMessage);
            }
        });
        /*
        FeedbackForm form = new FeedbackForm();
        form.setPackage_name(packageName);
        form.setFormId(String.valueOf(UUID.randomUUID())); // TODO: check if needed
        form.setTitle("Please rate your experience");
        form.setType("rating_text"); // or "text", "rating"

        applyFormSettings(form);
        */
    }

    private void submitFeedback() {
        String message = null;
        int rating = 0;

        ProgressBar loading = binding.loadingSpinner;
        loading.setVisibility(VISIBLE);

        if (feedback_TIL_message.getVisibility() == VISIBLE) {
            message = feedback_TXT_message.getText() != null ? feedback_TXT_message.getText().toString().trim() : "";

            if (TextUtils.isEmpty(message)) {
                feedback_TIL_message.setError("Feedback message is required");
                Log.e("pttt","Missing feedback message");
                loading.setVisibility(View.GONE);
                return;
            } else {
                feedback_TIL_message.setError(null);
            }
        }
        if (feedback_BAR_rating.getVisibility() == VISIBLE) {
            rating = (int) feedback_BAR_rating.getRating();
            if (rating == 0) {
                Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
                Log.e("pttt","Missing rating");
                loading.setVisibility(View.GONE);
                return;
            }
        }

        Feedback newFeedback = feedbackManager.buildFeedback(message, rating);

        feedbackManager.submitFeedback(newFeedback, new FeedbackManager.FeedbackSubmitCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                Log.d("pttt", "Feedback submitted successfully!");
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(FeedbackActivity.this, "Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("pttt","Failed: " + errorMessage);
                loading.setVisibility(View.GONE);
            }
        });

        Toast.makeText(this, "Feedback submitted. Thank you!", Toast.LENGTH_LONG).show();
        //finish();
    }

    private void applyFormSettings(FeedbackForm form) {
        feedback_LBL_description.setText(form.getTitle());

        switch (form.getType()) {
            case "rating":
                feedback_BAR_rating.setVisibility(VISIBLE);
                feedback_TIL_message.setVisibility(View.GONE);
                break;
            case "text":
                feedback_BAR_rating.setVisibility(View.GONE);
                feedback_TIL_message.setVisibility(VISIBLE);
                break;
            case "rating_text":
            default:
                feedback_BAR_rating.setVisibility(VISIBLE);
                feedback_TIL_message.setVisibility(VISIBLE);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;  // Helps avoid memory leaks
    }
}