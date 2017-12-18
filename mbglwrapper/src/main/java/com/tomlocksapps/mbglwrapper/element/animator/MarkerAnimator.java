package com.tomlocksapps.mbglwrapper.element.animator;

import android.view.animation.Animation;
import com.mapbox.mapboxsdk.maps.MapboxMap;
/**
 * Created by walczewski on 2016-07-07.
 */
public class MarkerAnimator implements IMarkerAnimator {

    private MapboxMap map;

    private Animation animation;

  //  private ContainerViewHolder lastAnimatedViewHolder;

    public MarkerAnimator() {

     //   animation = AnimationUtils.loadAnimation(App.getContext(), R.anim.blink_poi_alpha);
    }

  /*  private ContainerViewHolder findAssociatedMarker(Object o) {
        for (Marker marker : map.getMarkers()) {
            if(marker instanceof ComparableMarkerView) {
                final ComparableMarkerView comparableMarkerView = (ComparableMarkerView) marker;

                if(comparableMarkerView.getComparableObject().equals(o)) {
                    final View view = map.getMarkerViewManager().getView(comparableMarkerView);
                    if(view == null)
                        return null;

                    ContainerViewHolder containerViewHolder = (ContainerViewHolder) view.getTag();

                    return containerViewHolder;
                }
            }
        }

        return null;
    }*/

    @Override
    public boolean animateObjectOnMap(Object o) {
   /*     final ContainerViewHolder containerViewHolder = findAssociatedMarker(o);

        if(containerViewHolder != null) {
            if(lastAnimatedViewHolder!=null)
                lastAnimatedViewHolder.container.clearAnimation();

            containerViewHolder.container.startAnimation(animation);
            lastAnimatedViewHolder = containerViewHolder;
        }*/

        return false;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void uninitialize() {

    }
    @Override
    public void setMap(MapboxMap map) {
        this.map = map;
    }


}
