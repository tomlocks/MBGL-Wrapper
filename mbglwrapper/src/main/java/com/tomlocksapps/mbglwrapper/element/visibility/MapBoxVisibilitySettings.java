package com.tomlocksapps.mbglwrapper.element.visibility;


import com.tomlocksapps.mbglwrapper.element.visibility.settings.MapZoomSettings;
import com.tomlocksapps.mbglwrapper.element.visibility.settings.marker.MarkerZoomSetting;

/**
 * Created by walczewski on 2016-07-07.
 */
public class MapBoxVisibilitySettings {

    private MapZoomSettings mapZoomSettings;

    private MapBoxVisibilitySettings() {
        mapZoomSettings = null;// new YanosikMapZoomSettings(); // todo set instance
    }

    public MarkerZoomSetting getZoomSettings(Object compare) {
        return mapZoomSettings.getMarkerZoomSetting(compare);
    }

    public boolean isObjectVisibleOnLayer(double zoom, Object objectToCompare) {
        final MarkerZoomSetting zoomSettings = getZoomSettings(objectToCompare);

        if(zoomSettings == null)
            return false;

        return zoom > zoomSettings.getMinZoomForMarker(objectToCompare);
    }

}
