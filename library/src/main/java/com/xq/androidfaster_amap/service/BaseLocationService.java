package com.xq.androidfaster_amap.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.xq.androidfaster.util.tools.BundleUtil;

public class BaseLocationService extends Service {

    public static final String ACTION_LOCATION = "com.xq.androidfaster_map.service.BaseLocationService";

    public GoogleApiClient locationClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient != null)
            locationClient.disconnect();
    }

    public void startLocation(){
        //定位
        locationClient = new GoogleApiClient.Builder(getBaseContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LocationRequest mLocationRequest = LocationRequest.create();
                        mLocationRequest.setInterval(1000);
                        mLocationRequest.setFastestInterval(1000);
                        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                        LocationServices.FusedLocationApi.requestLocationUpdates(locationClient, mLocationRequest, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                Intent intent = new Intent();
                                intent.setAction(ACTION_LOCATION);
                                intent.putExtras(new BundleUtil.Builder().putParcelable(BundleUtil.KEY_DATA,location).build());
                                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent) ;
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

}
