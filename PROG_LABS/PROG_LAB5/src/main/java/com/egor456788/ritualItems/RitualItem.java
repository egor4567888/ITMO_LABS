package com.egor456788.ritualItems;

import com.egor456788.common.Place;
import com.egor456788.common.RitualItems;
import com.egor456788.entities.Entity;
import com.egor456788.entities.Hattifattener;

import java.util.LinkedHashSet;
import java.util.Objects;

public abstract class RitualItem {
    private final String name;
    private final RitualItems type;
    protected RitualItem(String name, RitualItems type){
        this.name = name;
        this.type = type;
    }
    public abstract void ritual(LinkedHashSet<Entity> hattifatteners, Place place);

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

    public RitualItems getType() { return type;
    }
}
