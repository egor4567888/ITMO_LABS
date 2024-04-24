package com.egor456788.ritualItems;

import com.egor456788.common.Conditions;
import com.egor456788.common.Devotions;
import com.egor456788.common.Place;
import com.egor456788.common.RitualItems;
import com.egor456788.entities.Entity;
import com.egor456788.entities.Hattifattener;

import java.util.LinkedHashSet;

public class ResurrectionAltar extends RitualItem{
    public ResurrectionAltar(String name) {
        super(name, RitualItems.RESURRECTION_ALTAR);
    }

    @Override
    public void ritual(LinkedHashSet<Entity> listHattifatteners, Place place) {
        Hattifattener[] hattifatteners = listHattifatteners.toArray(new Hattifattener[0]);
        System.out.println("В " + place + " начался ритуал воскрешения");
        for (int i = 0; i < hattifatteners.length; i++) {
            if (hattifatteners[i].getCondition() == Conditions.DEAD)
                if (hattifatteners[i].getDevotion() == Devotions.LIGHT)
                    hattifatteners[i].setCondition(Conditions.ALIVE);
                else hattifatteners[i].setCondition(Conditions.UNDEAD);
        }
    }
}
