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

//    public static class Builder {
//        private final Course course;
//
//        private Builder() {
//            this.course = new Course();
//        }
//
//        public static Builder create() {
//            return new Builder();
//        }
//
//        public Builder vetCode(String vetCode) {
//            course.setVetCode(vetCode);
//            return this;
//        }
//
//        public Builder name(String name) {
//            course.setName(name);
//            return this;
//        }
//
//        public Builder duration(int duration) {
//            course.setDuration(duration);
//            return this;
//        }
//
//        public Builder durationDetail(String durationDetail) {
//            course.setDurationDetail(durationDetail);
//            return this;
//        }
//
//        public Builder offshoreTuition(long offshoreTuition) {
//            course.setOffshoreTuition(offshoreTuition);
//            return this;
//        }
//
//        public Builder onshoreTuition(long onshoreTuition) {
//            course.setOffshoreTuition(onshoreTuition);
//            return this;
//        }
//
//        public Builder location(List<String> location) {
//            course.setLocation(location);
//            return this;
//        }
//
//        public Builder unpaidPlacement(int unpaidPlacement) {
//            course.setUnpaidPlacement(unpaidPlacement);
//            return this;
//        }
//
//        public Builder completeServicePeriods(String completeServicePeriods) {
//            course.setCompleteServicePeriods(completeServicePeriods);
//            return this;
//        }
//    }

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
}
