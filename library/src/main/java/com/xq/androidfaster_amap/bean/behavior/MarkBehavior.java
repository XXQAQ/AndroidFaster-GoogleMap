package com.xq.androidfaster_amap.bean.behavior;

import java.io.Serializable;

/**
 * Created by xq on 2017/6/30.
 */

public interface MarkBehavior extends Serializable{

    public double getLatitude();

    public double getLongitude();

    public String getTitle();

    public String getLittleTitle();

}
