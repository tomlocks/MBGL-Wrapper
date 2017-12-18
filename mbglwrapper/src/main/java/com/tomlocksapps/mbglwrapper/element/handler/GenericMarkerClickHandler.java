package com.tomlocksapps.mbglwrapper.element.handler;

import com.tomlocksapps.mbglwrapper.element.custom.marker.view.impl.GenericMarkerView;

/**
 * Created by walczewski on 2016-06-16.
 */
public class GenericMarkerClickHandler extends MarkerClickHandler<GenericMarkerView> {
    
    @Override
    protected boolean handleMyMarkerClick(GenericMarkerView marker) {

        if(!marker.isClickable() || marker.getUrl() == null)
            return true;

     /*   Intent webViewIntent = new Intent(App.getContext(), WebViewActivity.class);
        webViewIntent.putExtra(WebViewActivity.SET_URL, marker.getUrl());

        webViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        App.getContext().startActivity(webViewIntent);*/

     //todo

        return true;
    }
}
