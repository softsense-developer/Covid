package com.smartsense.covid.api.model.responses;

import com.google.gson.annotations.SerializedName;

public class InsertResponse {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int code;

    public InsertResponse(boolean error, String message, int code) {
        this.error = error;
        this.message = message;
        this.code = code;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
