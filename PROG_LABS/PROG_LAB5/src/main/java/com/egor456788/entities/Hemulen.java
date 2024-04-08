package com.egor456788.entities;
import com.egor456788.common.*;
import com.egor456788.things.Flower;

import java.util.ArrayList;

public class Hemulen extends Entity{
    private Phrases phrase;
    public Hemulen(String name, Phrases phrase, int age, int height, int weight, Genders gender, Races race){
        super(name, Devotions.FLOWERS, age, height, weight, gender, race);
        this.phrase = phrase;
        setCondition(Conditions.ALIVE);
    }
    public Hemulen(String name, int age, int height, int weight, Genders gender, Races race){
        super(name, Devotions.FLOWERS, age, height, weight, gender, race);
        setCondition(Conditions.ALIVE);
    }
    private ArrayList<Flower> herbarium = new ArrayList<>();
    public void collectFlower()
    {
        Flower flower = new Flower();
        herbarium.add(flower);
        System.out.println(this + " собрал " + flower);
        System.out.println(this + " пробормотал \"Это будет номер " + herbarium.size() + " в моём гербарии\"");
    }
    /*public void mumble(){
        System.out.println(this + " пробормотал " + phrase);
    }*/
    public void countPetals(){
        if (herbarium.size() != 0) {
            int ind = (int) (Math.random() * (herbarium.size()-0.001));
            System.out.println(this + " посчитал количество лепестков у " + herbarium.get(ind) + " их " + herbarium.get(ind).getNumOfPetals());

        }
    }
    public void collectFlowers(int num){
        for (int i = 0; i < num; ++i)
            this.collectFlower();

    }

    public void setPhrase(Phrases phrase) {
        this.phrase = phrase;
    }
}
