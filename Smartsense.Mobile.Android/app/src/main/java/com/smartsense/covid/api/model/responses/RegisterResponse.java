package com.smartsense.covid.api.model.responses;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("Message")
    private String message;

    @SerializedName("ModelState")
    private int modelState;

    public RegisterResponse() {
    }

    public RegisterResponse(String message, int modelState) {
        this.message = message;
        this.modelState = modelState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getModelState() {
        return modelState;
    }

    public void setModelState(int modelState) {
        this.modelState = modelState;
    }
}
