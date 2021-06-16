package com.smartsense.covid.api.model.requests;

public class AddWarningRequest extends BaseApiRequest{

    private int dataType;
    private double warningValue;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public double getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(double warningValue) {
        this.warningValue = warningValue;
    }
}
