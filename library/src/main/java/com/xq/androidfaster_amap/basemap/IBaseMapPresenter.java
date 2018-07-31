package com.xq.androidfaster_amap.basemap;


import com.amap.api.location.AMapLocation;
import com.xq.androidfaster_amap.baselocation.IBaseLocationPresenter;
import com.xq.projectdefine.base.abs.AbsPresenter;


public interface IBaseMapPresenter<T extends AbsMapView> extends AbsMapPresenter<T>,IBaseLocationPresenter<T> {

    @Override
    default LocationBuilder getLocationBuilder() {
        return getMapBuilder();
    }

    public MapBuilder getMapBuilder();

    public abstract class MapBuilder<T extends AbsMapView> extends LocationBuilder<T> implements AbsMapPresenter<T>{

        public MapBuilder(AbsPresenter presenter) {
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
