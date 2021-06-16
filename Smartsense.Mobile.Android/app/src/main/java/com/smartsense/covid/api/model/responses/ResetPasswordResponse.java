package com.smartsense.covid.api.model.responses;

import java.util.List;

public class ResetPasswordResponse extends BaseApiResponse{

    public ResetPasswordResponse(List<String> errors) {
        super(errors);
    }
}
