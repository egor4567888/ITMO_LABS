package com.egor456788;

import com.egor456788.entities.Entity;
import com.egor456788.entities.Hattifattener;
import com.egor456788.common.Place;
import com.egor456788.ritualItems.*;

import java.util.LinkedHashSet;
import java.util.Objects;

public class Gathering {
    private LinkedHashSet<Entity> hattifatteners;
    private RitualItem ritualItem;
    private Place place;
    Gathering(LinkedHashSet<Entity> hattifatteners, RitualItem ritual_item, Place place){
        this.hattifatteners = hattifatteners;
        this.ritualItem = ritual_item;
        this.place = place;
    }

    public void startRitual(){
        this.ritualItem.ritual(hattifatteners, place);
    }
    /*private int[] getIndex(Devotions devotion){
        boolean someoneAlive = false;
        int [] ind = new int[2];
        for (int i = 0; i < 300; ++i)
        {
         if (hattifatteners[i].getDevotion() == devotion && (hattifatteners[i].getCondition() == Conditions.ALIVE || hattifatteners[i].getCondition() == Conditions.UNDEAD))
             someoneAlive = true;
        }
        if (!someoneAlive)
        {
            ind[0] = 0;
            ind[1] = 0;
            return ind;
        }
         ind[0] = (int)(Math.random() * (hattifatteners.length -1));
        while (hattifatteners[ind[0]].getDevotion() != devotion && (hattifatteners[ind[0]].getCondition() == Conditions.ALIVE || hattifatteners[ind[0]].getCondition() == Conditions.UNDEAD))
            ind[0] = (int)(Math.random() * (hattifatteners.length -1));
        ind[1] = (int)(Math.random() * (hattifatteners.length -1));
        while (ind[0] == ind[1])
            ind[1] = (int)(Math.random() * (hattifatteners.length -1));
        return ind;
    }*/
    /*public void ritual(){
        switch (this.ritualItem) {
            case BAROMETER ->{
                System.out.println("В " + this.place + " начался ритуал барометра");
                int ind[] = this.getIndex(Devotions.BAROMETER);
                int hat1 = ind[0];
                int hat2 = ind[1];
                if (hat1 == hat2)
                {
                    System.out.println("Нет достуаных последователей, ритуал не удался.");
                    break;
                }
                else
                {
                    switch (hattifatteners[hat2].getDevotion()){
                        case Devotions.BAROMETER -> {
                            hattifatteners[hat1].kneelHat(hattifatteners[hat2]);
                            hattifatteners[hat2].kneelHat(hattifatteners[hat1]);
                            hattifatteners[hat1].kneel(ritualItem);
                            hattifatteners[hat2].kneel(ritualItem);
                        }
                        case Devotions.LIGHT -> {
                            hattifatteners[hat1].kneelHat(hattifatteners[hat2]);
                            hattifatteners[hat2].kneelHat(hattifatteners[hat1]);
                            hattifatteners[hat1].kneel(ritualItem);
                        }
                        case Devotions.BOETHIAH -> {
                            hattifatteners[hat1].kneelHat(hattifatteners[hat2]);
                            hattifatteners[hat2].kneelHat(hattifatteners[hat1]);
                            hattifatteners[hat1].kneel(ritualItem);
                            hattifatteners[hat2].killHat(hattifatteners[hat1]);
                        }
                        default -> {
                            hattifatteners[hat1].kneelHat(hattifatteners[hat2]);
                            hattifatteners[hat2].scornfulGlanceHat(hattifatteners[hat1]);
                            hattifatteners[hat1].kneel(ritualItem);
                            hattifatteners[hat2].scornfulGlance(ritualItem);
                        }
                    }
                }
            }
            case NAMIRA_ALTAR -> {
                System.out.println("В " + this.place + " начался ритуал Намиры");
                int ind[] = this.getIndex(Devotions.NAMIRA);
                int hat1 = ind[0];
                int hat2 = ind[1];
                if (hat1 == hat2)
                {
                    System.out.println("Нет достуаных последователей, ритуал не удался.");
                    break;
                }
                switch (hattifatteners[hat2].getCondition()) {
                    case ALIVE, UNDEAD ->  {
                        hattifatteners[hat1].killHat(hattifatteners[hat2]);
                        hattifatteners[hat1].eatHat(hattifatteners[hat2]);
                    }
                    case DEAD ->  hattifatteners[hat1].eatHat(hattifatteners[hat2]);
                    default -> System.out.println("У " + hattifatteners[hat1] + " не получилось провести ритуал Намиры с " + hattifatteners[hat2] + " потому что " + hattifatteners[hat2] + " " + hattifatteners[hat2].getCondition());
                }
            }
            case RESURRECTION_ALTAR -> {
                System.out.println("В " + this.place + " начался ритуал воскрешения");
                for (int i = 0; i < hattifatteners.length; i++) {
                    if (hattifatteners[i].getCondition() == Conditions.DEAD)
                        if (hattifatteners[i].getDevotion() == Devotions.LIGHT)
                            hattifatteners[i].setCondition(Conditions.ALIVE);
                        else hattifatteners[i].setCondition(Conditions.UNDEAD);
                }
            }
            case BOETHIAH_ALTAR -> {
                System.out.println("В " + this.place + " начался ритуал Боэтии");
                for (int i = 0; i < hattifatteners.length; i++) {
                    if (hattifatteners[i].getDevotion() == Devotions.BOETHIAH && (hattifatteners[i].getCondition() == Conditions.ALIVE || hattifatteners[i].getCondition() == Conditions.UNDEAD))
                        hattifatteners[i].killHat(hattifatteners[(int)(Math.random() * (hattifatteners.length -1))]);
                }
            }
        }

    }*/
    @Override
    public String toString() {
        return "Собрание в " + place + " ритуальный предмет - " + ritualItem;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Gathering gathering = (Gathering) object;
        return Objects.equals(ritualItem, gathering.ritualItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ritualItem);
    }
}
