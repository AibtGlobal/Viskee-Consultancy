package com.example.brochure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.brochure.R;
import com.example.brochure.adapter.SearchResultAdapter;
import com.example.brochure.model.SearchResult;

import static com.example.brochure.model.GroupEnum.AIBT;
import static com.example.brochure.model.GroupEnum.REACH;

public class SearchResultActivity extends AppCompatActivity {

    private SearchResult searchResult;
    private GridView searchResultGridView;
    private RadioButton aibtSwitchButton;
    private RadioButton reachSwitchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_search_result);

        searchResult = (SearchResult) getIntent().getSerializableExtra("SearchResult");

        TextView searchResultTitle = findViewById(R.id.search_result_title);
        searchResultTitle.setText("Search Results for: " + searchResult.getSearchText());
        searchResultGridView = findViewById(R.id.search_result_grid_view);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            searchResultGridView.setNumColumns(1);
        }

        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(this, searchResult.getSearchResults().get(AIBT));
        searchResultGridView.setAdapter(searchResultAdapter);

        aibtSwitchButton = findViewById(R.id.aibt_switch);
        reachSwitchButton = findViewById(R.id.reach_switch);
    }

    public void aibtSwitchClick(View view) {
        aibtSwitchButton.setTypeface(null, Typeface.BOLD);
        reachSwitchButton.setTypeface(null, Typeface.NORMAL);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(this, searchResult.getSearchResults().get(AIBT));
        searchResultGridView.setAdapter(searchResultAdapter);
    }

    public void reachSwitchClick(View view) {
        aibtSwitchButton.setTypeface(null, Typeface.NORMAL);
        reachSwitchButton.setTypeface(null, Typeface.BOLD);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(this, searchResult.getSearchResults().get(REACH));
        searchResultGridView.setAdapter(searchResultAdapter);
    }
}