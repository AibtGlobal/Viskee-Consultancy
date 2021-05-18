package com.viskee.brochure.activity;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.adapter.SearchResultAdapter;
import com.viskee.brochure.model.SearchResult;

import static com.viskee.brochure.model.GroupEnum.AIBT;
import static com.viskee.brochure.model.GroupEnum.REACH;

public class SearchResultActivity extends AppCompatActivity {

    private SearchResult searchResult;
    private GridView searchResultGridView;
    private RadioButton aibtSwitchButton;
    private RadioButton reachSwitchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_search_result_portrait);
        } else {
            setContentView(R.layout.activity_search_result_landscape);
        }

        searchResult = (SearchResult) getIntent().getSerializableExtra(getString(R.string.SEARCH_RESULT));

        TextView searchResultTitle = findViewById(R.id.search_result_title);
        searchResultTitle.setText("Search Results for: " + searchResult.getSearchText());
        searchResultGridView = findViewById(R.id.search_result_grid_view);
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

    public void backToPrevious(View view) {
        finish();
    }
}