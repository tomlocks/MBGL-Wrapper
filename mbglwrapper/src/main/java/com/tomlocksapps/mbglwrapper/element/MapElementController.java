package com.tomlocksapps.mbglwrapper.element;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.tomlocksapps.mbglwrapper.element.custom.info.MarkerInfoWindowManager;
import com.tomlocksapps.mbglwrapper.element.custom.marker.adapter.ContainerAdapterView;
import com.tomlocksapps.mbglwrapper.element.custom.marker.adapter.GenericAdapterView;
import com.tomlocksapps.mbglwrapper.element.handler.MarkerClickHandler;
import com.tomlocksapps.mbglwrapper.element.handler.PoiClickHandlerManager;
import com.tomlocksapps.mbglwrapper.element.provider.MapBoxElementsProvider;
import com.tomlocksapps.mbglwrapper.element.scale.MapElementScalingManager;
import com.tomlocksapps.mbglwrapper.element.scale.settings.ElementProviderScaleSettings;
import com.tomlocksapps.mbglwrapper.element.scale.settings.IElementScalingSetting;
import com.tomlocksapps.mbglwrapper.element.visibility.MapElementVisibilityManager;
import com.tomlocksapps.mbglwrapper.element.visibility.settings.MapZoomSettings;
import com.tomlocksapps.mbglwrapper.element.visibility.settings.marker.MarkerZoomSetting;
import com.tomlocksapps.mbglwrapper.logger.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by walczewski on 2016-06-15.
 */
public class MapElementController implements IElementController{

    public static final long REFRESH_MARKER_DELAY = 1000;

    public static final double LAT_THRESHOLD = 0.00002d;
    public static final double DISTANCE_THRESHOLD = 20;
    private Context context;

    private boolean enabled = true;

    private MapboxMap map;
    private final MapElementVisibilityManager mapElementVisibilityManager;
    private final MapElementScalingManager elementScalingManager;
    private final ElementProviderScaleSettings elementProviderScaleSettings;
    private final Set<MapBoxElementsProvider> elementsProviders = new HashSet<>();
    private final Set<ContainerAdapterView> containerAdapterViews = new LinkedHashSet<>();

   // private IMapController mapBoxController;
    private PoiClickHandlerManager poiClickHandlerManager;
    private MarkerInfoWindowManager markerInfoWindowManager;
 //   private IMarkerAnimator markerAnimator;

    /**
     * Watek poboczny oraz handler dzialajacy na nim.
     */
    private HandlerThread handlerThread = new HandlerThread("MapElementProvidersThread");
    private Handler handler = new Handler();

    /**
     * Hadnler dzialajacy na watku glownym.
     */
    private Handler mainHandler = new Handler(Looper.getMainLooper());

//    private final NaviElementsProvider naviElementsProvider;
//    private final TrafficJamAdapterView trafficJamAdapterView;

    private final Logger logger = Logger.getInstance();

    public MapElementController(Context context) {
        this.context = context;

        markerInfoWindowManager = new MarkerInfoWindowManager(context);
        elementProviderScaleSettings = new ElementProviderScaleSettings();

        poiClickHandlerManager = new PoiClickHandlerManager();

        mapElementVisibilityManager = new MapElementVisibilityManager(context);
        elementScalingManager = new MapElementScalingManager(elementProviderScaleSettings, elementsProviders, this);

        addMarkerViewAdapter(new GenericAdapterView(context));

        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
    }


    /**
     * Callbacki z providerow. Dodawanie elementow odbywa sie na watku glownym.
     */
    private MapBoxElementsProvider.MapCallbacks mapCallbacks = new MapBoxElementsProvider.MapCallbacks() {

        @Override
        public void addMarkers(final List<MarkerView> toBeDeleted, final List<BaseMarkerViewOptions> toBeAdded, final MapBoxElementsProvider mapBoxElementsProvider) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    logger.d("----- UI ------ MarkerController - OnNewElementListener - onNewMarkers ----- UI ------");
                    logger.d("MapElementController - OnNewElementListener - addMarkers - " + mapBoxElementsProvider.getTag() + " - toBeDeleted: " + toBeDeleted.size() + " - toBeAdded: " + toBeAdded.size());

                    for (MarkerView markerView : toBeDeleted) {
                        map.removeMarker(markerView);
                    }

                    logger.d("MarkerController - OnNewElementListener - onNewMarkers - adding new Views: " + toBeAdded.size() + " | toBeDeleted size: " + toBeDeleted.size());
                    for (BaseMarkerViewOptions marker : toBeAdded) {
                        markerPositionValidator(marker);
                        final MarkerView markerView = map.addMarker(marker);
                        mapBoxElementsProvider.addMarkerView(markerView);

                        mapElementVisibilityManager.checkMarkerVisibility(mapElementVisibilityManager.getZoom(), markerView);
                    }

                    logger.d("MapElementController - OnNewElementListener - addMarkers - " + mapBoxElementsProvider.getTag() + " - toBeDeleted: " + toBeDeleted.size() + " - toBeAdded: " + toBeAdded.size() + " - UPDATED");


                }
            });
        }

        @Override
        public void removePolylines(final MapBoxElementsProvider mapBoxElementsProvider) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    logger.d("MarkerController - OnNewElementListener - removePolylines - | mapBoxElementsProvider: " + mapBoxElementsProvider);

                    logger.d("MapElementController - OnNewElementListener - removePolylines - " + mapBoxElementsProvider.getTag());

                    final List<Polyline> polyLines = mapBoxElementsProvider.getPolyLines();

                    for (final Polyline polyLine : polyLines) {
                        map.removePolyline(polyLine);
                    }

                    mapBoxElementsProvider.clearPolylinesList();

                    logger.d("MapElementController - OnNewElementListener - removePolylines - " + mapBoxElementsProvider.getTag() + " - UPDATED ");
                }
            });
        }

        @Override
        public void addPolyLines(final List<PolylineOptions> polyLines, final MapBoxElementsProvider mapBoxElementsProvider) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    logger.d("MarkerController - OnNewElementListener - onNewPolyLines - polylines: " + polyLines.size() + " | mapBoxElementsProvider: " + mapBoxElementsProvider);

                    logger.d("MapElementController - OnNewElementListener - addPolyLines - " + mapBoxElementsProvider.getTag() + " - " + polyLines.size());

                    final List<PolylineOptions> polyLine = mapBoxElementsProvider.getPolyLinesOptions();

                    elementScalingManager.applyScaleForProviderOptions(mapBoxElementsProvider);

                    final List<Polyline> polylines = map.addPolylines(polyLine);
                    mapBoxElementsProvider.addPolylines(polylines);

                    logger.d("MapElementController - OnNewElementListener - addPolyLines - " + mapBoxElementsProvider.getTag() + " - " + polyLines.size() + " - UPDATED ");

                }
            });
        }

        @Override
        public void updatePolylines(final List<Polyline> polylines, final MapBoxElementsProvider mapBoxElementsProvider) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    logger.d("MarkerController - OnNewElementListener - updatePolylines - polylines: ");

                    logger.d("MapElementController - OnNewElementListener - updatePolylines - " + mapBoxElementsProvider.getTag() + " - " + polylines.size());
                    for (Polyline polyline : polylines) {
                        map.updatePolyline(polyline);
                    }
                    logger.d("MapElementController - OnNewElementListener - updatePolylines - " + mapBoxElementsProvider.getTag() + " - " + polylines.size() + " - UPDATED");


                }
            });
        }

        @Override
        public void onMapInteractionStopped(final MapBoxElementsProvider mapBoxElementsProvider) {
            //todo zakleszczanie watkow
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    //todo usuniecie zbednych logow
                    logger.d("MapElementController - OnNewElementListener - all Objects - markers: " + map.getMarkers().size() + " | polylines: " + map.getPolylines().size() + " | annotations: " + map.getAnnotations().size());
                    logger.d("MapElementController - OnNewElementListener - " + mapBoxElementsProvider.getTag() + " - Objects - markers: " + mapBoxElementsProvider.getMarkerViews().size() + " | polylines: " + mapBoxElementsProvider.getPolyLines().size() + " | annotations: " + map.getAnnotations().size());

                    logger.d("MarkerController - OnNewElementListener - current Objects - markers: " + map.getMarkers().size() + " | polylines: " + map.getPolylines().size() + " | annotations: " + map.getAnnotations().size());
                    logger.d("MarkerController - OnNewElementListener - current Provider Objects - markers: " + mapBoxElementsProvider.getMarkerViews().size() + " | polylines: " + mapBoxElementsProvider.getPolyLines().size() + " | annotations: " + map.getAnnotations().size());

                    mapBoxElementsProvider.releaseLock();

                    logger.d("MapElementController - OnNewElementListener - " + mapBoxElementsProvider.getTag() + " - Objects - markers: " + mapBoxElementsProvider.getMarkerViews().size() + " | polylines: " + mapBoxElementsProvider.getPolyLines().size() + " | annotations: " + map.getAnnotations().size() + " - UPDATED ");

                }
            });

        }


    };

    /**
     * Sprawdza czy nowo dodawany marker nie jest za blisko innego. Gdy tak jest to nastepuje lekka zmiana jego wspolrzednych.
     *
     * @param toBeValidated Marker do sprawdzenia
     */
    private void markerPositionValidator(BaseMarkerViewOptions toBeValidated) {
        final List<Marker> markers = map.getMarkers();

        Collections.sort(markers, new Comparator<Marker>() {
            @Override
            public int compare(Marker lhs, Marker rhs) {
                if (lhs.getPosition().getLatitude() > rhs.getPosition().getLatitude()) {
                    return 1;
                } else if (lhs.getPosition().getLatitude() < rhs.getPosition().getLatitude()) {
                    return -1;
                }

                return 0;
            }
        });

        for (Marker mapMarker : markers) {
            final LatLng position = toBeValidated.getPosition();
            double mapLat = mapMarker.getPosition().getLatitude();
            double newLat = position.getLatitude();
            final double distanceTo = mapMarker.getPosition().distanceTo(toBeValidated.getPosition());

            double diff = mapLat - newLat;

            if (Math.abs(diff) < LAT_THRESHOLD && distanceTo < DISTANCE_THRESHOLD) {
                newLat = mapLat + LAT_THRESHOLD;

                toBeValidated.position(new LatLng(newLat, position.getLongitude()));
            }
        }
    }

    @Override
    public void initialize() {
//        markerAnimator.initialize();
    }

    @Override
    public void uninitialize() {
//        markerAnimator.uninitialize();
    }

    @Override
    public void setMap(MapboxMap map) {
        this.map = map;

        for (ContainerAdapterView adapterView : containerAdapterViews) {
            map.getMarkerViewManager().addMarkerViewAdapter(adapterView);
        }


        map.getMarkerViewManager().setOnMarkerViewClickListener(onMarkerClickListener);

        map.setOnCameraChangeListener(onCameraChangeListener);



//        map.getMarkerViewManager().addMarkerViewAdapter(new PoiAdapterView(context, elementScalingManager));
//        map.getMarkerViewManager().addMarkerViewAdapter(trafficJamAdapterView);
//        map.getMarkerViewManager().addMarkerViewAdapter(new GenericAdapterView(context, elementScalingManager));

        map.setInfoWindowAdapter(markerInfoWindowManager);

        mapElementVisibilityManager.setMap(map);


//        markerAnimator.setMap(map);
        elementScalingManager.setMarkerViewManager(map.getMarkerViewManager());

        refreshProviders();
    }

    private MapboxMap.OnMarkerViewClickListener onMarkerClickListener = new MapboxMap.OnMarkerViewClickListener() {
        @Override
        public boolean onMarkerClick(@NonNull Marker marker, @NonNull View view, @NonNull MapboxMap.MarkerViewAdapter adapter) {

            if(markerInfoWindowManager.shouldShowInfoWindow(marker))
                return false;

            if(marker instanceof MarkerView && !((MarkerView) marker).isVisible())
                return true;

            return poiClickHandlerManager.handleClick(marker);
        }
    };

    /**
     * Odwieza providery. Powoduje to dodanie do mapy elementow z ich domyslnych list.
     */
    private void refreshProviders() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (MapBoxElementsProvider provider : elementsProviders) {
                    provider.refresh();
                }
            }
        }, REFRESH_MARKER_DELAY);
    }

    private MapboxMap.OnCameraChangeListener onCameraChangeListener = new MapboxMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition position) {
            mapElementVisibilityManager.onCameraChange(position);
          //  trafficJamAdapterView.setZoomLevel(mapElementVisibilityManager.getZoom());
            elementScalingManager.onCameraChange(position);
        }
    };


    @Override
    public void provideObjects(final Collection<? extends Object> objects) {
        if (!enabled || map == null)
            return;

        logger.d("MarkerController - MapBoxMarkerProvider - new Objects - thread: " + Thread.currentThread().hashCode());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (MapBoxElementsProvider provider : elementsProviders) {
                    logger.d("MapElementController - provideObjects - " + provider.getTag() + " - " + objects.size());
                    if (provider.createNewMapObject(objects)) {

                        break;
                    }
                }
            }
        });
    }

    @Override
    public void clearObjects(final Class modelClass) {
        logger.d("MarkerController - MapBoxMarkerProvider - clearObjects - thread: " + Thread.currentThread().hashCode());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (MapBoxElementsProvider provider : elementsProviders) {
                    if (provider.getTypeClass().equals(modelClass)) {
                        provider.clearAllElements();

                        break;
                    }

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        mainHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        handlerThread.quit();
        handlerThread.interrupt();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            refreshProviders();
        } else {
            if (map != null)
                map.removeAnnotations();
        }
    }

    @Override
    public void updateElements(final MapElementUpdateRunnable elementUpdateRunnable) {
        logger.d("MarkerController - MapBoxMarkerProvider - updatePolylines - thread: " + Thread.currentThread().hashCode());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (MapBoxElementsProvider provider : elementsProviders) {
                    if (provider.getClass().equals(elementUpdateRunnable.getProviderClass())) {

                        elementUpdateRunnable.setElementsProvider(provider);
                        mainHandler.post(elementUpdateRunnable);

                        logger.d("UpdateElements - MapElementUpdateRunnable - lock - acquiring");
                        provider.acquireLock();
                        logger.d("UpdateElements - MapElementUpdateRunnable - lock -  Released - anyUpdated: " + elementUpdateRunnable.wasAnyUpdaated());

                        break;
                    }
                }
            }
        });
    }

    @Override
    public void updatePolylines(final MapElementUpdateRunnable elementUpdateRunnable) {
        logger.d("UpdatePolylines - MarkerController - MapBoxMarkerProvider - updatePolylines - thread: " + Thread.currentThread().hashCode());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (MapBoxElementsProvider provider : elementsProviders) {
                    if (provider.getClass().equals(elementUpdateRunnable.getProviderClass())) {

                        elementUpdateRunnable.setElementsProvider(provider);
                        mainHandler.post(elementUpdateRunnable);

                        logger.d("UpdatePolylines - MapElementUpdateRunnable - lock - acquiring");
                        provider.acquireLock();
                        logger.d("UpdatePolylines - MapElementUpdateRunnable - lock -  Released - anyUpdated: " + elementUpdateRunnable.wasAnyUpdaated());

                        if(elementUpdateRunnable.wasAnyUpdaated())
                            provider.updatePolylines();
                        break;
                    }
                }
            }
        });
    }

    @Override
    public Set<MapBoxElementsProvider> getProviders() {
        return elementsProviders;
    }

    @Override
    public <T extends MapBoxElementsProvider >T getProvider(Class<T> providerClass) {
        for (MapBoxElementsProvider mapBoxElementsProvider : elementsProviders) {
            if(providerClass.isAssignableFrom(mapBoxElementsProvider.getClass()))
                return (T) mapBoxElementsProvider;
        }

        return null;
    }

    @Override
    public <T extends MapBoxElementsProvider> boolean hasProvider(Class<T> providerClass) {
        for (MapBoxElementsProvider mapBoxElementsProvider : elementsProviders) {
            if(providerClass.isAssignableFrom(mapBoxElementsProvider.getClass()))
                return true;
        }

        return false;
    }

    public void addElementProvider(MapBoxElementsProvider mapBoxElementsProvider) {
        mapBoxElementsProvider.setMapCallbacks(mapCallbacks);

        elementsProviders.add(mapBoxElementsProvider);
    }

    public void addMarkerViewAdapter(ContainerAdapterView adapterView) {
        if(map != null)
            throw new IllegalStateException("You cannot add MarkerViewAdapter when map is initialized");

        adapterView.setScalingManager(elementScalingManager);
        containerAdapterViews.add(adapterView);
    }

    public void addScaleSettings(Class<? extends MapBoxElementsProvider> key, IElementScalingSetting value) {
        elementProviderScaleSettings.put(key, value);
    }

    public void addZoomSetting(MarkerZoomSetting markerZoomSetting) {
        mapElementVisibilityManager.getMapZoomSettings().putZoomSetting(markerZoomSetting);
    }

    public void addClickHandler(MarkerClickHandler value) {
        poiClickHandlerManager.putHandler(value);
    }
}
