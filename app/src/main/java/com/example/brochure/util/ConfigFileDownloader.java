package com.example.brochure.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.example.brochure.R;
import com.example.brochure.adapter.MainViewAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class ConfigFileDownloader extends AsyncTask<String, Integer, String> {

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = mContext.getResources().openRawResource(R.raw.json_download_github);
            Certificate ca = cf.generateCertificate(caInput);
            caInput.close();

            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getWrappedTrustManagers(tmf.getTrustManagers()), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            return HttpsURLConnection.getDefaultSSLSocketFactory();
        }
    }


    private final Activity mContext;
    private final ProgressBar progressBar;

    public ConfigFileDownloader(Activity context, ProgressBar progressBar) {
        this.mContext = context;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... sUrl) {
        boolean aibtResult = downloadConfigurationFile(sUrl[0], "AIBT.json");
        boolean reachResult = downloadConfigurationFile(sUrl[1], "REACH.json");
        if (!aibtResult || !reachResult) {
            File AIBT = new File(mContext.getFilesDir() + "/AIBT.json");
            File REACH = new File(mContext.getFilesDir() + "/REACH.json");
            if (!AIBT.exists() || !REACH.exists()) {
                new AlertDialog.Builder(mContext)
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

        int orientation = mContext.getResources().getConfiguration().orientation;
        final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View layoutView;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutView = layoutInflater.inflate(R.layout.activity_main_portrait, null);
        } else {
            layoutView = layoutInflater.inflate(R.layout.activity_main_landscape, null);
        }
        mContext.setContentView(layoutView);
        new MainViewAdapter(mContext, layoutView).prepareData();
        super.onPostExecute(s);

    }

    private boolean downloadConfigurationFile(String sUrl, String fileName) {
        InputStream input = null;
        OutputStream output = null;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(sUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(getSSLSocketFactory());
            connection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("github.com", session);
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

            output = new FileOutputStream(mContext.getFilesDir() + "/" + fileName);

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

