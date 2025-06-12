package com.avitalshmueli.demoapp;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.avitalshmueli.demoapp.databinding.ActivityMainBinding;
import com.avitalshmueli.inappfeedbacksdk.FeedbackFormManager;
import com.avitalshmueli.inappfeedbacksdk.model.FeedbackForm;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.avitalshmueli.demoapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        FeedbackFormManager feedbackManager = FeedbackFormManager.getInstance(getApplicationContext());
        feedbackManager.setUserId("USER_123456");

        binding.mainBTNShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackManager.getActiveFeedbackForm(MainActivity.this, getSupportFragmentManager(), new FeedbackFormManager.FeedbackFormCallback<>() {
                    @Override
                    public void ready(FeedbackForm data) {
                        Log.d("Active Feedback Form","Form is ready");
                    }

                    @Override
                    public void failed(String errorMsg) {
                        Toast.makeText(MainActivity.this, errorMsg, LENGTH_LONG).show();
                        Log.d("Active Feedback Form","Error! " + errorMsg);
                    }
                });
            }
        });
    }
}