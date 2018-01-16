package com.tomlocksapps.mbglwrapper.logger;

import android.util.Log;

/**
 * Created by walczewski on 16.12.2017.
 */

public class Logger {
    private static Logger instance;

    private final boolean active = false;
    private final String TAG = "MapBoxLogger";

    private Logger() {}

    public static Logger getInstance() {
        if(instance == null)
            instance = new Logger();

        return instance;
    }

    public void d(String message) {
        if(active)
            Log.d(TAG, message);
    }
}
