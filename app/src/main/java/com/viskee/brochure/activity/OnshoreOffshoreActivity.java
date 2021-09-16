package com.viskee.brochure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.viskee.brochure.R;

public class OnshoreOffshoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onshore_offshore);
    }

    public void goToOnshore(View view) {
        Intent intent = new Intent(view.getContext(), CoeSelectionActivity.class);
        startActivity(intent);
    }

    public void goToOffshore(View view) {
        Intent intent = new Intent(view.getContext(), RegionSelectionActivity.class);
        startActivity(intent);
    }

    public void backToPrevious(View view) {
        finish();
    }
}