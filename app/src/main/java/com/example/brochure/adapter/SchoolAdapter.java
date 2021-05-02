package com.example.brochure.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.brochure.R;
import com.example.brochure.activity.SchoolCoursesActivity;
import com.example.brochure.model.AIBTSchoolNameEnum;
import com.example.brochure.model.Group;
import com.example.brochure.model.School;

public class SchoolAdapter extends BaseAdapter {

    private final Context context;
    private final Group group;

    public SchoolAdapter(Context context, Group group) {
        this.context = context;
        this.group = group;
    }

    @Override
    public int getCount() {
        return group.getSchools().size();
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

        School school = group.getSchools().get(position);
        AIBTSchoolNameEnum aibtSchoolNameEnum = AIBTSchoolNameEnum.fromValue(school.getName().toUpperCase());
        ImageView imageView = convertView.findViewById(R.id.school_logo);
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
        imageView.setImageDrawable(drawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SchoolCoursesActivity.class);
                intent.putExtra("School", school);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
