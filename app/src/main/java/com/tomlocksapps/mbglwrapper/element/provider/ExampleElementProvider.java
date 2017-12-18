package com.tomlocksapps.mbglwrapper.element.provider;

import android.content.Context;
import android.graphics.Bitmap;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.picasso.Picasso;
import com.tomlocksapps.mbglwrapper.element.custom.marker.option.GenericMarkerViewOptions;
import com.tomlocksapps.mbglwrapper.element.model.PoiModel;
import com.tomlocksapps.mbglwrapper.example.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by walczewski on 17.12.2017.
 */

public class ExampleElementProvider extends MapBoxElementsProvider<PoiModel> {

    private final Context context;

    public ExampleElementProvider(Context context) {
        this.context = context;
    }

    @Override
    protected BaseMarkerViewOptions[] createMarkerViewsFromObject(PoiModel object) {
        GenericMarkerViewOptions options = null;

        try {
            Bitmap bitmap = Picasso.with(context).load(object.getIconUrl()).get();
            options = new GenericMarkerViewOptions();
            options.bitmap(bitmap).url(object.getUrl()).clickable(object.isClickable())
                    .comparableObject(object).position(new LatLng(object.getLocation().getLatitude(), object.getLocation().getLongitude()))
                    .size((int) context.getResources().getDimension(R.dimen.map_marker_size_poi_big));

            return new BaseMarkerViewOptions[]{options};
        } catch (IOException e) {}

        return null;
    }

    @Override
    protected String provideElementProviderTag() {
        return "ExampleElementProvider";
    }

    @Override
    protected List<PoiModel> provideDefaultObjectLocation() {
        return null;
    }
}
