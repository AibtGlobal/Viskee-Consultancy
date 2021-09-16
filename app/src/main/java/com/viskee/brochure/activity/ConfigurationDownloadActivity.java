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

public class ConfigurationDownloadActivity extends AppCompatActivity {

    private static final long SPLASH_TIME = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_download);

        String subFolder = getIntent().getStringExtra(getString(R.string.SUB_FOLDER));

        if (Utils.checkInternetConnection(this)) {
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            new ConfigFileDownloader(this, progressBar, subFolder).execute();
        } else {
            File ACE = new File(getFilesDir() + "/" + subFolder + "_" + getString(R.string.ACE_FILE_NAME));
            File BESPOKE = new File(getFilesDir() + "/" + subFolder + "_" + getString(R.string.BESPOKE_FILE_NAME));
            File BRANSON = new File(getFilesDir() + "/" + subFolder + "_" + getString(R.string.BRANSON_FILE_NAME));
            File DIANA = new File(getFilesDir() + "/" + subFolder + "_" + getString(R.string.DIANA_FILE_NAME));
            File EDISON = new File(getFilesDir() + "/" + subFolder + "_" + getString(R.string.EDISON_FILE_NAME));
            File SHELDON = new File(getFilesDir() + "/" + subFolder + "_" + getString(R.string.SHELDON_FILE_NAME));
            File REACH = new File(getFilesDir() + "/" + subFolder + "_" + getString(R.string.REACH_FILE_NAME));
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
        Intent intent = new Intent(ConfigurationDownloadActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}