package com.smartsense.covid.repo;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.smartsense.covid.dao.MedicineTimeDao;
import com.smartsense.covid.database.CovidDatabase;
import com.smartsense.covid.model.Covid;
import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MedicineTimeRepo {
    private MedicineTimeDao medicineTimeDao;

    public MedicineTimeRepo(Application application) {
        CovidDatabase covidDatabase = CovidDatabase.getInstance(application);
        medicineTimeDao = covidDatabase.medicineTimeDao();
    }

    public MedicineTimeRepo(Context context) {
        CovidDatabase covidDatabase = CovidDatabase.getInstance(context);
        medicineTimeDao = covidDatabase.medicineTimeDao();
    }

    public long insert(MedicineTime medicineTime) {
        Callable<Long> insertCallable = () -> medicineTimeDao.insert(medicineTime);
        long rowId = 0;
        Future<Long> future = Executors.newSingleThreadExecutor().submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public void update(MedicineTime medicineTime) {
        new UpdateMedicineTimeAsyncTask(medicineTimeDao).execute(medicineTime);
    }

    public void delete(MedicineTime medicineTime) {
        new DeleteMedicineTimeAsyncTask(medicineTimeDao).execute(medicineTime);
    }

    public void deleteAllData() {
        new DeleteAllMedicineTimeAsyncTask(medicineTimeDao).execute();
    }

    public void deleteByMedicineID(long medicineID) {
        new DeleteByMedicineIDAsyncTask(medicineTimeDao).execute(medicineID);
    }


    public MedicineTime getMedicineTimeByID(long id) {
        Callable<MedicineTime> insertCallable = () -> medicineTimeDao.getMedicineTimeByID(id);
        Future<MedicineTime> future = Executors.newSingleThreadExecutor().submit(insertCallable);
        try {
            return future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<MedicineTime> getAllDataList() {

        Callable<List<MedicineTime>> callable = () -> medicineTimeDao.getAllDataList();

        Future<List<MedicineTime>> future = Executors.newSingleThreadExecutor().submit(callable);
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<MedicineTime> getMedicineTimes(long medicineID){
        Callable<List<MedicineTime>> callable =() -> medicineTimeDao.getMedicineTimes(medicineID);

        Future<List<MedicineTime>> future = Executors.newSingleThreadExecutor().submit(callable);
        try {
            return future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       return null;
    }


    private static class InsertMedicineTimeAsyncTask extends AsyncTask<MedicineTime, Void, Void> {

        private MedicineTimeDao medicineTimeDao;

        private InsertMedicineTimeAsyncTask(MedicineTimeDao medicineTimeDao) {
            this.medicineTimeDao = medicineTimeDao;
        }

        @Override
        protected Void doInBackground(MedicineTime... medicines) {
            medicineTimeDao.insert(medicines[0]);
            return null;
        }
    }

    private static class UpdateMedicineTimeAsyncTask extends AsyncTask<MedicineTime, Void, Void> {

        private MedicineTimeDao medicineTimeDao;

        private UpdateMedicineTimeAsyncTask(MedicineTimeDao medicineTimeDao) {
            this.medicineTimeDao = medicineTimeDao;
        }

        @Override
        protected Void doInBackground(MedicineTime... medicines) {
            medicineTimeDao.update(medicines[0]);
            return null;
        }
    }

    private static class DeleteMedicineTimeAsyncTask extends AsyncTask<MedicineTime, Void, Void> {

        private MedicineTimeDao medicineTimeDao;

        private DeleteMedicineTimeAsyncTask(MedicineTimeDao medicineTimeDao) {
            this.medicineTimeDao = medicineTimeDao;
        }

        @Override
        protected Void doInBackground(MedicineTime... medicines) {
            medicineTimeDao.delete(medicines[0]);
            return null;
        }
    }

    private static class DeleteAllMedicineTimeAsyncTask extends AsyncTask<Void, Void, Void> {

        private MedicineTimeDao medicineTimeDao;

        private DeleteAllMedicineTimeAsyncTask(MedicineTimeDao medicineTimeDao) {
            this.medicineTimeDao = medicineTimeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicineTimeDao.deleteAllData();
            return null;
        }
    }

    private static class DeleteByMedicineIDAsyncTask extends AsyncTask<Long, Void, Void> {

        private MedicineTimeDao medicineTimeDao;

        private DeleteByMedicineIDAsyncTask(MedicineTimeDao medicineTimeDao) {
            this.medicineTimeDao = medicineTimeDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            medicineTimeDao.deleteByMedicineID(longs[0]);
            return null;
        }
    }


}
