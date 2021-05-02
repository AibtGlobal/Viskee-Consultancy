package com.example.brochure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.brochure.R;
import com.example.brochure.adapter.SearchResultAdapter;
import com.example.brochure.model.Group;
import com.example.brochure.model.GroupEnum;
import com.example.brochure.model.School;
import com.example.brochure.model.SearchResult;
import com.example.brochure.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<School> schools = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
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
    }

    public void search(View view) {
        SearchResult searchResult = new SearchResult();
        searchResult.getSearchResults().put(GroupEnum.AIBT, schools.get(0).getCourses());
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("SearchResult", searchResult);
        startActivity(intent);
    }

    public void goToAIBTSchoolPage(View view) {
        Intent intent = new Intent(MainActivity.this, SchoolActivity.class);
        intent.putExtra("Group", groups.get(0));
        startActivity(intent);
    }
}