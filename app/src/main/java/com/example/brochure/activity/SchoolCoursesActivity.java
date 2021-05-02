package com.example.brochure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.brochure.R;
import com.example.brochure.adapter.SchoolCoursesAdapter;
import com.example.brochure.model.School;

public class SchoolCoursesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_courses);

        School school = (School) getIntent().getSerializableExtra("School");
        SchoolCoursesAdapter schoolCoursesAdapter = new SchoolCoursesAdapter(this, school);

        GridView schoolCourseGridView = (GridView) findViewById(R.id.school_course_grid_view);
        schoolCourseGridView.setAdapter(schoolCoursesAdapter);
    }
}