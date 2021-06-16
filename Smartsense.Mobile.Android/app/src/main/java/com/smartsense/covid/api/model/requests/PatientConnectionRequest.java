package com.smartsense.covid.api.model.requests;

public class PatientConnectionRequest extends BaseApiRequest{

    private long id;
    private boolean isAccept;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }
}
