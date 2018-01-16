package com.tomlocksapps.mbglwrapper.element.provider;

import com.mapbox.mapboxsdk.annotations.Polyline;

import java.util.List;

/**
 * Created by walczewski on 25.11.2016.
 */

public interface OnNewElementsListener {
    void onNewPolylinesAdded(List<Polyline> polylines);
}
