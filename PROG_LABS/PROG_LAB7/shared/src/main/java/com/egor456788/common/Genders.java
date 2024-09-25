package com.egor456788.common;

import java.io.Serializable;

public enum Genders implements Serializable {
    MALE("Самец"),
    FEMALE("Самка"),
    HELICOPTER("Боевой вертолёт");

    private String gender;
    Genders(String gender){
        this.gender = gender;
    }
    @Override
    public String toString() {
        return gender;
    }
}
