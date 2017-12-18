package com.tomlocksapps.mbglwrapper.element.custom.info;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.tomlocksapps.mbglwrapper.element.custom.info.validator.InfoValidator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by walczewski on 2016-08-01.
 */
public class MarkerInfoWindowManager implements MapboxMap.InfoWindowAdapter {

    private Context context;
    private final Map<InfoValidator, Class<? extends View>> map = new HashMap<>();

    public MarkerInfoWindowManager(Context context) {
        this.context = context;

//        map.put(new GasStationInfoValidator(), GasStationInfoView.class);
    }

    @SuppressWarnings("unchecked")
    public boolean shouldShowInfoWindow(@NonNull Marker marker) {
        return findAssosciatedClass(marker) != null;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends View> findAssosciatedClass(@NonNull Marker marker) {
        for (Map.Entry<InfoValidator, Class<? extends View>> entry : map.entrySet()) {
            if (entry.getKey().isCorresponding(marker)) {
                final Class<? extends View> aClass = entry.getValue();

                return aClass;
            }
        }

        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {

        final Class<? extends View> assosciatedClass = findAssosciatedClass(marker);

        View view = null;

        if(assosciatedClass != null) {
            try {
                assosciatedClass.getDeclaredConstructor(Context.class).newInstance(context);
            } catch (InstantiationException e) {
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(e);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }

        return view;
    }
}
