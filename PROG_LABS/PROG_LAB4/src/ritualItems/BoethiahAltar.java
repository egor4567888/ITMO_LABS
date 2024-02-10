package ritualItems;

import common.Conditions;
import common.Devotions;
import common.Place;
import entities.Hattifattener;

public class BoethiahAltar extends RitualItem{
    public BoethiahAltar(String name) {
        super(name);
    }

    @Override
    public void ritual(Hattifattener[] hattifatteners, Place place) {
        System.out.println("В " + place + " начался ритуал Боэтии");
        for (int i = 0; i < hattifatteners.length; i++) {
            if (hattifatteners[i].getDevotion() == Devotions.BOETHIAH && (hattifatteners[i].getCondition() == Conditions.ALIVE || hattifatteners[i].getCondition() == Conditions.UNDEAD))
                hattifatteners[i].killHat(hattifatteners[(int)(Math.random() * (hattifatteners.length -1))]);
        }
    }
}
