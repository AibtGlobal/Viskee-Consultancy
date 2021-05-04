package com.example.brochure.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.brochure.R;
import com.example.brochure.model.AIBTSchoolNameEnum;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int totalHeight2 = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            TextView item = listView.findViewById(R.id.school_course_name);
//            item.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight2 += item.getMeasuredHeight();
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static Drawable getSchoolLogoDrawable(Context context, String schoolName) {
        AIBTSchoolNameEnum aibtSchoolNameEnum = AIBTSchoolNameEnum.fromValue(schoolName.toUpperCase());
        Drawable drawable = null;
        switch (aibtSchoolNameEnum) {
            case ACE:
                drawable = ContextCompat.getDrawable(context, R.drawable.ace_aviation);
                break;
            case BESPOKE:
                drawable = ContextCompat.getDrawable(context, R.drawable.bespoke);
                break;
            case BRANSON:
                drawable = ContextCompat.getDrawable(context, R.drawable.branson);
                break;
            case DIANA:
                drawable = ContextCompat.getDrawable(context, R.drawable.diana);
                break;
            case EDISON:
                drawable = ContextCompat.getDrawable(context, R.drawable.edison);
                break;
            case SHELDON:
                drawable = ContextCompat.getDrawable(context, R.drawable.sheldon);
                break;
        }
        return drawable;
    }
}
