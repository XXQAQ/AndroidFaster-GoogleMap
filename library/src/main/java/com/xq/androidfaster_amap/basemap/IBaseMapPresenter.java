package com.xq.androidfaster_amap.basemap;


import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;


public interface IBaseMapPresenter<T extends IBaseMapView> extends IBaseLocationPresenter<T> {

    @Override
    default void afterOnCreate(Bundle bundle) {
        IBaseLocationPresenter.super.afterOnCreate(bundle);
    }

    @Override
    default void onResume() {
        IBaseLocationPresenter.super.onResume();
    }

    @Override
    default void onPause() {
        IBaseLocationPresenter.super.onPause();
    }

    @Override
    default void onDestroy() {
        IBaseLocationPresenter.super.onDestroy();
    }

    @Override
    default void onActivityResult(int i, int i1, Intent intent) {
        IBaseLocationPresenter.super.onActivityResult(i,i1,intent);
    }

    @Override
    default void afterReceiveLocation(AMapLocation location) {
        if (getLocationBuilder().isFirstLocation)
            getBindView().moveMapToLocationPoint();
    }

}
