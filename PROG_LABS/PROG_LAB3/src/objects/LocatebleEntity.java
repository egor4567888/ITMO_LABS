package objects;

import common.Places;
import interfaces.Locateble;

public class LocatebleEntity extends Entity implements Locateble {
    public LocatebleEntity(String name){
        super(name);
    }
    private String location;
    @Override
    public void setLocation(Places pl) {
        this.location = pl.getLocation();
    }

    @Override
    public String getLocation() {
        return this.location;
    }
}
