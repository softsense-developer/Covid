package com.smartsense.covid.api.model.responses;

import com.smartsense.covid.api.model.Hospital;
import com.smartsense.covid.api.model.VitalHistory;

import java.util.ArrayList;
import java.util.List;

public class GetHospitalResponse extends BaseApiResponse{

    public List<Hospital> hospitals;

    public GetHospitalResponse(List<String> errors) {
        super(errors);
        hospitals = new ArrayList<>();
    }
}
