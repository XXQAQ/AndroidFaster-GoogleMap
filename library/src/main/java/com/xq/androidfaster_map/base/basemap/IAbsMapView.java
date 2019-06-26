package com.xq.androidfaster_map.base.basemap;

import com.xq.androidfaster.base.abs.IAbsView;
import com.xq.androidfaster_map.bean.behavior.MarkerBehavior;
import java.util.List;

public interface IAbsMapView<T extends IAbsMapPresenter> extends IAbsView<T> {

    //设置Markers(不会对地图上已添加的Markers去重)
    public void setMarkers(List list);

    //设置Markers(对重复添加的Markers去重，且使用覆盖模式)
    public void setDifferentMarkers(final List list);

    //设置Markers(对重复添加的Markers去重，isAppend表示追加模式或者覆盖模式)
    public void setDifferentMarkers(final List list, boolean isAppend);

    //删除指定Markers
    public void removeMarkers(final List list);

    //清空所有Markers
    public void clearMarkers();

    //清空地图
    public void clearMap();

    //步行路线规划
    public void walkRoute(double[][] position);

    //公交路线规划
    public void trafficRoute(double[][] position, String city);

    //驾车路线规划
    public void driverRoute(double[][] position);

    //兴趣点搜索（地区内搜索）
    public void regionPoi(String keyWord, String city, int page);

    //兴趣点搜索（附近搜索）
    public void nearbyPoi(String keyWord, double[]position, int radius, int page);

    //清除上次路线规划
    public void removeLastRoute();

    //清除上次兴趣点检索
    public void removeLastPoi();

    //隐藏弹窗
    public void hideInfoWindow();

    //移动地图至多个点组成的区域
    public void moveMapToArea(double[][] position);

    //移动地图至某点
    public void moveMapToPoint(double[] position);

    //移动地图至某点（带缩放）
    public void moveMapToPoint(double[] position, int scale);

    //移动地图至当前定位位置
    public void moveMapToLocationPoint();

    //移动地图至当前定位位置（带缩放）
    public void moveMapToLocationPoint(int scale);

    //调整地图缩放范围（单位米）
    public void zoomMap(int scale);

    //获取当前地图可视经纬度(上左与右下的经纬度)
    public double[][] getMapArea();

    //获取当前地图中心经纬度
    public double[] getMapCenter();

}