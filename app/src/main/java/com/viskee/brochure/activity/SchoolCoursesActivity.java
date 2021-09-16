package com.viskee.brochure.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.adapter.SchoolCoursesAdapter;
import com.viskee.brochure.model.Brochures;
import com.viskee.brochure.model.School;
import com.viskee.brochure.util.Utils;

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
        Brochures brochures = (Brochures) getIntent().getSerializableExtra(getString(R.string.BROCHURES));
        Button showBrochures = findViewById(R.id.show_brochures);
        if (brochures != null) {
            showBrochures.setVisibility(View.VISIBLE);
            showBrochures.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBrochures(school, brochures);
                }
            });
        }

        ImageView schoolLogo = findViewById(R.id.school_course_logo);
        schoolLogo.setImageDrawable(Utils.getSchoolLogoDrawable(this, school.getName(), Configuration.ORIENTATION_LANDSCAPE));

        GridView schoolCourseGridView = (GridView) findViewById(R.id.school_course_grid_view);
        schoolCourseGridView.setAdapter(schoolCoursesAdapter);
    }

    public void showBrochures(School school, Brochures brochures) {
        if (brochures == null || brochures.getBrochures() == null || brochures.getBrochures().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No brochure found")
                    .setMessage("Currently there is no brochure for " + school.getName())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            Intent intent = new Intent(SchoolCoursesActivity.this, BrochureDownloadActivity.class);
            intent.putExtra(getString(R.string.GROUP_NAME), school.getName());
            intent.putExtra(getString(R.string.BROCHURES), brochures);
            startActivity(intent);
        }
    }

    public void backToPrevious(View view) {
        finish();
    }
}