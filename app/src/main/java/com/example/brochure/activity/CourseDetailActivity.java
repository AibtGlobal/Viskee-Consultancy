package com.example.brochure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.brochure.R;
import com.example.brochure.model.Course;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_course_detail_portrait);
        } else {
            setContentView(R.layout.activity_course_detail_landscape);
        }

        Course courseDetail = (Course) getIntent().getSerializableExtra("CourseDetail");
        TextView courseName = findViewById(R.id.course_detail_title);
        TextView courseVetCode = findViewById(R.id.course_detail_vet_code);
        TextView courseDuration = findViewById(R.id.course_duration);
        TextView courseDurationDetail = findViewById(R.id.course_duration_detail);
        TextView courseLocation = findViewById(R.id.course_location);
        TextView offshorePricing = findViewById(R.id.course_pricing_offshore);
        TextView onshorePricing = findViewById(R.id.course_pricing_onshore);
        courseName.setText(courseDetail.getName());
        if (!StringUtils.isBlank(courseDetail.getVetCode())) {
            courseVetCode.setText("VET National Code: " + courseDetail.getVetCode());
        } else {
            courseVetCode.setText("");
        }
        if (courseDetail.getDuration() != 0) {
            courseDuration.setText(courseDetail.getDuration() + " Weeks");
        } else {
            courseDuration.setText("");
        }
        courseDurationDetail.setText(courseDetail.getDurationDetail());
        courseLocation.setText(String.join(" | ", courseDetail.getLocation()));
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        if (courseDetail.getOffshoreTuition() != 0) {
            offshorePricing.setText("Tuition Fee - OffShore Int Student: $"+myFormat.format(courseDetail.getOffshoreTuition()));
        } else {
            offshorePricing.setText("");
        }
        if (courseDetail.getOnshoreTuition() != 0) {
            onshorePricing.setText("Tuition Fee - OnShore Student Visa Holder: $" + myFormat.format(courseDetail.getOnshoreTuition()));
        } else {
            onshorePricing.setText("");
        }

    }
}