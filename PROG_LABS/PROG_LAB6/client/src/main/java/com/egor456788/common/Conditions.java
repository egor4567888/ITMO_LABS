package com.egor456788.common;

import java.io.Serializable;

public enum Conditions implements Serializable {
    ALIVE("жив"),
    DEAD("мёртв"),
    EATEN("съеден"),
    UNDEAD("нежить");
    String condition;
    Conditions(String condition){
        this.condition = condition;
    }
    @Override
    public String toString() {
        return condition;
    }
}
