package com.example.brochure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brochure.R;
import com.example.brochure.adapter.SearchSuggestionAdapter;
import com.example.brochure.model.Course;
import com.example.brochure.model.Group;
import com.example.brochure.model.GroupEnum;
import com.example.brochure.model.School;
import com.example.brochure.model.SearchResult;
import com.example.brochure.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class MainActivity extends AppCompatActivity {

    private final Map<GroupEnum, Group> groups = new HashMap<>();
    private Course courseSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        String aibtJson = Utils.getJsonFromAssets(getApplicationContext(), "AIBT.json");
        String reachJson = Utils.getJsonFromAssets(getApplicationContext(), "REACH.json");

        List<School> aibtSchools = getSchoolFromJson(aibtJson);
        List<School> reachSchools = getSchoolFromJson(reachJson);

        Group aibt = new Group();
        aibt.setName("AIBT");
        aibt.setSchools(aibtSchools);
        groups.put(GroupEnum.AIBT, aibt);

        Group reach = new Group();
        reach.setName("REACH");
        reach.setSchools(reachSchools);
        groups.put(GroupEnum.REACH, reach);

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

        AutoCompleteTextView searchTextBar = findViewById(R.id.search_text);

        SearchSuggestionAdapter searchSuggestionAdapter = new SearchSuggestionAdapter(this, courses);
        searchTextBar.setAdapter(searchSuggestionAdapter);
        searchTextBar.setThreshold(0);

        searchTextBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                courseSelected = (Course)parent.getItemAtPosition(position);

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
    }

    public void search(View view) {
        AutoCompleteTextView searchTextBar = findViewById(R.id.search_text);
        String textToSearch = searchTextBar.getText().toString();
        if (StringUtils.isBlank(textToSearch)) {
            Toast.makeText(this, "Please type text to do the search.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Course> suggestions = ((SearchSuggestionAdapter) searchTextBar.getAdapter()).getSuggestions();
//        EditText searchTextView = findViewById(R.id.search_text);
        SearchResult searchResult = new SearchResult();
        searchResult.setSearchText(searchTextBar.getText().toString());
        if (courseSelected != null) {
            searchResult.getSearchResults().put(courseSelected.getGroup(), Collections.singletonList(courseSelected));
        } else {
            Map<GroupEnum, List<Course>> map = suggestions.stream().collect(groupingBy(Course::getGroup));
            searchResult.getSearchResults().put(GroupEnum.REACH, map.get(GroupEnum.REACH));
            searchResult.getSearchResults().put(GroupEnum.AIBT, map.get(GroupEnum.AIBT));
        }
//        searchResult.getSearchResults().put(GroupEnum.REACH, schools.get(1).getCourses());
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("SearchResult", searchResult);
        startActivity(intent);
    }

    public void goToAIBTSchoolPage(View view) {
        Intent intent = new Intent(MainActivity.this, SchoolLogoActivity.class);
        intent.putExtra("Group", groups.get(GroupEnum.AIBT));
        startActivity(intent);
    }

    public void goToREACHCoursePage(View view) {
        Group reachGroup = groups.get(GroupEnum.REACH);
        if (reachGroup != null && reachGroup.getSchools() != null && reachGroup.getSchools().size() != 0) {
            Intent intent = new Intent(MainActivity.this, SchoolCoursesActivity.class);
            intent.putExtra("School", reachGroup.getSchools().get(0));
            startActivity(intent);
        }
    }

    private List<School> getSchoolFromJson(String json) {
        Gson gson = new Gson();
        Type listSchoolType = new TypeToken<List<School>>() {
        }.getType();
        return gson.fromJson(json, listSchoolType);
    }
}