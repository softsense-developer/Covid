package com.smartsense.covid.repo;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.smartsense.covid.dao.MedicineUsageDao;
import com.smartsense.covid.database.CovidDatabase;
import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.model.MedicineUsage;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MedicineUsageRepo {
    private MedicineUsageDao medicineUsageDao;

    public MedicineUsageRepo(Application application) {
        CovidDatabase covidDatabase = CovidDatabase.getInstance(application);
        medicineUsageDao = covidDatabase.medicineUsageDao();
    }

    public MedicineUsageRepo(Context context) {
        CovidDatabase covidDatabase = CovidDatabase.getInstance(context);
        medicineUsageDao = covidDatabase.medicineUsageDao();
    }

    public void insert(MedicineUsage medicineUsage) {
        new InsertMedicineUsageAsyncTask(medicineUsageDao).execute(medicineUsage);
    }

    public void update(MedicineUsage medicineUsage) {
        new UpdateMedicineUsageAsyncTask(medicineUsageDao).execute(medicineUsage);
    }

    public void delete(MedicineUsage medicineUsage) {
        new DeleteMedicineUsageAsyncTask(medicineUsageDao).execute(medicineUsage);
    }

    public void deleteAllData() {
        new DeleteAllMedicineUsageAsyncTask(medicineUsageDao).execute();
    }

    public MedicineUsage getMedicineUsageByID(long id) {
        Callable<MedicineUsage> insertCallable = () -> medicineUsageDao.getMedicineUsageByID(id);
        Future<MedicineUsage> future = Executors.newSingleThreadExecutor().submit(insertCallable);
        try {
            return future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MedicineUsage> getAllUsedDataList(boolean useStatus) {
        Callable<List<MedicineUsage>> insertCallable = () -> medicineUsageDao.getAllUsedDataList(useStatus);
        Future<List<MedicineUsage>> future = Executors.newSingleThreadExecutor().submit(insertCallable);
        try {
            return future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MedicineUsage> getAllDataList() throws ExecutionException, InterruptedException {

        Callable<List<MedicineUsage>> callable = () -> medicineUsageDao.getAllDataList();

        Future<List<MedicineUsage>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    private static class InsertMedicineUsageAsyncTask extends AsyncTask<MedicineUsage, Void, Void> {

        private MedicineUsageDao medicineUsageDao;

        private InsertMedicineUsageAsyncTask(MedicineUsageDao medicineUsageDao) {
            this.medicineUsageDao = medicineUsageDao;
        }

        @Override
        protected Void doInBackground(MedicineUsage... medicines) {
            medicineUsageDao.insert(medicines[0]);
            return null;
        }
    }

    private static class UpdateMedicineUsageAsyncTask extends AsyncTask<MedicineUsage, Void, Void> {

        private MedicineUsageDao medicineUsageDao;

        private UpdateMedicineUsageAsyncTask(MedicineUsageDao medicineUsageDao) {
            this.medicineUsageDao = medicineUsageDao;
        }

        @Override
        protected Void doInBackground(MedicineUsage... medicines) {
            medicineUsageDao.update(medicines[0]);
            return null;
        }
    }

    private static class DeleteMedicineUsageAsyncTask extends AsyncTask<MedicineUsage, Void, Void> {

        private MedicineUsageDao medicineUsageDao;

        private DeleteMedicineUsageAsyncTask(MedicineUsageDao medicineUsageDao) {
            this.medicineUsageDao = medicineUsageDao;
        }

        @Override
        protected Void doInBackground(MedicineUsage... medicines) {
            medicineUsageDao.delete(medicines[0]);
            return null;
        }
    }

    private static class DeleteAllMedicineUsageAsyncTask extends AsyncTask<Void, Void, Void> {

        private MedicineUsageDao medicineUsageDao;

        private DeleteAllMedicineUsageAsyncTask(MedicineUsageDao medicineUsageDao) {
            this.medicineUsageDao = medicineUsageDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicineUsageDao.deleteAllData();
            return null;
        }
    }


}
