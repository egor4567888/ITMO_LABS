package common;

public enum ValuableActions {
    KILL("убить"),
    EAT("съесть"),
    KNEEL("поклониться"),
    COLLECT("собрать");
    private String valuableAction;
    ValuableActions(String valuebleAction){
        this.valuableAction = valuebleAction;
    }
    @Override
    public String toString() {
        return valuableAction;
    }
}
