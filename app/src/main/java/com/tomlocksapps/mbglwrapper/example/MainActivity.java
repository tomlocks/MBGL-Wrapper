package com.tomlocksapps.mbglwrapper.example;

import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.tomlocksapps.mbglwrapper.ExamplePoiProvider;
import com.tomlocksapps.mbglwrapper.element.MapElementController;
import com.tomlocksapps.mbglwrapper.element.custom.marker.option.ComparableMarkerViewOptions;
import com.tomlocksapps.mbglwrapper.element.custom.marker.option.GenericMarkerViewOptions;
import com.tomlocksapps.mbglwrapper.element.model.PoiModel;
import com.tomlocksapps.mbglwrapper.element.provider.ExampleElementProvider;
import com.tomlocksapps.mbglwrapper.element.provider.OnNewElementsListener;
import com.tomlocksapps.mbglwrapper.element.scale.settings.impl.DefaultElementScallingSetting;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MapElementController mapElementController;
    private ExamplePoiProvider examplePoiProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        examplePoiProvider = new ExamplePoiProvider(onNewPoisListener);

        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapElementController = new MapElementController(getApplicationContext());
        mapElementController.addElementProvider(new ExampleElementProvider(getApplicationContext()));
        mapElementController.addScaleSettings(ExampleElementProvider.class, new DefaultElementScallingSetting());

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapElementController.setMap(mapboxMap);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mapElementController.initialize();
        examplePoiProvider.initialize();
        mapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();

        mapElementController.uninitialize();
        examplePoiProvider.uninitialize();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);
    }

    private final ExamplePoiProvider.OnNewPoisListener onNewPoisListener = new ExamplePoiProvider.OnNewPoisListener() {
        @Override
        public void onNewPois(List<PoiModel> poiModels) {
            mapElementController.provideObjects(poiModels);
        }
    };

}
