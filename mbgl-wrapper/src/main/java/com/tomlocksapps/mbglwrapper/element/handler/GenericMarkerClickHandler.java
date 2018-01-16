package com.tomlocksapps.mbglwrapper.element.handler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.tomlocksapps.mbglwrapper.element.custom.marker.view.impl.GenericMarkerView;

/**
 * Created by walczewski on 2016-06-16.
 */
public class GenericMarkerClickHandler extends MarkerClickHandler<GenericMarkerView> {

    private final Activity activity;

    public GenericMarkerClickHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected boolean handleMyMarkerClick(GenericMarkerView marker) {
        if(!marker.isClickable() || marker.getUrl() == null)
            return true;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(marker.getUrl()));
        activity.startActivity(browserIntent);

        return true;
    }
}
