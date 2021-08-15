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
            new ConfigFileDownloader(this, progressBar).execute(getString(R.string.ACE_AVIATION_AEROSPACE_ACADEMY_FILE_LINK),
                    getString(R.string.BESPOKE_GRAMMAR_SCHOOL_OF_ENGLISH_FILE_LINK),
                    getString(R.string.BRANSON_SCHOOL_OF_BUSINESS_AND_TECHNOLOGY_FILE_LINK),
                    getString(R.string.DIANA_SCHOOL_OF_COMMUNITY_SERVICES_FILE_LINK),
                    getString(R.string.EDISON_SCHOOL_OF_TECH_SCIENCES_FILE_LINK),
                    getString(R.string.SHELDON_SCHOOL_OF_HOSPITALITY_FILE_LINK),
                    getString(R.string.REACH_COMMUNITY_COLLEGE_FILE_LINK));
        } else {
            File ACE = new File(getFilesDir() + "/" + getString(R.string.ACE_AVIATION_AEROSPACE_ACADEMY_FILE_NAME));
            File BESPOKE = new File(getFilesDir() + "/" + getString(R.string.BESPOKE_GRAMMAR_SCHOOL_OF_ENGLISH_FILE_NAME));
            File BRANSON = new File(getFilesDir() + "/" + getString(R.string.BRANSON_SCHOOL_OF_BUSINESS_AND_TECHNOLOGY_FILE_NAME));
            File DIANA = new File(getFilesDir() + "/" + getString(R.string.DIANA_SCHOOL_OF_COMMUNITY_SERVICES_FILE_NAME));
            File EDISON = new File(getFilesDir() + "/" + getString(R.string.EDISON_SCHOOL_OF_TECH_SCIENCES_FILE_NAME));
            File SHELDON = new File(getFilesDir() + "/" + getString(R.string.SHELDON_SCHOOL_OF_HOSPITALITY_FILE_NAME));
            File REACH = new File(getFilesDir() + "/" + getString(R.string.REACH_COMMUNITY_COLLEGE_FILE_NAME));
            if (!ACE.exists() || !BESPOKE.exists() || !BRANSON.exists() || !DIANA.exists() || !EDISON.exists()
                    || !SHELDON.exists() || !REACH.exists()) {
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