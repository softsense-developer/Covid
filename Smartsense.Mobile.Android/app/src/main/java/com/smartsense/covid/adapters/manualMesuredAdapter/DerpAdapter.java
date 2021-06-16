package com.smartsense.covid.adapters.manualMesuredAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsense.covid.MyConstant;
import com.smartsense.covid.R;

import java.util.ArrayList;
import java.util.List;

@Keep
public class DerpAdapter extends RecyclerView.Adapter<DerpAdapter.DerpHolder> {

    private List<ManualItem> listData;
    private LayoutInflater inflater;
    private Context context;
    private ItemClickCallback itemClickCallback;


    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public DerpAdapter(List<ManualItem> listData, Context c) {
        this.context = c;
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_manually_measured, parent, false);
        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(final DerpHolder holder, int position) {
        ManualItem item = listData.get(position);
        holder.data.setText(item.getData());
        holder.date.setText(item.getDate());
        if (item.getType() == MyConstant.TEMP) {
            holder.unit.setText(context.getString(R.string.temp_unit));
            holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_temp, null));
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.temp_color), android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (item.getType() == MyConstant.SPO2) {
            holder.unit.setText(context.getString(R.string.spo2_unit));
            holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_spo2, null));
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.spo2_color), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            holder.unit.setText(context.getString(R.string.heart_unit));
            holder.icon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_heart_rate, null));
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.heart_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setListData(ArrayList<ManualItem> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }


    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView data, date, unit;
        private ImageView icon;
        private View container;

        private DerpHolder(View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.manuallyMeasurmentDataText);
            unit = itemView.findViewById(R.id.manuallyMeasurmentDataType);
            date = itemView.findViewById(R.id.manuallyMeasurmentDate);
            icon = itemView.findViewById(R.id.manuallyIcon);

            container = itemView.findViewById(R.id.itemManualCard);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.itemBackupCard) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
        }
    }
}
