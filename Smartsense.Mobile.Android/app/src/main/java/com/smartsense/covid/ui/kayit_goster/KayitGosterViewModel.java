package com.smartsense.covid.ui.kayit_goster;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.smartsense.covid.model.Covid;
import com.smartsense.covid.repo.CovidRepository;

public class KayitGosterViewModel extends AndroidViewModel {
    private CovidRepository repository;
    // private LiveData<List<Epilepsy>> allData;

    public KayitGosterViewModel(@NonNull Application application) {
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
}