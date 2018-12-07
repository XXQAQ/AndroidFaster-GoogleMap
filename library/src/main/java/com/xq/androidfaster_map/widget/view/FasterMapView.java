package com.xq.androidfaster_map.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

public class FasterMapView extends MapView {

    public FasterMapView(Context context) {
        super(context);
    }

    public FasterMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FasterMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public FasterMapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context, googleMapOptions);
    }
}
