package com.tomlocksapps.mbglwrapper.element.scale.settings;

import java.util.HashMap;
import java.util.Map;

import com.tomlocksapps.mbglwrapper.element.provider.MapBoxElementsProvider;

/**
 * Created by walczewski on 20.12.2016.
 */

public class ElementProviderScaleSettings {

    private final Map<Class <? extends MapBoxElementsProvider>, IElementScalingSetting> map = new HashMap<>();

    public void put(Class<? extends MapBoxElementsProvider> key, IElementScalingSetting value) {
        final IElementScalingSetting scallingSetting = map.put(key, value);

        if(scallingSetting != null)
            throw new IllegalStateException("You've already put scale settings for this provider");
    }

    public IElementScalingSetting get(MapBoxElementsProvider key) {
        return map.get(key.getClass());
    }
}
