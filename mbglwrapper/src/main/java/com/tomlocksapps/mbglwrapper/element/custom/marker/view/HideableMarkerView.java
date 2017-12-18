package com.tomlocksapps.mbglwrapper.element.custom.marker.view;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;

/**
 * Created by walczewski on 2016-06-17.
 */
public abstract class HideableMarkerView extends MarkerView {


    /**
     * Creates a instance of MarkerView using the builder of MarkerView
     *
     * @param baseMarkerViewOptions the builder used to construct the MarkerView
     */
    public HideableMarkerView(BaseMarkerViewOptions baseMarkerViewOptions) {
        super(baseMarkerViewOptions);
    }

/*    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }*/

    public boolean canShowMarker(double zoomLevel) {
        return true;
    }
}
