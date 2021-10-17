package com.viskee.brochure.util;

import static com.viskee.brochure.util.Constants.ACE_FILE_NAME;
import static com.viskee.brochure.util.Constants.COURSE_BASE_URL;
import static com.viskee.brochure.util.Constants.ONSHORE;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.viskee.brochure.R;
import com.viskee.brochure.activity.MainActivity;
import com.viskee.brochure.model.SubFolderEnum;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ConfigFileDownloader extends AsyncTask<String, Integer, Boolean> {

    private static final long SPLASH_TIME = 1500L;

    private final Activity context;
    private final ProgressBar progressBar;
    private final String subFolder;

    public ConfigFileDownloader(Activity context, ProgressBar progressBar, String subFolder) {
        this.context = context;
        this.progressBar = progressBar;
        this.subFolder = subFolder;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(String... sUrl) {
        String subUrl = buildSubUrl(subFolder);
        boolean basicConfigurationsResult = downloadBasicConfigurationFiles(subUrl);
        downloadPromotionsConfigurationFiles(subUrl);
        return basicConfigurationsResult;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean isConfigurationDownloadSuccessfully) {
        super.onPostExecute(isConfigurationDownloadSuccessfully);
        if (isConfigurationDownloadSuccessfully) {
            File ACE = new File(context.getFilesDir() + "/" + subFolder + "_" + context.getString(R.string.ACE_FILE_NAME));
            File BESPOKE = new File(context.getFilesDir() + "/" + subFolder + "_" + context.getString(R.string.BESPOKE_FILE_NAME));
            File BRANSON = new File(context.getFilesDir() + "/" + subFolder + "_" + context.getString(R.string.BRANSON_FILE_NAME));
            File DIANA = new File(context.getFilesDir() + "/" + subFolder + "_" + context.getString(R.string.DIANA_FILE_NAME));
            File EDISON = new File(context.getFilesDir() + "/" + subFolder + "_" + context.getString(R.string.EDISON_FILE_NAME));
            File SHELDON = new File(context.getFilesDir() + "/" + subFolder + "_" + context.getString(R.string.SHELDON_FILE_NAME));
            File REACH = new File(context.getFilesDir() + "/" + subFolder + "_" + context.getString(R.string.REACH_FILE_NAME));
            if (!ACE.exists() || !BESPOKE.exists() || !BRANSON.exists() || !DIANA.exists() || !EDISON.exists()
                    || !SHELDON.exists() || !REACH.exists()) {
                displayAlert(context);
            } else {
                Handler handler = new Handler();
                handler.postDelayed(() -> goToMainActivity(context, subFolder), SPLASH_TIME);
            }
        } else {
            displayAlert(context);
        }
    }

    private String buildSubUrl(String subFolder) {
        SubFolderEnum subFolderEnum = SubFolderEnum.valueOf(subFolder);
        switch (subFolderEnum) {
            case COE:
                return Constants.ONSHORE + Constants.COE;
            case NON_COE:
                return Constants.ONSHORE + Constants.NON_COE;
            case SEAPAE:
                return Constants.OFFSHORE + Constants.SEAPAE;
            case SISMIC:
                return Constants.OFFSHORE + Constants.SISMIC;
            default:
                return "";
        }
    }

    private boolean downloadBasicConfigurationFiles(String subUrl) {

        String aibtSubUrl = subUrl + Constants.AIBT;
        // AIBT School Configurations
        boolean ace = downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.ACE_FILE_NAME,
                Constants.ACE_FILE_NAME);
        boolean bespoke = downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.BESPOKE_FILE_NAME,
                Constants.BESPOKE_FILE_NAME);
        boolean branson = downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.BRANSON_FILE_NAME,
                Constants.BRANSON_FILE_NAME);
        boolean diana = downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.DIANA_FILE_NAME,
                Constants.DIANA_FILE_NAME);
        boolean edison = downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.EDISON_FILE_NAME,
                Constants.EDISON_FILE_NAME);
        boolean sheldon = downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.SHELDON_FILE_NAME,
                Constants.SHELDON_FILE_NAME);
        // REACH School Configurations
        String reachSubUrl = subUrl + Constants.REACH;
        boolean reach = downloadConfigurationFile(COURSE_BASE_URL + reachSubUrl + Constants.REACH_FILE_NAME,
                Constants.REACH_FILE_NAME);
        return ace && bespoke && branson && diana && edison && sheldon && reach;
    }

    private void downloadPromotionsConfigurationFiles(String subUrl) {
        String aibtSubUrl = subUrl + Constants.AIBT + Constants.PROMOTIONS;
        // AIBT School Configurations
        downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.ACE_FILE_NAME,
                Constants.PROMOTION + "_" + Constants.ACE_FILE_NAME);
        downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.BESPOKE_FILE_NAME,
                Constants.PROMOTION + "_" + Constants.BESPOKE_FILE_NAME);
        downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.BRANSON_FILE_NAME,
                Constants.PROMOTION + "_" + Constants.BRANSON_FILE_NAME);
        downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.DIANA_FILE_NAME,
                Constants.PROMOTION + "_" + Constants.DIANA_FILE_NAME);
        downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.EDISON_FILE_NAME,
                Constants.PROMOTION + "_" + Constants.EDISON_FILE_NAME);
        downloadConfigurationFile(COURSE_BASE_URL + aibtSubUrl + Constants.SHELDON_FILE_NAME,
                Constants.PROMOTION + "_" + Constants.SHELDON_FILE_NAME);
        // REACH School Configurations
        String reachSubUrl = subUrl + Constants.REACH + Constants.PROMOTIONS;
        downloadConfigurationFile(COURSE_BASE_URL + reachSubUrl + Constants.REACH_FILE_NAME,
                Constants.PROMOTION + "_" + Constants.REACH_FILE_NAME);
    }

    private void downloadBrochureConfigurationFiles() {
        // AIBT Brochure Configuration
        downloadConfigurationFile(context.getString(R.string.AIBT_BROCHURE_FILE_LINK),
                context.getString(R.string.AIBT_BROCHURE_FILE_NAME));

        // REACH Brochure Configuration
        downloadConfigurationFile(context.getString(R.string.REACH_BROCHURE_FILE_LINK),
                context.getString(R.string.REACH_BROCHURE_FILE_NAME));
    }

    private boolean downloadConfigurationFile(String sUrl, String fileName) {
        InputStream inputStream = null;
        try {
            URL url = new URL(sUrl);
            inputStream = url.openStream();
            Files.copy(inputStream, Paths.get(context.getFilesDir() + "/" + subFolder + "_" + fileName), StandardCopyOption.REPLACE_EXISTING);
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

    private void goToMainActivity(Activity context, String subFolder) {
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(context.getString(R.string.SUB_FOLDER), subFolder);
        context.startActivity(intent);
        context.finish();
    }
}

