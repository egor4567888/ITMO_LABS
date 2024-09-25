package com.egor456788.common;

import java.io.Serializable;

public enum Genders implements Serializable {
    MALE("Male"),
    FEMALE("Female"),
    HELICOPTER("Helicopter");

    private String gender;
    Genders(String gender){
        this.gender = gender;
    }
    @Override
    public String toString() {
        return gender;
    }
}
