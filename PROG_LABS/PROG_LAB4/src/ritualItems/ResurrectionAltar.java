package ritualItems;

import common.Conditions;
import common.Devotions;
import common.Place;
import entities.Hattifattener;

public class ResurrectionAltar extends RitualItem{
    public ResurrectionAltar(String name) {
        super(name);
    }

    @Override
    public void ritual(Hattifattener[] hattifatteners, Place place) {
        System.out.println("В " + place + " начался ритуал воскрешения");
        for (int i = 0; i < hattifatteners.length; i++) {
            if (hattifatteners[i].getCondition() == Conditions.DEAD)
                if (hattifatteners[i].getDevotion() == Devotions.LIGHT)
                    hattifatteners[i].setCondition(Conditions.ALIVE);
                else hattifatteners[i].setCondition(Conditions.UNDEAD);
        }
    }
}
