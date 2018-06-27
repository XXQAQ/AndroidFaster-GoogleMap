package com.xq.androidfaster_amap.basemap;


import com.amap.api.location.AMapLocation;
import com.xq.projectdefine.base.abs.AbsPresenter;

/**
 * Created by xq on 2017/4/11 0011.
 */

public interface IBaseMapPresenter<T extends IBaseMapView> extends AbsPresenter<T> {


    //该方法用于接受定位数据，开发中建议使用afterReceiveLocation方法免去判空等操纵
    default void onReceiveLocation(AMapLocation location) {
        if (location != null)
        {
            if (location.getErrorCode() == 0)
            {
                getMapBuilder().location = location;

                afterReceiveLocation(location);
                //可在其中解析amapLocation获取相应内容。
//                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                        amapLocation.getLatitude();//获取纬度
//                        amapLocation.getLongitude();//获取经度
//                        amapLocation.getAccuracy();//获取精度信息
//                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                        amapLocation.getCountry();//国家信息
//                        amapLocation.getProvince();//省信息
//                        amapLocation.getCity();//城市信息
//                        amapLocation.getDistrict();//城区信息
//                        amapLocation.getStreet();//街道信息
//                        amapLocation.getStreetNum();//街道门牌号信息
//                        amapLocation.getCityCode();//城市编码
//                        amapLocation.getAdCode();//地区编码
//                        amapLocation.getAoiName();//获取当前定位点的AOI信息
//                        amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
//                        amapLocation.getFloor();//获取当前室内定位的楼层
//                        amapLocation.getGpsStatus();//获取GPS的当前状态
//                        //获取定位时间
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date date = new Date(amapLocation.getTime());
//                        df.format(date);
            }
            else
            {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                Log.e(TAG,"location Error, ErrCode:" + location.getErrorCode() + ", errInfo:" + location.getErrorInfo());
            }
        }
    }

    //该方法在接收到定位数据后调用，重写该方法完成后续逻辑
    public abstract void afterReceiveLocation(AMapLocation location);

    default AMapLocation getLocation() {
        return getMapBuilder().location;
    }

    public MapBuilder getMapBuilder();

    public static class MapBuilder{
        public AMapLocation location;
    }

}
