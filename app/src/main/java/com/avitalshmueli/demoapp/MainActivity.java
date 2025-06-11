package com.avitalshmueli.demoapp;

import static android.widget.Toast.LENGTH_LONG;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.avitalshmueli.demoapp.databinding.ActivityMainBinding;
import com.avitalshmueli.inappfeedbacksdk.FeedbackFormManager;

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
                //FeedbackFormManager
                FeedbackFormManager.getActiveFeedbackForm(MainActivity.this, getSupportFragmentManager(), new FeedbackFormManager.FeedbackFormCallback() {
                    @Override
                    public void ready(Object data) {

                    }

                    @Override
                    public void failed(String errorMsg) {
                        Toast.makeText(MainActivity.this,errorMsg,LENGTH_LONG).show();
                    }
                });



//                FeedbackManager feedbackManager = FeedbackManager.getInstance(MainActivity.this);
//                feedbackManager.getFormByPackageName(getPackageName(), new FeedbackFormCallback() {
//                    @Override
//                    public void onFormLoaded(FeedbackForm form) {
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        feedbackManager.showFeedbackDialog(fragmentManager, form);
//                    }
//
//                    @Override
//                    public void onError(String errorMessage) {
//                        Toast.makeText(MainActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }
}