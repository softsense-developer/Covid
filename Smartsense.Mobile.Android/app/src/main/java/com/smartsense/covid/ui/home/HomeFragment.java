package com.smartsense.covid.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.smartsense.covid.CovidMainActivity;
import com.smartsense.covid.GraphActivity;
import com.smartsense.covid.MyConstant;
import com.smartsense.covid.PrefManager;
import com.smartsense.covid.R;
import com.smartsense.covid.api.ApiConstantText;
import com.smartsense.covid.model.Medicine;
import com.smartsense.covid.model.MedicineTime;
import com.smartsense.covid.model.MedicineUsage;
import com.smartsense.covid.repo.MedicineRepository;
import com.smartsense.covid.repo.MedicineTimeRepo;
import com.smartsense.covid.repo.MedicineUsageRepo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MaterialCardView cardConnectionStatus, cardHomeLocation, warningCard;
    private ImageView heartIcon;
    private TextView textConnectionStatus, textHeartRate, warningText, warningTypeText, warningDateText, batteryText;
    private View cardConnectionColor, cardPrioriStatusColor, cardBluetoothColor, cardWearingColor, cardBatteryColor;
    private LinearLayout connectionLayout;
    private Handler handler;
    private Runnable run;
    private Handler dataHandler;
    private Runnable runnable;
    private int delay = 100;
    private PrefManager prefManager;
    private Activity activity;
    private Animation anim;
    private ImageView callIcon;

    private Handler hndler;

    private TextView tempText, tempDateText, heartText, heartDateText, spO2Text, spO2DateText, homeWarningText;
    private static final String TAG = "Smartsense";

    private MaterialCardView tempMainCard, heartMainCard, spO2MainCard;
    private ApiConstantText apiText;


    private MaterialCardView medicineStatus;
    private TextView medicineName, medicineDose, medicineTimeText, medicineSecret;
    private MaterialButton medicineSaveButton, warningOkayButton;
    private MedicineUsageRepo medicineUsageRepo;
    private MedicineRepository medicineRepository;
    private MedicineTimeRepo medicineTimeRepo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        cardConnectionStatus = root.findViewById(R.id.cardConnectionStatus);
        heartIcon = root.findViewById(R.id.homeHeartIcon);
        callIcon = root.findViewById(R.id.callIcon);

        textConnectionStatus = root.findViewById(R.id.textConnectionStatus);

        textHeartRate = root.findViewById(R.id.textHeartRate);

        cardConnectionColor = root.findViewById(R.id.cardConnectionColor);

        tempText = root.findViewById(R.id.mainTempText);
        tempDateText = root.findViewById(R.id.mainTempDateText);
        heartText = root.findViewById(R.id.mainHeartText);
        heartDateText = root.findViewById(R.id.mainHeartDateText);
        spO2Text = root.findViewById(R.id.mainSpO2Text);
        spO2DateText = root.findViewById(R.id.mainSpO2DateText);

        warningCard = root.findViewById(R.id.warningCard);
        warningText = root.findViewById(R.id.warningText);
        warningTypeText = root.findViewById(R.id.warningTypeText);
        warningDateText = root.findViewById(R.id.warningDateText);
        warningOkayButton = root.findViewById(R.id.warningOkayButton);

        tempMainCard = root.findViewById(R.id.tempMainCard);
        heartMainCard = root.findViewById(R.id.heartMainCard);
        spO2MainCard = root.findViewById(R.id.spO2MainCard);

        cardHomeLocation = root.findViewById(R.id.cardSetHomeLocation);

        medicineStatus = root.findViewById(R.id.medicineStatus);
        medicineName = root.findViewById(R.id.medicineName);
        medicineDose = root.findViewById(R.id.medicineDose);
        medicineTimeText = root.findViewById(R.id.medicineTime);
        medicineSecret = root.findViewById(R.id.medicineSecret);
        medicineSaveButton = root.findViewById(R.id.medicineSaveButton);

        connectionLayout = root.findViewById(R.id.connectionLayout);
        batteryText = root.findViewById(R.id.batteryText);
        cardBluetoothColor = root.findViewById(R.id.cardBluetoothColor);
        cardWearingColor = root.findViewById(R.id.cardWearingColor);
        cardBatteryColor = root.findViewById(R.id.cardBatteryColor);

        anim = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
        handler = new Handler();
        dataHandler = new Handler();
        prefManager = new PrefManager(getContext());
        apiText = new ApiConstantText(getContext());
        medicineUsageRepo = new MedicineUsageRepo(getContext());
        medicineRepository = new MedicineRepository(getContext());
        medicineTimeRepo = new MedicineTimeRepo(getContext());

        cardConnectionStatus.setOnClickListener(view -> {
            if (!CovidMainActivity.isConnected) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_connection2);
                cardConnectionStatus.setClickable(false);
                cardConnectionStatus.setFocusable(false);
            }
        });

        activity = getActivity();


        homeViewModel.getLastTypeData(MyConstant.TEMP).observe(getViewLifecycleOwner(), covid -> {
            if (covid != null) {
                tempText.setText((getString(R.string.temp) + ": " + String.format(Locale.getDefault(), "%.1f", covid.getData()) + getString(R.string.temp_unit)));
                tempDateText.setText(getDate(covid.getTime()));
            } else {
                tempText.setText((getString(R.string.temp) + ": " + getString(R.string.no_measurement)));
            }
        });

        homeViewModel.getLastTypeData(MyConstant.HEART).observe(getViewLifecycleOwner(), covid -> {
            if (covid != null) {
                heartText.setText((getString(R.string.heart) + ": " + String.format(Locale.getDefault(), "%.0f", covid.getData()) + " " + getString(R.string.heart_unit)));
                heartDateText.setText(getDate(covid.getTime()));
            } else {
                heartText.setText((getString(R.string.heart) + ": " + getString(R.string.no_measurement)));
            }
        });

        homeViewModel.getLastTypeData(MyConstant.SPO2).observe(getViewLifecycleOwner(), covid -> {
            if (covid != null) {
                spO2Text.setText((getString(R.string.spo2) + ": " + getString(R.string.spo2_unit) + String.format(Locale.getDefault(), "%.1f", covid.getData())));
                spO2DateText.setText(getDate(covid.getTime()));
            } else {
                spO2Text.setText((getString(R.string.spo2) + ": " + getString(R.string.no_measurement)));
            }
        });

        tempMainCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), GraphActivity.class);
            intent.putExtra("type", MyConstant.TEMP);
            startActivity(intent);
        });

        heartMainCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), GraphActivity.class);
            intent.putExtra("type", MyConstant.HEART);
            startActivity(intent);
        });

        spO2MainCard.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), GraphActivity.class);
            intent.putExtra("type", MyConstant.SPO2);
            startActivity(intent);
        });


        cardHomeLocation.setOnClickListener(v -> {
            try {
                if (prefManager.getSentDataServer()) {
                    CovidMainActivity.isHomeSetClicked = true;
                    CovidMainActivity.isForHome = true;
                    ((CovidMainActivity) getActivity()).setupHomeLocation();
                } else {
                    Toast.makeText(getContext(), getString(R.string.home_location_send_permission), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("SmartSense", e.getMessage());
            }

        });


        medicineSaveButton.setOnClickListener(view -> {
            MedicineUsage medicineUsage = medicineUsageRepo.getMedicineUsageByID(Long.parseLong(medicineSecret.getText().toString()));
            if (medicineUsage != null) {
                medicineUsage.setUsed(true);
                medicineUsageRepo.update(medicineUsage);
                Log.i(TAG, medicineSecret.getText().toString());
                medicineStatus.setVisibility(View.GONE);

                Handler handler = new Handler();
                Runnable runnable = this::checkMedicineUsage;
                handler.postDelayed(runnable, 100);

            } else {
                Log.i(TAG, "Medicine usage not found");
            }
        });

        warningOkayButton.setOnClickListener(view -> {
            prefManager.setWarningData(-1);
            prefManager.setWarningType(0);
            warningCard.setVisibility(View.GONE);
        });


        return root;
    }

    private void checkMedicineUsage() {
        List<MedicineUsage> medicineUsages = medicineUsageRepo.getAllUsedDataList(false);
        if (medicineUsages != null) {
            for (int i = 0; i < medicineUsages.size(); i++) {
                MedicineTime medicineTime = medicineTimeRepo.getMedicineTimeByID(medicineUsages.get(i).getForeignMedicineTimeID());
                if (medicineTime != null) {
                    Medicine medicine = medicineRepository.getMedicineByID(medicineTime.getForeignMedicineID());
                    if (medicine != null) {
                        medicineDose.setText((getString(R.string.medicine_dose) + ": " + medicineTime.getDose()));
                        medicineName.setText((getString(R.string.medicine_name) + ": " + medicine.getMedicineName()));
                        medicineTimeText.setText((getString(R.string.medicine_should_used_time) + " " + getTimeForMedicineUsage(medicineUsages.get(i).getUseTimestamp())));
                        medicineSecret.setText(String.valueOf(medicineUsages.get(i).getMedicineUsageID()));
                        medicineStatus.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private String getTimeForMedicineUsage(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("HH:mm", cal).toString();
    }


    private LineDataSet createSet(int type) {
        LineDataSet set1 = new LineDataSet(null, "");
        set1.setForm(Legend.LegendForm.NONE);
        set1.setLineWidth(1.5f);
        set1.setHighLightColor(getResources().getColor(R.color.greyColor));
        set1.setDrawHighlightIndicators(true);
        set1.setHighlightLineWidth(1f);
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(true);

        if (type == MyConstant.HEART_CHART) {
            set1.setCircleColor(getResources().getColor(R.color.graph_color_3));
            set1.setColor(getResources().getColor(R.color.graph_color_3));


        } else {
            /*set1.setCircleColor(getResources().getColor(R.color.graph_color));
            set1.setColor(getResources().getColor(R.color.graph_color));*/
            set1.setCircleColor(getResources().getColor(R.color.graph_color_4));
            set1.setColor(getResources().getColor(R.color.graph_color_4));
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            set1.setFillAlpha(80);
            set1.setDrawFilled(true);
            // set1.setFillColor(getResources().getColor(R.color.graph_color_3));
            set1.setFillDrawable(getResources().getDrawable(R.drawable.fade_red));
        }

        set1.setDrawValues(false);
        return set1;
    }

    private void chart(LineChart chart) {
        List<String> dateData = getDateData();

        /*set1.setForm(Legend.LegendForm.NONE);
        set1.setLineWidth(2.75f);
        set1.setHighLightColor(getResources().getColor(R.color.greyColor));
        set1.setDrawHighlightIndicators(true);
        set1.setHighlightLineWidth(1f);
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(true);

        set1.setCircleColor(getResources().getColor(R.color.graph_color));
        set1.setColor(getResources().getColor(R.color.graph_color));
        set1.setDrawValues(false);

         */

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        IndexAxisValueFormatter axisValueFormatter = new IndexAxisValueFormatter(dateData);
        xAxis.setValueFormatter(axisValueFormatter);
        //xAxis.setGranularity(1.5f);
        //xAxis.setGranularityEnabled(true);
        //xAxis.setLabelCount(6);
        xAxis.setTextColor(getResources().getColor(R.color.textColorSecondary));

        chart.getAxisRight().setEnabled(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(8f);
        yAxis.setDrawGridLines(true);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxis.setDrawAxisLine(false);
        yAxis.setTextColor(getResources().getColor(R.color.textColorSecondary));
        yAxis.setYOffset(0f);
        yAxis.setXOffset(-18f);
        yAxis.setLabelCount(5);
        /*yAxis.setAxisMaximum(45);
        yAxis.setAxisMinimum(25);*/


        //LineData lineData = new LineData(set1);
        LineData lineData = new LineData();
        chart.setData(lineData);

        chart.getDescription().setEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(false);
        chart.setExtraOffsets(30, 0, 0, 0);
        chart.setViewPortOffsets(55f, 0f, 10f, 0f);

        /*MyMarkerView mv = new MyMarkerView(getContext(), R.layout.chart_marker_view, axisValueFormatter);
        mv.setChartView(chart); // Set the marker to the chart
        chart.setMarker(mv);*/

        chart.animateXY(500, 500);
    }

    public List<String> getDateData() {
        List<String> dateData = new ArrayList<>();
        List<String> dateDataForDatabase = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        dateData.add(getGraphDateType(cal));

        return dateData;

    }

    public static String getDate(Date date) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    private String getGraphDateType(Calendar cal) {
        String str1 = "dd LLL";
        Date d = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(str1, Locale.getDefault());
        return sdf.format(d);
    }

    private String getDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd/MM HH:mm", cal).toString();
    }

    private String getDateForWarning(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd/MM HH:mm", cal).toString();
    }

    private void twoSecondAction() {
        heartIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.postDelayed(run, 2000);
                        callIcon.setVisibility(View.VISIBLE);
                        callIcon.startAnimation(anim);
                        break;
                    case MotionEvent.ACTION_UP:
                        callIcon.setVisibility(View.GONE);
                        handler.removeCallbacks(run);
                        callIcon.clearAnimation();
                        break;
                }
                return true;
            }
        });

        run = new Runnable() {
            @Override
            public void run() {
                // Your code to run on long click
                callIcon.setVisibility(View.GONE);
                callIcon.clearAnimation();
            }
        };
    }

    public void connectionStatus(boolean status) {
        if (status) {

            cardConnectionStatus.setVisibility(View.GONE);
            connectionLayout.setVisibility(View.VISIBLE);
            cardBluetoothColor.setBackgroundColor(getResources().getColor(R.color.cardActiveColor));
            cardWearingColor.setBackgroundColor(getResources().getColor(R.color.cardActiveColor));
            cardBatteryColor.setBackgroundColor(getResources().getColor(R.color.cardActiveColor));

            textConnectionStatus.setText(getString(R.string.connection_active));
            cardConnectionStatus.setClickable(false);
            cardConnectionStatus.setFocusable(false);
            cardConnectionColor.setBackgroundColor(getResources().getColor(R.color.cardActiveColor));
            if (prefManager.getWarningData() <= 0) {
                heartIcon.setColorFilter(getResources().getColor(R.color.cardActiveColor));
            } else {
                if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_IN_HOSPITAL) {
                    heartIcon.setColorFilter(getResources().getColor(R.color.warningHospital));
                } else {
                    heartIcon.setColorFilter(getResources().getColor(R.color.warning));
                }
            }
        } else {
            cardConnectionStatus.setVisibility(View.VISIBLE);
            connectionLayout.setVisibility(View.GONE);
            cardBluetoothColor.setBackgroundColor(getResources().getColor(R.color.cardPassiveColor));
            cardWearingColor.setBackgroundColor(getResources().getColor(R.color.cardPassiveColor));
            cardBatteryColor.setBackgroundColor(getResources().getColor(R.color.cardPassiveColor));

            cardConnectionStatus.setClickable(true);
            cardConnectionStatus.setFocusable(true);
            textConnectionStatus.setText(getString(R.string.connection_no_click));
            cardConnectionColor.setBackgroundColor(getResources().getColor(R.color.cardPassiveColor));
            heartIcon.setColorFilter(getResources().getColor(R.color.cardPassiveColor));
        }
    }

    public void tempWarning() {
        CovidMainActivity.warningType = 0;
        if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_IN_HOSPITAL) {
            warningText.setText((getString(R.string.warning_temp) + " " + getString(R.string.quarantine_hospital_warning)));
            heartIcon.setColorFilter(getResources().getColor(R.color.warningHospital));
        } else {
            warningText.setText((getString(R.string.warning_temp) + " " + getString(R.string.quarantine_home_warning)));
            heartIcon.setColorFilter(getResources().getColor(R.color.warning));
        }

        warningTypeText.setText((prefManager.getWarningData() + " " + getString(R.string.temp_unit)));
        warningDateText.setText(getDateForWarning(prefManager.getWarningDate()));
        warningCard.setVisibility(View.VISIBLE);
    }

    public void spO2Warning() {
        CovidMainActivity.warningType = 0;
        if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_IN_HOSPITAL) {
            warningText.setText((getString(R.string.warning_spo2) + " " + getString(R.string.quarantine_hospital_warning)));
            heartIcon.setColorFilter(getResources().getColor(R.color.warningHospital));
        } else {
            warningText.setText((getString(R.string.warning_spo2) + " " + getString(R.string.quarantine_home_warning)));
            heartIcon.setColorFilter(getResources().getColor(R.color.warning));
        }
        warningTypeText.setText((getString(R.string.spo2_unit) + prefManager.getWarningData()));
        warningDateText.setText(getDateForWarning(prefManager.getWarningDate()));
        warningCard.setVisibility(View.VISIBLE);
    }

    public void heartrateWarning(boolean isLow) {
        CovidMainActivity.warningType = 0;
        if (isLow) {
            if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_IN_HOSPITAL) {
                warningText.setText((getString(R.string.warning_heartrate_low) + " " + getString(R.string.quarantine_hospital_warning)));
                heartIcon.setColorFilter(getResources().getColor(R.color.warningHospital));
            } else {
                warningText.setText((getString(R.string.warning_heartrate_low) + " " + getString(R.string.quarantine_home_warning)));
                heartIcon.setColorFilter(getResources().getColor(R.color.warning));
            }
        } else {
            if (prefManager.getUserTrackingType() == MyConstant.QUARANTINE_IN_HOSPITAL) {
                warningText.setText((getString(R.string.warning_heartrate_high) + " " + getString(R.string.quarantine_hospital_warning)));
                heartIcon.setColorFilter(getResources().getColor(R.color.warningHospital));
            } else {
                warningText.setText((getString(R.string.warning_heartrate_high) + " " + getString(R.string.quarantine_home_warning)));
                heartIcon.setColorFilter(getResources().getColor(R.color.warning));
            }
        }
        warningTypeText.setText((prefManager.getWarningData() + " " + getString(R.string.heart_unit)));
        warningDateText.setText(getDateForWarning(prefManager.getWarningDate()));
        warningCard.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        checkMedicineUsage();

        dataHandler.postDelayed(runnable = new Runnable() {
            public void run() {
                if (CovidMainActivity.isConnected) {
                    if (isAdded() && activity != null) {
                        connectionStatus(true);

                        if (prefManager.getWarningData() >= 0) {
                            if (prefManager.getWarningType() == MyConstant.TEMP) {
                                tempWarning();
                            } else if (prefManager.getWarningType() == MyConstant.SPO2) {
                                spO2Warning();
                            } else if (prefManager.getWarningType() == MyConstant.HEART) {
                                heartrateWarning(true);
                            } else if (prefManager.getWarningType() == MyConstant.HEART_HIGH) {
                                heartrateWarning(false);
                            }
                        }

                        batteryText.setText(("%" + CovidMainActivity.batteryPercentage));
                        if (CovidMainActivity.isWearingNow) {
                            cardWearingColor.setBackgroundColor(getResources().getColor(R.color.cardActiveColor));
                        } else {
                            cardWearingColor.setBackgroundColor(getResources().getColor(R.color.cardPassiveColor));
                        }
                    }
                } else {
                    if (isAdded() && activity != null) {
                        connectionStatus(false);
                    }
                }

                if (CovidMainActivity.isHomeSetClicked) {
                    if (prefManager.getIsLocationCanChange()) {
                        cardHomeLocation.setVisibility(View.VISIBLE);
                    } else {
                        cardHomeLocation.setVisibility(View.GONE);
                    }
                }


                dataHandler.postDelayed(runnable, delay);
            }
        }, delay);

        if (prefManager.getIsLocationCanChange()) {
            cardHomeLocation.setVisibility(View.VISIBLE);
        } else {
            cardHomeLocation.setVisibility(View.GONE);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}