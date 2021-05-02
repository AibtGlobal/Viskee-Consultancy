package com.example.brochure.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResult implements Serializable {
    private Map<GroupEnum, List<Course>> searchResults = new HashMap<>();

    public Map<GroupEnum, List<Course>> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(Map<GroupEnum, List<Course>> searchResults) {
        this.searchResults = searchResults;
    }
}
