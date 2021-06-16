package com.smartsense.covid.ui.kayit_goster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.smartsense.covid.MyConstant;
import com.smartsense.covid.R;
import com.smartsense.covid.adapters.covidAdapter.DerpAdapter;
import com.smartsense.covid.model.Covid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class KayitGosterFragment extends Fragment {

    private KayitGosterViewModel kayitGosterViewModel;
    private RecyclerView recView;
    private LinearLayoutManager layoutManager;
    private DerpAdapter adapter;
    private ChipGroup dataTypeGroup;
    private Chip chipAllData, chipHeart, chipSpO2, chipTemp;
    private int showingDataType = 0;
    private LiveData<PagedList<Covid>> mAllData;
    private LiveData<PagedList<Covid>> searchByLiveData;
    private MutableLiveData<String> filterLiveData = new MutableLiveData<>();
    private int firstVisibleInListview = -1;
    private boolean autoScroll = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kayit_goster, container, false);

        recView = root.findViewById(R.id.epilepsyRecView);
        layoutManager = new LinearLayoutManager(getContext());
        recView.setLayoutManager(layoutManager);
        recView.setHasFixedSize(true);

        adapter = new DerpAdapter(getContext());
        recView.setAdapter(adapter);
        dataTypeGroup = root.findViewById(R.id.dataTypeGroup);
        chipAllData = root.findViewById(R.id.chipAllData);
        chipHeart = root.findViewById(R.id.chipHeart);
        chipSpO2 = root.findViewById(R.id.chipSpO2);
        chipTemp = root.findViewById(R.id.chipTemp);


        kayitGosterViewModel =
                ViewModelProviders.of(this).get(KayitGosterViewModel.class);


        mAllData = kayitGosterViewModel.getAllData();

        searchByLiveData = Transformations.switchMap(filterLiveData,
                new Function<String, LiveData<PagedList<Covid>>>() {
                    @Override
                    public LiveData<PagedList<Covid>> apply(String v) {
                        return kayitGosterViewModel.getTypeData(Integer.parseInt(v));
                    }
                });


        setFilter("0");
        dataTypeGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (chipTemp.isChecked()) {
                    showingDataType = MyConstant.TEMP;
                    setFilter(String.valueOf(MyConstant.TEMP));
                } else if (chipHeart.isChecked()) {
                    showingDataType = MyConstant.HEART;
                    setFilter(String.valueOf(MyConstant.HEART));
                } else if (chipSpO2.isChecked()) {
                    showingDataType = MyConstant.SPO2;
                    setFilter(String.valueOf(MyConstant.SPO2));
                } else if (chipAllData.isChecked()) {
                    showingDataType = 0;
                    setFilter("0");
                } else {
                    showingDataType = -1;
                    setFilter("-1");
                }
            }
        });

        searchByLiveData.observe(getViewLifecycleOwner(), new Observer<PagedList<Covid>>() {
            @Override
            public void onChanged(PagedList<Covid> epilepsies) {
                adapter.submitList(epilepsies);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (autoScroll) {
                    recView.scrollToPosition(0);
                }
            }
        });


        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleInListview != 0) {
                    if (autoScroll) {
                        stopAutoScroll();
                    }
                } else {
                    if (!autoScroll) {
                        startAutoScroll();
                    }
                }
            }
        });

        return root;
    }

    private void startAutoScroll() {
        autoScroll = true;
    }

    private void stopAutoScroll() {
        autoScroll = false;
    }

    LiveData<PagedList<Covid>> getSearchBy() {
        return searchByLiveData;
    }

    void setFilter(String filter) {
        filterLiveData.setValue(filter);
    }

    LiveData<PagedList<Covid>> getAllWords() {
        return mAllData;
    }




    /*private LineDataSet createSet(int type) {
        LineDataSet set1 = new LineDataSet(null, "");
        set1.setForm(Legend.LegendForm.NONE);
        set1.setLineWidth(2.75f);
        set1.setHighLightColor(getResources().getColor(R.color.greyColor));
        set1.setDrawHighlightIndicators(true);
        set1.setHighlightLineWidth(1f);
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(true);
        set1.setMode(LineDataSet.Mode.LINEAR);

        if (type == MyConstant.HEART_CHART) {
            set1.setCircleColor(getResources().getColor(R.color.graph_color_3));
            set1.setColor(getResources().getColor(R.color.graph_color_3));
        } else {
            set1.setCircleColor(getResources().getColor(R.color.graph_color));
            set1.setColor(getResources().getColor(R.color.graph_color));
        }

        set1.setDrawValues(false);
        return set1;
    }

    private void addEntry(LineChart graph, Entry entry) {
        LineData data = graph.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                if (graph == heartChart) {
                    set = createSet(1);
                } else {
                    set = createSet(0);
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

    private void feedMultiple() {

        hndler = new Handler();
        runnable = () -> {
            hndler.postDelayed(runnable, 1000);
            addEntry(tempChart, new Entry(i, (float) (Math.random() * 2.0f)));
            addEntry(heartChart, new Entry(i, (float) (Math.random() * 3.0f)));
            i++;
        };
        hndler.postDelayed(runnable, 1000);
    }
*/

   /* private void chart(LineChart chart) {
        List<String> dateData = getDateData();

        set1.setForm(Legend.LegendForm.NONE);
        set1.setLineWidth(2.75f);
        set1.setHighLightColor(getResources().getColor(R.color.greyColor));
        set1.setDrawHighlightIndicators(true);
        set1.setHighlightLineWidth(1f);
        set1.setDrawCircleHole(false);
        set1.setDrawCircles(true);

        set1.setCircleColor(getResources().getColor(R.color.graph_color));
        set1.setColor(getResources().getColor(R.color.graph_color));
        set1.setDrawValues(false);



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
        yAxis.setTextSize(14f);
        yAxis.setDrawGridLines(true);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxis.setSpaceTop(20);
        yAxis.setDrawAxisLine(false);
        yAxis.setYOffset(-8f);
        yAxis.setXOffset(-20f);
        yAxis.setTextColor(getResources().getColor(R.color.textColorSecondary));

        //LineData lineData = new LineData(set1);
        LineData lineData = new LineData();
        chart.setData(lineData);

        chart.getDescription().setEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(false);
        chart.setExtraOffsets(32, 16, 32, 8);

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.chart_marker_view, axisValueFormatter);
        mv.setChartView(chart); // Set the marker to the chart
        chart.setMarker(mv);

        chart.animateXY(500, 500);
    }*/

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
}