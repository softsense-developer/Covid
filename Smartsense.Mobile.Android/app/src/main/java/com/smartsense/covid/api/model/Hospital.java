package com.smartsense.covid.api.model;

public class Hospital {

    private long id;
    private String hospitalName;

    public Hospital(long id, String hospitalName) {
        this.id = id;
        this.hospitalName = hospitalName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", hospitalName='" + hospitalName + '\'' +
                '}';
    }
}
