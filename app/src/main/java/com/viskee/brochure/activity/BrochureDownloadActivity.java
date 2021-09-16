package com.viskee.brochure.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.adapter.BrochureDownloadAdapter;
import com.viskee.brochure.model.Brochures;

public class BrochureDownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_brochure_download_portrait);
        } else {
            setContentView(R.layout.activity_brochure_download_landscape);
        }

        String groupName = (String) getIntent().getSerializableExtra(getString(R.string.GROUP_NAME));
        Brochures brochures = (Brochures) getIntent().getSerializableExtra(getString(R.string.BROCHURES));

        TextView brochureTitle = findViewById(R.id.brochure_title);
        brochureTitle.setText("Latest Brochures For " + groupName);

        BrochureDownloadAdapter brochureDownloadAdapter = new BrochureDownloadAdapter(this, brochures.getBrochures());
        GridView brochureGridView = findViewById(R.id.brochure_grid_view);
        brochureGridView.setAdapter(brochureDownloadAdapter);
    }

    public void backToPrevious(View view) {
        finish();
    }
}