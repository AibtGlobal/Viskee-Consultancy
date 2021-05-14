package com.example.brochure.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.brochure.R;
import com.example.brochure.adapter.MainViewAdapter;
import com.example.brochure.util.ConfigFileDownloader;

import java.io.File;

import static java.util.stream.Collectors.groupingBy;

public class MainActivity extends AppCompatActivity {

    private static final String AIBT_CONFIGURATION = "https://raw.githubusercontent.com/ZelongChen/paomia/gh-pages/AIBT.json";
    private static final String REACH_CONFIGURATION = "https://raw.githubusercontent.com/ZelongChen/paomia/gh-pages/REACH.json";

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

        if (checkInternetConnection()) {
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            new ConfigFileDownloader(this, progressBar).execute(AIBT_CONFIGURATION, REACH_CONFIGURATION);
        } else {
            File AIBT = new File(getFilesDir() + "/AIBT.json");
            File REACH = new File(getFilesDir() + "/REACH.json");
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

    @SuppressLint("WrongConstant")
    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities == null) {
                return false;
            }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true;
            }
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo.isConnected();
        }
        return false;
    }
}