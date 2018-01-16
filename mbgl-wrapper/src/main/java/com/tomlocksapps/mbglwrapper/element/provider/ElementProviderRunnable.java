package com.tomlocksapps.mbglwrapper.element.provider;

import java.lang.reflect.ParameterizedType;

/**
 * Created by walczewski on 20.12.2016.
 */

public abstract class ElementProviderRunnable<T extends MapBoxElementsProvider> implements Runnable {
    private T provider;
    private final Class<T> typeClass;

    private boolean anyUpdated;

    public ElementProviderRunnable() {
        typeClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public ElementProviderRunnable(Class<T> provider) {
        this.typeClass = provider;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }

    public void setProvider(T provider) {
        this.provider = provider;
    }

    protected void setAnyUpdated(boolean anyUpdated) {
        this.anyUpdated = anyUpdated;
    }

    protected T getProvider() {
        return provider;
    }

    public boolean wasAnyPolylineUpdated() {
        return anyUpdated;
    }
}
