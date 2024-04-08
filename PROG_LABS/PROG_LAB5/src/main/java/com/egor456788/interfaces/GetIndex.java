package com.egor456788.interfaces;

import com.egor456788.common.Conditions;
import com.egor456788.common.Devotions;
import com.egor456788.entities.Entity;

public interface GetIndex {
    default int[] getIndex(Entity[] hattifatteners, Devotions devotion){
        boolean someoneAlive = false;
        int n = hattifatteners.length;
        int [] ind = new int[2];
        for (int i = 0; i < n; ++i)
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
    }
}
