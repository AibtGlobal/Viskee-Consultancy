package com.viskee.brochure.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.model.SubFolderEnum;

public class CoeSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coe_selection);
    }

    public void goToCoe(View view) {
        Intent intent = new Intent(view.getContext(), ConfigurationDownloadActivity.class);
        intent.putExtra(getString(R.string.SUB_FOLDER), SubFolderEnum.COE.name());
        startActivity(intent);
    }

    public void goToNonCoe(View view) {
        Intent intent = new Intent(view.getContext(), ConfigurationDownloadActivity.class);
        intent.putExtra(getString(R.string.SUB_FOLDER), SubFolderEnum.NON_COE.name());
        startActivity(intent);
    }

    public void backToPrevious(View view) {
        finish();
    }
}