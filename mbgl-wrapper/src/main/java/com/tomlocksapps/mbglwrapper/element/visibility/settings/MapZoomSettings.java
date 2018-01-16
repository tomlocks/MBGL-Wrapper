package com.tomlocksapps.mbglwrapper.element.visibility.settings;

import com.tomlocksapps.mbglwrapper.element.visibility.settings.marker.MarkerZoomSetting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by walczewski on 09.09.2016.
 */
public class MapZoomSettings {

    private Map<Class, MarkerZoomSetting> map = new HashMap<>();

    public MapZoomSettings() {

    }

    public MarkerZoomSetting getMarkerZoomSetting(Object object) {
        for (Map.Entry<Class, MarkerZoomSetting> entry : map.entrySet()) {
            if(entry.getKey().equals(object.getClass()))
                return entry.getValue();
        }

        return null;
    }


    public void putZoomSetting(MarkerZoomSetting markerZoomSetting) {
        final MarkerZoomSetting setting = map.put(markerZoomSetting.getTypedClass(), markerZoomSetting);
        if(setting != null)
            throw new IllegalStateException("Ups it seems that you've already addded zoom settings for this class: " + setting.getTypedClass());
    }

}
