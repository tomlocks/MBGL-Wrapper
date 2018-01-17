# MBGL-Wrapper
This library simplifies the process of creating layers on mapbox using Android's MarkerViews. It provides another abstraction layer for defining elements on the map. The main idea was to create a framework that creates markers/polylines from a POJO object using the separate thread.
The main features are:
- ability to scale MarkerViews
- ability to decide whether MarkerView should be visible or hidden based on zoom level
- increased performance by checking if object is already added on map - using comparable POJO objects

Example
--------
MapElementController class is the entrance to this framework. It needs to be initialized like below:

```Java

    private MapView mapView;
    private MapElementController mapElementController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        ...

        mapElementController = new MapElementController(getApplicationContext());

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
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mapElementController.uninitialize();
        mapView.onPause();
    }
```
It consists of 4 main methods:
```Java
  mapElementController.addElementProvider(new ExampleTrafficElementProvider()); // define a new element provider
  mapElementController.addScaleSettings(elementProvider.getClass(), new DefaultElementScalingSetting()); // define a markerview scale settings
  mapElementController.addZoomSetting(new MarkerZoomSetting<PoiModel>()); // define min/max for POJO object
  mapElementController.addClickHandler(new GenericMarkerClickHandler(this)); // add click handler for this type of marker
```

MapBoxElementsProvider is an adapter class between POJO object and map markersViews/polylines. It allows you to create map object's based on the POJO objects delivered to MapElementController class.

For a more comprehensive explanation please see the example app module in this repository.

Download
--------

```Java
dependencies {
  compile 'com.tomlocksapps:mbgl-wrapper:0.1.0'
}
```

Deprecation
--------
Please keep in mind that the newest version of mapbox encourages you to use SymbolLayer instead of MarkerView
https://www.mapbox.com/android-docs/api/map-sdk/5.3.1/com/mapbox/mapboxsdk/maps/MapboxMap.html#addMarker-com.mapbox.mapboxsdk.annotations.BaseMarkerViewOptions-


License
-------
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
