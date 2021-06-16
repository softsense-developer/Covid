package com.smartsense.covid.api.model.responses;

import java.util.List;

public class AddInfoResponse extends BaseApiResponse{

    public AddInfoResponse(List<String> errors) {
        super(errors);
    }
}
