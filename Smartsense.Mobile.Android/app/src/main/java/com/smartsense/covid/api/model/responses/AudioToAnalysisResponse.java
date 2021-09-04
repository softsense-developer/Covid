package com.smartsense.covid.api.model.responses;

import java.util.List;

public class AudioToAnalysisResponse extends BaseApiResponse{

    private boolean isCovid;

    public AudioToAnalysisResponse(List<String> errors) {
        super(errors);
    }

    public boolean isCovid() {
        return isCovid;
    }

    public void setCovid(boolean covid) {
        isCovid = covid;
    }
}
