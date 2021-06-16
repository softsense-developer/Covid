package com.smartsense.covid.ui.medicine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.smartsense.covid.dao.MedicineDao;
import com.smartsense.covid.model.Covid;
import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.repo.CovidRepository;
import com.smartsense.covid.repo.MedicineRepository;
import com.smartsense.covid.repo.MedicineTimeRepo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MedicineViewModel extends AndroidViewModel {
    private MedicineRepository medicineRepository;
    private MedicineTimeRepo medicineTimeRepo;

    public MedicineViewModel(@NonNull Application application) {
        super(application);
        medicineRepository = new MedicineRepository(application);
        medicineTimeRepo = new MedicineTimeRepo(application);
    }

    public void insert(Medicine medicine) {
        medicineRepository.insert(medicine);
    }

    public void update(Medicine medicine) {
        medicineRepository.update(medicine);
    }

    public void delete(Medicine medicine) {
        medicineRepository.delete(medicine);
    }

    public void deleteAllData() {
        medicineRepository.deleteAllData();
    }

    public List<Medicine> getAllData() {
        try {
            return medicineRepository.getAllDataList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MedicineTime> getAllMedicineTimeData() {
        return medicineTimeRepo.getAllDataList();
    }

    public void insert(MedicineTime medicineTime) {
        medicineTimeRepo.insert(medicineTime);
    }
}