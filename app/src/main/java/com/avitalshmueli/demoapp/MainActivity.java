package com.avitalshmueli.demoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.avitalshmueli.demoapp.databinding.ActivityMainBinding;
import com.avitalshmueli.inappfeedbacksdk.FeedbackManager;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        binding.mainBTNShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedbackManager feedbackManager = FeedbackManager.getInstance(MainActivity.this);
                feedbackManager.getFormByPackageName(getPackageName(), new FeedbackManager.FeedbackCallback() {
                    @Override
                    public void onFormLoaded(FeedbackForm form) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        feedbackManager.showFeedbackDialog(fragmentManager, form);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(MainActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}