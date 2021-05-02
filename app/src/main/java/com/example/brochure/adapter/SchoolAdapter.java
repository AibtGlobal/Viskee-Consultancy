package com.example.brochure.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.brochure.R;

public class SchoolAdapter extends BaseAdapter {

    private final Context context;

    public SchoolAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.layout_school_logo, null);
        }

        ImageView imageView = convertView.findViewById(R.id.school_logo);
        Drawable drawable = null;
        switch (position) {
            case 0:
                drawable = ContextCompat.getDrawable(context, R.drawable.ace_aviation);
                break;
            case 1:
                drawable = ContextCompat.getDrawable(context, R.drawable.bespoke);
                break;
            case 2:
                drawable = ContextCompat.getDrawable(context, R.drawable.branson);
                break;
            case 3:
                drawable = ContextCompat.getDrawable(context, R.drawable.diana);
                break;
            case 4:
                drawable = ContextCompat.getDrawable(context, R.drawable.edison);
                break;
            case 5:
                drawable = ContextCompat.getDrawable(context, R.drawable.sheldon);
                break;
            default:
                drawable = ContextCompat.getDrawable(context, R.drawable.ace_aviation);
                break;
        }
        imageView.setImageDrawable(drawable);
        return convertView;
    }
}
