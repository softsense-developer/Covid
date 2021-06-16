package com.smartsense.covid.api.model.requests;

public class ResetPasswordRequest extends BaseApiRequest{

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
