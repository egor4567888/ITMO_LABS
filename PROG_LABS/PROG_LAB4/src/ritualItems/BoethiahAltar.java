package ritualItems;

import common.Conditions;
import common.Devotions;
import common.Place;
import common.RitualItems;
import entities.Entity;
import entities.Hattifattener;

import java.util.LinkedHashSet;

public class BoethiahAltar extends RitualItem{
    public BoethiahAltar(String name) {
        super(name, RitualItems.BOETHIAH_ALTAR);
    }

    @Override
    public void ritual(LinkedHashSet<Entity> listHattifatteners, Place place) {
        Hattifattener[] hattifatteners = listHattifatteners.toArray(new Hattifattener[0]);
        System.out.println("В " + place + " начался ритуал Боэтии");
        for (int i = 0; i < hattifatteners.length; i++) {
            if (hattifatteners[i].getDevotion() == Devotions.BOETHIAH && (hattifatteners[i].getCondition() == Conditions.ALIVE || hattifatteners[i].getCondition() == Conditions.UNDEAD))
                hattifatteners[i].killHat(hattifatteners[(int)(Math.random() * (hattifatteners.length -1))]);
        }
    }
}
