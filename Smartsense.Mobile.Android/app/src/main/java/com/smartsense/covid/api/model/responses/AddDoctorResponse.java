package com.smartsense.covid.api.model.responses;

import java.util.List;

public class AddDoctorResponse extends BaseApiResponse{

    public AddDoctorResponse(List<String> errors) {
        super(errors);
    }
}
