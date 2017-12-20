package com.tomlocksapps.mbglwrapper;

import android.location.Location;
import android.os.Handler;

import com.tomlocksapps.mbglwrapper.element.model.PoiModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by walczewski on 17.12.2017.
 */

public class ExamplePoiProvider {

    public static final int DELAY_MILLIS = 30_000;

    private final OnNewPoisListener onNewPoisListener;
    private final Handler handler = new Handler();
    private final List<PoiModel> poiModels = new ArrayList<>();

    private final PoiModel constPoiModel;

    public ExamplePoiProvider(OnNewPoisListener onNewPoisListener) {
        this.onNewPoisListener = onNewPoisListener;

        Location location = new Location("Test");
        location.setLatitude(52.408155);
        location.setLongitude(16.929982);

        constPoiModel =  new PoiModel("https://www.google.com/search?q=constPoiModel", true, "http://www.clker.com/cliparts/g/9/4/c/Y/0/orange-map-pin.svg.hi.png", location, true, 999);
    }

    public void initialize() {
        generatePois();
        schedule();
    }

    public void uninitialize() {
        handler.removeCallbacksAndMessages(null);
    }

    private void schedule() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generatePois();

                schedule();
            }
        }, DELAY_MILLIS);
    }

    private void generatePois() {
        List<PoiModel> newPoiModels = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            PoiModel model = generateRandomPoi(i);
            newPoiModels.add(model);
        }

        newPoiModels.add(constPoiModel);

        poiModels.clear();
        poiModels.addAll(newPoiModels);

        onNewPoisListener.onNewPois(newPoiModels);
    }

    public List<PoiModel> getPoiModels() {
        return poiModels;
    }

    private PoiModel generateRandomPoi(int id) {
        String url = "https://www.google.com/search?q=test" + id;

        Random random = new Random();
        float number = (float)random.nextInt(30) / 10000;
        boolean sign = random.nextInt(2) == 1;

        Location location = new Location("PoiLocation");
        location.setLatitude(52.408135 + (sign ? +number : - number));
        location.setLongitude(16.929662 + number);

        PoiModel poiModel = new PoiModel(url, true, "http://www.clker.com/cliparts/g/9/4/c/Y/0/orange-map-pin.svg.hi.png", location, id);

        return poiModel;
    }

    public interface OnNewPoisListener {
        void onNewPois(List<PoiModel> poiModels);
    }

}
