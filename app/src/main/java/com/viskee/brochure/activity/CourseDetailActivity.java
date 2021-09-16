package com.viskee.brochure.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.model.Course;

import org.apache.commons.lang3.StringUtils;

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
        TextView courseTuition = findViewById(R.id.course_tuition);
        courseName.setText(courseDetail.getName());
        if (StringUtils.isNotEmpty(courseDetail.getVetCode())) {
            courseVetCode.setText("VET National Code: " + courseDetail.getVetCode());
        } else {
            courseVetCode.setText("");
        }
        if (StringUtils.isNotEmpty(courseDetail.getCricosCode())) {
            courseCricosCode.setText("CRICOS Course Code: " + courseDetail.getCricosCode());
        } else {
            courseVetCode.setText("");
        }
        if (StringUtils.isNotEmpty(courseDetail.getDurationString())) {
            courseDuration.setText(courseDetail.getDurationString() + " Weeks");
        } else {
            courseVetCode.setText("");
        }
        if (StringUtils.isNotEmpty(courseDetail.getDurationDetail())) {
            courseDurationDetail.setText(courseDetail.getDurationDetail());
        } else {
            courseDurationDetail.setText("");
        }
        if (!courseDetail.getLocationList().isEmpty()) {
            courseLocation.setText(StringUtils.join(courseDetail.getLocationList(), " | "));
        } else {
            courseLocation.setText("");
        }
        if (StringUtils.isNotEmpty(courseDetail.getTuition())) {
            courseTuition.setText("Tuition Fee: $" + courseDetail.getTuition());
        } else {
            courseTuition.setText("");
        }
        if (courseDetail.isOnPromotion()) {
            if (courseDetail.getPromotionDuration() != 0) {
                TextView courseDurationPromotion = findViewById(R.id.course_duration_promotion);
                courseDurationPromotion.setVisibility(View.VISIBLE);
                courseDurationPromotion.setText(courseDetail.getPromotionDuration() + " Weeks");
                courseDurationPromotion.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.promotion, 0, 0, 0);;
                courseDuration.setTextSize(12);
                courseDuration.setPaintFlags(courseDuration.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (StringUtils.isNotEmpty(courseDetail.getPromotionDurationDetail())) {
                TextView courseDurationDetailPromotion = findViewById(R.id.course_duration_detail_promotion);
                courseDurationDetailPromotion.setVisibility(View.VISIBLE);
                courseDurationDetailPromotion.setText(courseDetail.getPromotionDurationDetail());
                courseDurationDetailPromotion.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.promotion, 0, 0, 0);;
                courseDurationDetail.setTextSize(12);
                courseDurationDetail.setPaintFlags(courseDuration.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (StringUtils.isNotEmpty(courseDetail.getPromotionLocation())) {
                TextView courseLocationPromotion = findViewById(R.id.course_location_promotion);
                courseLocationPromotion.setVisibility(View.VISIBLE);
                courseLocationPromotion.setText(StringUtils.join(courseDetail.getPromotionLocationList(), " | "));
                courseLocationPromotion.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.promotion, 0, 0, 0);;
                courseLocation.setTextSize(12);
                courseLocation.setPaintFlags(courseDuration.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (StringUtils.isNotEmpty(courseDetail.getPromotionTuition())) {
                TextView courseTuitionPromotion = findViewById(R.id.course_tuition_promotion);
                courseTuitionPromotion.setVisibility(View.VISIBLE);
                courseTuitionPromotion.setText("Tuition Fee: $" + courseDetail.getPromotionTuition());
                courseTuitionPromotion.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.promotion, 0, 0, 0);;
                courseTuition.setTextSize(12);
                courseTuition.setPaintFlags(courseDuration.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
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