package com.viskee.brochure.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.viskee.brochure.R;
import com.viskee.brochure.adapter.SearchSuggestionAdapter;
import com.viskee.brochure.model.Course;
import com.viskee.brochure.model.Group;
import com.viskee.brochure.model.GroupEnum;
import com.viskee.brochure.model.School;
import com.viskee.brochure.model.SearchResult;
import com.viskee.brochure.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;

public class MainActivity extends AppCompatActivity {

    private final Map<GroupEnum, Group> groups = new HashMap<>();
    private Course courseSelected;
    private SearchSuggestionAdapter searchSuggestionAdapter;
    private AutoCompleteTextView searchTextBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        searchTextBar.refreshAutoCompleteResults();
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
                search();
            }
        });
        searchTextBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                courseSelected = null;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        if (courseSelected != null) {
            searchResult.getSearchResults().put(courseSelected.getGroup(), Collections.singletonList(courseSelected));
        } else {
            Map<GroupEnum, List<Course>> map = suggestions.stream().collect(groupingBy(Course::getGroup));
            searchResult.getSearchResults().put(GroupEnum.REACH, map.get(GroupEnum.REACH));
            searchResult.getSearchResults().put(GroupEnum.AIBT, map.get(GroupEnum.AIBT));
        }
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
            intent.putExtra(getString(R.string.PROMOTION), reachGroup.getPromotion());
            startActivity(intent);
        }
    }

    private void prepareGroups() {

        String aibtJson = Utils.getJsonFromStorage(getApplicationContext(), getString(R.string.AIBT_CONFIGURATION_FILE_NAME));
        String reachJson = Utils.getJsonFromStorage(getApplicationContext(), getString(R.string.REACH_CONFIGURATION_FILE_NAME));

        Group aibt = getSchoolFromJson(aibtJson);
        Group reach = getSchoolFromJson(reachJson);

        groups.put(GroupEnum.AIBT, aibt);
        groups.put(GroupEnum.REACH, reach);
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

    private Group getSchoolFromJson(String json) {
        Gson gson = new Gson();
        Type groupType = new TypeToken<Group>() {
        }.getType();
        return gson.fromJson(json, groupType);
    }
}