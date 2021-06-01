package com.viskee.brochure.util;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.viskee.brochure.R;
import com.viskee.brochure.activity.MainActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ConfigFileDownloader extends AsyncTask<String, Integer, Boolean> {

    private final Activity context;
    private final ProgressBar progressBar;

    public ConfigFileDownloader(Activity context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(String... sUrl) {
        boolean aibtResult = downloadConfigurationFile(sUrl[0],
                context.getString(R.string.AIBT_CONFIGURATION_FILE_NAME));
        boolean reachResult = downloadConfigurationFile(sUrl[1],
                context.getString(R.string.REACH_CONFIGURATION_FILE_NAME));
        return aibtResult && reachResult;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean isConfigurationDownloadSuccessfully) {
        super.onPostExecute(isConfigurationDownloadSuccessfully);
        if (isConfigurationDownloadSuccessfully) {
            File AIBT =
                    new File(context.getFilesDir() + "/" + context.getString(R.string.AIBT_CONFIGURATION_FILE_NAME));
            File REACH =
                    new File(context.getFilesDir() + "/" + context.getString(R.string.REACH_CONFIGURATION_FILE_NAME));
            if (!AIBT.exists() || !REACH.exists()) {
                displayAlert(context);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        } else {
            displayAlert(context);
        }
    }

    private boolean downloadConfigurationFile(String sUrl, String fileName) {
        InputStream inputStream = null;
        try {
            URL url = new URL(sUrl);
            inputStream = url.openStream();
            Files.copy(inputStream, Paths.get(context.getFilesDir() + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            Log.e(ConfigFileDownloader.class.getSimpleName(), e.getMessage());
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException exception) {
                Log.e(ConfigFileDownloader.class.getSimpleName(), exception.getMessage());
            }
        }
        return true;
    }

    private void displayAlert(Activity context) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)
                        .setTitle("No configuration found")
                        .setMessage("Could you please connect to the Internet and relaunch the app ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

}

