package com.xq.androidfaster_amap.basemap;

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

public interface IBaseLocationPresenter<T extends AbsView> extends AbsPresenter<T>{

    @Override
    default void afterOnCreate(Bundle bundle) {

        //如果不使用自带权限，请自行处理群贤后调用startLocation方法
        if (FasterInterface.isIsAutoPermission())
        {
            PermissionUtils.permission(PermissionConstants.LOCATION)
                    .callback(new PermissionUtils.FullCallback() {
                        @Override
                        public void onGranted(List<String> permissionsGranted) {
                            startLocation();
                        }

                        @Override
                        public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        }
                    })
                    .request();
        }
    }

    @Override
    default void onResume() {

    }

    @Override
    default void onPause() {

    }

    @Override
    default void onDestroy() {
        if (getLocationBuilder().locationClient != null)
            getLocationBuilder().locationClient.onDestroy();
    }

    @Override
    default void onActivityResult(int i, int i1, Intent intent) {

    }

    //开始定位
    default void startLocation(){
        //定位
        getLocationBuilder().locationClient = new AMapLocationClient(FasterInterface.getApp());
        getLocationBuilder().locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                if (location.getErrorCode() == 0)
                {
                    getLocationBuilder().location = location;

                    afterReceiveLocation(location);

                    if (getLocationBuilder().isFirstLocation)
                        getLocationBuilder().isFirstLocation = false;
                }
            }
        });
        AMapLocationClientOption clientOption = new AMapLocationClientOption();
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        clientOption.setInterval(1000);
        clientOption.setNeedAddress(true);
        clientOption.setMockEnable(true);
        getLocationBuilder().locationClient.setLocationOption(clientOption);
        getLocationBuilder().locationClient.startLocation();
    }

    //获取定位
    default AMapLocation getLocation() {
        return getLocationBuilder().location;
    }

    //该方法在接收到定位数据后调用，重写该方法完成后续逻辑
    public abstract void afterReceiveLocation(AMapLocation location);

    public LocationBuilder getLocationBuilder();

    public static class LocationBuilder{

        public AMapLocationClient locationClient;

        public AMapLocation location;

        public boolean isFirstLocation = true;
    }



}
