package com.tomlocksapps.mbglwrapper.element.custom;

import android.location.Location;

/**
 * Interfejs, ktory implementuja markery, ktore musza wykonac dodatkowe kalkulacje zwiazane z lokalizacja zanim zostanie sprawdzone czy moga sie pojawic na mapie.
 *
 * Created by walczewski on 2016-07-06.
 */
public interface ExtraMarkerLocationCalculation {
    void calculate(Location location);
}
