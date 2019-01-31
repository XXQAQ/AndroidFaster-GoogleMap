package com.xq.androidfaster_map.bean.behavior;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public interface MarkBehavior extends Serializable,Parcelable {

    @Override
    default int describeContents(){
        return 0;
    }

    @Override
    default void writeToParcel(Parcel dest, int flags){

    }

    public double getLatitude();

    public double getLongitude();

    public String getTitle();

    default Object getTag(){
        return null;
    }

}
