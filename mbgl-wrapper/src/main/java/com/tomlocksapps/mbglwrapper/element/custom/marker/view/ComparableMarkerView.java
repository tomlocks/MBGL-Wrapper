package com.tomlocksapps.mbglwrapper.element.custom.marker.view;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;

/**
 * Created by walczewski on 2016-06-29.
 */
public abstract class ComparableMarkerView extends HideableMarkerView {
    /**
     * Creates a instance of MarkerView using the builder of MarkerView
     *
     * @param baseMarkerViewOptions the builder used to construct the MarkerView
     */

    private Object comparableObject;

    public ComparableMarkerView(BaseMarkerViewOptions baseMarkerViewOptions, Object comparableObject) {
        super (baseMarkerViewOptions);

        this.comparableObject = comparableObject;
    }

    public Object getComparableObject() {
        return comparableObject;
    }
}
