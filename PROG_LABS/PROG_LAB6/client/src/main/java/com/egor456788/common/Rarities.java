package com.egor456788.common;

public enum Rarities {
    COMMON("обычный"),
    RARE("редкий"),
    EPIC("эпический"),
    LEGENDARY("легендарный");
    private String rarity;
    Rarities(String rarity){
        this.rarity = rarity;
    }
    @Override
    public String toString() {
        return rarity;
    }
}
