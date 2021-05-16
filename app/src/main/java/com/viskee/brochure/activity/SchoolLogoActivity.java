package com.viskee.brochure.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.adapter.SchoolLogoAdapter;
import com.viskee.brochure.model.Group;
import com.viskee.brochure.model.Promotion;
import com.viskee.brochure.util.PDFFileDownloader;
import com.viskee.brochure.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class SchoolLogoActivity extends AppCompatActivity {

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_school_logo_portrait);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_school_logo_portrait);
        } else {
            setContentView(R.layout.activity_school_logo_landscape);
        }

        group = (Group) getIntent().getSerializableExtra(getString(R.string.GROUP));

        Promotion promotion = group.getPromotion();
        Button downloadPromotion = findViewById(R.id.download_promotion);
        if (promotion != null && StringUtils.isNotBlank(promotion.getName()) && StringUtils.isNotBlank(promotion.getLink())) {
            downloadPromotion.setVisibility(View.VISIBLE);
            downloadPromotion.setText(promotion.getName());
        }

        GridView gridView = findViewById(R.id.school_logo_grid_view);
        SchoolLogoAdapter booksAdapter = new SchoolLogoAdapter(this, group);
        gridView.setAdapter(booksAdapter);
    }

    public void downloadPromotion(View view) {
        if (Utils.checkInternetConnection(this)) {
            new PDFFileDownloader(this).execute(group.getPromotion().getLink(), group.getPromotion().getName());
        } else {
            File pdfFile =
                    new File(getFilesDir() + "/" + getString(R.string.PROMOTION_DIRECTORY) + "/" + group.getPromotion().getName());
            if (!pdfFile.exists()) {
                new AlertDialog.Builder(this)
                        .setTitle("No promotion file found")
                        .setMessage("Could you please connect to the Internet and re-download the promotion file ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Utils.openPdfFile(this, group.getPromotion().getName());
            }
        }
    }
}