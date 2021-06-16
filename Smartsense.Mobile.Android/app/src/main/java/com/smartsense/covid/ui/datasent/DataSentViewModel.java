package com.smartsense.covid.ui.datasent;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.smartsense.covid.model.Covid;
import com.smartsense.covid.repo.CovidRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataSentViewModel extends AndroidViewModel {
    private CovidRepository repository;

    public DataSentViewModel(@NonNull Application application) {
        super(application);
        repository = new CovidRepository(application);
    }

    public List<Covid> getAllData() {
        try {
            return repository.getAllDataList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}