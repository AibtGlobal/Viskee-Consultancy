package com.viskee.brochure.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.viskee.brochure.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
            e.printStackTrace();
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
        HttpsURLConnection connection = null;
        try {

            URL url = new URL(fileUrl);
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
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
