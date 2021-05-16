package com.viskee.brochure.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.util.ConfigFileDownloader;
import com.viskee.brochure.util.Utils;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (Utils.checkInternetConnection(this)) {
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            new ConfigFileDownloader(this, progressBar).execute(getString(R.string.AIBT_CONFIGURATION_FILE_LINK),
                    getString(R.string.REACH_CONFIGURATION_FILE_LINK));
        } else {
            File AIBT = new File(getFilesDir() + "/" + getString(R.string.AIBT_CONFIGURATION_FILE_NAME));
            File REACH = new File(getFilesDir() + "/" + getString(R.string.REACH_CONFIGURATION_FILE_NAME));
            if (!AIBT.exists() || !REACH.exists()) {
                new AlertDialog.Builder(this)
                        .setTitle("No configuration found")
                        .setMessage("Could you please connect to the Internet and relaunch the app ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Handler handler = new Handler();
                handler.postDelayed(this::goToMainActivity, SPLASH_TIME);
            }
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}