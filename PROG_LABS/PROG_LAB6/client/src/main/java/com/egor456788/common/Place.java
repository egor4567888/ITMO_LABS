package com.egor456788.common;

import java.util.Objects;

public class Place {
    private final String place;
    private final float latitude;
    private final float longitude;
    public Place(String place, float latitude, float longitude){
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        return place + " координаты - " + "(" + latitude + "; " + longitude + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Place place1 = (Place) object;
        return Float.compare(latitude, place1.latitude) == 0 && Float.compare(longitude, place1.longitude) == 0 && Objects.equals(place, place1.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, latitude, longitude);
    }
}
