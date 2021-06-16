package com.smartsense.covid.api.model.responses;

import java.util.List;

public class UserInfoResponse extends BaseApiResponse{

    private long userId;
    private String name;
    private String surname;
    private String phone;

    public UserInfoResponse(List<String> errors) {
        super(errors);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
