package com.viskee.brochure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.viskee.brochure.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountrySearchSuggestionAdapter extends ArrayAdapter<String> {

    private final Context context;
    private List<String> data;
    private final List<String> temp;
    protected List<String> suggestions;

    public CountrySearchSuggestionAdapter(Context context, List<String> countries) {
        super(context, android.R.layout.simple_dropdown_item_1line, countries);
        data = countries;
        this.context = context;
        this.temp = new ArrayList<>(countries);
        this.suggestions = new ArrayList<>(countries);
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.layout_search_suggestion_item, null);
        }
        String country = data.get(position);
        TextView textView = convertView.findViewById(R.id.search_suggestion_item);
        textView.setText(country);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    final Filter myFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String country = (String) resultValue;
            if (country != null) {
                return country;
            }
            return "";
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                String searchText = constraint.toString().toLowerCase();
                for (String country : temp) {
                    if (country.toLowerCase().contains(searchText)) {
                        suggestions.add(country);
                    }
                }
                Collections.sort(suggestions);

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
            List<String> values = (List<String>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (String country : values.toArray(new String[]{})) {
                    add(country);
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
