package com.viskee.brochure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Promotions implements Serializable {
    private List<Promotion> promotions = new ArrayList<>();

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }
}
