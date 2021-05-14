package com.example.brochure.util;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.brochure.BuildConfig;
import com.example.brochure.R;
import com.example.brochure.adapter.SchoolCourseItemAdapter;
import com.example.brochure.model.AIBTSchoolNameEnum;
import com.example.brochure.model.Course;
import com.example.brochure.model.Department;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String getJsonFromStorage(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = new FileInputStream(new File(context.getFilesDir() + "/" + fileName));

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static void setListViewHeightBasedOnChildren(Department department, ListView listView) {
        SchoolCourseItemAdapter listAdapter = (SchoolCourseItemAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int orientation = listView.getResources().getConfiguration().orientation;
        for (int i = 0; i < department.getCourses().size(); i++) {

            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            Course course = department.getCourses().get(i);
            String name = course.getName();

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                totalHeight += listItem.getMeasuredHeight() * (name.length() / 40 + 1);
            } else {
                totalHeight += listItem.getMeasuredHeight() * (name.length() / 100 + 1);
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static Drawable getSchoolLogoDrawable(Context context, String schoolName, int orientation) {
        AIBTSchoolNameEnum aibtSchoolNameEnum = AIBTSchoolNameEnum.fromValue(schoolName.toUpperCase());
        Drawable drawable = null;
        switch (aibtSchoolNameEnum) {
            case ACE:
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    drawable = ContextCompat.getDrawable(context, R.drawable.ace_landscape);
                } else {
                    drawable = ContextCompat.getDrawable(context, R.drawable.ace_landscape);
                }
                break;
            case BESPOKE:
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    drawable = ContextCompat.getDrawable(context, R.drawable.bespoke_portrait);
                } else {
                    drawable = ContextCompat.getDrawable(context, R.drawable.bespoke_landscape);
                }
                break;
            case BRANSON:
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    drawable = ContextCompat.getDrawable(context, R.drawable.branson_portrait);
                } else {
                    drawable = ContextCompat.getDrawable(context, R.drawable.branson_landscape);
                }
                break;
            case DIANA:
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    drawable = ContextCompat.getDrawable(context, R.drawable.diana_portrait);
                } else {
                    drawable = ContextCompat.getDrawable(context, R.drawable.diana_landscape);
                }
                break;
            case EDISON:
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    drawable = ContextCompat.getDrawable(context, R.drawable.edison_portrait);
                } else {
                    drawable = ContextCompat.getDrawable(context, R.drawable.edison_landscape);
                }
                break;
            case SHELDON:
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    drawable = ContextCompat.getDrawable(context, R.drawable.sheldon_portrait);
                } else {
                    drawable = ContextCompat.getDrawable(context, R.drawable.sheldon_landscape);
                }
                break;
            case REACH:
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    drawable = ContextCompat.getDrawable(context, R.drawable.reach_portrait);
                } else {
                    drawable = ContextCompat.getDrawable(context, R.drawable.reach_landscape);
                }
                break;
        }
        return drawable;
    }

    @SuppressLint("WrongConstant")
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities == null) {
                return false;
            }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true;
            }
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static void openPdfFile(Context context, String fileName) {
        File pdfFile = new File(context.getFilesDir() + "/" + context.getString(R.string.PROMOTION_DIRECTORY) + "/" + fileName);  // -> filename = maven.pdf
//        Uri path = Uri.fromFile(pdfFile);
        Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try{
            context.startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            e.printStackTrace();
            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
