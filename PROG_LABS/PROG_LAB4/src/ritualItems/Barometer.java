package ritualItems;

import common.Devotions;
import common.Place;
import entities.Hattifattener;
import exceptions.noCultistException;
import interfaces.GetIndex;

public class Barometer extends RitualItem implements GetIndex {

    public Barometer(String name){
        super(name);
    }
    @Override
    public void ritual(Hattifattener[] hattifatteners, Place place){
        System.out.println("В " + place + " начался ритуал барометра");
        int ind[] = this.getIndex(hattifatteners, Devotions.BAROMETER);
        int hat1 = ind[0];
        int hat2 = ind[1];
        try {
            if (hat1 == hat2) {
                throw new noCultistException("Нет достуаных последователей, ритуал не удался.");
            } else {
                switch (hattifatteners[hat2].getDevotion()) {
                    case Devotions.BAROMETER -> {
                        hattifatteners[hat1].kneelHat(hattifatteners[hat2]);
                        hattifatteners[hat2].kneelHat(hattifatteners[hat1]);
                        hattifatteners[hat1].kneel(getName());
                        hattifatteners[hat2].kneel(getName());
                    }
                    case Devotions.LIGHT -> {
                        hattifatteners[hat1].kneelHat(hattifatteners[hat2]);
                        hattifatteners[hat2].kneelHat(hattifatteners[hat1]);
                        hattifatteners[hat1].kneel(getName());
                    }
                    case Devotions.BOETHIAH -> {
                        hattifatteners[hat1].kneelHat(hattifatteners[hat2]);
                        hattifatteners[hat2].kneelHat(hattifatteners[hat1]);
                        hattifatteners[hat1].kneel(getName());
                        hattifatteners[hat2].killHat(hattifatteners[hat1]);
                    }
                    default -> {
                        hattifatteners[hat1].kneelHat(hattifatteners[hat2]);
                        hattifatteners[hat2].scornfulGlanceHat(hattifatteners[hat1]);
                        hattifatteners[hat1].kneel(getName());
                        hattifatteners[hat2].scornfulGlance(getName());
                    }
                }
            }
        } catch (noCultistException e) {
            System.out.println(e.getMessage());
        }
    }
}
