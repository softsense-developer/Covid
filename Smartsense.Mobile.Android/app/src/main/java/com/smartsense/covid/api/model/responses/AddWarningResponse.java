package com.smartsense.covid.api.model.responses;

import java.util.List;

public class AddWarningResponse extends BaseApiResponse{

    public AddWarningResponse(List<String> errors) {
        super(errors);
    }
}
