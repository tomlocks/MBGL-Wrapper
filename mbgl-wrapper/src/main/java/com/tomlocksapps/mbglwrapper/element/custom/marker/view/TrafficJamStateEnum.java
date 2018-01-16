package com.tomlocksapps.mbglwrapper.element.custom.marker.view;

/**
 * Created by walczewski on 21.09.2016.
 */
public enum TrafficJamStateEnum {
    // uwaga wazna jest kolejnosc, od najwiekszego zoomu, do najmniejszego
    BIG(12d, 15), SMALL(10d, 18);

    TrafficJamStateEnum(double minZoomLevel, int numberOfMarkers) {
        this.minZoomLevel = minZoomLevel;
        this.numberOfMarkers = numberOfMarkers;
    }

    public static TrafficJamStateEnum getStateForZoom(double zoom) {
        for (TrafficJamStateEnum state : TrafficJamStateEnum.values()) {
            if(zoom > state.getMinZoomLevel()) {
                return state;
            }
        }

        return null;
    }

    public double getMinZoomLevel() {
        return minZoomLevel;
    }

    public int getNumberOfMarkers() {
        return numberOfMarkers;
    }

    private double minZoomLevel;
    private int numberOfMarkers;
}
