package com.viskee.brochure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String name;
    private Promotion promotion;
    private List<School> schools = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", schools=" + schools +
                '}';
    }
}
