package com.tomlocksapps.mbglwrapper.element.handler;

import com.mapbox.mapboxsdk.annotations.Marker;

import java.util.HashMap;
import java.util.Map;

import com.tomlocksapps.mbglwrapper.element.custom.marker.view.ComparableMarkerView;

/**
 * Odpowiada za obsluge kliku na mapie. Nalezy tutaj zarejestrowac dany MarkerView do odpowiedniego ClickHandlera.
 *
 * Created by walczewski on 2016-06-16.
 */
public class PoiClickHandlerManager {

    private Map<Class<? extends ComparableMarkerView> , MarkerClickHandler> map = new HashMap<>();

    public final <T extends Marker> boolean handleClick(T marker) {
        final MarkerClickHandler markerClickHandler = map.get(marker.getClass());

        if(markerClickHandler != null)
            return markerClickHandler.handleMyMarkerClick(marker);

        return true;
    }

    public MarkerClickHandler putHandler(MarkerClickHandler value) {
        return map.put(value.getType(), value);
    }
}
