package com.tomlocksapps.mbglwrapper.element.custom.marker.option;

import android.graphics.Bitmap;
import android.os.Parcel;

import com.tomlocksapps.mbglwrapper.element.custom.marker.view.impl.GenericMarkerView;


/**
 * Created by walczewski on 2016-06-16.
 */
public class GenericMarkerViewOptions extends ComparableMarkerViewOptions<GenericMarkerView, GenericMarkerViewOptions> {

    private Integer imageResId;
    private Bitmap bitmap;

    private int size;

    private Integer tintColor;
    private String url;
    private boolean isClickable;
    private double mixZoomLevel = -1;

    public GenericMarkerViewOptions size(int size) {
        this.size = size;

        return getThis();
    }

    public GenericMarkerViewOptions tintColor(Integer tint) {
        this.tintColor = tint;

        return getThis();
    }

    public GenericMarkerViewOptions minZoomLevel(double zoomLevel) {
        this.mixZoomLevel = zoomLevel;

        return getThis();
    }

    public GenericMarkerViewOptions bitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return getThis();
    }

    public GenericMarkerViewOptions url(String name) {
        url = name;
        return getThis();
    }

    public GenericMarkerViewOptions imageResId(int imageResId) {
        this.imageResId = imageResId;
        return getThis();
    }

    public GenericMarkerViewOptions clickable(boolean enabled) {
        this.isClickable = enabled;
        return getThis();
    }

    public GenericMarkerViewOptions() {
    }



    @Override
    public GenericMarkerViewOptions getThis() {
        return this;
    }

    @Override
    public GenericMarkerView getMarker() {
        return new GenericMarkerView(this, getComparableObject(),url, isClickable, mixZoomLevel, imageResId, size, tintColor, bitmap);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.imageResId);
        dest.writeInt(this.size);
        dest.writeInt(this.tintColor);
        dest.writeString(this.url);
        dest.writeByte(this.isClickable ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.mixZoomLevel);
    }

    protected GenericMarkerViewOptions(Parcel in) {
        this.imageResId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.size = in.readInt();
        this.tintColor = in.readInt();
        this.url = in.readString();
        this.isClickable = in.readByte() != 0;
        this.mixZoomLevel = in.readDouble();
    }

    public static final Creator<GenericMarkerViewOptions> CREATOR = new Creator<GenericMarkerViewOptions>() {
        @Override
        public GenericMarkerViewOptions createFromParcel(Parcel source) {
            return new GenericMarkerViewOptions(source);
        }

        @Override
        public GenericMarkerViewOptions[] newArray(int size) {
            return new GenericMarkerViewOptions[size];
        }
    };
}

