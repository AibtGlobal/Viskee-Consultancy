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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<School> schools = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private Course courseSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        String json = Utils.getJsonFromAssets(getApplicationContext(), "AIBT.json");
        Gson gson = new Gson();
        Type listSchoolType = new TypeToken<List<School>>() {
        }.getType();
        schools = gson.fromJson(json, listSchoolType);
        Group aibt = new Group();
        aibt.setName("AIBT");
        aibt.setSchools(schools);
        groups.add(aibt);

        List<Course> courses = new ArrayList<>();
        for (School school : schools) {
            courses.addAll(school.getCourses());
        }

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
            searchResult.getSearchResults().put(GroupEnum.AIBT, Collections.singletonList(courseSelected));
        } else {
            searchResult.getSearchResults().put(GroupEnum.AIBT, suggestions);
        }
//        searchResult.getSearchResults().put(GroupEnum.REACH, schools.get(1).getCourses());
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("SearchResult", searchResult);
        startActivity(intent);
    }

    public void goToAIBTSchoolPage(View view) {
        Intent intent = new Intent(MainActivity.this, SchoolLogoActivity.class);
        intent.putExtra("Group", groups.get(0));
        startActivity(intent);
    }
}