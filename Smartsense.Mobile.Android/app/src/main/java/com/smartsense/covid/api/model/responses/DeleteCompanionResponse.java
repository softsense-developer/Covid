package com.smartsense.covid.api.model.responses;

import java.util.List;

public class DeleteCompanionResponse extends BaseApiResponse{

    public DeleteCompanionResponse(List<String> errors) {
        super(errors);
    }
}
