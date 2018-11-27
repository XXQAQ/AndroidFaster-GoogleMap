package com.xq.androidfaster_map.bean.behavior;

import java.io.Serializable;

public interface MarkBehavior extends Serializable{

    public double getLatitude();

    public double getLongitude();

    public String getTitle();

    public String getLittleTitle();

}
