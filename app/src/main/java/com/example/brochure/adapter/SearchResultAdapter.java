package com.example.brochure.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brochure.R;
import com.example.brochure.activity.CourseDetailActivity;
import com.example.brochure.model.Course;
import com.example.brochure.model.Department;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchResultAdapter extends BaseAdapter {

    private final Context context;
    private List<Course> courses = new ArrayList<>();

    private static final Comparator<Course> departmentNameComparator;
    static {
        departmentNameComparator = new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                return c1.getName().length() - c2.getName().length();
            }
        };
    }

    public SearchResultAdapter(Context context, List<Course> courses) {
        this.context = context;
        if (courses != null) {
            this.courses = courses;
            this.courses.sort(departmentNameComparator);
        }
    }

    @Override
    public int getCount() {
        return courses.size();
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
            convertView = layoutInflater.inflate(R.layout.layout_search_result, null);
        }

        Course course = courses.get(position);
        TextView courseNameTextView = convertView.findViewById(R.id.course_name);
        TextView vetCodeTextView = convertView.findViewById(R.id.vet_code);
        LinearLayout searchResultCard = convertView.findViewById(R.id.search_result_card);

        courseNameTextView.setText(course.getName());
        vetCodeTextView.setText("VET National Code: " + course.getVetCode());
        searchResultCard.setOnClickListener(new View.OnClickListener() {
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
