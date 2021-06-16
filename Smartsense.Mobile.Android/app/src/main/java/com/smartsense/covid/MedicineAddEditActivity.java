package com.smartsense.covid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.smartsense.covid.adapters.medicineTimeDoseAdapter.DerpAdapter;
import com.smartsense.covid.adapters.medicineTimeDoseAdapter.DerpData;
import com.smartsense.covid.adapters.medicineTimeDoseAdapter.Item;
import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.repo.MedicineRepository;
import com.smartsense.covid.repo.MedicineTimeRepo;
import com.smartsense.covid.repo.MedicineUsageRepo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MedicineAddEditActivity extends AppCompatActivity implements DerpAdapter.ItemClickCallback {

    private static final String TAG = "Smartsense";
    private TextInputLayout medicineNameTI;
    private MaterialButton medicineSaveButton, medicineDeleteButton;
    private MaterialCardView medicineReminderAdd;
    private String medicineName;
    private ArrayList listData;
    private DerpAdapter adapter;
    private boolean anyEmpty = false;
    private PrefManager prefManager;
    private AlarmManager alarmManager;
    private Intent alarmIntent, alarmCheckIntent;
    private int addEdit;

    private MedicineRepository medicineRepository;
    private MedicineTimeRepo medicineTimeRepo;
    private MedicineUsageRepo medicineUsageRepo;
    long medicineID;
    private List<MedicineTime> medicineTimes;
    private long checkValueAdd = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_add_edit);

        addEdit = getIntent().getExtras().getInt("addEdit", MyConstant.MEDICINE_ADD);

        medicineNameTI = findViewById(R.id.medicineNameTI);

        medicineSaveButton = findViewById(R.id.medicineSaveButton);
        medicineDeleteButton = findViewById(R.id.medicineDeleteButton);
        medicineReminderAdd = findViewById(R.id.medicineReminderAdd);

        prefManager = new PrefManager(this);

        RecyclerView recyclerView = findViewById(R.id.medicineTimeDoseRecView);
        listData = (ArrayList) DerpData.getListData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DerpAdapter(listData, this);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickCallback(this);
        adapter.setListData(listData);
        adapter.notifyDataSetChanged();

        medicineRepository = new MedicineRepository(getApplication());
        medicineTimeRepo = new MedicineTimeRepo(getApplication());
        medicineUsageRepo = new MedicineUsageRepo(getApplication());


        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(getApplicationContext(), MyBroadCastReceiver.class);
        alarmCheckIntent = new Intent(getApplicationContext(), MedicineCheckBroadCastReceiver.class);


        if (addEdit == MyConstant.MEDICINE_EDIT) {
            medicineNameTI.getEditText().setText(getIntent().getExtras().getString("medicineName", "Null"));

            medicineID = getIntent().getExtras().getLong("medicineID", 0);
            medicineTimes = medicineTimeRepo.getMedicineTimes(medicineID);
            if (medicineTimes != null) {
                for (int i = 0; i < medicineTimes.size(); i++) {
                    Log.i(TAG, medicineTimes.get(i).getDose());
                    Item item = new Item();
                    item.setTimeStamp(medicineTimes.get(i).getTimeUsage());
                    item.setDose(medicineTimes.get(i).getDose());
                    listData.add(item);
                }
                adapter.notifyDataSetChanged();
                medicineDeleteButton.setVisibility(View.VISIBLE);
                medicineSaveButton.setText(getString(R.string.update));
            }
        }


        medicineReminderAdd.setOnClickListener(view -> {
            getTimePicker();
        });

        medicineDeleteButton.setOnClickListener(view -> {
            cancelAllAlarm();
            medicineTimeRepo.deleteByMedicineID(medicineID);
            medicineRepository.delete(medicineRepository.getMedicineByID(medicineID));
            medicineNameTI.getEditText().setText("");
            finish();
        });


        medicineSaveButton.setOnClickListener(view -> {
            anyEmpty = false;
            medicineName = medicineNameTI.getEditText().getText().toString().trim();
            if (!medicineName.isEmpty()) {
                if (listData.size() > 0) {
                    for (int i = 0; i < listData.size(); i++) {
                        Item item = (Item) listData.get(i);
                        if (item.getDose().isEmpty()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.doses_empty), Toast.LENGTH_SHORT).show();
                            anyEmpty = true;
                            break;
                        }
                    }
                    if (!anyEmpty) {
                        if (addEdit == MyConstant.MEDICINE_ADD) {
                            Medicine medicine = new Medicine(medicineName);
                            medicineID = medicineInsert(medicine);
                            if (medicineID != 0) {
                                for (int i = 0; i < listData.size(); i++) {
                                    Item item = (Item) listData.get(i);
                                    Log.i(TAG, item.getDose());
                                    MedicineTime medicineTime = new MedicineTime(medicineID, item.getTimeStamp(), item.getDose());
                                    long medicineTimeID = medicineTimeInsert(medicineTime);
                                    addAlarm(item, medicineTimeID);
                                }
                                addEdit = MyConstant.MEDICINE_EDIT;

                                Handler handler = new Handler();
                                Runnable runnable = () -> {
                                    medicineTimes = medicineTimeRepo.getMedicineTimes(medicineID);
                                    Toast.makeText(getApplicationContext(), getString(R.string.data_saved), Toast.LENGTH_SHORT).show();
                                };
                                handler.postDelayed(runnable, 100);

                            } else {
                                Log.i(TAG, "medicine id 0");
                            }
                        } else {
                            //EDIT
                            Medicine medicine = medicineRepository.getMedicineByID(medicineID);
                            medicine.setMedicineName(medicineName);
                            medicineRepository.update(medicine);
                            cancelAllAlarm();
                            medicineTimeRepo.deleteByMedicineID(medicineID);

                            Handler handler = new Handler();
                            Runnable runnable = () -> {
                                if (medicineID != 0) {
                                    for (int i = 0; i < listData.size(); i++) {
                                        Item item = (Item) listData.get(i);
                                        MedicineTime medicineTime = new MedicineTime(medicineID, item.getTimeStamp(), item.getDose());
                                        long medicineTimeID = medicineTimeInsert(medicineTime);
                                        addAlarm(item, medicineTimeID);
                                    }
                                    Toast.makeText(getApplicationContext(), getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i(TAG, "medicine id 0");
                                }
                                medicineTimes = medicineTimeRepo.getMedicineTimes(medicineID);
                            };
                            handler.postDelayed(runnable, 100);

                        }

                        medicineDeleteButton.setVisibility(View.VISIBLE);
                        medicineSaveButton.setText(getString(R.string.update));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.reminder_empty), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (medicineName.isEmpty()) {
                    medicineNameTI.setError(getString(R.string.not_empty));
                }
                errorNull();
            }
        });


    }


    private long medicineInsert(Medicine medicine) {
        return medicineRepository.insert(medicine);
    }


    private long medicineTimeInsert(MedicineTime medicineTime) {
        return medicineTimeRepo.insert(medicineTime);
    }


    private void addAlarm(Item item, long medicineTimeID) {
        if (medicineTimeID != 0) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) medicineTimeID, alarmIntent.putExtra("id", ((int) medicineTimeID)), 0);
            PendingIntent pendingIntentCheck = PendingIntent.getBroadcast(getApplicationContext(), (int) (medicineTimeID + checkValueAdd), alarmCheckIntent.putExtra("id", ((int) (medicineTimeID + checkValueAdd))), 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.getTimeStamp());
            calendar.add(Calendar.HOUR_OF_DAY, 1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, item.getTimeStamp(), AlarmManager.INTERVAL_DAY, pendingIntent);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentCheck);
            } else {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, item.getTimeStamp(), AlarmManager.INTERVAL_DAY, pendingIntent);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentCheck);
            }
            Log.i(TAG, "Alarm " + (int) medicineTimeID + " " + item.getTimeStamp() + " added");
            Log.i(TAG, "Alarm Check " + ((int) medicineTimeID + checkValueAdd) + " " + calendar.getTimeInMillis() + " added");
        } else {
            Log.i(TAG, "medicineTimeID 0");
        }
    }

    private List<Medicine> geta() {
        try {
            return medicineRepository.getAllDataList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void cancelAllAlarm() {
        if (medicineTimes != null) {
            for (int i = 0; i < medicineTimes.size(); i++) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) medicineTimes.get(i).getMedicineTimeID(), alarmIntent, 0);
                PendingIntent pendingIntentCheck = PendingIntent.getBroadcast(getApplicationContext(), (int) (medicineTimes.get(i).getMedicineTimeID() + checkValueAdd), alarmCheckIntent, 0);
                alarmManager.cancel(pendingIntent);
                alarmManager.cancel(pendingIntentCheck);
                Log.i(TAG, "Alarm " + (int) medicineTimes.get(i).getMedicineTimeID() + " Cancelled");
                Log.i(TAG, "Alarm checked " + ((int) medicineTimes.get(i).getMedicineTimeID() + checkValueAdd) + " Cancelled");
            }
        }
    }

    private void cancelSpecificAlarm(long medicineID) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) medicineID, alarmIntent, 0);
        PendingIntent pendingIntentCheck = PendingIntent.getBroadcast(getApplicationContext(), (int) (medicineID + checkValueAdd), alarmCheckIntent, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.cancel(pendingIntentCheck);
        Log.i(TAG, "Alarm " + medicineID + " Cancelled");
        Log.i(TAG, "Alarm check" + medicineID + checkValueAdd + " Cancelled");
    }

    private void getTimePicker() {
        Calendar takvim = Calendar.getInstance();
        int saat = takvim.get(Calendar.HOUR_OF_DAY);
        int dakika = takvim.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);
                    cal.set(Calendar.SECOND, 0);
                    Item item = new Item();
                    item.setTimeStamp(cal.getTimeInMillis());
                    item.setDose("1");
                    listData.add(item);
                    adapter.notifyDataSetChanged();

                }, saat, dakika, true);

        tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, getString(R.string.select), tpd);
        tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), tpd);
        tpd.show();
    }

    private void errorNull() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                medicineNameTI.setError(null);
            }
        }, 4000);
    }

    @Override
    public void onItemClick(int p) {
        Item item = (Item) listData.get(p);
        if (adapter.getItemCount() > 1) {
            listData.remove(item);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTimeClick(int p) {
        Item item = (Item) listData.get(p);
        Calendar takvim = Calendar.getInstance();
        int saat = takvim.get(Calendar.HOUR_OF_DAY);
        int dakika = takvim.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);
                    cal.set(Calendar.SECOND, 0);
                    item.setTimeStamp(cal.getTimeInMillis());
                    adapter.notifyItemChanged(p, item);
                }, saat, dakika, true);

        tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, getString(R.string.select), tpd);
        tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), tpd);
        tpd.show();
    }
}