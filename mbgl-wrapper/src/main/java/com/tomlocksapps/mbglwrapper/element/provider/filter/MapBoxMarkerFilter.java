package com.tomlocksapps.mbglwrapper.element.provider.filter;

import java.util.List;

/**
 * Pozwala na wstepne odfiltrowanie obiektow, zanim na ich podstawie zostana stworzeone Adnotacje mapowe.
 *
 * Created by walczewski on 2016-06-15.
 */
public interface MapBoxMarkerFilter<T> {
    List<T> filterObjects(List<T> objects);
}
