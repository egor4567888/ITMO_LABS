package com.egor456788.interfaces;

import com.egor456788.common.ValuableActions;
import com.egor456788.entities.Entity;

import java.io.Serializable;


public interface ScoreCounter extends Serializable {
    void count(ValuableActions action, Entity entity);
}
