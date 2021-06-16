package com.smartsense.covid.repo;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.smartsense.covid.dao.MedicineDao;
import com.smartsense.covid.database.CovidDatabase;
import com.smartsense.covid.model.Medicine;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MedicineRepository {
    private MedicineDao medicineDao;

    public MedicineRepository(Application application) {
        CovidDatabase covidDatabase = CovidDatabase.getInstance(application);
        medicineDao = covidDatabase.medicineDao();
    }

    public MedicineRepository(Context context) {
        CovidDatabase covidDatabase = CovidDatabase.getInstance(context);
        medicineDao = covidDatabase.medicineDao();
    }

    public long insert(Medicine medicine) {
        Callable<Long> insertCallable = () -> medicineDao.insert(medicine);
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

    public void update(Medicine medicine) {
        new UpdateMedicineAsyncTask(medicineDao).execute(medicine);
    }

    public void delete(Medicine medicine) {
        new DeleteMedicineAsyncTask(medicineDao).execute(medicine);
    }

    public void deleteAllData() {
        new DeleteAllMedicineAsyncTask(medicineDao).execute();
    }


    public Medicine getMedicineByID(long id) {
        Callable<Medicine> insertCallable = () -> medicineDao.getMedicineByID(id);
        Future<Medicine> future = Executors.newSingleThreadExecutor().submit(insertCallable);
        try {
            return future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Medicine> getAllDataList() throws ExecutionException, InterruptedException {

        Callable<List<Medicine>> callable = () -> medicineDao.getAllDataList();

        Future<List<Medicine>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    private static class InsertMedicineAsyncTask extends AsyncTask<Medicine, Void, Long> {

        private MedicineDao medicineDao;

        private InsertMedicineAsyncTask(MedicineDao medicineDao) {
            this.medicineDao = medicineDao;
        }

        @Override
        protected Long doInBackground(Medicine... medicines) {
            return medicineDao.insert(medicines[0]);
        }
    }

    private static class UpdateMedicineAsyncTask extends AsyncTask<Medicine, Void, Void> {

        private MedicineDao medicineDao;

        private UpdateMedicineAsyncTask(MedicineDao medicineDao) {
            this.medicineDao = medicineDao;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            medicineDao.update(medicines[0]);
            return null;
        }
    }

    private static class DeleteMedicineAsyncTask extends AsyncTask<Medicine, Void, Void> {

        private MedicineDao medicineDao;

        private DeleteMedicineAsyncTask(MedicineDao medicineDao) {
            this.medicineDao = medicineDao;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            medicineDao.delete(medicines[0]);
            return null;
        }
    }

    private static class DeleteAllMedicineAsyncTask extends AsyncTask<Void, Void, Void> {

        private MedicineDao medicineDao;

        private DeleteAllMedicineAsyncTask(MedicineDao medicineDao) {
            this.medicineDao = medicineDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            medicineDao.deleteAllData();
            return null;
        }
    }


}
