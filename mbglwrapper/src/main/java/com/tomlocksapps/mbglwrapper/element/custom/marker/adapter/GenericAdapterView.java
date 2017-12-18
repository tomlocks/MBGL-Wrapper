package com.tomlocksapps.mbglwrapper.element.custom.marker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tomlocksapps.mbglwrapper.R;
import com.tomlocksapps.mbglwrapper.element.custom.marker.view.impl.GenericMarkerView;
import com.tomlocksapps.mbglwrapper.element.scale.MapElementScalingManager;


/**
 * Created by walczewski on 2016-06-16.
 */
public class GenericAdapterView extends ContainerAdapterView<GenericMarkerView, GenericAdapterView.ViewHolder> {

    private LayoutInflater inflater;

    public GenericAdapterView(@NonNull Context context) {
        super(context);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    protected int provideContainerId() {
        return R.id.container;
    }


    @Override
    public View createConvertView(@NonNull GenericMarkerView marker, @NonNull ViewGroup parent) {
        View convertView = inflater.inflate(R.layout.view_generic_marker, parent, false);

        return convertView;
    }

    @Override
    protected void bindViews(ViewHolder viewHolder, View convertView) {
        viewHolder.icon = (ImageView) convertView.findViewById(R.id.imageView);
    }

    @Override
    protected void fillViews(ViewHolder viewHolder, GenericMarkerView marker) {
        int size = marker.getSize();

        if(size == 0) // przywrocenie domyslnej wielkosci - dziala to w adapterze wiec przy przywracaniu widokow, moga one miec inna wielkosc
           size = (int) getContext().getResources().getDimension(R.dimen.map_marker_size_poi);

        final ViewGroup.LayoutParams layoutParams = viewHolder.container.getLayoutParams();
        layoutParams.height = size;
        layoutParams.width = size;

        viewHolder.container.setLayoutParams(layoutParams);

        if(marker.getDrawableResId() != null)  {
            viewHolder.icon.setImageResource(marker.getDrawableResId());
        } else if(marker.getBitmap() != null) {
            viewHolder.icon.setImageBitmap(marker.getBitmap());
        }

        if(marker.getTintColor() != null) {
            viewHolder.icon.setColorFilter(marker.getTintColor());
        } else
            viewHolder.icon.clearColorFilter();
    }

    public static class ViewHolder extends ContainerViewHolder {
        ImageView icon;
    }
}