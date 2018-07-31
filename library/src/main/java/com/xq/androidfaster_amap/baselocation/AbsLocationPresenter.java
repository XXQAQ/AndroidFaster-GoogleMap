package com.xq.androidfaster_amap.baselocation;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xq.projectdefine.FasterInterface;
import com.xq.projectdefine.base.abs.AbsPresenter;
import com.xq.projectdefine.base.abs.AbsView;
import com.xq.projectdefine.util.constant.PermissionConstants;
import com.xq.projectdefine.util.tools.PermissionUtils;

import java.util.List;

public interface AbsLocationPresenter<T extends AbsView> extends AbsPresenter<T>{

    //开始定位
    public void startLocation();

    //获取定位
    public AMapLocation getLocation();

    //该方法在接收到定位数据后调用，您需要忽略此方法，而选择重写afterReceiveLocation完成后续逻辑
    @Deprecated
    public void onReceiveLocation(AMapLocation location);

    //该方法在onReceiveLocation调用，重写该方法完成后续逻辑
    public abstract void afterReceiveLocation(AMapLocation location);


}
