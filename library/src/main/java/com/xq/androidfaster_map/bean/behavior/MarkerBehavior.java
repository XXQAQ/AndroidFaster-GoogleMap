package com.xq.androidfaster_map.bean.behavior;

import com.xq.worldbean.bean.behavior.BaseBehavior;
import com.xq.worldbean.bean.behavior.CoordinateBehavior;
import com.xq.worldbean.bean.behavior.TitleBehavior;

public interface MarkerBehavior<T extends MarkerBehavior> extends BaseBehavior<T>, CoordinateBehavior<T>, TitleBehavior<T> {

    @Override
    default double getX() {
        return getLongitude();
    }

    @Override
    default double getY() {
        return getLatitude();
    }

    @Override
    default double getZ() {
        return 0;
    }

    public double getLatitude();

    public T setLatitude(double latitude);

    public double getLongitude();

    public T setLongitude(double longitude);

}
