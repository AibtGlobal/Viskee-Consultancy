package com.viskee.brochure.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.model.SubFolderEnum;

public class RegionSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_selection);
    }

    public void goToSismic(View view) {
        Intent intent = new Intent(view.getContext(), ConfigurationDownloadActivity.class);
        intent.putExtra(getString(R.string.SUB_FOLDER), SubFolderEnum.SISMIC.name());
        startActivity(intent);
    }

    public void goToSeapae(View view) {
        Intent intent = new Intent(view.getContext(), ConfigurationDownloadActivity.class);
        intent.putExtra(getString(R.string.SUB_FOLDER), SubFolderEnum.SEAPAE.name());
        startActivity(intent);
    }

    public void backToPrevious(View view) {
        finish();
    }
}