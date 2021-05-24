package com.viskee.brochure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.viskee.brochure.R;
import com.viskee.brochure.model.Course;
import com.viskee.brochure.util.SearchUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SearchSuggestionAdapter extends ArrayAdapter<Course> {

    private final Context context;
    private List<Course> data;
    private final List<Course> temp;
    protected List<Course> suggestions;

    public SearchSuggestionAdapter(Context context, List<Course> courses) {
        super(context, android.R.layout.simple_dropdown_item_1line, courses);
        data = courses;
        this.context = context;
        this.temp = new ArrayList<>(courses);
        this.suggestions = new ArrayList<>(courses);
    }

    public List<Course> getSuggestions() {
        return suggestions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.layout_search_suggestion_item, null);
        }
        Course course = data.get(position);
        TextView textView = convertView.findViewById(R.id.search_suggestion_item);
        textView.setText(course.getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }
    Filter myFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Course course = (Course) resultValue;
            if (course != null) {
                return course.getName();
            }
            return "";
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                String searchText = constraint.toString().toLowerCase();
                searchText = searchText.replaceAll("\\+ ","");
                for (Course course : temp) {
                    List<String> splitList = new ArrayList<>(Arrays.asList(searchText.split(" ")));
                    Optional<Integer> yearOptional = SearchUtils.extractYear(splitList);
                    Optional<Integer> weekOptional = SearchUtils.extractWeek(splitList);
                    boolean isDurationMatch = SearchUtils.isDurationMatch(course.getDuration(), yearOptional, weekOptional);
                    boolean isLocationMatch = SearchUtils.isLocationMatch(course.getLocation(), splitList);
                    boolean isTextMatch = SearchUtils.isTextMatch(course.toString().toLowerCase(), splitList);
                    if (isDurationMatch && isTextMatch && isLocationMatch) {
                        suggestions.add(course);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Course> values = (List<Course>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Course course : values.toArray(new Course[]{})) {
                    add(course);
                    notifyDataSetChanged();
                }
            }
            else{
                clear();
                notifyDataSetChanged();
            }
        }
    };

}
