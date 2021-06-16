package com.smartsense.covid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smartsense.covid.model.Medicine;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    long insert(Medicine medicine);

    @Update
    void update(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Query("DELETE FROM medicine_table")
    void deleteAllData();

    @Query("SELECT * FROM medicine_table WHERE medicineID=:id ")
    Medicine getMedicineByID(long id);

    @Query("SELECT * FROM medicine_table ORDER BY medicineID ASC")
    List<Medicine> getAllDataList();

}
