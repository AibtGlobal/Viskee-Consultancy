package com.viskee.brochure.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.viskee.brochure.R;
import com.viskee.brochure.adapter.CountrySearchSuggestionAdapter;
import com.viskee.brochure.adapter.OnKeyboardVisibilityListener;
import com.viskee.brochure.model.Region;
import com.viskee.brochure.model.SubFolderEnum;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RegionSelectionActivity extends AppCompatActivity implements OnKeyboardVisibilityListener {

    private CountrySearchSuggestionAdapter searchSuggestionAdapter;
    private AutoCompleteTextView searchTextBar;
    private String countrySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_selection);
        setupSearchBar();
    }

    private void setupSearchBar() {
        searchSuggestionAdapter = new CountrySearchSuggestionAdapter(RegionSelectionActivity.this, prepareCountries());
        searchTextBar = findViewById(R.id.country_search_text);
        searchTextBar.setAdapter(searchSuggestionAdapter);
        searchTextBar.setThreshold(0);
        searchTextBar.clearListSelection();

        searchTextBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countrySelected = (String) parent.getItemAtPosition(position);
                search();
            }
        });
        searchTextBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                countrySelected = null;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchSuggestionAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchSuggestionAdapter.notifyDataSetChanged();
            }
        });
        searchTextBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        setKeyboardVisibilityListener(this);
    }

    private List<String> prepareCountries() {
        List<String> countries = new ArrayList<>();
        countries.addAll(Region.SEAPAE);
        countries.addAll(Region.SISMIC);
        return countries;
    }

    public void search() {
        String textToSearch = searchTextBar.getText().toString();
        if (StringUtils.isBlank(textToSearch)) {
            Toast.makeText(RegionSelectionActivity.this, "Please type your country name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(countrySelected)) {
            Toast.makeText(RegionSelectionActivity.this, "Please select your country.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Region.SEAPAE.stream().anyMatch(countrySelected::equalsIgnoreCase)) {
            goToSeapae();
        } else {
            goToSismic();
        }
    }

    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int defaultKeyboardHeightDP = 100;
                int estimatedKeyboardDP = defaultKeyboardHeightDP + 48;
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        estimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                alreadyOpen = isShown;
                onKeyboardVisibilityListener.onVisibilityChanged(isShown);
            }
        });
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) searchTextBar.getLayoutParams();
        if (visible) {
//            params.verticalBias = 0.4f;
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                searchTextBar.setDropDownVerticalOffset(-120);
            }
        } else {
//            params.verticalBias = 0.55f;
            searchTextBar.setDropDownVerticalOffset(0);
        }
        searchTextBar.setLayoutParams(params);
    }

    public void goToSismic() {
        Intent intent = new Intent(RegionSelectionActivity.this, ConfigurationDownloadActivity.class);
        intent.putExtra(getString(R.string.SUB_FOLDER), SubFolderEnum.SISMIC.name());
        startActivity(intent);
    }

    public void goToSeapae() {
        Intent intent = new Intent(RegionSelectionActivity.this, ConfigurationDownloadActivity.class);
        intent.putExtra(getString(R.string.SUB_FOLDER), SubFolderEnum.SEAPAE.name());
        startActivity(intent);
    }

    public void backToPrevious(View view) {
        finish();
    }
}