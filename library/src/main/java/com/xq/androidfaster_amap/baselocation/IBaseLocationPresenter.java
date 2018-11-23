package com.xq.androidfaster_amap.baselocation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
        getLocationDelegate().startLocation();
    }

    @Override
    default Location getLocation() {
        return getLocationDelegate().getLocation();
    }

    public LocationDelegate getLocationDelegate();

    public abstract class LocationDelegate<T extends AbsView> extends AbsPresenterDelegate<T> implements AbsLocationPresenter<T>{

        public GoogleApiClient locationClient;

        public Location location;

        public boolean isFirstLocation = true;

        public LocationDelegate(AbsPresenter presenter) {
            super(presenter);
        }

        @Override
        public void afterOnCreate(Bundle bundle) {
            //如果不使用自带权限方案，请在处理权限后自行调用startLocation方法
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
                locationClient.disconnect();
        }

        @Override
        public void onActivityResult(int i, int i1, Intent intent) {

        }

        //开始定位
        public void startLocation(){
            //定位
            locationClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            LocationRequest mLocationRequest = LocationRequest.create();
                            mLocationRequest.setInterval(2000);
                            mLocationRequest.setFastestInterval(1000);
                            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                            LocationServices.FusedLocationApi.requestLocationUpdates(locationClient, mLocationRequest, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    LocationDelegate.this.location = location;

                                    onReceiveLocation(location);

                                    if (isFirstLocation)
                                        isFirstLocation = false;
                                }
                            });
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    })
                    .addApi(LocationServices.API)
                    .build();
            locationClient.connect();
        }

        //获取定位
        public Location getLocation() {
            return location;
        }

        //该方法在接收到定位数据后调用，您需要忽略此方法，而选择重写afterReceiveLocation完成后续逻辑
        @Deprecated
        protected void onReceiveLocation(Location location){
            afterReceiveLocation(location);
        }

        //该方法在onReceiveLocation调用，重写该方法完成后续逻辑
        protected abstract void afterReceiveLocation(Location location);
    }



}
