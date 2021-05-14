package com.example.brochure.model;

import java.io.Serializable;
import java.util.List;

public class School implements Serializable {
    private String name;
    private List<Course> courses;

    public School(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "School{" +
                "name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }
}
