package com.xq.androidfaster_amap.util.eventbus;


import android.location.Location;


public class LocationEvent {

    private Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
