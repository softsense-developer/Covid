package com.smartsense.covid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.model.MedicineUsage;

import java.util.List;

@Dao
public interface MedicineUsageDao {

    @Insert
    void insert(MedicineUsage medicineUsage);

    @Update
    void update(MedicineUsage medicineUsage);

    @Delete
    void delete(MedicineUsage medicineUsage);

    @Query("DELETE FROM medicine_usage_table")
    void deleteAllData();

    @Query("SELECT * FROM medicine_usage_table WHERE medicineUsageID=:id ")
    MedicineUsage getMedicineUsageByID(long id);

    @Query("SELECT * FROM medicine_usage_table ORDER BY medicineUsageID ASC")
    List<MedicineUsage> getAllDataList();

    @Query("SELECT * FROM medicine_usage_table WHERE isUsed=:useStatus ORDER BY medicineUsageID DESC")
    List<MedicineUsage> getAllUsedDataList(boolean useStatus);
}
