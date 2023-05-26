package com.shubham.englishtalk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.shubham.englishtalk.R;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {

//                    if (auth.getCurrentUser() != null){
//                        goToNextActivity();
//                    }
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }; thread.start();
    }
//    void goToNextActivity(){
//        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//        finish();
//    }
}