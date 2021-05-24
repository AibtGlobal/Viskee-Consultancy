package com.viskee.brochure.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchUtils {

    //---------------Duration---------------
    private static final String YEAR = "year";
    private static final String YEARS = "years";
    private static final String WEEK = "week";
    private static final String WEEKS = "weeks";
    private static final String WKS = "wks";
    //---------------Location---------------
    private static final Map<String, List<String>> LOCATION_MAP = new HashMap<>();
    static {
        LOCATION_MAP.put("nsw", Arrays.asList("nsw", "new south wales", "sydney"));
        LOCATION_MAP.put("new south wales", Arrays.asList("nsw", "new south wales", "sydney"));
        LOCATION_MAP.put("sydney", Arrays.asList("nsw", "new south wales", "sydney"));
        LOCATION_MAP.put("tas", Arrays.asList("tas", "tasmania", "hobart"));
        LOCATION_MAP.put("tasmania", Arrays.asList("tas", "tasmania", "hobart"));
        LOCATION_MAP.put("hobart", Arrays.asList("tas", "tasmania", "hobart"));
        LOCATION_MAP.put("qld", Arrays.asList("qld", "queensland", "brisbane"));
        LOCATION_MAP.put("queensland", Arrays.asList("qld", "queensland", "brisbane"));
        LOCATION_MAP.put("brisbane", Arrays.asList("qld", "queensland", "brisbane"));
    }
    private static final List<String> LOCATION_NAME_LIST = Arrays.asList("nsw", "new south wales", "sydney", "tas", "tasmania", "hobart", "qld", "queensland", "brisbane");

    public static Optional<Integer> extractYear(List<String> splitList) {
        int yearTextIndex = splitList.indexOf(YEAR);
        int yearNumberIndex;
        if (yearTextIndex < 1) {
            yearTextIndex = splitList.indexOf(YEARS);
        }
        if (yearTextIndex >= 1) {
            yearNumberIndex = yearTextIndex - 1;
            String year = splitList.get(yearNumberIndex);
            if (NumberUtils.isCreatable(year)) {
                splitList.remove(yearTextIndex);
                splitList.remove(yearNumberIndex);
                return Optional.of(Integer.valueOf(year));
            }
        } else {
            ListIterator<String> listIterator = splitList.listIterator();
            while (listIterator.hasNext()) {
                String split = listIterator.next();
                if (split.contains(YEAR)) {
                    String[] strings = split.split(YEAR);
                    if (NumberUtils.isCreatable(strings[0])) {
                        listIterator.remove();
                        return Optional.of(Integer.valueOf(strings[0]));
                    }
                }
                if (split.contains(YEARS)) {
                    String[] strings = split.split(YEARS);
                    if (NumberUtils.isCreatable(strings[0])) {
                        listIterator.remove();
                        return Optional.of(Integer.valueOf(strings[0]));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<Integer> extractWeek(List<String> splitList) {
        int weekTextIndex = splitList.indexOf(WEEK);
        int weekNumberIndex;
        if (weekTextIndex < 1) {
            weekTextIndex = splitList.indexOf(WEEKS);
        }
        if (weekTextIndex < 1) {
            weekTextIndex = splitList.indexOf(WKS);
        }
        if (weekTextIndex > 1) {
            weekNumberIndex = weekTextIndex - 1;
            String week = splitList.get(weekNumberIndex);
            if (NumberUtils.isCreatable(week)) {
                splitList.remove(weekTextIndex);
                splitList.remove(weekNumberIndex);
                return Optional.of(Integer.valueOf(week));
            }
        } else {
            ListIterator<String> listIterator = splitList.listIterator();
            while (listIterator.hasNext()) {
                String split = listIterator.next();
                if (split.contains(WKS)) {
                    String[] strings = split.split(WKS);
                    if (NumberUtils.isCreatable(strings[0])) {
                        listIterator.remove();
                        return Optional.of(Integer.valueOf(strings[0]));
                    }
                }
                if (split.contains(WEEK)) {
                    String[] strings = split.split(WEEK);
                    if (NumberUtils.isCreatable(strings[0])) {
                        listIterator.remove();
                        return Optional.of(Integer.valueOf(strings[0]));
                    }
                }
                if (split.contains(WEEKS)) {
                    String[] strings = split.split(WEEKS);
                    if (NumberUtils.isCreatable(strings[0])) {
                        listIterator.remove();
                        return Optional.of(Integer.valueOf(strings[0]));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static boolean isDurationMatch(int duration, Optional<Integer> yearOptional,
                                          Optional<Integer> weekOptional) {
        if (!yearOptional.isPresent() && !weekOptional.isPresent()) {
            // No duration search exists.
            return true;
        }
        if (weekOptional.isPresent()) {
            int week = weekOptional.get();
            return duration <= week;
        }
        if (yearOptional.isPresent()) {
            int year = yearOptional.get();
            if (year == 1 && duration <= 52) {
                return true;
            }
            if (year == 2 && duration > 52 && duration <= 104) {
                return true;
            }
            if (year > 2 && duration > 104) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLocationMatch(List<String> locations, List<String> splitList) {
        boolean isLocationSearchTextProvided = false;
        boolean isLocationSearchMatch = false;

        String searchText = StringUtils.join(splitList, " ");
        List<String> locationsLowerCase = locations.stream().map(String::toLowerCase).collect(Collectors.toList());
        for (String locationName : LOCATION_NAME_LIST) {
            if (searchText.contains(locationName)) {
                isLocationSearchTextProvided = true;
                searchText = searchText.replace(locationName, "");
                List<String> locationNameList = LOCATION_MAP.get(locationName);
                if (locationNameList != null && !Collections.disjoint(locationNameList, locationsLowerCase)) {
                    isLocationSearchMatch = true;
                }
            }
        }
        splitList.clear();
        splitList.addAll(Arrays.asList(searchText.split(" ")));
        splitList.remove("");
        splitList.remove(" ");
        splitList.remove("  ");
        if (isLocationSearchMatch) {
            return true;
        } else {
            return !isLocationSearchTextProvided;
        }
    }

    public static boolean isTextMatch(String courseString, List<String> splitList) {
        if (splitList.isEmpty()) return true;
        String searchText = "(.*?)" + StringUtils.join(splitList, "(.*?)") + "(.*?)";
        Pattern pattern = Pattern.compile(searchText);
        Matcher matcher = pattern.matcher(courseString);
        return matcher.find();
    }
}
