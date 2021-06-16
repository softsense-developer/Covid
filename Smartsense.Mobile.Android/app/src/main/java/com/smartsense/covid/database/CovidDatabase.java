package com.smartsense.covid.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.smartsense.covid.dao.CovidDao;
import com.smartsense.covid.dao.MedicineDao;
import com.smartsense.covid.dao.MedicineTimeDao;
import com.smartsense.covid.dao.MedicineUsageDao;
import com.smartsense.covid.model.Covid;
import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.model.MedicineUsage;

@Database(entities = {Covid.class, Medicine.class, MedicineTime.class, MedicineUsage.class},  version = 13)
public abstract class CovidDatabase extends RoomDatabase {

    private static CovidDatabase instance;

    public abstract CovidDao covidDao();

    public abstract MedicineDao medicineDao();

    public abstract MedicineTimeDao medicineTimeDao();

    public abstract MedicineUsageDao medicineUsageDao();

    public static synchronized CovidDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , CovidDatabase.class, "covid_database")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
