package com.egor456788.entities;

import com.egor456788.common.Devotions;
import com.egor456788.common.Genders;
import com.egor456788.common.Races;
import com.egor456788.interfaces.EatHat;
import com.egor456788.interfaces.KillHat;

public class Hattifattener extends Entity implements KillHat, EatHat {


    public Hattifattener(String name, Devotions devotion, int age, int height, int weight, Genders gender, Races race){
        super(name,devotion, age, height, weight, gender, race);

    }
    public void kneelHat(Hattifattener hattifattener){
        System.out.println(this + " поклонился " + hattifattener);
    }
    public void kneel(String ritualItem){
        System.out.println(this + " поклонился " + ritualItem);
    }
    public void scornfulGlanceHat(Hattifattener hattifattener){
        System.out.println(this + " презрительно посмотрел на " + hattifattener);
    }
    public void scornfulGlance(String ritualItem){
        System.out.println(this + " презрительно посмотрел на " + ritualItem);
    }
   /*public static void Prnt(){
        System.out.println(1);
    }
    /*public void barometersRitual(Hattifattener hattifattener, RitualItems ritualItem){
        switch (hattifattener.getDevotion()){
            case Devotions.BAROMETER -> {
             this.kneelHat(hattifattener);
             hattifattener.kneelHat(this);
             this.kneelRitItem(ritualItem);
             hattifattener.kneelRitItem(ritualItem);
            }
            case Devotions.LIGHT -> {
                this.kneelHat(hattifattener);
                hattifattener.kneelHat(this);
                this.kneelRitItem(ritualItem);
            }
            case Devotions.BOETHIAH -> {
                this.kneelHat(hattifattener);
                hattifattener.kneelHat(this);
                this.kneelRitItem(ritualItem);
                hattifattener.killHat(this);
            }
            default -> {
                this.kneelHat(hattifattener);
                hattifattener.scornfulGlanceHat(this);
                this.kneelRitItem(ritualItem);
                hattifattener.scornfulGlanceRitItem(ritualItem);
            }
        }
    }*/
    /*public void vaerminasRitual(Hattifattener hattifattener) {
        switch (hattifattener.getCondition()) {
            case ALIVE  ->  {
                this.killHat(hattifattener);
                this.eatHat(hattifattener);
            }
            case UNDEAD  ->  {
                this.killHat(hattifattener);
                this.eatHat(hattifattener);
            }
            case DEAD ->  this.eatHat(hattifattener);
            default -> System.out.println("У " + this + " не получилось провести ритуал Вермины с " + hattifattener + " потому что " + hattifattener + " " + hattifattener.getCondition());
        }
    }*/



    public boolean equals(Hattifattener hattifattener) {
        return this.hashCode() == hattifattener.hashCode();
    }
    @Override
    public int hashCode() {
        return (this.getDevotion()+"").hashCode();
    }
}
