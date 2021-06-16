package com.smartsense.covid.api.model.requests;

public class PutNowLocationRequest extends BaseApiRequest{

    private double latitudeNow;
    private double longitudeNow;

    public double getLatitudeNow() {
        return latitudeNow;
    }

    public void setLatitudeNow(double latitudeNow) {
        this.latitudeNow = latitudeNow;
    }

    public double getLongitudeNow() {
        return longitudeNow;
    }

    public void setLongitudeNow(double longitudeNow) {
        this.longitudeNow = longitudeNow;
    }
}
