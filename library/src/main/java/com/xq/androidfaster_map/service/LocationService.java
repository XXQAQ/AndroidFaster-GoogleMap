package com.xq.androidfaster_map.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import com.xq.androidfaster.util.tools.BundleUtil;
import com.xq.androidfaster.util.tools.LocationUtils;

public class LocationService extends Service {

    public static final String ACTION_LOCATION = "com.xq.androidfaster_map.service.LocationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        LocationUtils.register(1000, 0, new LocationUtils.OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {
                Intent intent = new Intent();
                intent.setAction(ACTION_LOCATION);
                intent.putExtras(new BundleUtil.Builder().putParcelable(BundleUtil.KEY_DATA,location).build());
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent) ;
            }

            @Override
            public void onLocationChanged(Location location) {
                Intent intent = new Intent();
                intent.setAction(ACTION_LOCATION);
                intent.putExtras(new BundleUtil.Builder().putParcelable(BundleUtil.KEY_DATA,location).build());
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent) ;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationUtils.unregister();
    }

}
