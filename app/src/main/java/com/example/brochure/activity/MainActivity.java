package com.example.brochure.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brochure.R;
import com.example.brochure.adapter.MainViewAdapter;
import com.example.brochure.util.ConfigFileDownloader;
import com.example.brochure.util.Utils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main_placeholder_portrait);
        } else {
            setContentView(R.layout.activity_main_placeholder_landscape);
        }

        if (Utils.checkInternetConnection(this)) {
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            new ConfigFileDownloader(this, progressBar).execute(getString(R.string.AIBT_CONFIGURATION_FILE_LINK), getString(R.string.REACH_CONFIGURATION_FILE_LINK));
        } else {
            File AIBT = new File(getFilesDir() + "/" + getString(R.string.AIBT_CONFIGURATION_FILE_NAME));
            File REACH = new File(getFilesDir() + "" + getString(R.string.REACH_CONFIGURATION_FILE_NAME));
            if (!AIBT.exists() || !REACH.exists()) {
                new AlertDialog.Builder(this)
                        .setTitle("No configuration found")
                        .setMessage("Could you please connect to the Internet and relaunch the app ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                final LayoutInflater layoutInflater = LayoutInflater.from(this);
                View layoutView;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    layoutView = layoutInflater.inflate(R.layout.activity_main_portrait, null);
                } else {
                    layoutView = layoutInflater.inflate(R.layout.activity_main_landscape, null);
                }
                setContentView(layoutView);
                new MainViewAdapter(this, layoutView).prepareData();
            }
        }
    }


}