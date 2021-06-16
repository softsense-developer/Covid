package com.smartsense.covid.api.model.requests;

public class UserLoginRequest extends BaseApiRequest{
    private String email;
    private String password;
    private boolean isRemember;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public void setRemember(boolean remember) {
        isRemember = remember;
    }
}
