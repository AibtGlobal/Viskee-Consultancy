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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

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
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        } else {
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

    private boolean downloadConfigurationFile(String sUrl, String fileName) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Requested-With", "Curl");
            String userpass = context.getString(R.string.USER_NAME) + ":" + context.getString(R.string.PASSWORD);
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();

            output = new FileOutputStream(context.getFilesDir() + "/" + fileName);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return false;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    progressBar.setProgress((int) (total * 100 / fileLength), true);
                publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
            output.flush();
        } catch (Exception e) {
            Log.e(ConfigFileDownloader.class.getSimpleName(), e.getMessage());
            return false;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException exception) {
                Log.e(ConfigFileDownloader.class.getSimpleName(), exception.getMessage());
            }

            if (connection != null)
                connection.disconnect();
        }
        return true;
    }


}

