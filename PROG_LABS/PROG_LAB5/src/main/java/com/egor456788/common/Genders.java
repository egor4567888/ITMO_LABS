package com.egor456788.common;

public enum Genders {
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
