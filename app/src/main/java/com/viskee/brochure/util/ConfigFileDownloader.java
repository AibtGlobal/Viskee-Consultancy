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
        boolean ace = downloadConfigurationFile(sUrl[0],
                context.getString(R.string.ACE_AVIATION_AEROSPACE_ACADEMY_FILE_NAME));
        boolean bespoke = downloadConfigurationFile(sUrl[1],
                context.getString(R.string.BESPOKE_GRAMMAR_SCHOOL_OF_ENGLISH_FILE_NAME));
        boolean branson = downloadConfigurationFile(sUrl[2],
                context.getString(R.string.BRANSON_SCHOOL_OF_BUSINESS_AND_TECHNOLOGY_FILE_NAME));
        boolean diana = downloadConfigurationFile(sUrl[3],
                context.getString(R.string.DIANA_SCHOOL_OF_COMMUNITY_SERVICES_FILE_NAME));
        boolean edison = downloadConfigurationFile(sUrl[4],
                context.getString(R.string.EDISON_SCHOOL_OF_TECH_SCIENCES_FILE_NAME));
        boolean sheldon = downloadConfigurationFile(sUrl[5],
                context.getString(R.string.SHELDON_SCHOOL_OF_HOSPITALITY_FILE_NAME));
        boolean reach = downloadConfigurationFile(sUrl[6],
                context.getString(R.string.REACH_COMMUNITY_COLLEGE_FILE_NAME));
        boolean aibtPromotion = downloadConfigurationFile(sUrl[7],
                context.getString(R.string.AIBT_PROMOTION_FILE_NAME));
        boolean reachPromotion = downloadConfigurationFile(sUrl[8],
                context.getString(R.string.REACH_PROMOTION_FILE_NAME));
        return ace && bespoke && branson && diana && edison && sheldon && reach;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean isConfigurationDownloadSuccessfully) {
        super.onPostExecute(isConfigurationDownloadSuccessfully);
        if (isConfigurationDownloadSuccessfully) {
            File ACE = new File(context.getFilesDir() + "/" + context.getString(R.string.ACE_AVIATION_AEROSPACE_ACADEMY_FILE_NAME));
            File BESPOKE = new File(context.getFilesDir() + "/" + context.getString(R.string.BESPOKE_GRAMMAR_SCHOOL_OF_ENGLISH_FILE_NAME));
            File BRANSON = new File(context.getFilesDir() + "/" + context.getString(R.string.BRANSON_SCHOOL_OF_BUSINESS_AND_TECHNOLOGY_FILE_NAME));
            File DIANA = new File(context.getFilesDir() + "/" + context.getString(R.string.DIANA_SCHOOL_OF_COMMUNITY_SERVICES_FILE_NAME));
            File EDISON = new File(context.getFilesDir() + "/" + context.getString(R.string.EDISON_SCHOOL_OF_TECH_SCIENCES_FILE_NAME));
            File SHELDON = new File(context.getFilesDir() + "/" + context.getString(R.string.SHELDON_SCHOOL_OF_HOSPITALITY_FILE_NAME));
            File REACH = new File(context.getFilesDir() + "/" + context.getString(R.string.REACH_COMMUNITY_COLLEGE_FILE_NAME));
            if (!ACE.exists() || !BESPOKE.exists() || !BRANSON.exists() || !DIANA.exists() || !EDISON.exists()
                    || !SHELDON.exists() || !REACH.exists()) {
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

