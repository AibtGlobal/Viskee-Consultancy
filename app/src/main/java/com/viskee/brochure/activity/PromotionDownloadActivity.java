package com.viskee.brochure.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.adapter.PromotionDownloadAdapter;
import com.viskee.brochure.model.Promotions;

public class PromotionDownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_promotion_download_portrait);
        } else {
            setContentView(R.layout.activity_promotion_download_landscape);
        }

        String groupName = (String) getIntent().getSerializableExtra(getString(R.string.GROUP_NAME));
        Promotions promotions = (Promotions) getIntent().getSerializableExtra(getString(R.string.PROMOTIONS));

        TextView promotionTitle = findViewById(R.id.promotion_title);
        promotionTitle.setText("Latest Promotions For " + groupName);

        PromotionDownloadAdapter promotionDownloadAdapter = new PromotionDownloadAdapter(this, promotions.getPromotions());
        GridView promotionGridView = findViewById(R.id.promotion_grid_view);
        promotionGridView.setAdapter(promotionDownloadAdapter);
    }

    public void backToPrevious(View view) {
        finish();
    }
}