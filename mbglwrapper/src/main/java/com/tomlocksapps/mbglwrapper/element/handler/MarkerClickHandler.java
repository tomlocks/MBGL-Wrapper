package com.tomlocksapps.mbglwrapper.element.handler;

import com.mapbox.mapboxsdk.annotations.Marker;

import java.lang.reflect.ParameterizedType;

/**
 * Created by walczewski on 2016-06-16.
 */
public abstract class MarkerClickHandler<T extends Marker> {

    private final Class<T> type;

    public MarkerClickHandler() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public final void handleClick(T marker) {
        if(type.isAssignableFrom(marker.getClass())) {
            handleMyMarkerClick(marker);
        }
    }

    public Class<T> getType() {
        return type;
    }

    protected abstract boolean handleMyMarkerClick(T marker);

}
