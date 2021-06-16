package com.smartsense.covid.api.model;

public class VitalHistory {
    private long id;
    private long userId;
    private int dataType;
    private double dataValue;
    private String saveDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

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

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    @Override
    public String toString() {
        return "VitalHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", dataType=" + dataType +
                ", dataValue=" + dataValue +
                ", saveDate='" + saveDate + '\'' +
                '}';
    }
}
