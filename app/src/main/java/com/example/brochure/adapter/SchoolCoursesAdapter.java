package com.example.brochure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brochure.R;
import com.example.brochure.model.Course;
import com.example.brochure.model.Department;
import com.example.brochure.model.School;
import com.example.brochure.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class SchoolCoursesAdapter extends BaseAdapter {

    private final Context context;
    private final List<Department> departments = new ArrayList<>();

    public SchoolCoursesAdapter(Context context, School school) {
        this.context = context;
        buildDepartmentList(school);
    }

    @Override
    public int getCount() {
        return departments.size();
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
            convertView = layoutInflater.inflate(R.layout.layout_school_courses, null);
        }
        Department department = departments.get(position);
        TextView departmentNameTextView = convertView.findViewById(R.id.department_name);
        ListView coursesListView = convertView.findViewById(R.id.course_list);
        departmentNameTextView.setText(department.getName());
        SchoolCourseItemAdapter schoolCourseItemAdapter = new SchoolCourseItemAdapter(context, department);
        coursesListView.setAdapter(schoolCourseItemAdapter);
        Utils.setListViewHeightBasedOnChildren(coursesListView);
        return convertView;
    }

    private void buildDepartmentList(School school) {
        Map<String, List<Course>> departmentMap = school.getCourses().stream().collect(groupingBy(Course::getDepartment));
        for (Map.Entry<String, List<Course>> entry : departmentMap.entrySet()) {
            departments.add(new Department(entry.getKey(), entry.getValue()));
        }

    }
}
