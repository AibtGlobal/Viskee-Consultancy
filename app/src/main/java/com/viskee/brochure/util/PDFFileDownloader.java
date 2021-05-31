package com.viskee.brochure.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.viskee.brochure.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class PDFFileDownloader extends AsyncTask<String, Integer, Void> {
    private static final int MEGABYTE = 1024 * 1024;

    private final Context context;
    private String fileName;
    private ProgressDialog progressDialog;

    public PDFFileDownloader(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setTitle("Download Brochure");
        progressDialog.setMessage("Downloading, please wait!");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String fileUrl = strings[0];
        String fileName = strings[1];
        this.fileName = fileName;
        File folder = new File(context.getFilesDir(), context.getString(R.string.BROCHURE_DIRECTORY));
        folder.mkdir();

        File pdfFile = new File(folder, fileName);

        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            Log.e(PDFFileDownloader.class.getSimpleName(), e.getMessage());
        }
        downloadFile(fileUrl, pdfFile);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Utils.openPdfFile(context, fileName);
        progressDialog.dismiss();
    }

    public void downloadFile(String fileUrl, File directory) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;
        try {

            URL url = new URL(fileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Requested-With", "Curl");
            String userpass = context.getString(R.string.USER_NAME) + ":" + context.getString(R.string.PASSWORD);
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);


            int fileLength = connection.getContentLength();
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(directory);

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            int total = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                total += bufferLength;
                publishProgress((int) (total*100/fileLength));
                outputStream.write(buffer, 0, bufferLength);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e(PDFFileDownloader.class.getSimpleName(), e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e(PDFFileDownloader.class.getSimpleName(), e.getMessage());
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }
}
