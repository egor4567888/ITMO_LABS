package com.egor456788.interfaces;

import com.egor456788.common.ValuableActions;
import com.egor456788.entities.Entity;



public interface ScoreCounter {
    void count(ValuableActions action, Entity entity);
}
