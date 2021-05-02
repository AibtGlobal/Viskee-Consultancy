package com.example.brochure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.example.brochure.R;
import com.example.brochure.adapter.SchoolAdapter;
import com.example.brochure.model.Group;

public class SchoolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_school);

        Group group = (Group) getIntent().getSerializableExtra("Group");

        GridView gridView = findViewById(R.id.school_logo_grid_view);
        SchoolAdapter booksAdapter = new SchoolAdapter(this, group);
        gridView.setAdapter(booksAdapter);
    }
}