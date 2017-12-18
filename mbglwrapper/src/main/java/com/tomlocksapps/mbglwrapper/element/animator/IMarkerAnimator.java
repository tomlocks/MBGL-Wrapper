package com.tomlocksapps.mbglwrapper.element.animator;

import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by walczewski on 2016-07-07.
 */
public interface IMarkerAnimator {
    /**
     * Animuje marker z danym obiektem na mapie.
     *
     * @param o obiekt, z którym powiązany jest marker na mapie
     * @return true, jezeli obiekt zostal znaleziony
     */
    boolean animateObjectOnMap(Object o);

    /**
     * Inicjalizuje kontroler(bus itp.)
     */
    void initialize();

    /**
     * Odinicjalizowuje kontroler.
     */
    void uninitialize();

    /**
     * Ustawia mapę
     *
     * @param map Mapa Mapa MapBoxowa
     */
    void setMap(MapboxMap map);
}
