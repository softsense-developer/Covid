package com.smartsense.covid.api.model.requests;

public class AddQuarantineLocationRequest extends BaseApiRequest{

    private double latitudeQuarantine;
    private double longitudeQuarantine;

    public double getLatitudeQuarantine() {
        return latitudeQuarantine;
    }

    public void setLatitudeQuarantine(double latitudeQuarantine) {
        this.latitudeQuarantine = latitudeQuarantine;
    }

    public double getLongitudeQuarantine() {
        return longitudeQuarantine;
    }

    public void setLongitudeQuarantine(double longitudeQuarantine) {
        this.longitudeQuarantine = longitudeQuarantine;
    }
}
