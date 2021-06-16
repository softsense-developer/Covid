package com.smartsense.covid.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smartsense.covid.model.Covid;

import java.util.List;

@Dao
public interface CovidDao {

    @Insert
    void insert(Covid covid);

    @Update
    void update(Covid covid);

    @Delete
    void delete(Covid covid);

    @Query("DELETE FROM covid_table")
    void deleteAllData();

    @Query("SELECT * FROM covid_table ORDER BY time DESC")
    DataSource.Factory<Integer, Covid> getAllData();

    @Query("SELECT * FROM covid_table ORDER BY time DESC")
    List<Covid> getAllDataList();

    @Query("SELECT * FROM covid_table WHERE dataType=:type ORDER BY time DESC")
    DataSource.Factory<Integer, Covid> getTypeData(int type);

    @Query("SELECT * FROM covid_table WHERE (dataType=:type) AND (time >= :startTime AND time <= :endTime) AND saveType=1 ORDER BY time ASC")
    List<Covid> getDailyTypeData(int type, long startTime, long endTime);

    @Query("SELECT * FROM covid_table WHERE (dataType=:type) AND (time >= :startTime AND time <= :endTime) ORDER BY time ASC")
    List<Covid> getDailyAllTypeData(int type, long startTime, long endTime);

    @Query("SELECT * FROM covid_table WHERE (dataType=:type) AND (time >= :startTime AND time <= :endTime) AND saveType=2 ORDER BY time DESC")
    List<Covid> getDailyManualSaveTypeData(int type, long startTime, long endTime);

    @Query("SELECT * FROM covid_table WHERE dataType=:type ORDER BY time DESC LIMIT 10")
    LiveData<List<Covid>> getTypeDataLimit(int type);

    @Query("SELECT * FROM covid_table WHERE dataType=:type ORDER BY time DESC LIMIT 1")
    LiveData<Covid> getLastTypeData(int type);

    @Query("SELECT * FROM covid_table WHERE time=:sTimestamp")
    boolean isDataExist(long sTimestamp);
}
