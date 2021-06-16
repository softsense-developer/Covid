package com.smartsense.covid.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine_table")
public class Medicine {

    @PrimaryKey(autoGenerate = true)
    private long medicineID;
    private String medicineName;

    public Medicine(String medicineName) {
        this.medicineName = medicineName;
    }

    public long getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(long medicineID) {
        this.medicineID = medicineID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}
