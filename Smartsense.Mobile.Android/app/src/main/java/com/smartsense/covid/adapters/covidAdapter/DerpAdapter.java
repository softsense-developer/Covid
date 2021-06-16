package com.smartsense.covid.adapters.covidAdapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsense.covid.MyConstant;
import com.smartsense.covid.R;
import com.smartsense.covid.model.Covid;

import java.util.Calendar;
import java.util.Locale;

public class DerpAdapter extends PagedListAdapter<Covid, DerpAdapter.DerpHolder> {

    private ItemClickCallback itemClickCallback;
    private Context context;
    private String sData;

    public DerpAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Covid> DIFF_CALLBACK = new DiffUtil.ItemCallback<Covid>() {
        @Override
        public boolean areItemsTheSame(@NonNull Covid oldItem, @NonNull Covid newItem) {
            return oldItem.getID() == newItem.getID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Covid oldItem, @NonNull Covid newItem) {
            return oldItem.getTime() == newItem.getTime();
        }
    };

    public interface ItemClickCallback {

    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }


    @NonNull
    @Override
    public DerpAdapter.DerpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_covid, parent, false);
        return new DerpHolder(view);
    }


    @Override
    public void onBindViewHolder(final DerpHolder holder, int position) {
        Covid data = getItem(position);
        holder.timestamp.setText(getData(data.getTime()));
        if (data.getDataType() == MyConstant.TEMP) {
            sData = String.format(Locale.getDefault(), "%.2f", data.getData());
            holder.dataText.setText((sData + " " + context.getString(R.string.temp_unit)));
        } else if (data.getDataType() == MyConstant.HEART) {
            sData = String.format(Locale.getDefault(), "%.0f", data.getData());
            holder.dataText.setText((sData + " " + context.getString(R.string.heart_unit)));
        } else {
            sData = String.format(Locale.getDefault(), "%.0f", data.getData());
            holder.dataText.setText((context.getString(R.string.spo2_unit)+sData));
        }

    }

    public Covid getEpilepsyAt(int position) {
        return getItem(position);
    }

    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView dataText, timestamp;

        private DerpHolder(View itemView) {
            super(itemView);
            dataText = itemView.findViewById(R.id.itemCovidDataText);
            timestamp = itemView.findViewById(R.id.itemTimestampText);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private String getData(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd/MM/yyyy - HH:mm:ss", cal).toString();
    }
}
