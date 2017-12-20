package com.tomlocksapps.mbglwrapper.element.provider;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.tomlocksapps.mbglwrapper.element.custom.marker.option.ComparableMarkerViewOptions;
import com.tomlocksapps.mbglwrapper.element.custom.marker.view.ComparableMarkerView;
import com.tomlocksapps.mbglwrapper.element.provider.filter.MapBoxMarkerFilter;
import com.tomlocksapps.mbglwrapper.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Klasa odpowiedzialna za dostarczenie elementow na mape, np. Markerow czy Polylines. Przy rozszerzaniu przyjmuje ona klase typu T, z ktorej tworzone sa Annotacje na mapie.
 * <p>
 * Created by walczewski on 2016-06-15.
 */
public abstract class MapBoxElementsProvider<T> {

    private OnNewElementsListener onNewElementsListener;

    private Class<T> typeClass;
    private MapCallbacks mapCallbacks;
    private final Object lock = new Object();
    private final String tag;

    private boolean polylinesEnabled = true;
    private boolean markersEnabled = true;

    /**
     * Lista markerow, ktore maja byc dodane do mapy.
     */
    private final List<BaseMarkerViewOptions> markersOptions = new ArrayList<>();

    /**
     * List polylines, ktore maja byc dodane do mapy.
     */
    private final List<PolylineOptions> polyLinesOptions = new ArrayList<>();

    /**
     * Lista markerow aktualnie dodanych do mapy.
     */
    private final List<MarkerView> markerViews = new ArrayList<>();

    /**
     * List polylines aktualnie dodanych do mapy.
     */
    private final List<Polyline> polyLines = new ArrayList<>();

    private final List<MapBoxMarkerFilter> markerFilters = new ArrayList<>();

    public MapBoxElementsProvider() {
        Class<? extends MapBoxElementsProvider> providerClass = findMapBoxElementsProviderClass(getClass());

        typeClass = (Class<T>) ((ParameterizedType) providerClass.getGenericSuperclass()).getActualTypeArguments()[0];
        tag = provideElementProviderTag();
    }

    private Class<? extends MapBoxElementsProvider> findMapBoxElementsProviderClass(Class clazz) { // this method may fail
        if(clazz.getGenericSuperclass() instanceof ParameterizedType) {
            String s = clazz.toString();
            return clazz;
        } else {
            Class superclass = clazz.getSuperclass();
            return findMapBoxElementsProviderClass(superclass);
        }
    }

    protected abstract String provideElementProviderTag();

    public void setMapCallbacks(MapCallbacks mapCallbacks) {
        this.mapCallbacks = mapCallbacks;
    }

    public void setOnNewElementsListener(OnNewElementsListener onNewElementsListener) {
        this.onNewElementsListener = onNewElementsListener;
    }

    /**
     * Metoda ta tworzy adnotacje mapowe z listy, jezeli obiekty tej list sa typu T.
     *
     * @param objects Lista obiektow typu T
     * @return true jesli obiekty listy sa typu T, false w przciwnym wypadku.
     */
    public boolean createNewMapObject(Collection<T> objects) {
        if (objects.size() == 0)
            return false;

        if (!typeClass.isAssignableFrom(objects.iterator().next().getClass()))
            return false;

        Logger.getInstance().d("MarkerController - MapBoxMarkerProvider - createNewMarkersFromObjects : " + getClass().getName() + " | objects: " + objects.size() + " | thread: " + Thread.currentThread().hashCode());


        List<T> filteredObjects = new ArrayList<>(objects);

        synchronized (markerFilters) {
            for (MapBoxMarkerFilter filter : markerFilters) {
                filteredObjects = filter.filterObjects(filteredObjects);
            }
        }


        markersOptions.clear();
        polyLinesOptions.clear();

        for (T object : filteredObjects) {
            final BaseMarkerViewOptions[] markersFromObject = createMarkerViewsFromObject(object);

            if (markersFromObject != null) {
                for (BaseMarkerViewOptions markerFromObject : markersFromObject) {
                    if (markerFromObject != null)
                        markersOptions.add(markerFromObject);
                }
            }

            final PolylineOptions polylineFromObject = createPolylineFromObject(object);
            if (polylineFromObject != null)
                polyLinesOptions.add(polylineFromObject);
        }


        Logger.getInstance().d("MarkerController - MapBoxMarkerProvider - createNewMarkersFromObjects : " + getClass().getName() + " | objects: " + objects.size() + " | filteredObjects: " + filteredObjects.size());


        if (markersEnabled)
            onNewMarkers(markersOptions);

        mapCallbacks.removePolylines(this);

        if (polylinesEnabled)
            mapCallbacks.addPolyLines(polyLinesOptions, this);

        onProviderInteractionStopped();


        return true;
    }


    /**
     * Wywolywana, w momencie, gdy provider skonczyl juz swoja robote, ale musi oczekiwac za watkiem glownym, na ktorym odbywa sie interakcja z mapa.
     */
    private void onProviderInteractionStopped() {
        mapCallbacks.onMapInteractionStopped(this);
        acquireLock();
    }

    /**
     * Metoda wywolywana po stworzeniu nowych Marker Options. Sprawdza czy dane MarkerOptions sa juz na mapie, zapobiega to ich powtornemu dodaniu.
     *
     * @param markers
     */
    private void onNewMarkers(final List<BaseMarkerViewOptions> markers) {
        Logger.getInstance().d("MarkerController - OnNewElementListener - onNewMarkers - markers: " + markers.size() + " | mapBoxElementsProvider: " + this);

        final List<BaseMarkerViewOptions> markersOptionsAlreadyOnMap = new ArrayList<>();
        final List<MarkerView> markersToBeDeletedFromMap = new ArrayList<>();

        for (final Iterator<MarkerView> iterator = markerViews.iterator(); iterator.hasNext(); ) {
            MarkerView marker = iterator.next();
            if (marker instanceof ComparableMarkerView) {
                final ComparableMarkerView comparableMarker = (ComparableMarkerView) marker;

                final ComparableMarkerViewOptions markerOptionAlreadyOnMap = isMarkerOnNewList(markers, comparableMarker);

                if (markerOptionAlreadyOnMap != null) {
                    markersOptionsAlreadyOnMap.add(markerOptionAlreadyOnMap);
                    Logger.getInstance().d("MarkerController - OnNewElementListener - onNewMarkers - found  comparable Existing Marker");
                } else {
                    Logger.getInstance().d("MarkerController - OnNewElementListener - onNewMarkers - removing comparable non existing marker");
                    markersToBeDeletedFromMap.add(marker);


                    iterator.remove();
                }

            } else {
                Logger.getInstance().d("MarkerController - OnNewElementListener - onNewMarkers - removing non existing marker");
                markersToBeDeletedFromMap.add(marker);

                iterator.remove();
            }
        }

        markersOptions.removeAll(markersOptionsAlreadyOnMap);

        mapCallbacks.addMarkers(markersToBeDeletedFromMap, markersOptions, this);
    }

    /**
     * Sprawdza czy marker jest juz na mapie.
     *
     * @param markers
     * @param markerView
     * @return
     */
    private ComparableMarkerViewOptions isMarkerOnNewList(final List<BaseMarkerViewOptions> markers, ComparableMarkerView markerView) {
        for (BaseMarkerViewOptions markerOptions : markers) {
            if (markerOptions instanceof ComparableMarkerViewOptions) {
                final ComparableMarkerViewOptions comparableMarkerOptions = (ComparableMarkerViewOptions) markerOptions;


                if (comparableMarkerOptions.getComparableObject().equals(markerView.getComparableObject()))
                    return comparableMarkerOptions;

            }
        }

        return null;
    }

    /**
     * Dodanie filtra.
     *
     * @param object
     * @return
     */
    protected boolean addFilter(MapBoxMarkerFilter<T> object) {
        synchronized (markerFilters) {
            return markerFilters.add(object);
        }
    }

    /**
     * Usuniecie wszystkich filtrow.
     */

    protected void clearFilters() {
        synchronized (markerFilters) {
            markersOptions.clear();
        }
    }

    /**
     * Zwraca klase - szablon dla tego providera.
     *
     * @return Klasa - szablon.
     */
    public Class<T> getTypeClass() {
        return typeClass;
    }

    /**
     * Zwraca linie do wyrysowania na mapie.
     *
     * @return linie do wyrysowania na mapie.
     */
    public List<PolylineOptions> getPolyLinesOptions() {

        return polyLinesOptions;
    }

    /**
     * Zwraca markery do wyrysowania na mapie.
     *
     * @return Lista markerow do wyrysowania
     */
    public List<BaseMarkerViewOptions> getMarkersOptions() {

        return markersOptions;

    }

    /**
     * Tworzy liste markerow na podstawie obiektu typu T.
     *
     * @param object Obiekt z ktorego tworzone sa markery
     * @return
     */
    protected BaseMarkerViewOptions[] createMarkerViewsFromObject(T object) {
        return null;
    }

    /**
     * Tworzy liste polylines na podstawie obiektu typu T.
     *
     * @param object Obiekt z ktorego tworzone sa polylines
     * @return
     */
    protected PolylineOptions createPolylineFromObject(T object) {
        return null;
    }

    /**
     * Zwraca liste polylines aktualnie znajdujacyh sie na mapie
     *
     * @return
     */
    public List<Polyline> getPolyLines() {
        return polyLines;
    }

    /**
     * Zwraca liste markerow aktualnie znajdujacych sie na mapie.
     *
     * @return
     */
    public List<MarkerView> getMarkerViews() {

        return markerViews;

    }

    /**
     * Dodaje marker do listy, ktory zostal dodany do mapy.
     *
     * @param object marker dodany do mapy
     */
    public void addMarkerView(MarkerView object) {
        markerViews.add(object);
    }

    /**
     * Dodaje liste wszystkich polylines, ktore zostaly dodane do mapy.
     *
     * @param collection
     * @return
     */
    public boolean addPolylines(List<Polyline> collection) {
        if (onNewElementsListener != null)
            onNewElementsListener.onNewPolylinesAdded(collection);

        return polyLines.addAll(collection);
    }

    /**
     * Czysci liste polylines z mapy.
     */
    public void clearPolylinesList() {

        polyLines.clear();

    }

    public interface MapCallbacks {
        void addMarkers(final List<MarkerView> toBeDeleted, final List<BaseMarkerViewOptions> toBeAdded, final MapBoxElementsProvider mapBoxElementsProvider);

        void updatePolylines(final List<Polyline> polylines, final MapBoxElementsProvider mapBoxElementsProvider);

        void removePolylines(final MapBoxElementsProvider mapBoxElementsProvider);

        void addPolyLines(final List<PolylineOptions> polyLines, final MapBoxElementsProvider mapBoxElementsProvider);

        void onMapInteractionStopped(final MapBoxElementsProvider mapBoxElementsProvider);

    }

    /**
     * Pobiera ponownie liste obietkow dla danego providera, a na ich podstawie tworzy elementy. Tylko gdy provideDefaultObjectLocation zwraca jakas liste.
     */
    public void refresh() {
        if (provideDefaultObjectLocation() != null) {
            Logger.getInstance().d("MarkerController - MapBoxMarkerProvider - refresh : " + getClass().getName());

            createNewMapObject(provideDefaultObjectLocation());
        }
    }

    /**
     * Pozwala na wskazanie domyslnej listy, na podstawie ktorej maja zostac stworzone adnotacje mapowe.
     *
     * @return lista markerow
     */
    protected abstract List<T> provideDefaultObjectLocation();

    /**
     * Czysci wszsystkie adnotacje z mapy, ktore zostaly dodane przez ten provider.
     */
    public void clearAllElements() {
        Logger.getInstance().d("MarkerController - clearAllElements - | " + this);

        final int markerSize = markersOptions.size();
        final int polyLinesSize = polyLinesOptions.size();


        if (markerSize > 0) {
            markersOptions.clear();

            if (mapCallbacks != null) {
                onNewMarkers(new ArrayList<BaseMarkerViewOptions>());
            }
        }

        if (polyLinesSize > 0) {
            polyLinesOptions.clear();

            if (mapCallbacks != null) {
                mapCallbacks.removePolylines(this);
                mapCallbacks.addPolyLines(polyLinesOptions, this);
            }
        }


        onProviderInteractionStopped();
    }

    /**
     * Powoduje zatrzymanie watku na czas 10.
     */
    public void acquireLock() {

        synchronized (lock) {
            try {
                Logger.getInstance().d("MarkerController - MapElementUpdateRunnable - acquiring - thread: " + Thread.currentThread());
                Logger.getInstance().d("MarkerController - MapBoxMarkerProvider - locking - thread: " + Thread.currentThread().hashCode());
                lock.wait(10_000);
            } catch (InterruptedException e) {
                return;
            }
        }

    }

    /**
     * Sciaga blokade czasowa na watek.
     */
    public void releaseLock() {
        synchronized (lock) {
            Logger.getInstance().d("MarkerController - MapElementUpdateRunnable - releaseLock Released - thread: " + Thread.currentThread());
            Logger.getInstance().d("MarkerController - MapBoxMarkerProvider - releaseLock - thread: " + Thread.currentThread().hashCode());
            lock.notifyAll();
        }
    }

    public void updatePolylines() {
        if (!polylinesEnabled)
            return;

        mapCallbacks.updatePolylines(polyLines, this);
        onProviderInteractionStopped();
    }

    public void togglePolylinesEnabled() {
        this.polylinesEnabled = !polylinesEnabled;
    }

    public void toggleMarkersEnabled() {
        this.markersEnabled = !markersEnabled;
    }

    public String getTag() {
        return tag;
    }
}
