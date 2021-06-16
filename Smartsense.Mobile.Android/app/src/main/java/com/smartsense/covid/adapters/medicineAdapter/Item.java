package com.smartsense.covid.adapters.medicineAdapter;

import androidx.annotation.Keep;

@Keep
public class Item {
    private long id;
    private String medicineName;
    private String onComingUsageTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getOnComingUsageTime() {
        return onComingUsageTime;
    }

    public void setOnComingUsageTime(String onComingUsageTime) {
        this.onComingUsageTime = onComingUsageTime;
    }
}
