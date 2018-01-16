package com.tomlocksapps.mbglwrapper.element;

import com.tomlocksapps.mbglwrapper.element.provider.MapBoxElementsProvider;
import com.tomlocksapps.mbglwrapper.logger.Logger;

/**
 * Created by walczewski on 10.01.2017.
 */

public abstract class MapElementUpdateRunnable<T extends MapBoxElementsProvider> implements Runnable {

    private MapBoxElementsProvider elementsProvider;
    private final Class<T> providerClass;
    private boolean anyUpdaated;

    public MapElementUpdateRunnable(Class<? extends MapBoxElementsProvider> providerClass) {
            this.providerClass = (Class<T>) providerClass;
    }

    public Class<T> getProviderClass() {
        return providerClass;
    }

    public void setElementsProvider(T elementsProvider) {
        this.elementsProvider = elementsProvider;
    }

    public MapBoxElementsProvider getElementsProvider() {
        return elementsProvider;
    }

    protected void setAnyUpdaated(boolean anyUpdaated) {
        this.anyUpdaated = anyUpdaated;
    }

    public boolean wasAnyUpdaated() {
        return anyUpdaated;
    }

    @Override
    public final void run() {
        Logger.getInstance().d("MapElementUpdateRunnable - run()");
        runOnUi();
        Logger.getInstance().d("MapElementUpdateRunnable - releasing Lock");
        elementsProvider.releaseLock();
    }

    protected abstract void runOnUi();

}
