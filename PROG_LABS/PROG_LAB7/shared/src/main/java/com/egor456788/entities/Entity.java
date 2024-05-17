package com.egor456788.entities;

import com.egor456788.common.*;
import com.egor456788.interfaces.ScoreCounter;
import com.egor456788.ritualItems.RitualItem;
import com.egor456788.things.Flower;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


import java.io.Serializable;
import java.util.Objects;


public class Entity implements Comparable <Entity>, Serializable {

    private int id;
    final private String name;
    private Conditions condition;
    private final Devotions devotion;

    private Integer score;
    final private Races race;
    final private Integer age;
    final private Integer height;
    final private Integer weight;
    final private Genders gender;
    final private String creatorName;




    public Entity(String name, Devotions devotion, int age, int height, int weight, Genders gender, Races race, String creatorName) {
        this.name = name;
        this.devotion = devotion;
        this.creatorName = creatorName;
        this.condition = Conditions.ALIVE;
        this.race = race;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.score = 0;


    }
    public Entity(int id,String name, Devotions devotion, int age, int height, int weight, Genders gender, Races race, String creatorName) {
        this.id = id;
        this.name = name;
        this.devotion = devotion;
        this.creatorName = creatorName;
        this.condition = Conditions.ALIVE;
        this.race = race;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.score = 0;


    }
    @XStreamOmitField
    ScoreCounter scounter = new ScoreCounter(){
        public void count(ValuableActions action, Entity entity) {
            switch (action) {
                case KILL -> {
                    if (getDevotion() == Devotions.BOETHIAH)
                        score+= entity.score;
                }
                case EAT -> {
                    if (getDevotion() == Devotions.NAMIRA)
                        score+= 3;
                }
                case KNEEL -> {
                    if (getDevotion() == Devotions.LIGHT)
                        score += 1;
                }
                default ->{}
            }
        }

        public void count(ValuableActions action, Flower flower) {
            if (getDevotion() == Devotions.FLOWERS)
                switch (flower.getRarity()){
                    case COMMON -> score+=1;
                    case RARE -> score+=3;
                    case EPIC -> score+=7;
                    case LEGENDARY -> score+=25;
                    default -> {}
                }

        }
        public void count(ValuableActions action, RitualItem ritualItem) {
            if (getDevotion() == Devotions.BAROMETER)
                switch (ritualItem.getType()){
                    case  BAROMETER -> score+=20;
                    default -> {score+=3;}
                }

        }

    };

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " {" +
                "ent_id='" + id + '\'' +
                "name='" + name  +
                ", devotion=" + devotion +
                ", race=" + race +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", gender=" + gender +
                '}';
    }

    public void setCondition(Conditions condition){
        this.condition = condition;
        System.out.println(this + " теперь " + this.condition);
    }
    public Conditions getCondition(){
        return condition;
    }
    public Devotions getDevotion(){
        return devotion;
    }
    //public void addScore(int x) {score+=x;}
    public int getScore() {return score;}

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int compareTo(Entity entity) {
        /*int scoreComparison = Integer.compare(this.score, entity.score);
        if (scoreComparison != 0)
            return scoreComparison;*/
        return Integer.compare(this.age, entity.age);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Entity entity = (Entity) object;
        return Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Races getRace() {
        return race;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Genders getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatorName() {
        return creatorName;
    }
}
