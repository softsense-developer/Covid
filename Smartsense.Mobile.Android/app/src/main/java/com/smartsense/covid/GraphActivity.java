package com.smartsense.covid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.smartsense.covid.adapters.manualMesuredAdapter.DerpAdapter;
import com.smartsense.covid.adapters.manualMesuredAdapter.DerpData;
import com.smartsense.covid.adapters.manualMesuredAdapter.ManualItem;
import com.smartsense.covid.model.Covid;
import com.smartsense.covid.repo.CovidRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class GraphActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private static final String TAG = "Smartsense";
    private MaterialToolbar appbar;
    private TextView appBarText;
    private ConstraintLayout gradientView;
    private LinearLayout selectedDataView, manuallyMeasurmentBt;
    private TextView graphSelectedText, graphUnitText, graphSelectedDateText, graphDayText;
    private LineChart chart;
    private ImageButton previousDayBt, nextDayBt;
    private ImageView manuallyMeasurmentImage;
    private TextView maxData, maxDataText, minData, minDataText, avgData, avgDataText;
    private TextView minDataUnitText, maxDataUnitText, avgDataUnitText, measuredResultText, measuredResultUnitText;
    private LinearLayout manuallyMeasurmentLayout, dummyDataView;
    private RecyclerView manualMeasurmentRecView;
    private ArrayList listData;
    private DerpAdapter adapter;

    private CovidRepository repository;
    private long startTime, endTime;
    private List<String> dateData = new ArrayList<String>();
    private HashMap<Integer, String> dateIndex = new HashMap<Integer, String>();
    private int dayInterval = 0, viewType;
    private double minDataFloat = Float.MAX_VALUE, maxDataFloat = Float.MIN_VALUE, avgDataFloat = 0.0;
    private float minTime = 300.0f;

    private LottieAnimationView measuredAnimView;
    private ConstraintLayout measuredResultView;
    private AppCompatButton measuredOkayBt;

    private Runnable manuallyMeasuredRunnable;
    private Handler manuallyMeasuredHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        appbar = findViewById(R.id.appBar);
        setSupportActionBar(appbar);

        appBarText = findViewById(R.id.appBarText);

        gradientView = findViewById(R.id.gradientView);

        selectedDataView = findViewById(R.id.selectedDataView);
        graphSelectedText = findViewById(R.id.graphSelectedText);
        graphUnitText = findViewById(R.id.graphUnitText);
        graphSelectedDateText = findViewById(R.id.graphSelectedDateText);

        chart = findViewById(R.id.graphMain);

        previousDayBt = findViewById(R.id.previousDayBt);
        nextDayBt = findViewById(R.id.nextDayBt);
        graphDayText = findViewById(R.id.graphDayText);

        manuallyMeasurmentBt = findViewById(R.id.manuallyMeasurmentBt);
        manuallyMeasurmentImage = findViewById(R.id.manuallyMeasurmentImage);

        maxData = findViewById(R.id.maxData);
        maxDataText = findViewById(R.id.maxDataText);
        maxDataUnitText = findViewById(R.id.maxDataUnitText);
        minData = findViewById(R.id.minData);
        minDataText = findViewById(R.id.minDataText);
        minDataUnitText = findViewById(R.id.minDataUnitText);
        avgData = findViewById(R.id.avgData);
        avgDataText = findViewById(R.id.avgDataText);
        avgDataUnitText = findViewById(R.id.avgDataUnitText);

        manuallyMeasurmentLayout = findViewById(R.id.manuallyMeasurmentLayout);
        manualMeasurmentRecView = findViewById(R.id.manualMeasurmentRecView);

        measuredAnimView = findViewById(R.id.measuredAnimView);
        measuredResultView = findViewById(R.id.measuredResultView);
        measuredOkayBt = findViewById(R.id.measuredOkayBt);
        measuredResultText = findViewById(R.id.measuredResultText);
        measuredResultUnitText = findViewById(R.id.measuredResultUnitText);


        dummyDataView = findViewById(R.id.dummyDataView);


        listData = (ArrayList) DerpData.getListData();
        manualMeasurmentRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new DerpAdapter(listData, getApplicationContext());
        manualMeasurmentRecView.setAdapter(adapter);

        repository = new CovidRepository(getApplication());

        Intent intent = getIntent();
        if (null != intent) { //Null Checking
            viewType = intent.getIntExtra("type", MyConstant.HEART);
        }

        setStartEndTime(dayInterval);
        //dummyData();

        if (viewType == MyConstant.HEART) {
            getHeartView();
            getHeartData();
        } else if (viewType == MyConstant.SPO2) {
            getSpO2View();
            getSpO2Data();
        } else {
            getTempView();
            getTempData();
        }


        previousDayBt.setOnClickListener(view -> {
            dummyDataView.setVisibility(View.GONE);
            selectedDataView.setVisibility(View.GONE);
            dayInterval--;
            setStartEndTime(dayInterval);
            if (viewType == MyConstant.HEART) {
                getHeartData();
            } else if (viewType == MyConstant.SPO2) {
                getSpO2Data();
            } else {
                getTempData();
            }

            if (dayInterval < 0) {
                nextDayBt.setVisibility(View.VISIBLE);
            }
        });


        nextDayBt.setOnClickListener(view -> {
            dummyDataView.setVisibility(View.GONE);
            selectedDataView.setVisibility(View.GONE);
            dayInterval++;
            setStartEndTime(dayInterval);
            if (viewType == MyConstant.HEART) {
                getHeartData();
            } else if (viewType == MyConstant.SPO2) {
                getSpO2Data();
            } else {
                getTempData();
            }

            if (dayInterval >= 0) {
                nextDayBt.setVisibility(View.INVISIBLE);
            }
        });

        manuallyMeasurmentBt.setOnClickListener(view -> {
            previousDayBt.setEnabled(false);
            nextDayBt.setEnabled(false);
            chart.setVisibility(View.GONE);
            selectedDataView.setVisibility(View.GONE);
            manuallyMeasurmentBt.setClickable(false);
            manuallyMeasurmentBt.setFocusable(false);
            if (viewType == MyConstant.HEART) {
                measuredAnimView.setPadding(64, 64, 64, 64);
                measuredAnimView.setAnimation("heart.json");

            } else if (viewType == MyConstant.SPO2) {
                measuredAnimView.setAnimation("spo2.json");
            } else {
                measuredAnimView.setPadding(32, 32, 32, 32);
                measuredAnimView.setAnimation("temp.json");

            }
            measuredAnimView.setVisibility(View.VISIBLE);
            manuallyMeasuredHandler.postDelayed(manuallyMeasuredRunnable, 5000);
        });


        manuallyMeasuredHandler = new Handler();
        manuallyMeasuredRunnable = () -> {
            getManuallyMeasuredData();
        };

        measuredOkayBt.setOnClickListener(view -> {
            previousDayBt.setEnabled(true);
            nextDayBt.setEnabled(true);
            chart.setVisibility(View.VISIBLE);
            manuallyMeasurmentBt.setClickable(true);
            manuallyMeasurmentBt.setFocusable(true);
            measuredResultView.setVisibility(View.GONE);
        });


        dummyDataView.setOnClickListener(view -> {
            randomDummyData();
        });


        // getTempView();

    }

    private void getManuallyMeasuredData() {
        manuallyMeasurmentLayout.setEnabled(false);
        measuredAnimView.setVisibility(View.GONE);
        measuredResultView.setVisibility(View.VISIBLE);

        double data = 0;
        if (viewType == MyConstant.HEART) {
            data = (60 + (Math.random() * 40));
            measuredResultText.setText(String.valueOf((int) data));
            measuredResultUnitText.setText(getString(R.string.heart_unit));
            Covid covid = new Covid(((System.currentTimeMillis())), (int) data, MyConstant.HEART, MyConstant.MANUEL_SAVE);
            repository.insert(covid);

            Handler handler = new Handler();
            Runnable runnable = () -> {
                getHeartData();
            };
            handler.postDelayed(runnable, 100);

        } else if (viewType == MyConstant.SPO2) {
            data = (60 + (Math.random() * 40));
            measuredResultText.setText(String.format(Locale.getDefault(), "%.1f", data));
            measuredResultUnitText.setText(getString(R.string.spo2_unit));
            Covid covid = new Covid(((System.currentTimeMillis())), data, MyConstant.SPO2, MyConstant.MANUEL_SAVE);
            repository.insert(covid);

            Handler handler = new Handler();
            Runnable runnable = () -> {
                getSpO2Data();
            };
            handler.postDelayed(runnable, 100);
        } else {
            data = (20 + (Math.random() * 20));
            measuredResultText.setText(String.format(Locale.getDefault(), "%.1f", data));
            measuredResultUnitText.setText(getString(R.string.temp_unit));
            Covid covid = new Covid(((System.currentTimeMillis())), data, MyConstant.TEMP, MyConstant.MANUEL_SAVE);
            repository.insert(covid);

            Handler handler = new Handler();
            Runnable runnable = () -> {
                getTempData();
            };
            handler.postDelayed(runnable, 100);
        }
    }

    private void getTempView() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.temp_gradient_end));

        appBarText.setText(getString(R.string.temp));
        appbar.setBackgroundColor(getResources().getColor(R.color.temp_color));


        gradientView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.temp_graph_gradient, null));

        selectedDataView.setVisibility(View.GONE);
        graphUnitText.setText(R.string.temp_unit);

        manuallyMeasurmentImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_temp, null));
        manuallyMeasurmentImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.temp_color), android.graphics.PorterDuff.Mode.SRC_IN);

        maxDataText.setText(getString(R.string.max_temp));
        minDataText.setText(getString(R.string.min_temp));
        avgDataText.setText(getString(R.string.avg_temp));
        maxDataUnitText.setText(getString(R.string.temp_unit));
        minDataUnitText.setText(getString(R.string.temp_unit));
        avgDataUnitText.setText(getString(R.string.temp_unit));

        manuallyMeasurmentLayout.setVisibility(View.GONE);

        chart.setNoDataText(getString(R.string.no_temp_data));
        Paint p = chart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(40);
        p.setColor(getResources().getColor(R.color.textWhitePrimary));
        p.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_medium));
    }


    private void getHeartView() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.heart_gradient_end));

        appbar.setBackgroundColor(getResources().getColor(R.color.heart_gradient_end));
        appBarText.setText(getString(R.string.heart));

        gradientView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_graph_gradient, null));

        selectedDataView.setVisibility(View.GONE);
        graphUnitText.setText(R.string.heart_unit);

        manuallyMeasurmentImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_rate, null));
        manuallyMeasurmentImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.heart_color), android.graphics.PorterDuff.Mode.SRC_IN);

        maxDataText.setText(getString(R.string.max_heart_rate));
        minDataText.setText(getString(R.string.min_heart_rate));
        avgDataText.setText(getString(R.string.avg_heart_rate));
        maxDataUnitText.setText(getString(R.string.heart_unit));
        minDataUnitText.setText(getString(R.string.heart_unit));
        avgDataUnitText.setText(getString(R.string.heart_unit));

        manuallyMeasurmentLayout.setVisibility(View.GONE);

        chart.setNoDataText(getString(R.string.no_heart_data));
        Paint p = chart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(40);
        p.setColor(getResources().getColor(R.color.textWhitePrimary));
        p.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_medium));

    }

    private void getSpO2View() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.spo2_gradient_end));

        appBarText.setText(getString(R.string.spo2));
        appbar.setBackgroundColor(getResources().getColor(R.color.spo2_color));


        gradientView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.spo2_graph_gradient, null));

        selectedDataView.setVisibility(View.GONE);
        graphUnitText.setText(R.string.spo2_unit);

        manuallyMeasurmentImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_spo2, null));
        manuallyMeasurmentImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.spo2_color), android.graphics.PorterDuff.Mode.SRC_IN);

        maxDataText.setText(getString(R.string.max_spo2));
        minDataText.setText(getString(R.string.min_spo2));
        avgDataText.setText(getString(R.string.avg_spo2));
        maxDataUnitText.setText(getString(R.string.spo2_unit));
        minDataUnitText.setText(getString(R.string.spo2_unit));
        avgDataUnitText.setText(getString(R.string.spo2_unit));

        manuallyMeasurmentLayout.setVisibility(View.GONE);

        chart.setNoDataText(getString(R.string.no_spo2_data));
        Paint p = chart.getPaint(Chart.PAINT_INFO);
        p.setTextSize(40);
        p.setColor(getResources().getColor(R.color.textWhitePrimary));
        p.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_medium));

    }

    private void getHeartData() {
        List<Covid> dailyData = getDailyTypeData(MyConstant.HEART, startTime, endTime);
        if (dailyData.size() > 0) {
            getEntries(dailyData, MyConstant.HEART);
        } else {
            chart.invalidate();
            chart.clear();
            //dummyDataView.setVisibility(View.VISIBLE);
        }

        getMinMaxAvgData(MyConstant.HEART);

        getDailyDataView(MyConstant.HEART);
    }


    private void getTempData() {
        List<Covid> dailyData = getDailyTypeData(MyConstant.TEMP, startTime, endTime);
        if (dailyData.size() > 0) {
            getEntries(dailyData, MyConstant.TEMP);
        } else {
            chart.invalidate();
            chart.clear();
            //dummyDataView.setVisibility(View.VISIBLE);
        }

        getMinMaxAvgData(MyConstant.TEMP);

        getDailyDataView(MyConstant.TEMP);
    }

    private void getSpO2Data() {
        List<Covid> dailyData = getDailyTypeData(MyConstant.SPO2, startTime, endTime);
        if (dailyData.size() > 0) {
            getEntries(dailyData, MyConstant.SPO2);
        } else {
            chart.invalidate();
            chart.clear();
            //dummyDataView.setVisibility(View.VISIBLE);
        }

        getMinMaxAvgData(MyConstant.SPO2);

        getDailyDataView(MyConstant.SPO2);
    }

    private void getMinMaxAvgData(int type) {
        List<Covid> allDailyData = getDailyAllTypeData(type, startTime, endTime);

        minDataFloat = Float.MAX_VALUE;
        maxDataFloat = Float.MIN_VALUE;
        avgDataFloat = 0.0;
        if (allDailyData.size() > 0) {
            for (int i = 0; i < allDailyData.size(); i++) {
                if (allDailyData.get(i).getData() > maxDataFloat) {
                    maxDataFloat = allDailyData.get(i).getData();
                }
                if (allDailyData.get(i).getData() < minDataFloat) {
                    minDataFloat = allDailyData.get(i).getData();
                }
                avgDataFloat += allDailyData.get(i).getData();
            }
            avgDataFloat /= allDailyData.size();

            if (type == MyConstant.HEART) {
                maxData.setText(String.format(Locale.getDefault(), "%.0f", maxDataFloat));
                minData.setText(String.format(Locale.getDefault(), "%.0f", minDataFloat));
                avgData.setText(String.format(Locale.getDefault(), "%.0f", avgDataFloat));
            } else {
                maxData.setText(String.format(Locale.getDefault(), "%.1f", maxDataFloat));
                minData.setText(String.format(Locale.getDefault(), "%.1f", minDataFloat));
                avgData.setText(String.format(Locale.getDefault(), "%.1f", avgDataFloat));
            }
        } else {
            maxData.setText("-");
            minData.setText("-");
            avgData.setText("-");
        }
    }

    private void getDailyDataView(int type) {
        List<Covid> allManualData = getDailyTypeData(type, startTime, endTime);
        listData.clear();
        adapter.notifyDataSetChanged();
        if (allManualData.size() > 0) {
            manuallyMeasurmentLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < allManualData.size(); i++) {
                ManualItem item = new ManualItem();
                if (type == MyConstant.HEART) {
                    item.setData(String.format(Locale.getDefault(), "%.0f", allManualData.get(i).getData()));
                } else if (type == MyConstant.SPO2) {
                    item.setData(String.format(Locale.getDefault(), "%.1f", allManualData.get(i).getData()));
                } else {
                    item.setData(String.format(Locale.getDefault(), "%.1f", allManualData.get(i).getData()));
                }
                item.setDate(getDate(allManualData.get(i).getTime()));
                item.setType(viewType);
                listData.add(item);
            }
            adapter.notifyDataSetChanged();
        } else {
            manuallyMeasurmentLayout.setVisibility(View.GONE);
        }
    }

    private void setStartEndTime(int dayInterval) {
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, currentDay + dayInterval);

        startTime = cal.getTimeInMillis();

        if (dayInterval == 0) {
            graphDayText.setText(getString(R.string.today));
        } else if (dayInterval == -1) {
            graphDayText.setText(getString(R.string.yesterday));
        } else {
            graphDayText.setText(getOnlyDayMonthDate(cal.getTimeInMillis()));
        }

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.DAY_OF_MONTH, currentDay + dayInterval);

        endTime = cal.getTimeInMillis();
    }

    public List<Covid> getDailyTypeData(int type, long startTime, long endTime) {
        try {
            return repository.getDailyTypeData(type, startTime, endTime);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Covid> getDailyAllTypeData(int type, long startTime, long endTime) {
        try {
            return repository.getDailyAllTypeData(type, startTime, endTime);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Covid> getDailyManulSaveTypeData(int type, long startTime, long endTime) {
        try {
            return repository.getDailyManualSaveTypeData(type, startTime, endTime);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void randomDummyData() {

        dummyDataView.setVisibility(View.GONE);
        double data = 0.0;
        for (int i = 1; i <= 10; i++) {
            if (viewType == MyConstant.HEART) {
                data = (60 + (Math.random() * 40));
                Covid covid = new Covid((startTime + i * (int) ((Math.random() * 5000) + 5000)), (int) data, MyConstant.HEART, MyConstant.AUTO_SAVE);
                repository.insert(covid);
            } else if (viewType == MyConstant.SPO2) {
                data = (95 + (Math.random() * 5));
                Covid covid = new Covid((startTime + i * (int) ((Math.random() * 5000) + 5000)), data, MyConstant.SPO2, MyConstant.AUTO_SAVE);
                repository.insert(covid);
            } else {
                data = (35 + (Math.random() * 2));
                Covid covid = new Covid((startTime + i * (int) ((Math.random() * 5000) + 5000)), data, MyConstant.TEMP, MyConstant.AUTO_SAVE);
                repository.insert(covid);
            }
            /*if (i > 7) {
                if (viewType == MyConstant.HEART) {
                    data = (60 + (Math.random() * 40));
                    Covid covid = new Covid((startTime + (i - 7) * (int) ((Math.random() * 5000) + 5000)), (int) data, MyConstant.HEART, MyConstant.MANUEL_SAVE);
                    repository.insert(covid);
                } else if (viewType == MyConstant.SPO2) {
                    data = (90 + (Math.random() * 10));
                    Covid covid = new Covid((startTime + (i - 7) * (int) ((Math.random() * 5000) + 5000)), data, MyConstant.SPO2, MyConstant.MANUEL_SAVE);
                    repository.insert(covid);
                } else {
                    data = (35 + (Math.random() * 2));
                    Covid covid = new Covid((startTime + (i - 7) * (int) ((Math.random() * 5000) + 5000)), data, MyConstant.TEMP, MyConstant.MANUEL_SAVE);
                    repository.insert(covid);
                }
            }*/
        }

        if (viewType == MyConstant.HEART) {
            Handler handler = new Handler();
            Runnable runnable = () -> {
                getHeartData();
            };
            handler.postDelayed(runnable, 100);
        } else if (viewType == MyConstant.SPO2) {
            Handler handler = new Handler();
            Runnable runnable = () -> {
                getSpO2Data();
            };
            handler.postDelayed(runnable, 100);
        } else {
            Handler handler = new Handler();
            Runnable runnable = () -> {
                getTempData();
            };
            handler.postDelayed(runnable, 100);
        }

    }

    private void dummyData() {


        /*Covid covid = new Covid((1603487999), 67, MyConstant.HEART, MyConstant.AUTO_SAVE);
        repository.insert(covid);

         */

        /*PrefManager prefManager=new PrefManager(getApplicationContext());
        prefManager.clear();*/

        Covid covid2 = new Covid(((System.currentTimeMillis() + 5000000)), 88.9, MyConstant.SPO2, MyConstant.AUTO_SAVE);
        repository.insert(covid2);
        Covid covid3 = new Covid(((System.currentTimeMillis() - 10000000)), 96.3, MyConstant.SPO2, MyConstant.AUTO_SAVE);
        repository.insert(covid3);
        Covid covid4 = new Covid(((System.currentTimeMillis() + 15000000)), 98, MyConstant.SPO2, MyConstant.AUTO_SAVE);
        repository.insert(covid4);
        Covid covid5 = new Covid(((System.currentTimeMillis() - 20000000)), 94.2, MyConstant.SPO2, MyConstant.AUTO_SAVE);
        repository.insert(covid5);

        Covid covid6 = new Covid(((System.currentTimeMillis() + 5000000)), 99.2, MyConstant.SPO2, MyConstant.MANUEL_SAVE);
        repository.insert(covid6);

        /*ManualItem item = new ManualItem();
        item.setData("80");
        item.setDate("10/22 11:30");
        item.setType(MyConstant.HEART);
        listData.add(item);

        ManualItem item2 = new ManualItem();
        item2.setData("67");
        item2.setDate("10/22 15:32");
        item2.setType(MyConstant.HEART);
        listData.add(item2);

        ManualItem item3 = new ManualItem();
        item3.setData("92");
        item3.setDate("10/22 15:32");
        item3.setType(MyConstant.HEART);
        listData.add(item3);
        adapter.notifyItemInserted(listData.indexOf(item));

        feedMultiple();*/
    }

    private void feedMultiple() {
        /*addEntry(new Entry(1, 65), MyConstant.HEART);
        addEntry(graphMain, new Entry(2, 77), MyConstant.HEART);
        addEntry(graphMain, new Entry(3, 71), MyConstant.HEART);
        addEntry(graphMain, new Entry(4, 85), MyConstant.HEART);
        addEntry(graphMain, new Entry(5, 74), MyConstant.HEART);
        addEntry(graphMain, new Entry(6, 77), MyConstant.HEART);
        addEntry(graphMain, new Entry(7, 83), MyConstant.HEART);
        addEntry(graphMain, new Entry(8, 79), MyConstant.HEART);
        addEntry(graphMain, new Entry(9, 88), MyConstant.HEART);
        addEntry(graphMain, new Entry(10, 78), MyConstant.HEART);

         */


            /*
            addEntry(graphMain, new Entry(1, 65),MyConstant.TEMP);
            addEntry(graphMain, new Entry(2, 77),MyConstant.TEMP);
            addEntry(graphMain, new Entry(3, 71),MyConstant.TEMP);
            addEntry(graphMain, new Entry(4, 85),MyConstant.TEMP);
            addEntry(graphMain, new Entry(5, 74),MyConstant.TEMP);
            addEntry(graphMain, new Entry(6, 90),MyConstant.TEMP);
            addEntry(graphMain, new Entry(7, 83),MyConstant.TEMP);
            addEntry(graphMain, new Entry(8, 79),MyConstant.TEMP);
            addEntry(graphMain, new Entry(9, 88),MyConstant.TEMP);
            addEntry(graphMain, new Entry(10, 78),MyConstant.TEMP);*/

    }


    /*private void addEntry(Entry entry, int type) {
        LineData data = graph.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                if (type == MyConstant.HEART) {
                    set = createSet(MyConstant.HEART);
                } else {
                    set = createSet(MyConstant.TEMP);
                }
                data.addDataSet(set);
            }
            data.addEntry(entry, 0);
            data.notifyDataChanged();
            // let the graph know it's data has changed
            graph.notifyDataSetChanged();
            // limit the number of visible entries
            graph.setVisibleXRangeMaximum(10);
            // move to the latest entry
            graph.moveViewToX(data.getEntryCount());
        }
    }

     */

    private LineDataSet createSet(int type) {
        LineDataSet set1 = new LineDataSet(null, "");
        set1.setForm(Legend.LegendForm.NONE);
        set1.setLineWidth(1.5f);
        set1.setHighLightColor(getResources().getColor(R.color.greyColor));
        set1.setDrawHighlightIndicators(true);
        set1.setHighlightLineWidth(1f);
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(false);

        if (type == MyConstant.HEART_CHART) {
            set1.setCircleColor(getResources().getColor(R.color.textWhitePrimary));
            set1.setColor(getResources().getColor(R.color.textWhitePrimary));
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            set1.setFillAlpha(80);
            set1.setDrawFilled(true);
            set1.setFillDrawable(getResources().getDrawable(R.drawable.fade_white));
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


    private void getEntries(List<Covid> data, int type) {
        List<Entry> entries = new ArrayList<Entry>();

        int interval = 0;
        int index = 0;

        if (data.get(0).getTime() - startTime > minTime) {
            entries.add(new Entry(0, -1));
            dateData.add(getDate(startTime));
            dateIndex.put(0, getDate(startTime));
        }

        interval = (int) Math.floor((data.get(0).getTime() - startTime) / minTime) - 1;
        entries.add(new Entry(interval, -1));
        dateData.add(getDate(data.get(0).getTime() - (int) minTime));
        dateIndex.put(interval, getDate(data.get(0).getTime() - (int) minTime));

        index = interval + 1;

        for (int i = 0; i < data.size(); i++) {
            if (i > 0) {
                interval = (int) Math.floor((data.get(i).getTime() - data.get(i - 1).getTime()) / minTime);
                index = interval + index;
            }
            entries.add(new Entry(index, (float) data.get(i).getData()));
            dateData.add(getDate(data.get(i).getTime()));
            dateIndex.put(index, getDate(data.get(i).getTime()));
        }

        if (endTime - data.get(data.size() - 1).getTime() > minTime) {
            entries.add(new Entry(index + 1, -1));
            dateData.add(getDate(endTime));
            dateIndex.put(index + 1, getDate(endTime));

            entries.add(new Entry((int) Math.floor((endTime - startTime) / minTime), -1));
            dateData.add(getDate(endTime));
            dateIndex.put((int) Math.floor((endTime - startTime) / minTime), getDate(endTime));
        }

        chart(entries, type);
    }

    private void chart(List<Entry> entries, int type) {

        LineDataSet set1 = new LineDataSet(entries, "");
        set1.setForm(Legend.LegendForm.NONE);
        set1.setLineWidth(1.5f);
        set1.setHighLightColor(getResources().getColor(R.color.greyColor));
        set1.setDrawHighlightIndicators(true);
        set1.setHighlightLineWidth(0.5f);

        set1.setDrawCircleHole(false);
        set1.setDrawCircles(false);
        set1.setHighlightEnabled(true);

        set1.setCircleColor(getResources().getColor(R.color.textWhitePrimary));
        set1.setColor(getResources().getColor(R.color.textWhitePrimary));
        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setFillAlpha(60);
        set1.setDrawFilled(true);
        set1.setFillDrawable(getResources().getDrawable(R.drawable.fade_white));

        set1.setDrawValues(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        IndexAxisValueFormatter axisValueFormatter = new IndexAxisValueFormatter(dateData);
        xAxis.setValueFormatter(axisValueFormatter);

      /*  xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(5);



       */
        xAxis.setTextColor(getResources().getColor(R.color.textColorSecondary));

        chart.getAxisRight().setEnabled(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setEnabled(false); // Yan çizgileri aöçmak için
        yAxis.setTextSize(8f);
        yAxis.setDrawGridLines(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxis.setDrawAxisLine(false);
        yAxis.setTextColor(getResources().getColor(R.color.textColorSecondary));
        yAxis.setYOffset(0f);
        //yAxis.setXOffset(-18f);
        yAxis.setLabelCount(5);
        /*yAxis.setAxisMaximum(45);
        yAxis.setAxisMinimum(25);*/
        yAxis.setAxisMinimum(0);

        LineData lineData = new LineData(set1);
        //LineData lineData = new LineData();
        chart.setData(lineData);

        chart.getDescription().setEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(false);
        chart.setExtraOffsets(30, 0, 0, 0);
        chart.setViewPortOffsets(5f, 0f, 5f, 0f);
        chart.setOnChartValueSelectedListener(this);

        /*MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.chart_marker_view, axisValueFormatter);
        mv.setChartView(chart); // Set the marker to the chart
        chart.setMarker(mv);

         */


        chart.animateXY(200, 200);
    }

    public List<String> getDateData() {
        List<String> dateData = new ArrayList<>();
        List<String> dateDataForDatabase = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        dateData.add(getGraphDateType(cal));

        return dateData;

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

    private String getOnlyDayMonthDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd/MM", cal).toString();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e.getY() != -1) {
            selectedDataView.setVisibility(View.VISIBLE);
            if (viewType == MyConstant.HEART) {
                graphSelectedText.setText(String.format(Locale.getDefault(), "%.0f", e.getY()));
            } else if (viewType == MyConstant.SPO2) {
                graphSelectedText.setText(String.format(Locale.getDefault(), "%.1f", e.getY()));
            } else {
                graphSelectedText.setText(String.format(Locale.getDefault(), "%.1f", e.getY()));
            }
            graphSelectedDateText.setText(dateIndex.get((int) e.getX()));

        }
    }

    @Override
    public void onNothingSelected() {
        selectedDataView.setVisibility(View.GONE);

    }
}