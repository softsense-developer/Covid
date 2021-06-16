package com.smartsense.covid.api.model.responses;

import com.smartsense.covid.adapters.companionAdapter.Companion;
import com.smartsense.covid.adapters.requestAdapter.Promotion;

import java.util.ArrayList;
import java.util.List;

public class GetCompanionResponse extends BaseApiResponse {

    public List<Companion> companionModels;

    public GetCompanionResponse(List<String> errors) {
        super(errors);
        companionModels = new ArrayList<>();
    }
}
