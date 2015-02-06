package com.yorktown.yorktown.eventbus;

/**
 * Created by Daniel on 2/6/2015.
 */
public class GetLocationEvent {

    public final double latitude, longitude;

    public GetLocationEvent(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
