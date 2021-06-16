package com.smartsense.covid.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.smartsense.covid.CovidMainActivity;
import com.smartsense.covid.LoginActivity;
import com.smartsense.covid.MyConstant;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;
import com.smartsense.covid.location.Common;
import com.smartsense.covid.location.MyBackgroundService;
import com.smartsense.covid.repo.CovidRepository;
import com.smartsense.covid.repo.MedicineRepository;
import com.smartsense.covid.repo.MedicineTimeRepo;
import com.smartsense.covid.repo.MedicineUsageRepo;

import org.greenrobot.eventbus.EventBus;

public class SettingsActivity extends AppCompatActivity {

    private PrefManager prefManager;
    MyBackgroundService mService = null;
    private boolean mBound = false, isRequestEnable = false;
    private static final String TAG = "SettingsActivity";
    private CovidRepository covidRepository;
    private MedicineRepository medicineRepository;
    private MedicineTimeRepo medicineTimeRepo;
    private MedicineUsageRepo medicineUsageRepo;

    private final ServiceConnection mLocationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBackgroundService.LocalBinder binder = (MyBackgroundService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "onServiceDisconnected: ");
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialButton userData = findViewById(R.id.userDataSettingsButton);
        MaterialButton passwordChange = findViewById(R.id.changePasswordButton);
        MaterialButton logoutButton = findViewById(R.id.logOutButton);
        MaterialButton schedule = findViewById(R.id.scheduleSettingsButton);
        MaterialButton contactStatus = findViewById(R.id.contactStatusSettingsButton);

        prefManager = new PrefManager(getApplicationContext());
        covidRepository = new CovidRepository(getApplication());
        medicineRepository = new MedicineRepository(getApplication());
        medicineTimeRepo = new MedicineTimeRepo(getApplication());
        medicineUsageRepo = new MedicineUsageRepo(getApplication());

        userData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, UserSettingsActivity.class);
                startActivity(intent);

            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, PasswordChangeActivity.class);
                startActivity(intent);
            }
        });

        schedule.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, ScheduleSettingsActivity.class);
            startActivity(intent);
        });

        contactStatus.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, CovidContactStatusActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.clear();
                mService.removeLocationUpdates();
                EventBus.getDefault().removeAllStickyEvents();

                covidRepository.deleteAllData();
                medicineRepository.deleteAllData();
                medicineTimeRepo.deleteAllData();
                medicineUsageRepo.deleteAllData();

                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });







        Common.requestingLocationUpdates(SettingsActivity.this);
        bindService(new Intent(SettingsActivity.this, MyBackgroundService.class), mLocationServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        if (mBound) {
            unbindService(mLocationServiceConnection);
            mBound = false;
        }
        super.onStop();
    }
}