package interfaces;

import common.Conditions;
import entities.Hattifattener;
import exceptions.DeadException;

public interface KillHat {
    default     public void killHat(Hattifattener hattifattener){
        try {
            if  ((this.getCondition() != Conditions.ALIVE && this.getCondition() != Conditions.UNDEAD))
                throw new DeadException(this + " не может никого убить потому что он " + this.getCondition());
            if  ((hattifattener.getCondition() != Conditions.ALIVE && hattifattener.getCondition() != Conditions.UNDEAD))
                throw new DeadException("У " + this + " не получилось убить " + hattifattener + " потому что " + hattifattener + " " + hattifattener.getCondition());
            System.out.println(this + " убил " + hattifattener);
            hattifattener.setCondition(Conditions.DEAD);
        }
        catch (DeadException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public Conditions getCondition();
}
