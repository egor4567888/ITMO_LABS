package entities;

import common.Devotions;
import common.RitualItems;
import interfaces.EatHat;
import interfaces.KillHat;

public class Hattifattener extends Entity implements KillHat, EatHat {
    private final Devotions devotion;
    public Hattifattener(String name, Devotions devotion){
        super(name);
        this.devotion = devotion;
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
    public static void Prnt(){
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



    public Devotions getDevotion(){
        return devotion;
    }
    public boolean equals(Hattifattener hattifattener) {
        return this.hashCode() == hattifattener.hashCode();
    }
    @Override
    public int hashCode() {
        return (this.getDevotion()+"").hashCode();
    }
}
