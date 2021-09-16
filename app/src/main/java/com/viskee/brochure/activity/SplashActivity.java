package com.viskee.brochure.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

import com.viskee.brochure.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(this::goToOnshoreOffshoreActivity, SPLASH_TIME);
    }

    private void goToOnshoreOffshoreActivity() {
        Intent intent = new Intent(SplashActivity.this, OnshoreOffshoreActivity.class);
        startActivity(intent);
        finish();
    }
}