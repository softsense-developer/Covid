package com.smartsense.covid.api.model.requests;

public class BaseApiRequest {
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
