package com.tomlocksapps.mbglwrapper.element.provider;

import android.content.Context;
import android.graphics.Bitmap;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.picasso.Picasso;
import com.tomlocksapps.mbglwrapper.element.custom.marker.option.GenericMarkerViewOptions;
import com.tomlocksapps.mbglwrapper.element.model.PoiModel;
import com.tomlocksapps.mbglwrapper.element.provider.filter.MapBoxMarkerFilter;
import com.tomlocksapps.mbglwrapper.example.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by walczewski on 17.12.2017.
 */

public class ExamplePoiElementProvider extends MapBoxElementsProvider<PoiModel> {

    private final Context context;

    public ExamplePoiElementProvider(Context context) {
        this.context = context;

        addFilter(new MapBoxMarkerFilter<PoiModel>() {
            @Override
            public List<PoiModel> filterObjects(List<PoiModel> objects) {
                List<PoiModel> filteredList = new ArrayList<>(objects.size());

                for (PoiModel model : objects) {
                    if(model.getId() % 2 == 0 || model.getId() == 999)
                        filteredList.add(model);
                }

                return filteredList;
            }
        });
    }

    @Override
    protected BaseMarkerViewOptions[] createMarkerViewsFromObject(PoiModel object) {
        GenericMarkerViewOptions options = null;

        try {
            Bitmap bitmap = Picasso.with(context).load(object.getIconUrl()).get();
            options = new GenericMarkerViewOptions();
            options.bitmap(bitmap).url(object.getUrl()).clickable(object.isClickable())
                    .comparableObject(object).position(new LatLng(object.getLocation().getLatitude(), object.getLocation().getLongitude()));

            if(object.isHighlighted())
                  options.tintColor(R.color.colorAccent);

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
