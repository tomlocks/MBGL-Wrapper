package com.tomlocksapps.mbglwrapper.element.scale.settings;

import android.view.View;

import com.mapbox.mapboxsdk.annotations.MarkerView;

/**
 * Created by walczewski on 20.12.2016.
 */

public interface IElementScalingSetting {
    /**
     * Returns the list of polylines that have been changed
     *
     * @param polylineOptions
     * @return
     */

    float getPolylineWidth(double zoom);

   /* boolean scalePolylines(Polyline polyline, PolylineOptions polylineOptions, double zoom);

    boolean scalePolylineOptions(List<PolylineOptions> polylinesOptions, double zoom);*/

    float getMarkerScale(View view, MarkerView markerView, double zoom);

//    void scaleMarker(View view, MarkerView markerView, double zoom);
}
