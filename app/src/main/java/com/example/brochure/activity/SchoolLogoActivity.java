package com.example.brochure.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.brochure.R;
import com.example.brochure.adapter.SchoolLogoAdapter;
import com.example.brochure.model.Group;
import com.example.brochure.util.PDFFileDownloader;
import com.example.brochure.util.Utils;

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

        GridView gridView = findViewById(R.id.school_logo_grid_view);
        SchoolLogoAdapter booksAdapter = new SchoolLogoAdapter(this, group);
        gridView.setAdapter(booksAdapter);
    }

    public void downloadPDF(View view) {
        if (Utils.checkInternetConnection(this)) {
            new PDFFileDownloader(this).execute(group.getPromotion().getLink(), group.getPromotion().getName());
        } else {
            File pdfFile = new File(getFilesDir() + "/" + getString(R.string.PROMOTION_DIRECTORY) + "/" + group.getPromotion().getName());
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