package com.example.brochure.util;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.example.brochure.R;
import com.example.brochure.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class ConfigFileDownloader extends AsyncTask<String, Integer, String> {

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
    protected String doInBackground(String... sUrl) {
        boolean aibtResult = downloadConfigurationFile(sUrl[0], context.getString(R.string.AIBT_CONFIGURATION_FILE_NAME));
        boolean reachResult = downloadConfigurationFile(sUrl[1], context.getString(R.string.REACH_CONFIGURATION_FILE_NAME));
        if (!aibtResult || !reachResult) {
            File AIBT = new File(context.getFilesDir() + "/" + context.getString(R.string.AIBT_CONFIGURATION_FILE_NAME));
            File REACH = new File(context.getFilesDir() + "/" + context.getString(R.string.REACH_CONFIGURATION_FILE_NAME));
            if (!AIBT.exists() || !REACH.exists()) {
                new AlertDialog.Builder(context)
                        .setTitle("No configuration found")
                        .setMessage("Could you please connect to the Internet and relaunch the app ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        progressBar.setVisibility(View.INVISIBLE);

//        int orientation = context.getResources().getConfiguration().orientation;
//        final LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View layoutView;
//        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            layoutView = layoutInflater.inflate(R.layout.activity_main_portrait, null);
//        } else {
//            layoutView = layoutInflater.inflate(R.layout.activity_main_landscape, null);
//        }
//        context.setContentView(layoutView);
//        new MainViewAdapter(context, layoutView).prepareData();

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        super.onPostExecute(s);

    }

    private boolean downloadConfigurationFile(String sUrl, String fileName) {
        InputStream input = null;
        OutputStream output = null;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(sUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(HttpsConnectionUtils.getSSLSocketFactory(context));
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(context.getString(R.string.HOST_NAME), session);
                }
            });
            connection.connect();

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
            return false;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return true;
    }


}

