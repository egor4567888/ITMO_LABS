package interfaces;

import common.Conditions;
import entities.Hattifattener;
import exceptions.DeadException;

public interface EatHat {
    default public void eatHat(Hattifattener hattifattener){
        try{
            if  ((this.getCondition() != Conditions.ALIVE && this.getCondition() != Conditions.UNDEAD))
                throw new DeadException(this + " не может никого съесть потому что он " + this.getCondition());
            if (hattifattener.getCondition() != Conditions.DEAD)
                throw new DeadException("У " + this + " не получилось съесть " + hattifattener + " потому что " + hattifattener + " " + hattifattener.getCondition());
            System.out.println(this + " съел " + hattifattener);
            hattifattener.setCondition(Conditions.EATEN);
            }
        catch (DeadException ex) {
            System.out.println(ex.getMessage());
        }
    }

    Conditions getCondition();
}
