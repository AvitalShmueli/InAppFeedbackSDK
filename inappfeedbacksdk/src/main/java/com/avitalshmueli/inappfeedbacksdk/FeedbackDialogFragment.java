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
    private  FeedbackManager feedbackManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DialogFeedbackBinding.inflate(inflater, container, false);

        createBinding();

        feedbackManager = FeedbackManager.getInstance(getContext());
        loadForm();

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

    private void loadForm() {
        String packageName = requireContext().getPackageName();
        feedbackManager.getFormByPackageName(packageName, new FeedbackManager.FeedbackCallback() {
            @Override
            public void onFormLoaded(FeedbackForm form) {
                //formId = form.getId();
                applyFormSettings(form);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Failed to load form", Toast.LENGTH_SHORT).show();
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
        ProgressBar loading = binding.dialogLoadingSpinner;
        loading.setVisibility(VISIBLE);

        String message = dialog_TXT_message.getText() != null ? dialog_TXT_message.getText().toString().trim() : null;
        if (TextUtils.isEmpty(message)) {
            message = null;
        }

        int rating = (int) dialog_BAR_rating.getRating();
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

        /*if (dialog_TIL_message.getVisibility() == VISIBLE && dialog_BAR_rating.getVisibility() == VISIBLE) {
            if(message == null && rating == 0){
                dialog_TIL_message.setError("Feedback message is required");
                Toast.makeText(requireContext(), "Please provide a rating", Toast.LENGTH_SHORT).show();
                Log.e("pttt","Missing feedback message and rating");
                loading.setVisibility(View.GONE);
                return;
            }
            else{
                dialog_TIL_message.setError(null);
            }
        }
        else {
            if (dialog_TIL_message.getVisibility() == VISIBLE) {
                if (message == null) {
                    dialog_TIL_message.setError("Feedback message is required");
                    Log.e("pttt", "Missing feedback message");
                    loading.setVisibility(View.GONE);
                    return;
                } else {
                    dialog_TIL_message.setError(null);
                }
            }
            if (dialog_BAR_rating.getVisibility() == VISIBLE) {
                if (rating == 0) {
                    Toast.makeText(requireContext(), "Please provide a rating", Toast.LENGTH_SHORT).show();
                    Log.e("pttt", "Missing rating");
                    loading.setVisibility(View.GONE);
                    return;
                }
            }
        }*/

        Feedback newFeedback = feedbackManager.buildFeedback(message, rating);
        feedbackManager.submitFeedback(newFeedback, new FeedbackManager.FeedbackSubmitCallback() {
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
