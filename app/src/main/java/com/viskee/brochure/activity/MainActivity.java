package com.viskee.brochure.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viskee.brochure.R;
import com.viskee.brochure.adapter.OnKeyboardVisibilityListener;
import com.viskee.brochure.adapter.SearchSuggestionAdapter;
import com.viskee.brochure.model.Course;
import com.viskee.brochure.model.Group;
import com.viskee.brochure.model.GroupEnum;
import com.viskee.brochure.model.Promotion;
import com.viskee.brochure.model.Promotions;
import com.viskee.brochure.model.School;
import com.viskee.brochure.model.SearchResult;
import com.viskee.brochure.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;

public class MainActivity extends AppCompatActivity implements OnKeyboardVisibilityListener {

    private final Map<GroupEnum, Group> groups = new HashMap<>();
    private Course courseSelected;
    private SearchSuggestionAdapter searchSuggestionAdapter;
    private AutoCompleteTextView searchTextBar;
    private String subFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.subFolder = getIntent().getStringExtra(getString(R.string.SUB_FOLDER));

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main_portrait);
        } else {
            setContentView(R.layout.activity_main_landscape);
        }
        prepareGroups();
        setupSearchBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchSuggestionAdapter.notifyDataSetChanged();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            searchTextBar.refreshAutoCompleteResults();
        }
    }

    public void backToPrevious(View view) {
        finish();
    }

    private void setupSearchBar() {
        searchSuggestionAdapter = new SearchSuggestionAdapter(MainActivity.this, prepareCourses());
        searchTextBar = findViewById(R.id.search_text);
        searchTextBar.setAdapter(searchSuggestionAdapter);
        searchTextBar.setThreshold(0);
        searchTextBar.clearListSelection();

        searchTextBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                courseSelected = (Course) parent.getItemAtPosition(position);
                selectItemForSearch();
            }
        });
        searchTextBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                courseSelected = null;
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

    public void selectItemForSearch() {
        if (courseSelected != null) {
            searchTextBar.setText(courseSelected.getName());
            searchSuggestionAdapter.notifyDataSetChanged();
        }
    }

    public void search() {
        String textToSearch = searchTextBar.getText().toString();
        if (StringUtils.isBlank(textToSearch)) {
            Toast.makeText(MainActivity.this, "Please type text to do the search.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Course> suggestions = searchSuggestionAdapter.getSuggestions();
        SearchResult searchResult = new SearchResult();
        searchResult.setSearchText(searchTextBar.getText().toString());

        Map<GroupEnum, List<Course>> map = suggestions.stream().collect(groupingBy(Course::getGroup));
        searchResult.getSearchResults().put(GroupEnum.REACH, map.get(GroupEnum.REACH));
        searchResult.getSearchResults().put(GroupEnum.AIBT, map.get(GroupEnum.AIBT));

        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra(getString(R.string.SEARCH_RESULT), searchResult);
        startActivity(intent);
    }

    public void goToAIBTSchoolPage(View view) {
        Intent intent = new Intent(MainActivity.this, SchoolLogoActivity.class);
        intent.putExtra(getString(R.string.GROUP), groups.get(GroupEnum.AIBT));
        startActivity(intent);
    }

    public void goToREACHCoursePage(View view) {
        Group reachGroup = groups.get(GroupEnum.REACH);
        if (reachGroup != null && reachGroup.getSchools() != null && reachGroup.getSchools().size() != 0) {
            Intent intent = new Intent(MainActivity.this, SchoolCoursesActivity.class);
            intent.putExtra(getString(R.string.SCHOOL), reachGroup.getSchools().get(0));
            intent.putExtra(getString(R.string.PROMOTIONS), new Promotions(reachGroup.getPromotions()));
            startActivity(intent);
        }
    }

    private void prepareGroups() {

        String aceJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.ACE_AVIATION_AEROSPACE_ACADEMY_FILE_NAME));
        String bespokeJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.BESPOKE_GRAMMAR_SCHOOL_OF_ENGLISH_FILE_NAME));
        String bransonJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.BRANSON_SCHOOL_OF_BUSINESS_AND_TECHNOLOGY_FILE_NAME));
        String dianaJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.DIANA_SCHOOL_OF_COMMUNITY_SERVICES_FILE_NAME));
        String edisonJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.EDISON_SCHOOL_OF_TECH_SCIENCES_FILE_NAME));
        String sheldonJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.SHELDON_SCHOOL_OF_HOSPITALITY_FILE_NAME));
        String reachJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.REACH_COMMUNITY_COLLEGE_FILE_NAME));

        String aibtPromotionJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.AIBT_PROMOTION_FILE_NAME));
        String reachPromotionJson = Utils.getJsonFromStorage(getApplicationContext(),
                subFolder + "_" + getString(R.string.REACH_PROMOTION_FILE_NAME));

        School ace = getSchoolFromJson(aceJson);
        ace.setName("ACE AVIATION AEROSPACE ACADEMY");
        School bespoke = getSchoolFromJson(bespokeJson);
        bespoke.setName("BESPOKE GRAMMAR SCHOOL OF ENGLISH");
        School branson = getSchoolFromJson(bransonJson);
        branson.setName("BRANSON SCHOOL OF BUSINESS AND TECHNOLOGY");
        School diana = getSchoolFromJson(dianaJson);
        diana.setName("DIANA SCHOOL OF COMMUNITY SERVICES");
        School edison = getSchoolFromJson(edisonJson);
        edison.setName("EDISON SCHOOL OF TECH SCIENCES");
        School sheldon = getSchoolFromJson(sheldonJson);
        sheldon.setName("SHELDON SCHOOL OF HOSPITALITY");
        School reach = getSchoolFromJson(reachJson);
        reach.setName("REACH COMMUNITY COLLEGE");

        Group aibtGroup = new Group();
        aibtGroup.setName("AIBT");
        aibtGroup.setSchools(Arrays.asList(ace, bespoke, branson, diana, edison, sheldon));
        if (StringUtils.isNotEmpty(aibtPromotionJson)) {
            Promotions aibtPromotion = getPromotionsFromJson(aibtPromotionJson);
            aibtGroup.setPromotions(aibtPromotion.getPromotions());
        }
        groups.put(GroupEnum.AIBT, aibtGroup);

        Group reachGroup = new Group();
        reachGroup.setName("REACH");
        reachGroup.setSchools(Collections.singletonList(reach));
        if (StringUtils.isNotEmpty(reachPromotionJson)) {
            Promotions reachPromotion = getPromotionsFromJson(reachPromotionJson);
            reachGroup.setPromotions(reachPromotion.getPromotions());
        }
        groups.put(GroupEnum.REACH, reachGroup);
    }

    private List<Course> prepareCourses() {
        List<School> aibtSchools = Collections.emptyList();
        List<School> reachSchools = Collections.emptyList();

        if (groups.get(GroupEnum.AIBT) != null) {
            aibtSchools = Objects.requireNonNull(groups.get(GroupEnum.AIBT)).getSchools();
        }

        if (groups.get(GroupEnum.REACH) != null) {
            reachSchools = Objects.requireNonNull(groups.get(GroupEnum.REACH)).getSchools();
        }

        List<Course> aibtCourses = new ArrayList<>();
        for (School school : aibtSchools) {
            aibtCourses.addAll(school.getCourses());
        }
        aibtCourses.forEach(course -> course.setGroup(GroupEnum.AIBT));

        List<Course> reachCourses = new ArrayList<>();
        for (School school : reachSchools) {
            reachCourses.addAll(school.getCourses());
        }
        reachCourses.forEach(course -> course.setGroup(GroupEnum.REACH));

        List<Course> courses = new ArrayList<>();
        courses.addAll(aibtCourses);
        courses.addAll(reachCourses);

        return courses;
    }

    private School getSchoolFromJson(String json) {
        Gson gson = new Gson();
        Type schoolType = new TypeToken<School>() {
        }.getType();
        return gson.fromJson(json, schoolType);
    }

    private Promotions getPromotionsFromJson(String json) {
        Gson gson = new Gson();
        Type promotionsType = new TypeToken<Promotions>() {
        }.getType();
        return gson.fromJson(json, promotionsType);
    }
}