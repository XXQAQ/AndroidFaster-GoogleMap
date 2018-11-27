package com.xq.androidfaster_map.basemap;

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
import com.xq.androidfaster_map.R;
import com.xq.androidfaster_map.bean.behavior.MarkBehavior;
import com.xq.androidfaster_map.util.MapUtils;
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
    default void removeMarks(final List<MarkBehavior> list) {
        getMapDelegate().removeMarks(list);
    }

    @Override
    default void clearMarkes(){
        getMapDelegate().clearMarkes();
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
    default void removeLastRoute() {
        getMapDelegate().removeLastRoute();
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

        public Polyline lastOverlay;

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

        @SuppressLint("MissingPermission")
        protected void initMapView(){

            map.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            //map相关设置
            GoogleMap.InfoWindowAdapter adapter = new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    MarkBehavior behavior = (MarkBehavior) marker.getTag();
                    return getWindowView(behavior);
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
                    afterMapClick(latLng.latitude,latLng.longitude);
                }
            });

            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    afterMapLongClick(latLng.latitude,latLng.longitude);
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
        public void setDifferentMarks(final List<MarkBehavior> list){

            final List<MarkBehavior> list_new = new LinkedList<>();
            final List<MarkBehavior> list_newCopy = new LinkedList<>();
            final List<MarkBehavior> list_remove = new LinkedList<>();

            //遍历所有旧Marker，当发现旧marker在新集合中不存在的时候，则在原集合中删除且标记到删除集合中
            for (Marker marker : list_marker)
            {
                MarkBehavior markBehavior = (MarkBehavior) marker.getTag();
                if (!list.contains(markBehavior))
                {
                    list_remove.add(markBehavior);
                }
            }

            removeMarks(list_remove);

            for (Marker marker : list_marker)
            {
                MarkBehavior markBehavior = (MarkBehavior) marker.getTag();
                list_newCopy.add(markBehavior);
            }

            //遍历所有新markerBehavior，只要该marker未添加到地图上，则将Marker标记到新集合中
            for (MarkBehavior new_markBehavior : list)
            {
                if (!list_newCopy.contains(new_markBehavior))
                {
                    list_new.add(new_markBehavior);
                }
            }

            setMarks(list_new);
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
        public void clearMarkes(){
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
            MapUtils.removeRoutePolyLine(lastOverlay);
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
                            afterGetRouteFinish(null,-1);
                        }

                        @Override
                        public void onRoutingStart() {

                        }

                        @Override
                        public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
                            MapUtils.removeRoutePolyLine(lastOverlay);
                            Polyline polyline = map.addPolyline(MapUtils.addRoutePolyLine(arrayList, getColor(R.color.polyline)));
                            MapUtils.moveMapToPolyline(getContext(),map,polyline);

                            lastOverlay = polyline;

                            afterGetRouteFinish(arrayList,i);
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
        public void removeLastRoute() {
            if (lastOverlay != null)
            {
                MapUtils.removeRoutePolyLine(lastOverlay);
                lastOverlay = null;
            }
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

            map.animateCamera(CameraUpdateFactory.newLatLngBounds( boundsBuilder.build(),15));
        }

        @Override
        public void moveMapToPoint(double[] position){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position[0],position[1]),15));
        }

        @Override
        public void moveMapToLocationPoint(){
            if (getPresenter().getLocation() != null)
            {
                moveMapToPoint(new double[]{getPresenter().getLocation().getLatitude(),getPresenter().getLocation().getLongitude()});
            }
            else
            {
                afterGetLocationErro();
            }
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
        protected abstract void afterGetLocationErro();

        //重写该方法返回定位点图标
        protected abstract int getLocationIcon();

        //重写该方法返回定位圆圈颜色
        protected abstract int getLocationRadiusColor();

        //重写该方法返回Marker样式
        protected abstract BitmapDescriptor getMarkerDescript(MarkBehavior behavior);

        //重写该方法返回弹窗样式
        protected abstract View getWindowView(MarkBehavior behavior);

        //标记点击后调用
        protected abstract void afterMarkerClick(Marker marker);

        //地图状态改变后调用
        protected abstract void afterMapStatusChangeFinish(CameraPosition cameraPosition);

        //点击地图后调用
        protected abstract void afterMapClick(double lat,double lon);

        //长按地图后调用
        protected abstract void afterMapLongClick(double lat,double lon);

        //路线规划结束后调用
        protected abstract void afterGetRouteFinish(ArrayList<Route> result, int erroCode);

    }

}