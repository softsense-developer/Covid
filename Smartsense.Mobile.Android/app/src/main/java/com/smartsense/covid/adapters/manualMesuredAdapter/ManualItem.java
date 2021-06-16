package com.smartsense.covid.adapters.manualMesuredAdapter;

import androidx.annotation.Keep;

@Keep
public class ManualItem {
    private String data;
    private String date;
    private int type;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
