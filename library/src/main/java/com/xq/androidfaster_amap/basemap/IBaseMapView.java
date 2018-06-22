package com.xq.androidfaster_amap.basemap;


import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.Path;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.xq.androidfaster_amap.bean.behavior.MarkBehavior;
import com.xq.androidfaster_amap.util.overlay.BusRouteOverlay;
import com.xq.androidfaster_amap.util.overlay.DrivingRouteOverlay;
import com.xq.androidfaster_amap.util.overlay.RideRouteOverlay;
import com.xq.androidfaster_amap.util.overlay.RouteOverlay;
import com.xq.androidfaster_amap.util.overlay.WalkRouteOverlay;
import com.xq.projectdefine.base.abs.AbsView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xq on 2017/4/11 0011.
 */

public interface IBaseMapView<T extends IBaseMapPresenter> extends AbsView<T> {

    @Override
    default void afterOnCreate(Bundle savedInstanceState) {

        getMapBuilder().mapView = (TextureMapView) getRootView().findViewById(getContext().getResources().getIdentifier("mapView", "id", getContext().getPackageName()));

        getMapBuilder().mapView.onCreate(savedInstanceState);

        getMapBuilder().map = getMapBuilder().mapView.getMap();

        //定位
        getMapBuilder().locationClient = new AMapLocationClient(getContext().getApplicationContext());
        getMapBuilder().locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                getPresenter().onReceiveLocation(location);
                if (getMapBuilder().isFirstLocation)
                {
                    getMapBuilder().isFirstLocation = false;
                    resumeLocationPoint();
                }
            }
        });
        AMapLocationClientOption clientOption = new AMapLocationClientOption();
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        clientOption.setInterval(1000);
        clientOption.setNeedAddress(true);
        clientOption.setMockEnable(true);
        getMapBuilder().locationClient.setLocationOption(clientOption);
        getMapBuilder().locationClient.startLocation();

        //定位点初始化
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(getLocationIcon()));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(getLocationRadiusColor());// 设置圆形的填充颜色
        getMapBuilder().map.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        getMapBuilder().map.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //map相关设置
        AMap.InfoWindowAdapter adapter = new AMap.ImageInfoWindowAdapter() {
            @Override
            public long getInfoWindowUpdateTime() {
                return 0;
            }

            @Override
            public View getInfoWindow(Marker marker) {
                MarkBehavior behavior = (MarkBehavior) marker.getObject();
                return getWindowView(behavior);
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        };
        getMapBuilder().map.setInfoWindowAdapter(adapter);

        //map相关监听

        getMapBuilder().map.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                afterMapClick(latLng);
            }
        });

        getMapBuilder().map.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                beforeMarkerClick(marker);

                marker.showInfoWindow();

                getMapBuilder().lastMarker = marker;

                afterMarkerClick(marker);

                return true;
            }
        });

        getMapBuilder().map.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                afterMapStatusChangeFinish(cameraPosition);
            }
        });

        getMapBuilder().routeSearch = new RouteSearch(getContext());
        getMapBuilder().routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult result, int errorCode) {
                myRouteSearch(result,errorCode);
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
                myRouteSearch(result,errorCode);
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
                myRouteSearch(result,errorCode);
            }

            @Override
            public void onRideRouteSearched(RideRouteResult result, int errorCode) {
                myRouteSearch(result,errorCode);
            }

            public void myRouteSearch(RouteResult result, int errorCode){

                List list_path = null;
                if (result instanceof WalkRouteResult)
                {
                    list_path = ((WalkRouteResult) result).getPaths();
                }
                else    if (result instanceof BusRouteResult)
                {
                    list_path = ((BusRouteResult) result).getPaths();
                }
                else    if (result instanceof DriveRouteResult)
                {
                    list_path = ((DriveRouteResult) result).getPaths();
                }
                else    if (result instanceof RideRouteResult)
                {
                    list_path = ((RideRouteResult) result).getPaths();
                }

                if (errorCode == AMapException.CODE_AMAP_SUCCESS)
                {
                    if (result != null && list_path != null)
                    {
                        if (list_path.size() > 0)
                        {
                            final Path path = (Path) list_path.get(0);

                            RouteOverlay overlay = null;
                            if (result instanceof WalkRouteResult)
                            {
                                overlay = new WalkRouteOverlay(getContext(), getMapBuilder().map, (WalkPath) path, result.getStartPos(), result.getTargetPos());
                            }
                            else    if (result instanceof BusRouteResult)
                            {
                                overlay = new BusRouteOverlay(getContext(), getMapBuilder().map, (BusPath) path, result.getStartPos(), result.getTargetPos());
                            }
                            else    if (result instanceof DriveRouteResult)
                            {
                                overlay = new DrivingRouteOverlay(getContext(), getMapBuilder().map, (DrivePath) path, result.getStartPos(), result.getTargetPos(),null);
                            }
                            else    if (result instanceof RideRouteResult)
                            {
                                overlay = new RideRouteOverlay(getContext(), getMapBuilder().map, (RidePath) path, result.getStartPos(), result.getTargetPos());
                            }

                            overlay.removeFromMap();
                            overlay.addToMap();
                            overlay.zoomToSpan();

                            getMapBuilder().lastOverlay = overlay;

                            afterGetRouteSuccess(result);

                        }
                        else    if (result != null && list_path == null)
                        {
                            Toast.makeText(getContext(),"无路线规划结果",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(getContext(),"无路线规划结果",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"路线规划失败",Toast.LENGTH_SHORT).show();
                }

                afterGetRouteFinish(result,errorCode);

            }
        });
    }

    @Override
    default void onResume() {
        getMapBuilder().mapView.onResume();
    }

    @Override
    default void onPause() {
        getMapBuilder().mapView.onPause();
    }

    @Override
    default void onDestroy() {
        getMapBuilder().mapView.onDestroy();
        getMapBuilder().locationClient.onDestroy();
    }

    @Override
    default void onSaveInstanceState(Bundle outState) {
        getMapBuilder().mapView.onSaveInstanceState(outState);
    }

    default List<Marker> setMarks(List<MarkBehavior> list){

        List<Marker> list_mark = new LinkedList<>();

        for (MarkBehavior behavior : list)
        {
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.period(200);
            markerOption.position(new LatLng(behavior.getLatitude(),behavior.getLongitude()));
            markerOption.title(behavior.getTitle()).snippet(behavior.getLittleTitle());
            markerOption.draggable(false);//设置Marker可拖动
            markerOption.icons(getMarkerDescript(behavior));
            markerOption.setFlat(false);//设置marker平贴地图效果

            Marker marker = getMapBuilder().map.addMarker(markerOption);
            marker.setObject(behavior);

            Animation animation = new ScaleAnimation(0,1,0,1);
            animation.setDuration(500);
            animation.setInterpolator(new AccelerateInterpolator());
            marker.setAnimation(animation);
            marker.startAnimation();

            list_mark.add(marker);
        }

        return list_mark;

    }

    default void clearMark(final List<Marker> list) {

        for (Marker marker : list)
        {
            Animation animation = new AlphaAnimation(1,0);
            animation.setDuration(500);
            animation.setInterpolator(new LinearInterpolator());
            marker.setAnimation(animation);
            marker.startAnimation();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (final Marker marker : list)
                {
                    marker.remove();
                }
            }
        },500);
    }

    default void clearMap() {
        getMapBuilder().map.clear(true);
    }

    default void walk(LatLng latLng_from, LatLng latLng_to) {
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(latLng_from.latitude,latLng_from.longitude),new LatLonPoint(latLng_to.latitude,latLng_to.longitude)));
        getMapBuilder().routeSearch.calculateWalkRouteAsyn(query);
    }

    default void traffic(LatLng latLng_from, LatLng latLng_to, String city) {
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(latLng_from.latitude,latLng_from.longitude),new LatLonPoint(latLng_to.latitude,latLng_to.longitude)), RouteSearch.BUS_DEFAULT, city,1);
        getMapBuilder().routeSearch.calculateBusRouteAsyn(query);
    }

    default void driver(LatLng latLng_from, LatLng latLng_to) {
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(latLng_from.latitude,latLng_from.longitude),new LatLonPoint(latLng_to.latitude,latLng_to.longitude)), RouteSearch.WALK_DEFAULT, null, null, "");
        getMapBuilder().routeSearch.calculateDriveRouteAsyn(query);
    }

    default void removeLastLines() {
        if (getMapBuilder().lastOverlay != null)
        {
            getMapBuilder().lastOverlay.removeFromMap();
            getMapBuilder().lastOverlay = null;
        }
    }

    default void hideInfoWindow() {
        if (getMapBuilder().lastMarker != null)
        {
            getMapBuilder().lastMarker.hideInfoWindow();
        }
    }

    //回到定位点
    default void resumeLocationPoint(){

        if (getPresenter().getLocation() != null)
        {
            getMapBuilder().map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getPresenter().getLocation().getLatitude(),getPresenter().getLocation().getLongitude()),17));
        }
        else
        {
            showGetLocationErro();
        }

    }

    default double[][] getMapArea() {

        Projection projection = getMapBuilder().map.getProjection();

        LatLng topLeft = projection.fromScreenLocation(new Point(0,0));
        LatLng bottomRight = projection.fromScreenLocation(new Point(getMapBuilder().mapView.getWidth(),getMapBuilder().mapView.getHeight()));

        return new double[][]{new double[]{topLeft.latitude,topLeft.longitude},new double[]{bottomRight.latitude,bottomRight.longitude}};
    }

    public abstract void showGetLocationErro();

    public abstract int getLocationIcon();

    public abstract int getLocationRadiusColor();

    public abstract ArrayList<BitmapDescriptor> getMarkerDescript(MarkBehavior behavior);

    //可以根据具体对象设置弹窗样式
    public abstract View getWindowView(MarkBehavior behavior);

    //标记点击后调用，以处理更多程序员自己的逻辑
    public abstract void afterMarkerClick(Marker marker);

    public abstract void beforeMarkerClick(Marker marker);

    //地图状态改变后调用，以处理更多程序员自己的逻辑
    public abstract void afterMapStatusChangeFinish(CameraPosition cameraPosition);

    //点击地图后调用
    public abstract void afterMapClick(LatLng latLng);

    public abstract void afterGetRouteSuccess(RouteResult result);

    //无论路线规划成功还是失败，最终都会回调的方法
    public abstract void afterGetRouteFinish(RouteResult result, int erroCode);


    public MapBuilder getMapBuilder();

    public static class MapBuilder{
        public TextureMapView mapView ;

        public AMap map;

        public AMapLocationClient locationClient;

        public Marker lastMarker;

        public RouteSearch routeSearch;
        public RouteOverlay lastOverlay;

        public boolean isFirstLocation = true;
    }

}