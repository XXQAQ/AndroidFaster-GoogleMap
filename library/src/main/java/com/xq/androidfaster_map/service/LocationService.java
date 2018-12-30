package com.xq.androidfaster_map.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.xq.androidfaster.util.tools.BundleUtil;

public class LocationService extends Service {

    public static final String ACTION_LOCATION = "com.xq.androidfaster_map.service.LocationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected FusedLocationProviderClient locationClient;
    protected LocationCallback locationCallback;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000); // two minute interval
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();

                Intent intent = new Intent();
                intent.setAction(ACTION_LOCATION);
                intent.putExtras(new BundleUtil.Builder().putParcelable(BundleUtil.KEY_DATA,location).build());
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent) ;
            }
        };

        locationClient.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.removeLocationUpdates(locationCallback);
        locationCallback=null;
    }

}
