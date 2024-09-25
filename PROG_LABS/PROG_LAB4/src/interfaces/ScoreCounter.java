package interfaces;

import common.ValuableActions;
import entities.Entity;



public interface ScoreCounter {
    void count(ValuableActions action, Entity entity);
}
