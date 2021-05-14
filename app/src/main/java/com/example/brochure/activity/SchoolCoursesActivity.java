package com.example.brochure.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brochure.R;
import com.example.brochure.adapter.SchoolCoursesAdapter;
import com.example.brochure.model.School;
import com.example.brochure.util.Utils;

public class SchoolCoursesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_school_logo_portrait);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_school_courses_portrait);
        } else {
            setContentView(R.layout.activity_school_courses_landscape);
        }

        School school = (School) getIntent().getSerializableExtra(getString(R.string.SCHOOL));
        SchoolCoursesAdapter schoolCoursesAdapter = new SchoolCoursesAdapter(this, school);

        ImageView schoolLogo = findViewById(R.id.school_course_logo);
        schoolLogo.setImageDrawable(Utils.getSchoolLogoDrawable(this, school.getName(), Configuration.ORIENTATION_LANDSCAPE));

        GridView schoolCourseGridView = (GridView) findViewById(R.id.school_course_grid_view);
        schoolCourseGridView.setAdapter(schoolCoursesAdapter);
    }
}