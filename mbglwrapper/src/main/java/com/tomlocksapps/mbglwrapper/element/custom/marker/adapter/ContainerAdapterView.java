package com.tomlocksapps.mbglwrapper.element.custom.marker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.tomlocksapps.mbglwrapper.element.custom.marker.view.ComparableMarkerView;
import com.tomlocksapps.mbglwrapper.element.scale.MapElementScalingManager;

import java.lang.reflect.ParameterizedType;


/**
 * Created by walczewski on 2016-07-06.
 */
public abstract class ContainerAdapterView<U extends ComparableMarkerView, K extends ContainerViewHolder> extends MapboxMap.MarkerViewAdapter<U> {

    protected LayoutInflater inflater;
    private Class containerClass;
    private MapElementScalingManager scalingManager;
    private final int containerId;

    /**
     * Create an instance of MarkerViewAdapter.
     *
     * @param context the context associated to a MapView
     */
    public ContainerAdapterView(Context context) {
        super(context);

        this.inflater = LayoutInflater.from(context);

        containerClass = (Class<U>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        containerId = provideContainerId();
    }

    protected abstract int provideContainerId();

    protected abstract View createConvertView(@NonNull U marker, @NonNull ViewGroup parent);

    protected abstract void bindViews(K viewHolder, View convertView);

    protected abstract void fillViews(K viewHolder, U marker);


    @Nullable
    @Override
    public final View getView(@NonNull U marker, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContainerViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = createConvertView(marker, parent);

            try {
                viewHolder = (ContainerViewHolder) containerClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if(viewHolder == null)
                return null;

            viewHolder.container = convertView.findViewById(containerId);

            bindViews((K) viewHolder, convertView);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (K) convertView.getTag();

        scalingManager.scaleMarker(convertView, marker);

        if(marker.isVisible()) {
            if(viewHolder.container.getVisibility() != View.VISIBLE) {
                viewHolder.container.setVisibility(View.VISIBLE);
            }
        } else {
            if(viewHolder.container.getVisibility() == View.VISIBLE) {
                viewHolder.container.setVisibility(View.GONE);
            }
        }

        fillViews((K) viewHolder, marker);

        return convertView;
    }

    public void setScalingManager(MapElementScalingManager scalingManager) {
        this.scalingManager = scalingManager;
    }

}
