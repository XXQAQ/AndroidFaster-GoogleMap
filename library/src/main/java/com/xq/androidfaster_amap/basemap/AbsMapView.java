package com.xq.androidfaster_amap.basemap;


import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.xq.androidfaster_amap.bean.behavior.MarkBehavior;
import com.xq.projectdefine.base.abs.AbsView;
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

}