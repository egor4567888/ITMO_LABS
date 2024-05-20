package com.egor456788.common;

import java.io.Serializable;

public enum Races implements Serializable {
    Hattifattner("Hattifattner"),
    HEMULEN("Hemulen");
    private String race;
    Races(String race){
        this.race = race;
    }
    @Override
    public String toString() {
        return race;
    }
}
