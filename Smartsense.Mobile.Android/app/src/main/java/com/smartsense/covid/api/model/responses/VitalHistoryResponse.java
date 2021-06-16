package com.smartsense.covid.api.model.responses;

import com.smartsense.covid.api.model.VitalHistory;

import java.util.ArrayList;
import java.util.List;

public class VitalHistoryResponse extends BaseApiResponse{

    public List<VitalHistory> oxygen;
    public List<VitalHistory> pulses;
    public List<VitalHistory> temperatures;

    public VitalHistoryResponse(List<String> errors) {
        super(errors);
        oxygen = new ArrayList<>();
        pulses = new ArrayList<>();
        temperatures = new ArrayList<>();
    }
}
