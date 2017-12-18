package com.tomlocksapps.mbglwrapper.element.scale.settings.impl;

import android.view.View;

import com.mapbox.mapboxsdk.annotations.MarkerView;

import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.tomlocksapps.mbglwrapper.element.scale.settings.IElementScalingSetting;

/**
 * Created by walczewski on 20.12.2016.
 */

public class DefaultElementScallingSetting implements IElementScalingSetting {

    private float polylineScaleFactor = 1.1f;

    protected void setPolylineScaleFactor(float polylineScaleFactor) {
        this.polylineScaleFactor = polylineScaleFactor;
    }

    @Override
    public float getPolylineWidth(double zoom) {
        final float newWidth = (float) zoom * polylineScaleFactor;

        return newWidth;
    }

    @Override
    public float getMarkerScale(View view, MarkerView markerView, double zoom) {
        final float scale = (float) (zoom / MapboxConstants.MAXIMUM_ZOOM);

        return scale;
    }
}
