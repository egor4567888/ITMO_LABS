package com.egor456788.common;

import java.io.Serializable;

public enum Devotions implements Serializable {
    BAROMETER("Barometer"),
    NAMIRA("Namira"),
    LIGHT("Light"),
    BOETHIAH("Boethiah"),
    FLOWERS("Flowers");

    private String devotion;
    Devotions(String devotion){
        this.devotion = devotion;
    }
    @Override
    public String toString() {
        return devotion;
    }
}
