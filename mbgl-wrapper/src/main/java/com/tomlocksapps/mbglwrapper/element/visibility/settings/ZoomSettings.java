package com.tomlocksapps.mbglwrapper.element.visibility.settings;

import com.mapbox.mapboxsdk.annotations.MarkerView;

import java.lang.reflect.ParameterizedType;

import com.mapbox.mapboxsdk.constants.MapboxConstants;

/**
 * Klasa wiazaca MarkerView z zoomem na jakim moze byc pokazany.
 *
 * Created by walczewski on 2016-06-17.
 */
public abstract class ZoomSettings<T extends MarkerView> {

    private final Class<T> markerTypeClass;

    private final double defaultMinZoomLevel;
    private final double defaultMaxZoomLevel;

    public ZoomSettings() {
        defaultMinZoomLevel = provideDefaultMinZoomLevel();
        defaultMaxZoomLevel = provideDefaultMaxZoomLevel();

        markerTypeClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected double provideDefaultMinZoomLevel() {
        return MapboxConstants.MINIMUM_ZOOM*0.9;
    }

    protected double provideDefaultMaxZoomLevel() {
        return MapboxConstants.MAXIMUM_ZOOM*1.1;
    }

    public Class<T> getMarkerTypeClass() {
        return markerTypeClass;
    }

    public double getMinZoomLevel(T type) {
        return defaultMinZoomLevel;
    }

    public double getMaxZoomLevel(T type) {
        return defaultMaxZoomLevel;
    }

    public double getDefaultMinZoomLevel() {
        return defaultMinZoomLevel;
    }
}
