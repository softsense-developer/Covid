package com.smartsense.covid.api.model;

public class User {

    private String email, password, name, surname, phone;
    private long doctorID;

    public User(String email, String password, String name, String surname, String phone, long doctorID) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.doctorID = doctorID;
    }

    public String getPassword() {
        return password;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public long getDoctorID() {
        return doctorID;
    }

}
