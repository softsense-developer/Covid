package com.smartsense.covid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;


@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView tvContent, tvDate;
    private IndexAxisValueFormatter xAxisValueFormatter;

    public MyMarkerView(Context context, int layoutResource, IndexAxisValueFormatter xAxisValueFormatter) {
        super(context, layoutResource);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = findViewById(R.id.tvContent);
        tvDate = findViewById(R.id.tvContentDate);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText((Utils.formatNumber(ce.getHigh(), 2, true)));
            tvDate.setText(String.valueOf(xAxisValueFormatter.getFormattedValue(ce.getX())));
        } else {
            tvContent.setText((Utils.formatNumber(e.getY(), 2, true)));
            tvDate.setText(String.valueOf(xAxisValueFormatter.getFormattedValue(e.getX())));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
