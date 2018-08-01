package com.xq.androidfaster_amap.basemap;


import com.amap.api.location.AMapLocation;
import com.xq.androidfaster_amap.baselocation.IBaseLocationPresenter;
import com.xq.projectdefine.base.abs.AbsPresenter;


public interface IBaseMapPresenter<T extends AbsMapView> extends AbsMapPresenter<T>,IBaseLocationPresenter<T> {

    @Override
    default LocationDelegate getLocationDelegate() {
        return getMapDelegate();
    }

    public MapDelegate getMapDelegate();

    public abstract class MapDelegate<T extends AbsMapView> extends LocationDelegate<T> implements AbsMapPresenter<T>{

        public MapDelegate(AbsPresenter presenter) {
            super(presenter);
        }

        @Override
        public void onReceiveLocation(AMapLocation location) {
            super.onReceiveLocation(getLocation());

            if (isFirstLocation)
                getBindView().moveMapToLocationPoint();
        }

    }

}
