package com.viskee.brochure.util;

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

public class PDFFileDownloader extends AsyncTask<String, Void, Void> {
    private static final int MEGABYTE = 1024 * 1024;

    private final Context context;
    private String fileName;

    public PDFFileDownloader(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String fileUrl = strings[0];
        String fileName = strings[1];
        this.fileName = fileName;
        File folder = new File(context.getFilesDir(), context.getString(R.string.PROMOTION_DIRECTORY));
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
        Utils.openPdfFile(context, fileName);
        super.onPostExecute(aVoid);
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

            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(directory);

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
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
}
