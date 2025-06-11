package com.avitalshmueli.inappfeedbacksdk;

import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.avitalshmueli.inappfeedbacksdk.databinding.DialogFeedbackBinding;
import com.avitalshmueli.inappfeedbacksdk.interfaces.FeedbackSubmitCallback;
import com.avitalshmueli.inappfeedbacksdk.model.Feedback;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class FeedbackDialogFragment extends DialogFragment {
    private DialogFeedbackBinding binding;
    private TextInputLayout dialog_TIL_message;
    private TextInputEditText dialog_TXT_message;
    private MaterialTextView dialog_LBL_description;
    private RatingBar dialog_BAR_rating;
    private MaterialButton dialog_BTN_submit;
    private MaterialButton dialog_BTN_notNow;
    private FeedbackFormManager feedbackFormManager;
    private FeedbackForm form;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedbackFormManager = FeedbackFormManager.getInstance(requireContext());
        Bundle args = getArguments();
        if (args != null) {
            form = new FeedbackForm()
                    .setId(args.getString("form_id"))
                    .setType(args.getString("form_type"))
                    .setTitle(args.getString("form_title"));
            feedbackFormManager.setFeedbackForm(form);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DialogFeedbackBinding.inflate(inflater, container, false);

        createBinding();

        applyFormSettings(form);
        //loadForm();

        dialog_BTN_submit.setOnClickListener(v -> submitFeedback());
        dialog_BTN_notNow.setOnClickListener(v -> dismiss());

        return binding.getRoot();
    }

    public static FeedbackDialogFragment newInstance(FeedbackForm form) {
        FeedbackDialogFragment fragment = new FeedbackDialogFragment();
        Bundle args = new Bundle();
        args.putString("form_id", form.getId());
        args.putString("form_type", form.getType());
        args.putString("form_title", form.getTitle());
        fragment.setArguments(args);
        return fragment;
    }

    private void createBinding(){
        dialog_LBL_description = binding.dialogLBLDescription;
        dialog_TIL_message = binding.dialogTILMessage;
        dialog_TXT_message = binding.dialogTXTMessage;
        dialog_BAR_rating = binding.dialogBARRating;
        dialog_BTN_submit = binding.dialogBTNSubmit;
        dialog_BTN_notNow = binding.dialogBTNNotNow;
    }


    private void submitFeedback() {
        ProgressBar loading = binding.dialogLoadingSpinner;
        loading.setVisibility(VISIBLE);

        String message = dialog_TXT_message.getText() != null ? dialog_TXT_message.getText().toString().trim() : null;
        if (TextUtils.isEmpty(message)) {
            message = null;
        }

        int rating = dialog_BAR_rating.getVisibility() == View.VISIBLE
                ? (int) dialog_BAR_rating.getRating()
                : 0;
        boolean isMessageVisible = dialog_TIL_message.getVisibility() == View.VISIBLE;
        boolean isRatingVisible = dialog_BAR_rating.getVisibility() == View.VISIBLE;

        if (isMessageVisible && message == null && isRatingVisible && rating == 0) {
            showValidationError("Feedback message is required", "Please provide a rating");
            loading.setVisibility(View.GONE);
            return;
        }

        if (isMessageVisible && message == null && !isRatingVisible) {
            showMessageError("Feedback message is required");
            loading.setVisibility(View.GONE);
            return;
        }

        if (isRatingVisible && rating == 0 && !isMessageVisible) {
            showToast("Please provide a rating");
            Log.e("pttt", "Missing rating");
            loading.setVisibility(View.GONE);
            return;
        }

        dialog_TIL_message.setError(null); // Clear any previous errors

        Feedback newFeedback = feedbackFormManager.buildFeedback(message, rating);

        feedbackFormManager.submitFeedback(newFeedback, new FeedbackSubmitCallback() {
            @Override
            public void onSuccess() {
                Log.d("pttt", "Feedback submitted successfully!");
                Toast.makeText(requireContext(), "Feedback submitted successfully. Thank you!", Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
                dismiss();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), "Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("pttt","Failed: " + errorMessage);
                loading.setVisibility(View.GONE);
            }
        });
    }

    private void showValidationError(String messageError, String ratingToast) {
        dialog_TIL_message.setError(messageError);
        showToast(ratingToast);
        Log.e("pttt", "Missing feedback message and rating");
    }

    private void showMessageError(String messageError) {
        dialog_TIL_message.setError(messageError);
        Log.e("pttt", "Missing feedback message");
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void applyFormSettings(FeedbackForm form) {
        dialog_LBL_description.setText(form.getTitle());

        switch (form.getType()) {
            case "rating":
                dialog_BAR_rating.setVisibility(VISIBLE);
                dialog_TIL_message.setVisibility(View.GONE);
                break;
            case "text":
                dialog_BAR_rating.setVisibility(View.GONE);
                dialog_TIL_message.setVisibility(VISIBLE);
                break;
            case "rating_text":
            default:
                dialog_BAR_rating.setVisibility(VISIBLE);
                dialog_TIL_message.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // prevent memory leaks
    }
}
