package com.smartsense.covid.api.model.responses;

import com.smartsense.covid.api.model.Doctor;
import com.smartsense.covid.api.model.Hospital;

import java.util.ArrayList;
import java.util.List;

public class GetDoctorResponse extends BaseApiResponse{

    public List<Doctor> doctors;

    public GetDoctorResponse(List<String> errors) {
        super(errors);
        doctors = new ArrayList<>();
    }
}
