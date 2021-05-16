package com.viskee.brochure.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.viskee.brochure.R;
import com.viskee.brochure.activity.SchoolCoursesActivity;
import com.viskee.brochure.model.Group;
import com.viskee.brochure.model.School;
import com.viskee.brochure.util.Utils;

public class SchoolLogoAdapter extends BaseAdapter {

    private final Context context;
    private final Group group;

    public SchoolLogoAdapter(Context context, Group group) {
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
        ImageView imageView = convertView.findViewById(R.id.school_logo);
        imageView.setImageDrawable(Utils.getSchoolLogoDrawable(context, school.getName(), context.getResources().getConfiguration().orientation));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SchoolCoursesActivity.class);
                intent.putExtra(context.getString(R.string.SCHOOL), school);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
