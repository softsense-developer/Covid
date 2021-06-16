package com.smartsense.covid.api.model.responses;

import java.util.ArrayList;
import java.util.List;

public class BaseApiResponse {
    private String code;
    private String message;
    private List<String> errors;

    public BaseApiResponse(List<String> errors) {
        this.errors = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
