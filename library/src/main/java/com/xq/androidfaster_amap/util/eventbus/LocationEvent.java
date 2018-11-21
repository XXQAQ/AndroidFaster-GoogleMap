package com.xq.androidfaster_amap.util.eventbus;


import android.location.Location;

import com.amap.api.location.AMapLocation;


public class LocationEvent {

    private Location location;

    public LocationEvent(AMapLocation location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(AMapLocation location) {
        this.location = location;
    }
}
