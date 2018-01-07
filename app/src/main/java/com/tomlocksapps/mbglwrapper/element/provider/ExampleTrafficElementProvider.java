package com.tomlocksapps.mbglwrapper.element.provider;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.picasso.Picasso;
import com.tomlocksapps.mbglwrapper.ExampleTrafficProvider;
import com.tomlocksapps.mbglwrapper.element.custom.marker.option.GenericMarkerViewOptions;
import com.tomlocksapps.mbglwrapper.element.model.TrafficModel;
import com.tomlocksapps.mbglwrapper.example.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by tomlo on 26.12.2017.
 */

public class ExampleTrafficElementProvider extends MapBoxElementsProvider<TrafficModel> {

    @Override
    protected String provideElementProviderTag() {
        return "ExampleTrafficProvider";
    }

    @Override
    protected PolylineOptions createPolylineFromObject(TrafficModel object) {
        PolylineOptions polylineOptions = new PolylineOptions();

        for (Location location : object.getLocationList()) {
            polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        switch (object.getType()) {
            case LIGHT:
                polylineOptions.color(Color.GREEN);
                break;
            case MODERATE:
                polylineOptions.color(Color.MAGENTA);
                break;
            case HEAVY:
                polylineOptions.color(Color.RED);
                break;
        }

        return polylineOptions;
    }


    @Override
    protected BaseMarkerViewOptions[] createMarkerViewsFromObject(TrafficModel object) {
        GenericMarkerViewOptions options = null;

        List<Location> list = object.getLocationList();
        Location location = list.get(list.size()/2);

        options = new GenericMarkerViewOptions();
        options.imageResId(R.drawable.jam).clickable(false)
                .comparableObject(object).position(new LatLng(location.getLatitude(), location.getLongitude()));

        return new BaseMarkerViewOptions[]{options};
    }


    @Override
    protected List<TrafficModel> provideDefaultObjectLocation() {
        ExampleTrafficProvider exampleTrafficProvider = new ExampleTrafficProvider();

        return exampleTrafficProvider.getExampleTraffic();
    }
}
