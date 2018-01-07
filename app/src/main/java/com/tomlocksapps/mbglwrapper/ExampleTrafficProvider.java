package com.tomlocksapps.mbglwrapper;

import android.location.Location;

import com.tomlocksapps.mbglwrapper.element.model.TrafficModel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tomlo on 26.12.2017.
 */

public class ExampleTrafficProvider {

    public List<TrafficModel> getExampleTraffic() {
        TrafficModel lightTrafficModel = new TrafficModel(TrafficModel.Type.LIGHT);
        lightTrafficModel.addLocation(createLocation(52.406461, 16.929824));
        lightTrafficModel.addLocation(createLocation(52.406906, 16.928773));
        lightTrafficModel.addLocation(createLocation(52.407874, 16.929299));

        TrafficModel moderateTrafficModel = new TrafficModel(TrafficModel.Type.MODERATE);
        moderateTrafficModel.addLocation(createLocation(52.406389, 16.925168));
        moderateTrafficModel.addLocation(createLocation(52.405989, 16.927561));
        moderateTrafficModel.addLocation(createLocation(52.406009, 16.928226));
        moderateTrafficModel.addLocation(createLocation(52.406552, 16.928591));

        TrafficModel heavyTrafficModel = new TrafficModel(TrafficModel.Type.HEAVY);
        heavyTrafficModel.addLocation(createLocation(52.407855, 16.926155));
        heavyTrafficModel.addLocation(createLocation(52.407835, 16.925393));
        heavyTrafficModel.addLocation(createLocation(52.406487, 16.925168));

        return Arrays.asList(lightTrafficModel, moderateTrafficModel, heavyTrafficModel);
    }

    private Location createLocation(double lat, double lng) {
        Location loc = new Location("MockLocation");
        loc.setLatitude(lat);
        loc.setLongitude(lng);

        return loc;
    }

}
