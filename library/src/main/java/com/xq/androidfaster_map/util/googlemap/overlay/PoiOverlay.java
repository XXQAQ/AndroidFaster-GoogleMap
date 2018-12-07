package com.xq.androidfaster_map.util.googlemap.overlay;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.xq.androidfaster_map.bean.entity.MarkBean;
import java.util.ArrayList;

/**
 * Poi图层类。在高德地图API里，如果要显示Poi，可以用此类来创建Poi图层。如不满足需求，也可以自己创建自定义的Poi图层。
 */
public class PoiOverlay {
	private List<PoiItem> mPois;
	private GoogleMap mAMap;
	private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
	/**
	 * 通过此构造函数创建Poi图层。
	 * @param amap 地图对象。
	 * @param pois 要在地图上添加的poi。列表中的poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类<strong> <a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
	 * @since V2.1.0
	 */
	public PoiOverlay(GoogleMap amap, List<PoiItem> pois) {
		mAMap = amap;
		mPois = pois;
	}
	/**
	 * 添加Marker到地图中。
	 */
	public void addToMap() {
		try{
			for (int i = 0; i < mPois.size(); i++) {
				PoiItem item = mPois.get(i);
				Marker marker = mAMap.addMarker(getMarkerOptions(i));
				marker.setObject(new MarkBean(item.getLatLonPoint().getLatitude(),item.getLatLonPoint().getLongitude(),item.getTitle(),item));
				mPoiMarks.add(marker);
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	/**
	 * 去掉PoiOverlay上所有的Marker。
	 */
	public void removeFromMap() {
		for (Marker mark : mPoiMarks) {
			mark.remove();
		}
	}
	/**
	 * 移动镜头到当前的视角。
	 */
	public void zoomToSpan() {
		try{
			if (mPois != null && mPois.size() > 0) {
				if (mAMap == null)
					return;
//				if(mPois.size()==1){
//					mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mPois.get(0).getLatLonPoint().getLatitude(),
//							mPois.get(0).getLatLonPoint().getLongitude()), 15f));
//				}else{
//					LatLngBounds bounds = getLatLngBounds();
//					mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
//				}
				//建议移动至第一个兴趣点即可
				mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mPois.get(0).getLatLonPoint().getLatitude(), mPois.get(0).getLatLonPoint().getLongitude()), 15f));
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	private LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		for (int i = 0; i < mPois.size(); i++) {
			b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
					mPois.get(i).getLatLonPoint().getLongitude()));
		}
		return b.build();
	}

	private MarkerOptions getMarkerOptions(int index) {
		return new MarkerOptions()
				.position(
						new LatLng(mPois.get(index).getLatLonPoint()
								.getLatitude(), mPois.get(index)
								.getLatLonPoint().getLongitude()))
				.title(getTitle(index)).snippet(getSnippet(index))
				.icon(getBitmapDescriptor(index));
	}
	/**
	 * 给第几个Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
	 * @param index 第几个Marker。
	 * @return 更换的Marker图片。
	 */
	protected BitmapDescriptor getBitmapDescriptor(int index) {
		return null;
	}
	/**
	 * 返回第index的Marker的标题。
	 * @param index 第几个Marker。
	 * @return marker的标题。
	 */
	protected String getTitle(int index) {
		return mPois.get(index).getTitle();
	}
	/**
	 * 返回第index的Marker的详情。
	 * @param index 第几个Marker。
	 * @return marker的详情。
	 */
	protected String getSnippet(int index) {
		return mPois.get(index).getSnippet();
	}
	/**
	 * 从marker中得到poi在list的位置。
	 * @param marker 一个标记的对象。
	 * @return 返回该marker对应的poi在list的位置。
	 * @since V2.1.0
	 */
	public int getPoiIndex(Marker marker) {
		for (int i = 0; i < mPoiMarks.size(); i++) {
			if (mPoiMarks.get(i).equals(marker)) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * 返回第index的poi的信息。
	 * @param index 第几个poi。
	 * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
	 */
	public PoiItem getPoiItem(int index) {
		if (index < 0 || index >= mPois.size()) {
			return null;
		}
		return mPois.get(index);
	}
}
