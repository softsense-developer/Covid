package com.smartsense.covid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;

import java.util.List;

@Dao
public interface MedicineTimeDao {

    @Insert
    long insert(MedicineTime medicineTime);

    @Update
    void update(MedicineTime medicineTime);

    @Delete
    void delete(MedicineTime medicineTime);

    @Query("DELETE FROM medicine_time_table WHERE foreignMedicineID=:medicineID ")
    void deleteByMedicineID(long medicineID);

    @Query("DELETE FROM medicine_time_table")
    void deleteAllData();

    @Query("SELECT * FROM medicine_time_table WHERE medicineTimeID=:id ")
    MedicineTime getMedicineTimeByID(long id);

    @Query("SELECT * FROM medicine_time_table ORDER BY medicineTimeID ASC")
    List<MedicineTime> getAllDataList();

    @Query("SELECT * FROM medicine_time_table WHERE foreignMedicineID=:medicineID ORDER BY medicineTimeID ASC")
    List<MedicineTime> getMedicineTimes(long medicineID);

}
