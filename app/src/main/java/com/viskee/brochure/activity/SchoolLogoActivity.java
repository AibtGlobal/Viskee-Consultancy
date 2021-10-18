package com.viskee.brochure.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.adapter.SchoolLogoAdapter;
import com.viskee.brochure.model.Brochure;
import com.viskee.brochure.model.Group;
import com.viskee.brochure.model.Brochures;

import java.util.List;

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

    public void showBrochures(View view) {
        List<Brochure> brochures = group.getBrochures();
        if (brochures == null || brochures.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No brochure found")
                    .setMessage("Currently there is no brochure for " + group.getName())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            Intent intent = new Intent(SchoolLogoActivity.this, BrochureDownloadActivity.class);
            intent.putExtra(getString(R.string.GROUP_NAME), group.getName());
            intent.putExtra(getString(R.string.BROCHURES), new Brochures(brochures));
            startActivity(intent);
        }
    }

    public void backToPrevious(View view) {
        finish();
    }
}