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

    public static final int DELAY_MILLIS = 10_000;

    private final OnNewPoisListener onNewPoisListener;
    private final Handler handler = new Handler();

    public ExamplePoiProvider(OnNewPoisListener onNewPoisListener) {
        this.onNewPoisListener = onNewPoisListener;
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
        List<PoiModel> poiModels = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            PoiModel model = generateRandomPoi();
            poiModels.add(model);
        }

        onNewPoisListener.onNewPois(poiModels);
    }

    private PoiModel generateRandomPoi() {
        String url = "https://www.google.com/search?q=test" + System.currentTimeMillis();

        Random random = new Random();
        float number = (float)random.nextInt(30) / 10000;

        Location location = new Location("PoiLocation");
        location.setLatitude(52.408135 + number);
        location.setLongitude(16.929662 + number);

        PoiModel poiModel = new PoiModel(url, true, "http://wfarm3.dataknet.com/static/resources/icons/set28/58aac1c.png", location);

        return poiModel;
    }

    public interface OnNewPoisListener {
        void onNewPois(List<PoiModel> poiModels);
    }

}
