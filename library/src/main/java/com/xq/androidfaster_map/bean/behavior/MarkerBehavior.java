package com.xq.androidfaster_map.bean.behavior;

import android.os.Parcel;
import com.xq.worldbean.bean.behavior.CoordinateBehavior;
import com.xq.worldbean.bean.behavior.TitleBehavior;

public interface MarkerBehavior extends CoordinateBehavior, TitleBehavior {

    @Override
    default int describeContents(){
        return 0;
    }

    @Override
    default void writeToParcel(Parcel dest, int flags){

    }

    @Override
    default int getId() {
        return 0;
    }

    @Override
    default Object getTag(){
        return null;
    }

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

    public double getLongitude();

}
