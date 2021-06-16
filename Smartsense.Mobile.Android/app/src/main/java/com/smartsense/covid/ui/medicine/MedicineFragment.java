package com.smartsense.covid.ui.medicine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartsense.covid.MedicineAddEditActivity;
import com.smartsense.covid.MyConstant;
import com.smartsense.covid.R;
import com.smartsense.covid.adapters.medicineAdapter.DerpAdapter;
import com.smartsense.covid.adapters.medicineAdapter.DerpData;
import com.smartsense.covid.adapters.medicineAdapter.Item;
import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.repo.MedicineTimeRepo;

import java.util.ArrayList;
import java.util.List;

public class MedicineFragment extends Fragment implements DerpAdapter.ItemClickCallback {

    private MedicineViewModel mViewModel;
    private static final String TAG = "Smartsense";
    private FloatingActionButton addMedicineButton;

    private RecyclerView recView;
    private LinearLayoutManager layoutManager;
    private DerpAdapter adapter;
    private ArrayList listData;
    private TextView noMedicineText;

    private MedicineTimeRepo medicineTimeRepo;


    public static MedicineFragment newInstance() {
        return new MedicineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.medicine_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(MedicineViewModel.class);

        addMedicineButton = root.findViewById(R.id.addMedicineButton);


        recView = root.findViewById(R.id.medicineItemRecView);
        noMedicineText = root.findViewById(R.id.noMedicineText);

        listData = (ArrayList) DerpData.getListData();
        layoutManager = new LinearLayoutManager(getContext());
        recView.setLayoutManager(layoutManager);
        recView.setHasFixedSize(true);
        adapter = new DerpAdapter(listData, getContext());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        medicineTimeRepo = new MedicineTimeRepo(getContext());


        List<MedicineTime> medicineTimes = mViewModel.getAllMedicineTimeData();
        for (int i = 0; i < medicineTimes.size(); i++) {
            Log.i(TAG, medicineTimes.get(i).toString());
        }


        addMedicineButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), MedicineAddEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("addEdit", MyConstant.MEDICINE_ADD);
            intent.putExtras(bundle);
            startActivity(intent);

           /*Medicine medicine=new Medicine("Test");
            mViewModel.insert(medicine);
            MedicineTime medicineTime=new MedicineTime(1,1234544123,"5");
            mViewModel.insert(medicineTime);
            MedicineTime medicineTime2=new MedicineTime(1,1235543443,"7");
            mViewModel.insert(medicineTime2);*/

        });


        return root;
    }

    private void getAllMedicineData() {
        listData.clear();
        adapter.notifyDataSetChanged();
        List<Medicine> allData = mViewModel.getAllData();
        if (allData.size() != 0) {
            noMedicineText.setVisibility(View.GONE);
            for (int i = 0; i < allData.size(); i++) {
                Log.i(TAG, allData.get(i).getMedicineName() + " " + allData.get(i).getMedicineID());
                Item item = new Item();
                item.setId(allData.get(i).getMedicineID());
                item.setMedicineName(allData.get(i).getMedicineName());
                item.setOnComingUsageTime(allData.get(i).getMedicineName());
                listData.add(item);
            }
            adapter.notifyDataSetChanged();
        } else {
            noMedicineText.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClick(int p) {
        Item item = (Item) listData.get(p);

        Intent intent = new Intent(getActivity(), MedicineAddEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("addEdit", MyConstant.MEDICINE_EDIT);
        bundle.putLong("medicineID", item.getId());
        bundle.putString("medicineName", item.getMedicineName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllMedicineData();
    }
}