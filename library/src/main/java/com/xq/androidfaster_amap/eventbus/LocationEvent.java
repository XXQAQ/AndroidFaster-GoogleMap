package com.xq.androidfaster_amap.eventbus;


import com.amap.api.location.AMapLocation;


public class LocationEvent {

    private AMapLocation location;

    public LocationEvent(AMapLocation location) {
        this.location = location;
    }

    public AMapLocation getLocation() {
        return location;
    }

    public void setLocation(AMapLocation location) {
        this.location = location;
    }
}
