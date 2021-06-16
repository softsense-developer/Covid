package com.smartsense.covid.api.model.responses;

import java.util.List;

public class RefreshTokenResponse extends BaseApiResponse{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RefreshTokenResponse(List<String> errors) {
        super(errors);
    }
}
