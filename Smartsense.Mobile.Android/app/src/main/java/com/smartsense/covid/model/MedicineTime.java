package com.smartsense.covid.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "medicine_time_table",
        foreignKeys = @ForeignKey(entity = Medicine.class,
        parentColumns = "medicineID",
        childColumns = "foreignMedicineID",
        onDelete = CASCADE),
        indices = {@Index(value = {"foreignMedicineID"}, unique = false)})

public class MedicineTime {
    @PrimaryKey(autoGenerate = true)
    private long medicineTimeID;
    private long foreignMedicineID;
    private long timeUsage;
    private String dose;

    public MedicineTime(long foreignMedicineID, long timeUsage, String dose) {
        this.foreignMedicineID = foreignMedicineID;
        this.timeUsage = timeUsage;
        this.dose = dose;
    }

    public long getMedicineTimeID() {
        return medicineTimeID;
    }

    public void setMedicineTimeID(long medicineTimeID) {
        this.medicineTimeID = medicineTimeID;
    }

    public long getForeignMedicineID() {
        return foreignMedicineID;
    }

    public void setForeignMedicineID(long foreignMedicineID) {
        this.foreignMedicineID = foreignMedicineID;
    }

    public long getTimeUsage() {
        return timeUsage;
    }

    public void setTimeUsage(long timeUsage) {
        this.timeUsage = timeUsage;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    @Override
    public String toString() {
        return "MedicineTime{" +
                "medicineTimeID=" + medicineTimeID +
                ", foreignMedicineID=" + foreignMedicineID +
                ", timeUsage=" + timeUsage +
                ", dose='" + dose + '\'' +
                '}';
    }
}
