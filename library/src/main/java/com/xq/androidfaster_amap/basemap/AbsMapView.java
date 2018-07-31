package com.xq.androidfaster_amap.basemap;


import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
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



public interface AbsMapView<T extends AbsMapPresenter> extends AbsView<T> {

    //设置Marks
    public void setMarks(List<MarkBehavior> list);

    //设置Marks(新添加进来会增加至地图，已存在则不变，不存在则会被删除)
    public void setDifferentMarks(final List<MarkBehavior> list);

    //删除指定Markes
    public void removeMarks(final List<Marker> list);

    //清空所有Marker
    public void clearMarkes();

    //清空地图
    public void clearMap();

    //步行路线规划
    public void walk(LatLng latLng_from, LatLng latLng_to);

    //交通路线规划
    public void traffic(LatLng latLng_from, LatLng latLng_to, String city);

    //驾车路线规划
    public void driver(LatLng latLng_from, LatLng latLng_to);

    //清除上次路线规划
    public void removeLastRoute();

    //隐藏弹窗
    public void hideInfoWindow();

    //移动地图至多个点组成的区域
    public void moveMapToArea(double[][] position);

    //移动地图至某点
    public void moveMapToPoint(double lat, double lon);

    //移动地图至当前位置
    public void moveMapToLocationPoint();

    //获取当前地图可视经纬度(上左与右下的经纬度)
    public double[][] getMapArea();

    //获取当前地图中心经纬度
    public double[] getMapCenter();

    //重写该方法处理定位失败后逻辑
    public abstract void afterGetLocationErro();

    //重写该方法返回定位点图标
    public abstract int getLocationIcon();

    //重写该方法返回定位圆圈颜色
    public abstract int getLocationRadiusColor();

    //重写该方法返回Marker样式
    public abstract ArrayList<BitmapDescriptor> getMarkerDescript(MarkBehavior behavior);

    //重写该方法返回弹窗样式
    public abstract View getWindowView(MarkBehavior behavior);

    //标记点击后调用
    public abstract void afterMarkerClick(Marker marker);

    //地图状态改变后调用
    public abstract void afterMapStatusChangeFinish(CameraPosition cameraPosition);

    //点击地图后调用
    public abstract void afterMapClick(LatLng latLng);

    //路线规划结束后调用
    public abstract void afterGetRouteFinish(RouteResult result, int erroCode);

}