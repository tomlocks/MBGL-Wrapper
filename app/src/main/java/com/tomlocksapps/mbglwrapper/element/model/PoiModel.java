package com.tomlocksapps.mbglwrapper.element.model;

import android.location.Location;

/**
 * Created by walczewski on 17.12.2017.
 */

public class PoiModel {
    private final String url;
    private final boolean isClickable;
    private final String iconUrl;
    private final Location location;

    public PoiModel(String url, boolean isClickable, String iconUrl, Location location) {
        this.url = url;
        this.isClickable = isClickable;
        this.iconUrl = iconUrl;
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PoiModel)) return false;

        PoiModel poiModel = (PoiModel) o;

        if (isClickable != poiModel.isClickable) return false;
        if (url != null ? !url.equals(poiModel.url) : poiModel.url != null) return false;
        if (iconUrl != null ? !iconUrl.equals(poiModel.iconUrl) : poiModel.iconUrl != null)
            return false;
        return location != null ? location.equals(poiModel.location) : poiModel.location == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (isClickable ? 1 : 0);
        result = 31 * result + (iconUrl != null ? iconUrl.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
