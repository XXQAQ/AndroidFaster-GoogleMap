package com.xq.androidfaster_map.basemap;

import android.location.Location;

import com.xq.androidfaster.base.abs.IAbsPresenter;
import com.xq.androidfaster_map.baselocation.IBaseLocationPresenter;

public interface IBaseMapPresenter<T extends IBaseMapView> extends AbsMapPresenter<T>,IBaseLocationPresenter<T> {

    @Override
    default LocationDelegate getLocationDelegate() {
        return getMapDelegate();
    }

    public MapDelegate getMapDelegate();

    public abstract class MapDelegate<T extends IBaseMapView> extends LocationDelegate<T> implements AbsMapPresenter<T>{

        public MapDelegate(IAbsPresenter presenter) {
            super(presenter);
        }

        @Override
        protected void onLocationPermissionSuccess() {
            super.onLocationPermissionSuccess();
            getBindView().initLocationPoint();
        }

        @Override
        public void onReceiveLocation(Location location) {
            super.onReceiveLocation(getLocation());

            if (isFirstLocation)
                getBindView().moveMapToLocationPoint();
        }

    }

}
