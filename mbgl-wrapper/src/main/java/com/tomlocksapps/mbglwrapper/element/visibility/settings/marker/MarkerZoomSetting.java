package com.tomlocksapps.mbglwrapper.element.visibility.settings.marker;

import com.mapbox.mapboxsdk.constants.MapboxConstants;
import java.lang.reflect.ParameterizedType;

/**
 * Created by walczewski on 09.09.2016.
 */
public abstract class MarkerZoomSetting<T> {

    private Class typedClass;

    public MarkerZoomSetting() {
        typedClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];;
    }

    public Class getTypedClass() {
        return typedClass;
    }

    public abstract double getMinZoomForMarker(T object);
    public double getMaxZoomForMarker(T object) {
        return MapboxConstants.MAXIMUM_ZOOM;
    }
}
