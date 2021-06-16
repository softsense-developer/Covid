package com.smartsense.covid.api.model.responses;

import java.util.List;

public class PasswordChangeResponse extends BaseApiResponse{

    public PasswordChangeResponse(List<String> errors) {
        super(errors);
    }
}
