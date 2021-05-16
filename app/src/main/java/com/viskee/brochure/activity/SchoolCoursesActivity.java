package com.viskee.brochure.activity;

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
import com.viskee.brochure.model.Promotion;
import com.viskee.brochure.model.School;
import com.viskee.brochure.util.PDFFileDownloader;
import com.viskee.brochure.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

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
        Promotion promotion = (Promotion) getIntent().getSerializableExtra(getString(R.string.PROMOTION));
        Button downloadPromotion = findViewById(R.id.download_promotion);
        if (promotion != null && StringUtils.isNotBlank(promotion.getName()) && StringUtils.isNotBlank(promotion.getLink())) {
            downloadPromotion.setVisibility(View.VISIBLE);
            downloadPromotion.setText(promotion.getName());
            downloadPromotion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadPromotion(promotion);
                }
            });
        }

        ImageView schoolLogo = findViewById(R.id.school_course_logo);
        schoolLogo.setImageDrawable(Utils.getSchoolLogoDrawable(this, school.getName(), Configuration.ORIENTATION_LANDSCAPE));

        GridView schoolCourseGridView = (GridView) findViewById(R.id.school_course_grid_view);
        schoolCourseGridView.setAdapter(schoolCoursesAdapter);
    }

    public void downloadPromotion(Promotion promotion) {
        if (Utils.checkInternetConnection(this)) {
            new PDFFileDownloader(this).execute(promotion.getLink(), promotion.getName());
        } else {
            File pdfFile =
                    new File(getFilesDir() + "/" + getString(R.string.PROMOTION_DIRECTORY) + "/" + promotion.getName());
            if (!pdfFile.exists()) {
                new AlertDialog.Builder(this)
                        .setTitle("No promotion file found")
                        .setMessage("Could you please connect to the Internet and re-download the promotion file ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Utils.openPdfFile(this, promotion.getName());
            }
        }
    }
}