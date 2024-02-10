package entities;

import common.Conditions;

import java.util.Objects;

abstract class Entity {
    final private String name;
    private Conditions condition = Conditions.ALIVE;

    public Entity(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
    public void setCondition(Conditions condition){
        this.condition = condition;
        System.out.println(this + " теперь " + this.condition);
    }
    public Conditions getCondition(){
        return condition;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Entity entity = (Entity) object;
        return Objects.equals(name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
