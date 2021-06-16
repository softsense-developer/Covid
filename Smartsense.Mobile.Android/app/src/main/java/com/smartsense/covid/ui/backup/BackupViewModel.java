package com.smartsense.covid.ui.backup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.smartsense.covid.model.Covid;
import com.smartsense.covid.repo.CovidRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BackupViewModel extends AndroidViewModel {
    private CovidRepository repository;

    public BackupViewModel(@NonNull Application application) {
        super(application);
        repository = new CovidRepository(application);
    }

    public void insert(Covid covid) {
        repository.insert(covid);
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

    public boolean isDataExist(long sTimeStamp) {
        try {
            return repository.isDataExist(sTimeStamp);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

}