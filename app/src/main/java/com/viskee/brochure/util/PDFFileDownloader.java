package com.viskee.brochure.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.viskee.brochure.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PDFFileDownloader extends AsyncTask<String, Integer, Void> {

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
        try {
            URL url = new URL(fileUrl);
            inputStream = url.openStream();
            Files.copy(inputStream, Paths.get(directory.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            Log.e(PDFFileDownloader.class.getSimpleName(), e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e(PDFFileDownloader.class.getSimpleName(), e.getMessage());
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }
}
