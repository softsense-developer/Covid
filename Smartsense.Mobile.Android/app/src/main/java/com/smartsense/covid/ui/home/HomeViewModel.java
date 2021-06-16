package com.smartsense.covid.ui.home;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.smartsense.covid.model.Covid;
import com.smartsense.covid.repo.CovidRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeViewModel extends AndroidViewModel {

    private CovidRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new CovidRepository(application);
    }

    public void insert(Covid covid) {
        repository.insert(covid);
    }

    public void update(Covid covid) {
        repository.update(covid);
    }

    public void delete(Covid covid) {
        repository.delete(covid);
    }

    public void deleteAllData() {
        repository.deleteAllData();
    }


    public LiveData<PagedList<Covid>> getAllData() {
        return repository.getAllData();
    }

    public LiveData<PagedList<Covid>> getTypeData(int type) {
        if (type == 0) {
            return getAllData();
        } else {
            return repository.getTypeData(type);
        }
    }

    public LiveData<List<Covid>> getTypeDataWithLimit(int type){
        return repository.getTypeDataWithLimit(type);
    }

    public LiveData<Covid> getLastTypeData(int type){
        return repository.getLastTypeData(type);
    }

    public List<Covid> getAllDataList() throws ExecutionException, InterruptedException {
        return repository.getAllDataList();
    }

}