package com.example.brochure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    public Course () {

    }
    public Course(String vetCode, String department, String name, int duration, String durationDetail, int offshoreTuition, int onshoreTuition, List<String> location, int unpaidPlacement, String completeServicePeriods) {
        this.vetCode = vetCode;
        this.department = department;
        this.name = name;
        this.duration = duration;
        this.durationDetail = durationDetail;
        this.offshoreTuition = offshoreTuition;
        this.onshoreTuition = onshoreTuition;
        this.location = location;
        this.unpaidPlacement = unpaidPlacement;
        this.completeServicePeriods = completeServicePeriods;
    }

    private GroupEnum group;
    private String vetCode;
    private String department;
    private String name;
    private int duration;
    private String durationDetail;
    private int offshoreTuition;
    private int onshoreTuition;
    private List<String> location = new ArrayList<>();
    private int unpaidPlacement;
    private String completeServicePeriods;

    public GroupEnum getGroup() {
        return group;
    }

    public void setGroup(GroupEnum group) {
        this.group = group;
    }

    public String getVetCode() {
        return vetCode;
    }

    public void setVetCode(String vetCode) {
        this.vetCode = vetCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDurationDetail() {
        return durationDetail;
    }

    public void setDurationDetail(String durationDetail) {
        this.durationDetail = durationDetail;
    }

    public int getOffshoreTuition() {
        return offshoreTuition;
    }

    public void setOffshoreTuition(int offshoreTuition) {
        this.offshoreTuition = offshoreTuition;
    }

    public int getOnshoreTuition() {
        return onshoreTuition;
    }

    public void setOnshoreTuition(int onshoreTuition) {
        this.onshoreTuition = onshoreTuition;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public int getUnpaidPlacement() {
        return unpaidPlacement;
    }

    public void setUnpaidPlacement(int unpaidPlacement) {
        this.unpaidPlacement = unpaidPlacement;
    }

    public String getCompleteServicePeriods() {
        return completeServicePeriods;
    }

    public void setCompleteServicePeriods(String completeServicePeriods) {
        this.completeServicePeriods = completeServicePeriods;
    }

    @Override
    public String toString() {
        return "Course{" +
                "vetCode='" + vetCode + '\'' +
                ", department='" + department + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", durationDetail='" + durationDetail + '\'' +
                ", offshoreTuition=" + offshoreTuition +
                ", onshoreTuition=" + onshoreTuition +
                ", location=" + location +
                ", unpaidPlacement=" + unpaidPlacement +
                ", completeServicePeriods='" + completeServicePeriods + '\'' +
                '}';
    }
}
