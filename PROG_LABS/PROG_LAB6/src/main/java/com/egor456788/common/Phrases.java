package com.egor456788.common;

public enum Phrases {
NUMBER_TWO_HUNDREED_NINTEEN_IN_MY_HERBARIUM("Это будет номер двести девятнадцать в моем гербарии");
    String phrase;
    Phrases(String phrase){
        this.phrase = phrase;
    }
    @Override
    public String toString() {
        return phrase;
    }
}
