package com.xq.androidfaster_map.base.basemap;

import com.xq.androidfaster.base.abs.IAbsView;
import com.xq.androidfaster_map.bean.behavior.MarkBehavior;
import java.util.List;

public interface AbsMapView<T extends AbsMapPresenter> extends IAbsView<T> {

    //设置Marks
    public void setMarks(List<MarkBehavior> list);

    //设置Marks(新添加进来会增加至地图，已存在则不变，不存在则会被删除)
    public void setDifferentMarks(final List<MarkBehavior> list);

    //设置Marks(isAppend决定采用追加模式还是覆盖模式)
    public void setDifferentMarks(final List<MarkBehavior> list,boolean isAppend);

    //删除指定Markes
    public void removeMarks(final List<MarkBehavior> list);

    //清空所有Marker
    public void clearMarks();

    //清空地图
    public void clearMap();

    //步行路线规划
    public void walk(double[][] position);

    //交通路线规划
    public void traffic(double[][] position, String city);

    //驾车路线规划
    public void driver(double[][] position);

    //兴趣点搜索
    public void poi(String keyWord,String city,int page);

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

    //移动地图至当前位置
    public void moveMapToLocationPoint();

    //获取当前地图可视经纬度(上左与右下的经纬度)
    public double[][] getMapArea();

    //获取当前地图中心经纬度
    public double[] getMapCenter();

}