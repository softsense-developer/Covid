package com.smartsense.covid.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;

public class ScheduleSettingsActivity extends AppCompatActivity {

    private TextInputLayout temp, heart, spo2;
    private MaterialCheckBox sentDataServer;
    private MaterialButton update;
    private String tempData, heartData, spO2Data;
    private int tempInt, heartInt, spO2Int;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_settings);


        temp = findViewById(R.id.tempScheduleTI);
        heart = findViewById(R.id.heartScheduleTI);
        spo2 = findViewById(R.id.spO2ScheduleTI);
        update = findViewById(R.id.scheduleUpdateBt);
        sentDataServer = findViewById(R.id.sendDataToServerCB);

        prefManager = new PrefManager(getApplicationContext());


        temp.getEditText().setText(String.valueOf(((prefManager.getTimeTemp() / 1000) / 60)));
        heart.getEditText().setText(String.valueOf(((prefManager.getTimeHeart() / 1000) / 60)));
        spo2.getEditText().setText(String.valueOf(((prefManager.getTimeSpO2() / 1000) / 60)));

        if (prefManager.getSentDataServer()) {
            sentDataServer.setChecked(true);
        }

        update.setOnClickListener(view -> {
            tempData = temp.getEditText().getText().toString().trim();
            heartData = heart.getEditText().getText().toString().trim();
            spO2Data = spo2.getEditText().getText().toString().trim();

            temp.setError(null);
            heart.setError(null);
            spo2.setError(null);

            try {
                if (!tempData.isEmpty() && !heartData.isEmpty() && !spO2Data.isEmpty()) {
                    tempInt = Integer.parseInt(tempData);
                    heartInt = Integer.parseInt(heartData);
                    spO2Int = Integer.parseInt(spO2Data);
                    if ((tempInt >= 5 && tempInt <= 360) && (heartInt >= 5 && heartInt <= 360) && (spO2Int >= 5 && spO2Int <= 360)) {
                        prefManager.setTimeTemp((tempInt * 60 * 1000));
                        prefManager.setTimeHeart((heartInt * 60 * 1000));
                        prefManager.setTimeSpO2((spO2Int * 60 * 1000));
                        Toast.makeText(ScheduleSettingsActivity.this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
                    } else {
                        if (tempInt < 5 || tempInt > 360) {
                            temp.setError(getString(R.string.schedule_helper_text));
                        }
                        if (heartInt < 5 || heartInt > 360) {
                            heart.setError(getString(R.string.schedule_helper_text));
                        }
                        if (spO2Int < 5 || spO2Int > 360) {
                            spo2.setError(getString(R.string.schedule_helper_text));
                        }
                    }
                } else {
                    if (tempData.isEmpty()) {
                        temp.setError(getString(R.string.measurement_empty));
                    }
                    if (heartData.isEmpty()) {
                        heart.setError(getString(R.string.measurement_empty));
                    }
                    if (spO2Data.isEmpty()) {
                        spo2.setError(getString(R.string.measurement_empty));
                    }
                }
            } catch (Exception e) {
                Log.i("SmartSense", e.getMessage());
            }

        });


        sentDataServer.setOnCheckedChangeListener((compoundButton, b) -> {
            if (sentDataServer.isChecked()) {
                prefManager.setSentDataServer(true);
                Toast.makeText(this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
            } else {
                prefManager.setSentDataServer(false);
                Toast.makeText(this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
            }
        });

    }


}