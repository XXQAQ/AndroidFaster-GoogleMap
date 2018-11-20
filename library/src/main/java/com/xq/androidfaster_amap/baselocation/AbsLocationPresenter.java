package com.xq.androidfaster_amap.baselocation;


import android.location.Location;
import com.xq.projectdefine.base.abs.AbsPresenter;
import com.xq.projectdefine.base.abs.AbsView;


public interface AbsLocationPresenter<T extends AbsView> extends AbsPresenter<T>{

    //开始定位
    public void startLocation();

    //获取定位
    public Location getLocation();

}
