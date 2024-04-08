package entities;
import common.Devotions;
import common.Genders;
import common.Phrases;
import common.Races;
import things.Flower;

import java.util.ArrayList;

public class Hemulen extends Entity{
    final private Phrases phrase;
    public Hemulen(String name, Phrases phrase, int age, int height, int weight, Genders gender){
        super(name, Devotions.FLOWERS, age, height, weight, gender);
        this.phrase = phrase;
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

}
