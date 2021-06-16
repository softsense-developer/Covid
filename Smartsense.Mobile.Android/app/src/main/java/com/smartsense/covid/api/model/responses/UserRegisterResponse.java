package com.smartsense.covid.api.model.responses;

import java.util.List;

public class UserRegisterResponse extends BaseApiResponse{

    public UserRegisterResponse(List<String> errors) {
        super(errors);
    }
}
