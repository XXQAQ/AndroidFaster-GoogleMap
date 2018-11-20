package com.xq.androidfaster_amap.basemap;


import android.location.Location;
import com.xq.androidfaster_amap.baselocation.IBaseLocationPresenter;
import com.xq.projectdefine.base.abs.AbsPresenter;


public interface IBaseMapPresenter<T extends IBaseMapView> extends AbsMapPresenter<T>,IBaseLocationPresenter<T> {

    @Override
    default LocationDelegate getLocationDelegate() {
        return getMapDelegate();
    }

    public MapDelegate getMapDelegate();

    public abstract class MapDelegate<T extends IBaseMapView> extends LocationDelegate<T> implements AbsMapPresenter<T>{

        public MapDelegate(AbsPresenter presenter) {
            super(presenter);
        }

        @Override
        public void onReceiveLocation(Location location) {
            super.onReceiveLocation(getLocation());

            if (isFirstLocation)
                getBindView().moveMapToLocationPoint();
        }

    }

}
