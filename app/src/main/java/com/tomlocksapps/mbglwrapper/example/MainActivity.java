package com.tomlocksapps.mbglwrapper.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.tomlocksapps.mbglwrapper.ExamplePoiProvider;
import com.tomlocksapps.mbglwrapper.element.MapElementController;
import com.tomlocksapps.mbglwrapper.element.custom.marker.view.ComparableMarkerView;
import com.tomlocksapps.mbglwrapper.element.handler.GenericMarkerClickHandler;
import com.tomlocksapps.mbglwrapper.element.model.PoiModel;
import com.tomlocksapps.mbglwrapper.element.model.TrafficModel;
import com.tomlocksapps.mbglwrapper.element.provider.ExamplePoiElementProvider;
import com.tomlocksapps.mbglwrapper.element.provider.ExampleTrafficElementProvider;
import com.tomlocksapps.mbglwrapper.element.scale.settings.impl.DefaultElementScalingSetting;
import com.tomlocksapps.mbglwrapper.element.visibility.settings.marker.MarkerZoomSetting;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MapElementController mapElementController;
    private ExamplePoiProvider examplePoiProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1IjoidG9tYXN6dyIsImEiOiJjaXB5MGxoa2EwMDgzaThub2NtMzlkbDU4In0.ABcpoyeaYSCRLInNBNKBEw"); //todo remove key

        setContentView(R.layout.activity_main);

        examplePoiProvider = new ExamplePoiProvider(onNewPoisListener);

        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapElementController = new MapElementController(getApplicationContext());

        ExamplePoiElementProvider elementProvider = new ExamplePoiElementProvider(getApplicationContext()) {
            @Override
            protected List<PoiModel> provideDefaultObjectLocation() {
                return examplePoiProvider.getPoiModels();
            }
        };

        mapElementController.addElementProvider(elementProvider);
        mapElementController.addElementProvider(new ExampleTrafficElementProvider());

        mapElementController.addScaleSettings(elementProvider.getClass(), new DefaultElementScalingSetting() {

            @Override
            public float getMarkerScale(View view, MarkerView markerView, double zoom) {
                if(markerView instanceof ComparableMarkerView) {
                    ComparableMarkerView comparableMarkerView = (ComparableMarkerView) markerView;
                    Object comparableObject = comparableMarkerView.getComparableObject();
                    if(comparableObject instanceof PoiModel) {
                        PoiModel poiModel = (PoiModel) comparableObject;

                        float x = (float) (zoom / MapboxConstants.MAXIMUM_ZOOM); // from 0 to 1

                        double exponent = poiModel.isHighlighted() ? 2 : 6;
                        float scale = (float) Math.pow(x + 0.25, exponent); // (x+0.25)^4

                        if (scale > 1)
                            scale = 1;

                        return scale;
                    }
                }

                return super.getMarkerScale(view, markerView, zoom);
            }

        });


        mapElementController.addScaleSettings(ExampleTrafficElementProvider.class, new DefaultElementScalingSetting() {

            @Override
            public float getMarkerScale(View view, MarkerView markerView, double zoom) {
                return super.getMarkerScale(view, markerView, zoom);
            }

        });

        mapElementController.addZoomSetting(new MarkerZoomSetting<PoiModel>() {
            @Override
            public double getMinZoomForMarker(PoiModel object) {
                if(object.isHighlighted())
                    return 10.5f;

                return 12;
            }
        });

        mapElementController.addClickHandler(new GenericMarkerClickHandler(this));

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mapElementController.onDestroy();
    }

    private final ExamplePoiProvider.OnNewPoisListener onNewPoisListener = new ExamplePoiProvider.OnNewPoisListener() {
        @Override
        public void onNewPois(List<PoiModel> poiModels) {
            mapElementController.provideObjects(poiModels);
        }
    };

}
