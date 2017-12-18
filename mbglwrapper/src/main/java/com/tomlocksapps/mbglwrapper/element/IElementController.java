package com.tomlocksapps.mbglwrapper.element;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.tomlocksapps.mbglwrapper.element.provider.MapBoxElementsProvider;

import java.util.Collection;
import java.util.Set;

/**
 * Interfejs przez ktory odbywa sie dodawanie oraz usuwanie elementow z mapy.
 *
 * Created by walczewski on 2016-06-15.
 */
public interface IElementController {
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

    /**
     * Metoda pozwalajaca na dodanie elementow do mapy. Za dodanie elementow do mapy odpowieada c, ktory definiuje jakiego rodzaju
     * obiekty przyjmuje, a nastepnie tworzy z nich markery lub polylines.
     *
     * @param objects
     */
    void provideObjects(Collection<? extends Object> objects);

    /**
     * Usuwa z mapy wszystkie elementy dnaego typu, np. POIe, nawigacje. Nalezy tutaj podac klase szablonu z danego {@link MapBoxElementsProvider}.
     *
     * @param modelClass - klasa szablonu z {@link MapBoxElementsProvider}
     */
    void clearObjects(Class modelClass);

    /**
     * Wywolywana przy niszczeniu mapy.
     */
    void onDestroy();

    /**
     * Decyduje czy obiekty moga byc dodawane na mapie.
     *
     * @param enabled True jesli obiekty moga byc dodawane do mapy, false gdy maja zostac usuniete.
     */
    void setEnabled(boolean enabled);

    /**
     * Aktualizuje obiekty Polylines na mapie. Runnable odpalane jest na wątku głównym.
     */
    void updatePolylines(MapElementUpdateRunnable mapElementUpdateRunnable);

    /**
     * Aktualizuje elementy na mapie, np. MarkerViews. Runnable odpalane jest na wątku głównym.
     *
     */
    void updateElements(MapElementUpdateRunnable mapElementUpdateRunnable);


    /**
     * Zwraca listę providerow.
     *
     */
    Set<MapBoxElementsProvider> getProviders();

    <T extends MapBoxElementsProvider >T getProvider(Class<T> providerClass);

    <T extends MapBoxElementsProvider > boolean hasProvider(Class<T> providerClass);
}
