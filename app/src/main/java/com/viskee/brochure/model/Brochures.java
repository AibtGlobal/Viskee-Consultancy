package com.viskee.brochure.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Brochures implements Serializable {
    private List<Brochure> brochures = new ArrayList<>();

    public Brochures(List<Brochure> brochures) {
        this.brochures = brochures;
    }

    public List<Brochure> getBrochures() {
        return brochures;
    }

    public void setBrochures(List<Brochure> brochures) {
        this.brochures = brochures;
    }
}
