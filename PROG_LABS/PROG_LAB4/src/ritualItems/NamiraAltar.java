package ritualItems;

import common.Devotions;
import common.Place;
import entities.Hattifattener;
import exceptions.noCultistException;
import interfaces.GetIndex;

public class NamiraAltar extends RitualItem implements GetIndex {
    public NamiraAltar(String name) {
        super(name);
    }
    @Override
    public void ritual(Hattifattener[] hattifatteners, Place place) {
        System.out.println("В " + place + " начался ритуал Намиры");
        int ind[] = this.getIndex(hattifatteners, Devotions.NAMIRA);
        int hat1 = ind[0];
        int hat2 = ind[1];
        try {
            if (hat1 == hat2) {
                throw new noCultistException("Нет достуаных последователей, ритуал не удался.");
            }
            switch (hattifatteners[hat2].getCondition()) {
                case ALIVE, UNDEAD -> {
                    hattifatteners[hat1].killHat(hattifatteners[hat2]);
                    hattifatteners[hat1].eatHat(hattifatteners[hat2]);
                }
                default -> hattifatteners[hat1].eatHat(hattifatteners[hat2]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
