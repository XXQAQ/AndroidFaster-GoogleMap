package com.xq.androidfaster_map.util.googlemaptools;

import android.content.Context;
import com.directions.route.Route;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.xq.androidfaster.util.tools.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

public class GoogleMapUtils {

    public static PolylineOptions addRoutePolyLine(ArrayList<Route> route, int lineColor) {
        PolylineOptions polyOptions = new PolylineOptions();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {
            polyOptions.color(lineColor);
            polyOptions.width(12);
            polyOptions.addAll(route.get(i).getPoints());
        }

        return polyOptions;
    }

    public static void removeRoutePolyLine(Polyline polyline) {
        if (polyline != null)
            polyline.remove();
    }

    public static void moveMapToPolyline(Context context, GoogleMap map, Polyline polyline) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<LatLng> positionList = polyline.getPoints();
        for (int i = 0; i < positionList.size(); i++) {
            builder.include(positionList.get(i));
        }
        LatLngBounds bounds = builder.build();
        int padding = ScreenUtils.dip2px(context, 100);
        CameraUpdate u = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(u);
    }

}
