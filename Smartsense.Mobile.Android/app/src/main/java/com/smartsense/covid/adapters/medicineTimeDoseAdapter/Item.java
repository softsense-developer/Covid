package com.smartsense.covid.adapters.medicineTimeDoseAdapter;

import androidx.annotation.Keep;

@Keep
public class Item {
    private long timeStamp;
    private String dose;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }
}
