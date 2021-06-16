package com.smartsense.covid.api.model.responses;

import java.util.List;

public class PatientConnectionResponse extends BaseApiResponse{

    public PatientConnectionResponse(List<String> errors) {
        super(errors);
    }
}
