package com.egor456788.common;

public enum Races {
    Hattifattner("Хатиффнат"),
    HEMULEN("Хемуль");
    private String race;
    Races(String race){
        this.race = race;
    }
    @Override
    public String toString() {
        return race;
    }
}
