package com.smartsense.covid.api.model.responses;

import java.util.List;

public class QuarantineStatusResponse extends BaseApiResponse{

    private boolean quarantineStatus;

    public boolean isQuarantineStatus() {
        return quarantineStatus;
    }

    public void setQuarantineStatus(boolean quarantineStatus) {
        this.quarantineStatus = quarantineStatus;
    }

    public QuarantineStatusResponse(List<String> errors) {
        super(errors);
    }
}
