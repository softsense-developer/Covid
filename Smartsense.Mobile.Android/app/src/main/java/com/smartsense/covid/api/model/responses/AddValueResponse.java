package com.smartsense.covid.api.model.responses;

import java.util.List;

public class AddValueResponse extends BaseApiResponse{

    public AddValueResponse(List<String> errors) {
        super(errors);
    }
}
