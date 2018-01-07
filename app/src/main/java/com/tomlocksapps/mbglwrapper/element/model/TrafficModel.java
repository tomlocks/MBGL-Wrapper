package com.tomlocksapps.mbglwrapper.element.model;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomlo on 26.12.2017.
 */

public class TrafficModel {
    private final List<Location> locationList = new ArrayList<>();
    private final Type type;

    public TrafficModel(Type type) {
        this.type = type;
    }

    public void addLocation(Location location) {
        this.locationList.add(location);
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        LIGHT, MODERATE, HEAVY
    }

}
