package com.smartsense.covid.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.smartsense.covid.MyConstant;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;

public class CovidContactStatusActivity extends AppCompatActivity {

    private MaterialRadioButton quarantineHomeRB, quarantineHospitalRB, quarantineContactRB;
    private RadioGroup quarantineRBGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_contact_status);

        quarantineRBGroup = findViewById(R.id.quarantineRBGroup);
        quarantineHomeRB = findViewById(R.id.quarantineHomeRB);
        quarantineHospitalRB = findViewById(R.id.quarantineHospitalRB);
        quarantineContactRB = findViewById(R.id.quarantineContactRB);

        PrefManager prefManager = new PrefManager(this);

        if (prefManager.getQuarantineStatus() == MyConstant.QUARANTINE_HOME) {
            quarantineHomeRB.setChecked(true);
        } else if (prefManager.getQuarantineStatus() == MyConstant.QUARANTINE_HOSPITAL) {
            quarantineHospitalRB.setChecked(true);
        } else if (prefManager.getQuarantineStatus() == MyConstant.QUARANTINE_CONTACT) {
            quarantineContactRB.setChecked(true);
        }

        quarantineRBGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (quarantineHomeRB.isChecked()) {
                prefManager.setQuarantineStatus(MyConstant.QUARANTINE_HOME);
                Toast.makeText(this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
            } else if (quarantineHospitalRB.isChecked()) {
                prefManager.setQuarantineStatus(MyConstant.QUARANTINE_HOSPITAL);
                Toast.makeText(this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
            } else if (quarantineContactRB.isChecked()) {
                prefManager.setQuarantineStatus(MyConstant.QUARANTINE_CONTACT);
                Toast.makeText(this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
            }
        });
    }
}