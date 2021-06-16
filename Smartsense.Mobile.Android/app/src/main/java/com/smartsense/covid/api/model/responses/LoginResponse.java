package com.smartsense.covid.api.model.responses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    private String access_token;
    private String token_type;
    private long expires_in;
    private String userName;
    @SerializedName(".issued")
    private String issued;
    @SerializedName(".expires")
    private String expires;


    public LoginResponse(String access_token, String token_type, long expires_in, String userName, String issued, String expires) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.userName = userName;
        this.issued = issued;
        this.expires = expires;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public String getUserName() {
        return userName;
    }

    public String getIssued() {
        return issued;
    }

    public String getExpires() {
        return expires;
    }
}
