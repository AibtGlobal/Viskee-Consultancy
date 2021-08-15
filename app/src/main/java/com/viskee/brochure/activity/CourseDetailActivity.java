package com.viskee.brochure.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.model.Course;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_course_detail_portrait);
        } else {
            setContentView(R.layout.activity_course_detail_landscape);
        }

        Course courseDetail = (Course) getIntent().getSerializableExtra(getString(R.string.COURSE_DETAIL));
        TextView courseName = findViewById(R.id.course_detail_title);
        TextView courseVetCode = findViewById(R.id.course_detail_vet_code);
        TextView courseCricosCode = findViewById(R.id.course_detail_cricos_code);
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
        if (!StringUtils.isBlank(courseDetail.getCricosCode())) {
            courseCricosCode.setText("CRICOS Course Code: " + courseDetail.getCricosCode());
        } else {
            courseVetCode.setText("");
        }
        courseDuration.setText(courseDetail.getDurationString() + " Weeks");
        courseDurationDetail.setText(courseDetail.getDurationDetail());
        courseLocation.setText(StringUtils.join(courseDetail.getLocationList(), " | "));
        if (StringUtils.isNotEmpty(courseDetail.getOffshoreTuition())) {
            offshorePricing.setText("Tuition Fee - OffShore Int Student: $" + courseDetail.getOffshoreTuition());
        } else {
            offshorePricing.setText("");
        }
        if (StringUtils.isNotEmpty(courseDetail.getOnshoreTuition())) {
            onshorePricing.setText("Tuition Fee - OnShore Student Visa Holder: $" + courseDetail.getOnshoreTuition());
        } else {
            onshorePricing.setText("");
        }

    }

    public void viewTerms(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aibtglobal.edu" +
                ".au/courses/terms-for-courses/"));
        startActivity(browserIntent);
    }

    public void backToPrevious(View view) {
        finish();
    }
}