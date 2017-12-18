package com.tomlocksapps.mbglwrapper.element.custom.marker.view.impl;

import android.graphics.Bitmap;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;

import com.tomlocksapps.mbglwrapper.element.custom.marker.view.ComparableMarkerView;


/**
 * Created by walczewski on 2016-06-16.
 */
public class GenericMarkerView extends ComparableMarkerView {

    private String url;
    private boolean isClickable;
    private double minZoomLevel;

    private Integer drawableResId;
    private int size;
    private Integer tintColor;
    private Bitmap bitmap;

    public GenericMarkerView(BaseMarkerViewOptions baseMarkerViewOptions, Object comparableObject, String url, boolean isClickable, double minZoomLevel, Integer drawableResId, int size, Integer tintColor, Bitmap bitmap)
    {
        super(baseMarkerViewOptions, comparableObject);

        this.bitmap = bitmap;
        this.drawableResId = drawableResId;
        this.url = url;
        this.isClickable = isClickable;
        this.minZoomLevel = minZoomLevel;
        this.size = size;
        this.tintColor = tintColor;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getSize() {
        return size;
    }

    public Integer getTintColor() {
        return tintColor;
    }

    public double getMinZoomLevel() {
        return minZoomLevel;
    }


    public String getUrl() {
        return url;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public Integer getDrawableResId() {
        return drawableResId;
    }

    @Override
    public boolean canShowMarker(double zoomLevel) {
        return true;
    }


}
