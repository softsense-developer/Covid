package com.smartsense.covid.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "covid_table")
public class Covid {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private long time;
    private double data;
    private int dataType;
    private int saveType;

    public Covid(long time, double data, int dataType, int saveType) {
        this.time = time;
        this.data = data;
        this.dataType = dataType;
        this.saveType = saveType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getSaveType() {
        return saveType;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }
}
