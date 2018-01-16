package com.tomlocksapps.mbglwrapper.element.custom.info.view;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by walczewski on 2016-08-02.
 */
public class GasStationInfoView extends FrameLayout {
    public GasStationInfoView(Context context) {
        super(context);

        init();
    }

    private void init() {
        TextView textView = new TextView(getContext());
        textView.setText("InfoView Test");

        addView(textView);
    }

}
