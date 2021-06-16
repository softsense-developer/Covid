package com.smartsense.covid.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "medicine_usage_table",
        foreignKeys = @ForeignKey(entity = MedicineTime.class,
        parentColumns = "medicineTimeID",
        childColumns = "foreignMedicineTimeID",
        onDelete = CASCADE),
        indices = {@Index(value = {"foreignMedicineTimeID"}, unique = false)})

public class MedicineUsage {
    @PrimaryKey(autoGenerate = true)
    private long medicineUsageID;
    private long foreignMedicineTimeID;
    private long useTimestamp;
    private boolean isUsed;
    private boolean isNotifySent;


    public MedicineUsage(long foreignMedicineTimeID, long useTimestamp, boolean isUsed, boolean isNotifySent) {
        this.foreignMedicineTimeID = foreignMedicineTimeID;
        this.useTimestamp = useTimestamp;
        this.isUsed = isUsed;
        this.isNotifySent = isNotifySent;
    }

    public long getMedicineUsageID() {
        return medicineUsageID;
    }

    public void setMedicineUsageID(long medicineUsageID) {
        this.medicineUsageID = medicineUsageID;
    }

    public long getForeignMedicineTimeID() {
        return foreignMedicineTimeID;
    }

    public void setForeignMedicineTimeID(long foreignMedicineTimeID) {
        this.foreignMedicineTimeID = foreignMedicineTimeID;
    }

    public long getUseTimestamp() {
        return useTimestamp;
    }

    public void setUseTimestamp(long useTimestamp) {
        this.useTimestamp = useTimestamp;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isNotifySent() {
        return isNotifySent;
    }

    public void setNotifySent(boolean notifySent) {
        isNotifySent = notifySent;
    }
}
