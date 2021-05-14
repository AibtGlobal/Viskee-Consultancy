package com.example.brochure.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.brochure.R;
import com.example.brochure.activity.SchoolCoursesActivity;
import com.example.brochure.activity.SchoolLogoActivity;
import com.example.brochure.activity.SearchResultActivity;
import com.example.brochure.model.Course;
import com.example.brochure.model.Group;
import com.example.brochure.model.GroupEnum;
import com.example.brochure.model.School;
import com.example.brochure.model.SearchResult;
import com.example.brochure.util.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class MainViewAdapter {

    private final Activity context;
    private final View layoutView;
    private final Map<GroupEnum, Group> groups = new HashMap<>();
    private Course courseSelected;

    public MainViewAdapter(Activity context, View layoutView) {
        this.context = context;
        this.layoutView = layoutView;
    }

    public void prepareData() {
        String aibtJson = Utils.getJsonFromStorage(context.getApplicationContext(),
                context.getString(R.string.AIBT_CONFIGURATION_FILE_NAME));
        String reachJson = Utils.getJsonFromStorage(context.getApplicationContext(),
                context.getString(R.string.REACH_CONFIGURATION_FILE_NAME));

        Group aibt = getSchoolFromJson(aibtJson);
        Group reach = getSchoolFromJson(reachJson);

        groups.put(GroupEnum.AIBT, aibt);
        groups.put(GroupEnum.REACH, reach);

        List<School> aibtSchools = aibt.getSchools();
        List<School> reachSchools = reach.getSchools();

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

        AutoCompleteTextView searchTextBar = layoutView.findViewById(R.id.search_text);

        SearchSuggestionAdapter searchSuggestionAdapter = new SearchSuggestionAdapter(context, courses);
        searchTextBar.setAdapter(searchSuggestionAdapter);
        searchTextBar.setThreshold(0);

        searchTextBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                courseSelected = (Course) parent.getItemAtPosition(position);

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

        MaterialButton searchButton = layoutView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });

        ImageButton aibtButton = layoutView.findViewById(R.id.aibt);
        aibtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAIBTSchoolPage(v);
            }
        });
        ImageButton reachButton = layoutView.findViewById(R.id.reach);
        reachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToREACHCoursePage(v);
            }
        });
    }

    public void search(View view) {
        AutoCompleteTextView searchTextBar = layoutView.findViewById(R.id.search_text);
        String textToSearch = searchTextBar.getText().toString();
        if (StringUtils.isBlank(textToSearch)) {
            Toast.makeText(context, "Please type text to do the search.", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra("SearchResult", searchResult);
        context.startActivity(intent);
    }

    public void goToAIBTSchoolPage(View view) {
        Intent intent = new Intent(context, SchoolLogoActivity.class);
        intent.putExtra(context.getString(R.string.GROUP), groups.get(GroupEnum.AIBT));
        context.startActivity(intent);
    }

    public void goToREACHCoursePage(View view) {
        Group reachGroup = groups.get(GroupEnum.REACH);
        if (reachGroup != null && reachGroup.getSchools() != null && reachGroup.getSchools().size() != 0) {
            Intent intent = new Intent(context, SchoolCoursesActivity.class);
            intent.putExtra(context.getString(R.string.SCHOOL), reachGroup.getSchools().get(0));
            context.startActivity(intent);
        }
    }

    private Group getSchoolFromJson(String json) {
        Gson gson = new Gson();
        Type groupType = new TypeToken<Group>() {
        }.getType();
        return gson.fromJson(json, groupType);
    }
}
