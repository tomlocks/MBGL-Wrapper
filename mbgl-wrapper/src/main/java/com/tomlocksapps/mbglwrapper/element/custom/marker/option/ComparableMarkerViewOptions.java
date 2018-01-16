package com.tomlocksapps.mbglwrapper.element.custom.marker.option;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;

/**
 * Created by walczewski on 2016-06-29.
 */
public abstract class ComparableMarkerViewOptions<U extends MarkerView, T extends ComparableMarkerViewOptions<U, T>> extends BaseMarkerViewOptions<U, T> {

    private Object comparableObject;

    public T comparableObject(Object comparableObject) {
        this.comparableObject = comparableObject;

        return getThis();
    }

    public Object getComparableObject() {
        if(comparableObject == null)
            throw new IllegalStateException("You need to pass comparable object if you want to use ComparableMarkerViewOptions!!!!");

        return comparableObject;
    }
}
