package com.tomlocksapps.mbglwrapper.element.custom.info.validator;

import com.mapbox.mapboxsdk.annotations.Marker;

import java.lang.reflect.ParameterizedType;

/**
 * Created by walczewski on 2016-08-01.
 */
public abstract class InfoValidator<T extends Marker> {

    private Class<T> typeClass;

    public InfoValidator() {
        typeClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public final boolean isCorresponding(T markerView) {
        if(isClassCorresponding(markerView.getClass())) {
            return isObjectCorresponding(markerView);
        }

        return false;
    }

    protected boolean isObjectCorresponding(T markerView) {
        return true;
    }

    private boolean isClassCorresponding(Class clazz) {
        return  typeClass.equals(clazz);
    }

}
