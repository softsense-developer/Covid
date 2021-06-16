package com.smartsense.covid.repo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.smartsense.covid.dao.CovidDao;
import com.smartsense.covid.database.CovidDatabase;
import com.smartsense.covid.model.Covid;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CovidRepository {
    private CovidDao covidDao;
    //private LiveData<List<Epilepsy>> data;

    public CovidRepository(Application application) {
        CovidDatabase covidDatabase = CovidDatabase.getInstance(application);
        covidDao = covidDatabase.covidDao();
    }

    public void insert(Covid covid) {
        new InsertCovidAsyncTask(covidDao).execute(covid);
    }

    public void update(Covid covid) {
        new UpdateCovidAsyncTask(covidDao).execute(covid);
    }

    public void delete(Covid covid) {
        new DeleteCovidAsyncTask(covidDao).execute(covid);
    }

    public void deleteAllData() {
        new DeleteAllCovidAsyncTask(covidDao).execute();
    }

    PagedList.Config pagedListConfig =
            (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                    .setPageSize(5).build();

    public LiveData<PagedList<Covid>> getAllData() {
        return (new LivePagedListBuilder<Integer, Covid>(covidDao.getAllData()
                , pagedListConfig))
                .build();
    }


    public List<Covid> getAllDataList() throws ExecutionException, InterruptedException {

        Callable<List<Covid>> callable = new Callable<List<Covid>>() {
            @Override
            public List<Covid> call() throws Exception {
                return covidDao.getAllDataList();
            }
        };

        Future<List<Covid>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public LiveData<List<Covid>> getTypeDataWithLimit(int type) {
        return covidDao.getTypeDataLimit(type);
    }

    public List<Covid> getDailyTypeData(int type, long startTime, long endTime) throws ExecutionException, InterruptedException{
        Callable<List<Covid>> callable = new Callable<List<Covid>>() {
            @Override
            public List<Covid> call() throws Exception {
                return covidDao.getDailyTypeData(type, startTime, endTime);
            }
        };
        Future<List<Covid>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Covid> getDailyAllTypeData(int type, long startTime, long endTime) throws ExecutionException, InterruptedException{
        Callable<List<Covid>> callable = new Callable<List<Covid>>() {
            @Override
            public List<Covid> call() throws Exception {
                return covidDao.getDailyAllTypeData(type, startTime, endTime);
            }
        };
        Future<List<Covid>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Covid> getDailyManualSaveTypeData(int type, long startTime, long endTime) throws ExecutionException, InterruptedException{
        Callable<List<Covid>> callable = new Callable<List<Covid>>() {
            @Override
            public List<Covid> call() throws Exception {
                return covidDao.getDailyManualSaveTypeData(type, startTime, endTime);
            }
        };
        Future<List<Covid>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public LiveData<Covid> getLastTypeData(int type) {
        return covidDao.getLastTypeData(type);
    }

    public LiveData<PagedList<Covid>> getTypeData(int type) {
        return (new LivePagedListBuilder<Integer, Covid>(covidDao.getTypeData(type)
                , pagedListConfig))
                .build();
    }

    public boolean isDataExist(final long sTimestamp) throws ExecutionException, InterruptedException {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return covidDao.isDataExist(sTimestamp);
            }
        };

        Future<Boolean> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    private static class InsertCovidAsyncTask extends AsyncTask<Covid, Void, Void> {

        private CovidDao covidDao;

        private InsertCovidAsyncTask(CovidDao covidDao) {
            this.covidDao = covidDao;
        }

        @Override
        protected Void doInBackground(Covid... epilepsies) {
            covidDao.insert(epilepsies[0]);
            return null;
        }
    }

    private static class UpdateCovidAsyncTask extends AsyncTask<Covid, Void, Void> {

        private CovidDao covidDao;

        private UpdateCovidAsyncTask(CovidDao covidDao) {
            this.covidDao = covidDao;
        }

        @Override
        protected Void doInBackground(Covid... epilepsies) {
            covidDao.update(epilepsies[0]);
            return null;
        }
    }

    private static class DeleteCovidAsyncTask extends AsyncTask<Covid, Void, Void> {

        private CovidDao covidDao;

        private DeleteCovidAsyncTask(CovidDao covidDao) {
            this.covidDao = covidDao;
        }

        @Override
        protected Void doInBackground(Covid... epilepsies) {
            covidDao.delete(epilepsies[0]);
            return null;
        }
    }

    private static class DeleteAllCovidAsyncTask extends AsyncTask<Void, Void, Void> {

        private CovidDao covidDao;

        private DeleteAllCovidAsyncTask(CovidDao covidDao) {
            this.covidDao = covidDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            covidDao.deleteAllData();
            return null;
        }
    }


}
