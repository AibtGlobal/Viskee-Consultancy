package com.viskee.brochure.util;

import android.content.Context;
import android.util.Log;

import com.viskee.brochure.R;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class HttpsConnectionUtils {

    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        try {
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From github raw page: https://raw.githubusercontent.com/AibtGlobal/Viskee-Consultancy-Configuration/master/configuration/AIBT.json
            // To note that this cert is different from the one of default github page
            InputStream caInput = context.getResources().openRawResource(R.raw.certificate);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getWrappedTrustManagers(tmf.getTrustManagers()), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            Log.e(HttpsConnectionUtils.class.getSimpleName(), e.getMessage());
            return HttpsURLConnection.getDefaultSSLSocketFactory();
        }
    }

    private static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException e) {
                            Log.e(HttpsConnectionUtils.class.getSimpleName(), e.getMessage());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException e) {
                            Log.e(HttpsConnectionUtils.class.getSimpleName(), e.getMessage());
                        }
                    }
                }
        };
    }
}
