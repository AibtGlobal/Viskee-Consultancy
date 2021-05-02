package com.example.brochure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.example.brochure.R;
import com.example.brochure.adapter.SearchResultAdapter;
import com.example.brochure.model.SearchResult;

import static com.example.brochure.model.GroupEnum.AIBT;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_search_result);

        SearchResult searchResult = (SearchResult) getIntent().getSerializableExtra("SearchResult");

        GridView gridView = findViewById(R.id.search_result_grid_view);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(this, searchResult.getSearchResults().get(AIBT));
        gridView.setAdapter(searchResultAdapter);
    }
}