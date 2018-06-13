package com.xq.androidfaster_amap.event;


import com.amap.api.location.AMapLocation;

/**
 * Created by Administrator on 2017/9/22.
 */

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
