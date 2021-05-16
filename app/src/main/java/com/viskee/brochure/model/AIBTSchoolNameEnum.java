package com.viskee.brochure.model;

import java.util.HashMap;
import java.util.Map;

public enum AIBTSchoolNameEnum {
    ACE("ACE AVIATION AEROSPACE ACADEMY"),
    BESPOKE("BESPOKE GRAMMAR SCHOOL OF ENGLISH"),
    BRANSON("BRANSON SCHOOL OF BUSINESS AND TECHNOLOGY"),
    DIANA("DIANA SCHOOL OF COMMUNITY SERVICES"),
    EDISON("EDISON SCHOOL OF TECH SCIENCES"),
    SHELDON("SHELDON SCHOOL OF HOSPITALITY"),
    REACH("REACH COMMUNITY COLLEGE");


    private String name;
    private static final Map<String, AIBTSchoolNameEnum> map = new HashMap<>();
    static {
        map.put(ACE.name, ACE);
        map.put(BESPOKE.name, BESPOKE);
        map.put(BRANSON.name, BRANSON);
        map.put(DIANA.name, DIANA);
        map.put(EDISON.name, EDISON);
        map.put(SHELDON.name, SHELDON);
        map.put(REACH.name, REACH);
    }

    AIBTSchoolNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static AIBTSchoolNameEnum fromValue(String name) {
        return map.get(name);
    }
}
