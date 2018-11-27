package com.xq.androidfaster_map.baselocation;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.xq.androidfaster.FasterInterface;
import com.xq.androidfaster.base.abs.AbsPresenterDelegate;
import com.xq.androidfaster.base.abs.IAbsPresenter;
import com.xq.androidfaster.base.abs.IAbsView;
import com.xq.androidfaster.util.constant.PermissionConstants;
import com.xq.androidfaster.util.tools.BundleUtil;
import com.xq.androidfaster.util.tools.PermissionUtils;
import com.xq.androidfaster_map.service.BaseLocationService;
import java.util.List;

import static com.xq.androidfaster_map.service.BaseLocationService.ACTION_LOCATION;

public interface IBaseLocationPresenter<T extends IAbsView> extends AbsLocationPresenter<T>{

    @Override
    default void start(){
        getLocationDelegate().start();
    }

    @Override
    default Location getLocation() {
        return getLocationDelegate().getLocation();
    }

    public LocationDelegate getLocationDelegate();

    public abstract class LocationDelegate<T extends IAbsView> extends AbsPresenterDelegate<T> implements AbsLocationPresenter<T>{

        public Location location;

        public boolean isFirstLocation = true;

        protected BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_LOCATION.equals(intent.getAction()))
                {
                    Location location = intent.getExtras().getParcelable(BundleUtil.KEY_DATA);
                    LocationDelegate.this.location = location;
                    onReceiveLocation(location);
                    if (isFirstLocation)
                        isFirstLocation = false;
                }
            }
        };

        public LocationDelegate(IAbsPresenter presenter) {
            super(presenter);
        }

        @Override
        public void afterOnCreate(Bundle bundle) {
            super.afterOnCreate(bundle);

            LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,new IntentFilter(ACTION_LOCATION));

            //如果不使用自带权限方案，请在处理权限后自行调用start方法
            if (FasterInterface.isIsAutoPermission())
            {
                PermissionUtils.permission(PermissionConstants.LOCATION)
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> permissionsGranted) {
                                start();
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
            super.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        }

        //开始定位
        public void start(){
            getContext().startService(new Intent(getContext(),BaseLocationService.class));
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
