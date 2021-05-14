package com.example.brochure.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.brochure.R;
import com.example.brochure.activity.CourseDetailActivity;
import com.example.brochure.model.Course;
import com.example.brochure.model.Department;

public class SchoolCourseItemAdapter extends BaseAdapter {

    private final Context context;
    private final Department department;

    public SchoolCourseItemAdapter(Context context, Department department) {
        this.context = context;
        this.department = department;
    }

    @Override
    public int getCount() {
        return department.getCourses().size();
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
            convertView = layoutInflater.inflate(R.layout.layout_school_course_list_item, null);
        }
        Course course = department.getCourses().get(position);
        TextView courseNameTextView = convertView.findViewById(R.id.school_course_name);
        courseNameTextView.setText(course.getName());
        courseNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra(context.getString(R.string.COURSE_DETAIL), course);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
