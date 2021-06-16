package com.smartsense.covid.api.model;

public class UserModel {

    private String email, password, name, surname;

    public UserModel(String email, String password, String name, String surname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
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

}
