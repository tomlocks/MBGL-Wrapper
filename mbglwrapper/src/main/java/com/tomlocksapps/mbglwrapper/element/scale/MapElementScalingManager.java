package com.tomlocksapps.mbglwrapper.element.scale;

import android.support.annotation.UiThread;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewManager;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.tomlocksapps.mbglwrapper.element.MapElementController;
import com.tomlocksapps.mbglwrapper.element.MapElementUpdateRunnable;
import com.tomlocksapps.mbglwrapper.element.provider.MapBoxElementsProvider;
import com.tomlocksapps.mbglwrapper.element.scale.settings.ElementProviderScaleSettings;
import com.tomlocksapps.mbglwrapper.element.scale.settings.IElementScalingSetting;
import com.tomlocksapps.mbglwrapper.logger.Logger;

import java.util.List;
import java.util.Set;

/**
 * Created by walczewski on 20.12.2016.
 */

public class MapElementScalingManager implements MapboxMap.OnCameraChangeListener {

    private static final double ZOOM_DEADBAND = 0.25; // wartosc w obrebie ktorej uzytkonwik moze zoomowac mape i nie powoduje to zmienienania skali

    private final ElementProviderScaleSettings settings;
    private final Set<MapBoxElementsProvider> elementsProviders;
    private final MapElementController mapElementController;
    private MarkerViewManager markerViewManager;

    private double lastZoom = -1;

    public MapElementScalingManager(ElementProviderScaleSettings settings, Set<MapBoxElementsProvider> elementsProviders, MapElementController mapElementController) {
        this.settings = settings;
        this.elementsProviders = elementsProviders;
        this.mapElementController = mapElementController;
    }

    public void setMarkerViewManager(MarkerViewManager markerViewManager) {
        this.markerViewManager = markerViewManager;
    }

    @UiThread
    @Override
    public void onCameraChange(CameraPosition position) {
        if (Math.abs(lastZoom - position.zoom) < ZOOM_DEADBAND || position.zoom < 0.0f)
            return;

        Logger.getInstance().d("MarkerController - onCameraChange - MapElementScalingManager - position: " + position);

        applyScale(position.zoom);

        lastZoom = position.zoom;
    }

    private void applyScale(final double zoom) {
        Logger.getInstance().d("MarkerController - onCameraChange - MapElementScalingManager - applyScale: " + zoom);

        for (MapBoxElementsProvider elementsProvider : elementsProviders) {
            applyScaleForProvider(zoom, elementsProvider);
        }
    }

    @UiThread
    public void applyScaleForProviderOptions(MapBoxElementsProvider<Object> elementsProvider) {
        final IElementScalingSetting elementScallingSetting = settings.get(elementsProvider);
        if (elementScallingSetting != null) {
            Logger.getInstance().d("MarkerController - onCameraChange - MapElementScalingManager - applyScaleForProviderOptions - " + elementsProvider.getClass());
            //elementScallingSetting.getPolylineScale(elementsProvider.getPolyLinesOptions(), lastZoom);
            for (PolylineOptions polylineOptions : elementsProvider.getPolyLinesOptions()) {
                final float polylineWidth = elementScallingSetting.getPolylineWidth(lastZoom);

                polylineOptions.width(polylineWidth);
            }
        }
    }

    @UiThread
    private void applyScaleForProvider(final double zoom, final MapBoxElementsProvider<Object> elementsProvider) {
        final IElementScalingSetting elementScallingSetting = settings.get(elementsProvider);
        if (elementScallingSetting != null) {
            mapElementController.updatePolylines(new MapElementUpdateRunnable(elementsProvider.getClass()) {
                @Override
                protected void runOnUi() {
                    boolean anyUpdated = false;

                    final List<Polyline> polyLines = getElementsProvider().getPolyLines();

                    if(polyLines.size() > 0) {
                        final float newPolylineWidth = elementScallingSetting.getPolylineWidth(zoom);

                        Logger.getInstance().d("MarkerController - onCameraChange - MapElementScalingManager - applyScaleForProvider - zoom: " + zoom + " | width: " + newPolylineWidth);

                        for (Polyline polyline : polyLines) {
                            polyline.setWidth(newPolylineWidth);
                            anyUpdated = true;
                        }
                    }

                    if(anyUpdated) {
                        setAnyUpdaated(true);
                    }
                }
            });

            scaleMarkersFromProviders(elementScallingSetting, elementsProvider, zoom);
        }
    }

    @UiThread
    private void scaleMarkersFromProviders(final IElementScalingSetting scallingSetting, final MapBoxElementsProvider elementsProvider, final double zoom) {
        if (markerViewManager != null) {

            mapElementController.updateElements(new MapElementUpdateRunnable(elementsProvider.getClass()) {
                @Override
                protected void runOnUi() {
                    final List<MarkerView> markerViews = elementsProvider.getMarkerViews();

                    for (MarkerView markerView : markerViews) {
                        final View view = markerViewManager.getView(markerView);
                        if (view != null) {
                            final float scale = scallingSetting.getMarkerScale(view, markerView, zoom);

                            scaleMarkerView(view, markerView, scale);
                        }
                    }
                }
            });
        }
    }

    @UiThread
    public void scaleMarker(View view, MarkerView markerView) {
        for (MapBoxElementsProvider elementsProvider : elementsProviders) {
            if (elementsProvider.getMarkerViews().contains(markerView)) {
                final IElementScalingSetting elementScalingSetting = settings.get(elementsProvider);
                if (elementScalingSetting != null) {
                    final float scale = elementScalingSetting.getMarkerScale(view, markerView, lastZoom);

                    scaleMarkerView(view, markerView, scale);
                }
            }
        }
    }

    private void scaleMarkerView(View view, MarkerView markerView, float scale) {
        view.setPivotX(view.getWidth() * markerView.getAnchorU());
        view.setPivotY(view.getHeight() * markerView.getAnchorV());
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

}
