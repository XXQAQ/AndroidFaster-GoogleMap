package com.xq.androidfaster_amap.baselocation;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xq.projectdefine.FasterInterface;
import com.xq.projectdefine.base.abs.AbsPresenter;
import com.xq.projectdefine.base.abs.AbsPresenterDelegate;
import com.xq.projectdefine.base.abs.AbsView;
import com.xq.projectdefine.util.constant.PermissionConstants;
import com.xq.projectdefine.util.tools.PermissionUtils;

import java.util.List;

public interface IBaseLocationPresenter<T extends AbsView> extends AbsLocationPresenter<T>{

    @Override
    default void startLocation(){
        getLocationBuilder().startLocation();
    }

    @Override
    default AMapLocation getLocation() {
        return getLocationBuilder().getLocation();
    }

    @Override
    @Deprecated
    default void onReceiveLocation(AMapLocation location){
        getLocationBuilder().onReceiveLocation(location);
    }

    @Override
    default void afterReceiveLocation(AMapLocation location) {
        getLocationBuilder().afterReceiveLocation(location);
    }

    public LocationBuilder getLocationBuilder();

    public abstract class LocationBuilder<T extends AbsView> extends AbsPresenterDelegate<T> implements AbsLocationPresenter<T>{

        public AMapLocationClient locationClient;

        public AMapLocation location;

        public boolean isFirstLocation = true;

        public LocationBuilder(AbsPresenter presenter) {
            super(presenter);
        }

        @Override
        public void afterOnCreate(Bundle bundle) {
            //如果不使用自带权限方案，请处理权限后自行调用startLocation方法
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
        public void onResume() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onDestroy() {
            if (locationClient != null)
                locationClient.onDestroy();
        }

        @Override
        public void onActivityResult(int i, int i1, Intent intent) {

        }

        //开始定位
        public void startLocation(){
            //定位
            locationClient = new AMapLocationClient(FasterInterface.getApp());
            locationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation location) {
                    if (location.getErrorCode() == 0)
                    {
                        location = location;

                        onReceiveLocation(location);

                        if (isFirstLocation)
                            isFirstLocation = false;
                    }
                }
            });
            AMapLocationClientOption clientOption = new AMapLocationClientOption();
            clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            clientOption.setInterval(1000);
            clientOption.setNeedAddress(true);
            clientOption.setMockEnable(true);
            locationClient.setLocationOption(clientOption);
            locationClient.startLocation();
        }

        //获取定位
        public AMapLocation getLocation() {
            return location;
        }

        //该方法在接收到定位数据后调用，您需要忽略此方法，而选择重写afterReceiveLocation完成后续逻辑
        @Deprecated
        public void onReceiveLocation(AMapLocation location){
            afterReceiveLocation(location);
        }


    }



}
