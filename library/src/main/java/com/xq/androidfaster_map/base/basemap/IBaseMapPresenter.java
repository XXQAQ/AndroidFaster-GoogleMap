package com.xq.androidfaster_map.base.basemap;

import android.location.Location;

import com.xq.androidfaster.base.abs.IAbsPresenter;
import com.xq.androidfaster_map.base.baselocation.IBaseLocationPresenter;

public interface IBaseMapPresenter<T extends IBaseMapView> extends IAbsMapPresenter<T>,IBaseLocationPresenter<T> {

    @Override
    default LocationDelegate getLocationDelegate() {
        return getMapDelegate();
    }

    public MapDelegate getMapDelegate();

    public abstract class MapDelegate<T extends IBaseMapView> extends LocationDelegate<T> implements IAbsMapPresenter<T> {

        public MapDelegate(IAbsPresenter presenter) {
            super(presenter);
        }

        @Override
        public void onReceiveLocation(Location location) {
            super.onReceiveLocation(location);
            if (isFirstLocation)
                getBindView().moveMapToLocationPoint(100);
        }

    }

}
