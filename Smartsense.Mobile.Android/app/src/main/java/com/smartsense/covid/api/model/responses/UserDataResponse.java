package com.smartsense.covid.api.model.responses;

import com.google.gson.annotations.SerializedName;

public class UserDataResponse {

    @SerializedName("Ad")
    private String name;

    @SerializedName("Soyad")
    private String surname;

    @SerializedName("Number")
    private String phoneNumber;

    @SerializedName("dr_id")
    private int doctorID;

    public UserDataResponse() {
    }

    public UserDataResponse(String name, String surname, String phoneNumber, int doctorID) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.doctorID = doctorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }
}
