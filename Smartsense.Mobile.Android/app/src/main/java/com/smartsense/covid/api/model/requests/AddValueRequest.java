package com.smartsense.covid.api.model.requests;

public class AddValueRequest extends BaseApiRequest{

    private int dataType;
    private double dataValue;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public double getDataValue() {
        return dataValue;
    }

    public void setDataValue(double dataValue) {
        this.dataValue = dataValue;
    }
}
