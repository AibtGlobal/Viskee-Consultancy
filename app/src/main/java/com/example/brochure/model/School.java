package com.example.brochure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class School implements Serializable {
    private String name;
    private List<Course> courses;
//    private List<Department> departments = new ArrayList<>();

    public School() {

    }

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

//    public List<Department> getDepartments() {
//        return departments;
//    }

//    public void setDepartments(List<Department> departments) {
//        this.departments = departments;
//    }


    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
