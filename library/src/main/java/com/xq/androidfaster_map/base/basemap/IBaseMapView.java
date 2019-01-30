package com.xq.androidfaster_map.base.basemap;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.xq.androidfaster.base.abs.AbsViewDelegate;
import com.xq.androidfaster.base.abs.IAbsView;
import com.xq.androidfaster.util.constant.PermissionConstants;
import com.xq.androidfaster.util.tools.PermissionUtils;
import com.xq.androidfaster.util.tools.ScreenUtils;
import com.xq.androidfaster_map.bean.behavior.MarkBehavior;
import com.xq.androidfaster_map.bean.entity.MarkBean;
import com.xq.androidfaster_map.util.googlemap.GoogleMapUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface IBaseMapView<T extends IBaseMapPresenter> extends AbsMapView<T> {

    @Override
    default void setMarks(List<MarkBehavior> list){
        getMapDelegate().setMarks(list);
    }

    @Override
    default void setDifferentMarks(final List<MarkBehavior> list){
        getMapDelegate().setDifferentMarks(list);
    }

    @Override
    default void setDifferentMarks(final List<MarkBehavior> list, boolean isAppend) {
        getMapDelegate().setDifferentMarks(list,isAppend);
    }

    @Override
    default void removeMarks(final List<MarkBehavior> list) {
        getMapDelegate().removeMarks(list);
    }

    @Override
    default void clearMarks(){
        getMapDelegate().clearMarks();
    }

    @Override
    default void clearMap() {
        getMapDelegate().clearMap();
    }

    @Override
    default void walk(double[][] position) {
        getMapDelegate().walk(position);
    }

    @Override
    default void traffic(double[][] position, String city) {
        getMapDelegate().traffic(position,city);
    }

    @Override
    default void driver(double[][] position) {
        getMapDelegate().driver(position);
    }

    @Override
    default void poi(String keyWord, String city, int page) {
        getMapDelegate().poi(keyWord,city,page);
    }

    @Override
    default void removeLastRoute() {
        getMapDelegate().removeLastRoute();
    }

    @Override
    default void removeLastPoi() {
        getMapDelegate().removeLastPoi();
    }

    @Override
    default void hideInfoWindow() {
        getMapDelegate().hideInfoWindow();
    }

    @Override
    default void moveMapToArea(double[][] position){
        getMapDelegate().moveMapToArea(position);
    }

    @Override
    default void moveMapToPoint(double[] poition){
        getMapDelegate().moveMapToPoint(poition);
    }

    @Override
    default void moveMapToLocationPoint(){
        getMapDelegate().moveMapToLocationPoint();
    }

    @Override
    default void zoomMap(int scale) {
        getMapDelegate().zoomMap(scale);
    }

    @Override
    default double[][] getMapArea() {
        return getMapDelegate().getMapArea();
    }

    @Override
    default double[] getMapCenter(){
        return getMapDelegate().getMapCenter();
    }

    public MapDelegate getMapDelegate();

    public abstract class MapDelegate<T extends IBaseMapPresenter> extends AbsViewDelegate<T> implements AbsMapView<T> {

        public static int MARKERANIMATE_DURATION = 500;

        public MapView mapView ;

        public GoogleMap map;

        public CopyOnWriteArrayList<Marker> list_marker = new CopyOnWriteArrayList<>();
        public Marker lastMarker;
        public Polyline lastRouteOverlay;
//        public PoiOverlay lastPoiOverlay;

        public MapDelegate(IAbsView view) {
            super(view);
        }

        @Override
        public void afterOnCreate(Bundle savedInstanceState) {
            super.afterOnCreate(savedInstanceState);

            mapView = (MapView) getRootView().findViewById(getContext().getResources().getIdentifier("mapView", "id", getContext().getPackageName()));

            mapView.onCreate(savedInstanceState);

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    initMapView();
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            mapView.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
            mapView.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mapView.onDestroy();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            mapView.onSaveInstanceState(outState);
        }

        protected void initMapView(){

            PermissionUtils.permission(PermissionConstants.LOCATION)
                    .callback(new PermissionUtils.FullCallback() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onGranted(List<String> permissionsGranted) {
                            map.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
                        }
                        @Override
                        public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        }
                    })
                    .request();

            //map相关设置
            GoogleMap.InfoWindowAdapter adapter = new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return getWindowView(marker);
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            };
            map.setInfoWindowAdapter(adapter);


            //map相关监听
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    afterMapClick(new double[]{latLng.latitude,latLng.longitude});
                }
            });

            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    afterMapLongClick(new double[]{latLng.latitude,latLng.longitude});
                }
            });

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    lastMarker = marker;

                    afterMarkerClick(marker);

                    marker.showInfoWindow();

                    return true;
                }
            });

            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    afterMapStatusChangeFinish(cameraPosition);
                }
            });
        }

        @Override
        public void setMarks(List<MarkBehavior> list){

            List<Marker> list_mark = new LinkedList<>();

            for (MarkBehavior behavior : list)
            {
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(new LatLng(behavior.getLatitude(),behavior.getLongitude()));
                markerOption.title(behavior.getTitle()).snippet(behavior.getLittleTitle());
                markerOption.draggable(false);//设置Marker可拖动
                markerOption.icon(getMarkerDescript(behavior));
//                markerOption.setFlat(false);//设置marker平贴地图效果

                Marker marker = map.addMarker(markerOption);
                marker.setTag(behavior);

//                Animation animation = new ScaleAnimation(0,1,0,1);
//                animation.setDuration(MARKERANIMATE_DURATION);
//                animation.setInterpolator(new AccelerateInterpolator());
//                marker.setAnimation(animation);
//                marker.startAnimation();

                list_mark.add(marker);
            }

            list_marker.addAll(list_mark);
        }

        @Override
        public void setDifferentMarks(List<MarkBehavior> list) {
            setDifferentMarks(list,false);
        }

        @Override
        public void setDifferentMarks(final List<MarkBehavior> list,boolean isAppend){

            final List<MarkBehavior> list_old = new LinkedList<>();
            final List<MarkBehavior> list_remove = new LinkedList<>();
            final List<MarkBehavior> list_newAdd = new LinkedList<>();

            for (Marker marker : list_marker)
            {
                MarkBehavior markBehavior = (MarkBehavior) marker.getTag();
                list_old.add(markBehavior);
            }

            //遍历所有旧MarkBehavior，当发现旧marker在新list中不存在的时候，则在标记到删除集合中
            for (MarkBehavior markBehavior : list_old)
            {
                if (!list.contains(markBehavior))
                {
                    list_remove.add(markBehavior);
                }
            }
            if (!isAppend)
                removeMarks(list_remove);

            //遍历所有新list的MarkBehavior，只要该marker未添加到地图上，则标记到添加集合中
            for (MarkBehavior markBehavior : list)
            {
                if (!list_old.contains(markBehavior))
                {
                    list_newAdd.add(markBehavior);
                }
            }

            setMarks(list_newAdd);
        }

        @Override
        public void removeMarks(final List<MarkBehavior> list) {

            List<Marker> list_remove = new LinkedList();
            for (Marker marker : list_marker)
            {
                if (list.contains(marker.getTag()))
                    list_remove.add(marker);
            }
            reallyRemoveMarks(list_remove);
        }

        @Override
        public void clearMarks(){
            reallyRemoveMarks(list_marker);
        }

        protected void reallyRemoveMarks(final List<Marker> list) {

//            for (Marker marker : list)
//            {
//                Animation animation = new AlphaAnimation(1,0);
//                animation.setDuration(MARKERANIMATE_DURATION);
//                animation.setInterpolator(new LinearInterpolator());
//                marker.setAnimation(animation);
//                marker.startAnimation();
//            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (final Marker marker : list)
                    {
                        marker.remove();
                    }
                }
            },MARKERANIMATE_DURATION);

            list_marker.removeAll(list);
        }

        @Override
        public void clearMap() {
            map.clear();
            GoogleMapUtils.removeRoutePolyLine(lastRouteOverlay);
            lastMarker = null;
            list_marker.clear();
        }

        @Override
        public void walk(double[][] position) {
            commonRouting(AbstractRouting.TravelMode.WALKING,position);
        }

        @Override
        public void traffic(double[][] position,String city) {
            commonRouting(AbstractRouting.TravelMode.TRANSIT,position);
        }

        @Override
        public void driver(double[][] position) {
            commonRouting(AbstractRouting.TravelMode.DRIVING,position);
        }

        private void commonRouting(AbstractRouting.TravelMode travelMode,double[][] position){
            Routing routing = new Routing.Builder()
                    .travelMode(travelMode)
                    .withListener(new RoutingListener() {
                        @Override
                        public void onRoutingFailure(RouteException e) {
                            afterGetRouteFinish(null,false);
                        }

                        @Override
                        public void onRoutingStart() {

                        }

                        @Override
                        public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
                            Polyline polyline = map.addPolyline(GoogleMapUtils.addRoutePolyLine(arrayList));
                            GoogleMapUtils.moveMapToPolyline(getContext(),map,polyline);

                            lastRouteOverlay = polyline;

                            afterGetRouteFinish(arrayList,true);
                        }

                        @Override
                        public void onRoutingCancelled() {

                        }
                    })
                    .alternativeRoutes(false)
                    .waypoints(new LatLng(position[0][0],position[0][1]), new LatLng(position[1][0],position[1][1]))
                    .build();
            routing.execute();
        }

        @Override
        public void poi(String keyWord,String city,int page){

//            PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
//            query.setPageSize(20);
//            query.setPageNum(page);
//
//            PoiSearch poiSearch = new PoiSearch(getContext(), query);
//            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
//                @Override
//                public void onPoiSearched(PoiResult poiResult, int i) {
//                    List<PoiItem> poiItems = poiResult.getPois();
//                    if (poiItems != null && poiItems != null && poiItems.size() > 0)
//                    {
//                        PoiOverlay poiOverlay = new PoiOverlay(map, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();
//
//                        lastPoiOverlay = poiOverlay;
//
//                        afterGetPoiFinish(poiResult,true);
//                    }
//                    else
//                    {
//                        afterGetPoiFinish(poiResult,false);
//                    }
//                }
//
//                @Override
//                public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//                }
//            });
//            poiSearch.searchPOIAsyn();
        }

        @Override
        public void removeLastRoute() {
            if (lastRouteOverlay != null)
            {
                GoogleMapUtils.removeRoutePolyLine(lastRouteOverlay);
                lastRouteOverlay = null;
            }
        }

        @Override
        public void removeLastPoi(){
//            if (lastPoiOverlay != null)
//            {
//                lastPoiOverlay.removeFromMap();
//                lastPoiOverlay = null;
//            }
        }

        @Override
        public void hideInfoWindow() {
            if (lastMarker != null)
            {
                lastMarker.hideInfoWindow();
            }
        }

        @Override
        public void moveMapToArea(double[][] position){

            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for(int i=0;i<position.length;i++)
                boundsBuilder.include(new LatLng(position[i][0],position[i][1]));

            int padding = ScreenUtils.dip2px(getContext(), 100);
            map.animateCamera(CameraUpdateFactory.newLatLngBounds( boundsBuilder.build(),padding));
        }

        @Override
        public void moveMapToPoint(double[] position){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position[0],position[1]),map.getCameraPosition().zoom));
        }

        @Override
        public void moveMapToLocationPoint(){
            if (getPresenter().getLocation() != null)
                moveMapToPoint(new double[]{getPresenter().getLocation().getLatitude(),getPresenter().getLocation().getLongitude()});
            else
                afterGetLocationErro();
        }

        @Override
        public void zoomMap(int scale) {
            map.animateCamera(CameraUpdateFactory.zoomTo(scaleToZoom(scale)));
        }

        //缩放换算
        private int scaleToZoom(int scale) {
            if (scale <= 10) return 19;
            else if (scale <= 25) return 18;
            else if (scale <= 50) return 17;
            else if (scale <= 100) return 16;
            else if (scale <= 200) return 15;
            else if (scale <= 500) return 14;
            else if (scale <= 1000) return 13;
            else if (scale <= 2000) return 12;
            else if (scale <= 5000) return 11;
            else if (scale <= 10000) return 10;
            else if (scale <= 20000) return 9;
            else if (scale <= 30000) return 8;
            else if (scale <= 50000) return 7;
            else if (scale <= 100000) return 6;
            else if (scale <= 200000) return 5;
            else if (scale <= 500000) return 4;
            else if (scale <= 1000000) return 3;
            else if (scale > 1000000) return 2;
            return 20;
        }

        @Override
        public double[][] getMapArea() {

            Projection projection = map.getProjection();

            LatLng topLeft = projection.fromScreenLocation(new Point(0,0));
            LatLng bottomRight = projection.fromScreenLocation(new Point(mapView.getWidth(),mapView.getHeight()));

            return new double[][]{new double[]{topLeft.latitude,topLeft.longitude},new double[]{bottomRight.latitude,bottomRight.longitude}};
        }

        @Override
        public double[] getMapCenter(){
            LatLng latLng = map.getCameraPosition().target;
            return new double[]{latLng.latitude,latLng.longitude};
        }

        //重写该方法处理定位失败后逻辑
        public abstract void afterGetLocationErro();

        //重写该方法返回定位点图标
        protected abstract int getLocationIcon();

        //重写该方法返回定位圆圈颜色
        protected abstract int getLocationRadiusColor();

        //重写该方法返回Marker样式
        protected abstract BitmapDescriptor getMarkerDescript(MarkBehavior behavior);

        //重写该方法返回弹窗样式
        protected abstract View getWindowView(Marker marker);

        //标记点击后调用
        protected abstract void afterMarkerClick(Marker marker);

        //地图状态改变后调用
        protected abstract void afterMapStatusChangeFinish(CameraPosition cameraPosition);

        //点击地图后调用
        protected abstract void afterMapClick(double[] position);

        //长按地图后调用
        protected abstract void afterMapLongClick(double[] position);

        //路线规划结束后调用
        protected abstract void afterGetRouteFinish(List<Route> result, boolean isSuccess);

        //兴趣点搜索结束后调用
        protected abstract void afterGetPoiFinish(List<MarkBean> result, boolean isSuccess);

    }

}