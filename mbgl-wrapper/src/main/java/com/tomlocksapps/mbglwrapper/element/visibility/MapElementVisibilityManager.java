package com.tomlocksapps.mbglwrapper.element.visibility;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.tomlocksapps.mbglwrapper.element.custom.ExtraMarkerLocationCalculation;
import com.tomlocksapps.mbglwrapper.element.custom.marker.adapter.ContainerViewHolder;
import com.tomlocksapps.mbglwrapper.element.custom.marker.view.ComparableMarkerView;
import com.tomlocksapps.mbglwrapper.element.visibility.settings.MapZoomSettings;
import com.tomlocksapps.mbglwrapper.element.visibility.settings.marker.MarkerZoomSetting;
import com.tomlocksapps.mbglwrapper.logger.Logger;


/**
 * Zarzadza ukrywaniem markerow na mapie przy roznych poziomach zoomu.
 *
 * Created by walczewski on 2016-06-16.
 */
public class MapElementVisibilityManager implements MapboxMap.OnCameraChangeListener {

    /** Dystans co jaki sprawdzane sa Markery **/
    public static final int DISTANCE_THRESHOLD = 100;

    private MapboxMap map;
    private final MapZoomSettings mapZoomSettings = new MapZoomSettings();

    private double lastZoom = -1;

    private int distanceTraveled;
    private Location lastLocation;

    private static final double ZOOM_DEADBAND = 0.15; // wartosc w obrebie ktorej uzytkonwik moze zoomowac mape i nie powoduje to zmienienania widocznosci POIow
    private Animation showAnim;

    private final Context context;

    public MapElementVisibilityManager(Context context) {
        this.context = context;
        showAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
    }

    public void setMap(MapboxMap map) {
        this.map = map;
    }


    @Override
    public void onCameraChange(CameraPosition position) {

        if (map == null)
            return;

        if(Math.abs(lastZoom - position.zoom) < ZOOM_DEADBAND || position.zoom < 0.0f)
            return;

        Logger.getInstance().d("MarkerController - onCameraChange - MapElementVisibilityManager - refreshing Markers: " + position);

        for (Marker marker : map.getMarkers()) {
            checkMarkerVisibility(position.zoom, marker);
        }

        lastZoom = position.zoom;
    }

    /**
     * Sprawdza czy dany marker powinien zostac pokazany lub ukryty.
     *
     * @param marker Marker do sprawdzenia
     */
    public void checkMarkerVisibility(double zoom, Marker marker) {
        if (marker instanceof ComparableMarkerView) {
            final ComparableMarkerView markerView = (ComparableMarkerView) marker;

            // ekstra obliczenia dla markerow implementujacych interfesjs ExtraMarkerLocationCalculation, np. PoiMarkerView - obliczenie dystansu do POIa
            if(markerView instanceof ExtraMarkerLocationCalculation && lastLocation != null) {
                final ExtraMarkerLocationCalculation extraCalculation = (ExtraMarkerLocationCalculation) markerView;
                extraCalculation.calculate(lastLocation);
            }

            final MarkerZoomSetting zoomSettings = mapZoomSettings.getMarkerZoomSetting(markerView.getComparableObject());

            if (zoomSettings != null) {
                final View view = map.getMarkerViewManager().getView(markerView);

                final double minZoomLevel = zoomSettings.getMinZoomForMarker(markerView.getComparableObject());
                final double maxZoomLevel = zoomSettings.getMaxZoomForMarker(markerView.getComparableObject());


                final boolean canShowMarker = maxZoomLevel >= zoom && zoom >= minZoomLevel && markerView.canShowMarker(zoom);


                if (view != null) {
                    final Object tag = view.getTag();

                    if (tag instanceof ContainerViewHolder) {
                        final ContainerViewHolder viewHolder = (ContainerViewHolder) tag;

                        if (canShowMarker) {
                            if(viewHolder.container.getVisibility() != View.VISIBLE) {
                                viewHolder.container.setVisibility(View.VISIBLE);
                                viewHolder.container.clearAnimation();
                                viewHolder.container.startAnimation(showAnim);
                                Logger.getInstance().d("Marker animation - animating: " + marker.getClass() + " | " + viewHolder.container + " | child count: " + ((ViewGroup)viewHolder.container).getChildCount());
                            }
                        } else {
                            if(viewHolder.container.getVisibility() == View.VISIBLE) {
                                    viewHolder.container.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }

                if (canShowMarker) {
                    markerView.setVisible(true);
                } else {
                    markerView.setVisible(false);
                }

            }
        }
    }

    public MapZoomSettings getMapZoomSettings() {
        return mapZoomSettings;
    }

    public double getZoom() {
        return lastZoom;
    }
}
