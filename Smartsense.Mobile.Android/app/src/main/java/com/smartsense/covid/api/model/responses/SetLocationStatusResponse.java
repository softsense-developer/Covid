package com.smartsense.covid.api.model.responses;

import com.google.gson.annotations.SerializedName;

public class SetLocationStatusResponse {

    @SerializedName("Name")
    private String email;

    @SerializedName("Lat_1")
    private double lat;

    @SerializedName("Long_1")
    private double longt;

    @SerializedName("time")
    private boolean status;

    public SetLocationStatusResponse() {

    }

    public SetLocationStatusResponse(String email, double lat, double longt, boolean status) {
        this.email = email;
        this.lat = lat;
        this.longt = longt;
        this.status = status;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return longt;
    }

    public void setLongt(double longt) {
        this.longt = longt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
