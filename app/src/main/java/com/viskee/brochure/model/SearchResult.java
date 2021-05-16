package com.viskee.brochure.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResult implements Serializable {
    private String searchText;
    private Map<GroupEnum, List<Course>> searchResults = new HashMap<>();

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Map<GroupEnum, List<Course>> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(Map<GroupEnum, List<Course>> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "searchText='" + searchText + '\'' +
                ", searchResults=" + searchResults +
                '}';
    }
}
