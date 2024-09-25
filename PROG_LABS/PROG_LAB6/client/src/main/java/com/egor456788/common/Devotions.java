package com.egor456788.common;

import java.io.Serializable;

public enum Devotions implements Serializable {
    BAROMETER("барометр"),
    NAMIRA("Намира"),
    LIGHT("свет"),
    BOETHIAH("Боэтия"),
    FLOWERS("Цветы");

    private String devotion;
    Devotions(String devotion){
        this.devotion = devotion;
    }
    @Override
    public String toString() {
        return devotion;
    }
}
