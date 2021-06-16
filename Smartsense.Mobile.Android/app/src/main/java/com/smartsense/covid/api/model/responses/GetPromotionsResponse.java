package com.smartsense.covid.api.model.responses;

import com.smartsense.covid.adapters.requestAdapter.Promotion;

import java.util.ArrayList;
import java.util.List;

public class GetPromotionsResponse extends BaseApiResponse {

    public List<Promotion> connectionRequests;

    public GetPromotionsResponse(List<String> errors) {
        super(errors);
        connectionRequests = new ArrayList<>();
    }
}
