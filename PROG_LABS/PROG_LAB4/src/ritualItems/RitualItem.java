package ritualItems;

import common.Place;
import entities.Hattifattener;

import java.util.Objects;

public abstract class RitualItem {
    private final String name;
    protected RitualItem(String name){
        this.name = name;
    }
    public abstract void ritual(Hattifattener[] hattifatteners, Place place);

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RitualItem that = (RitualItem) object;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
