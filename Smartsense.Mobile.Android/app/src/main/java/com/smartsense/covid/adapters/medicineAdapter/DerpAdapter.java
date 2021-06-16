package com.smartsense.covid.adapters.medicineAdapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsense.covid.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Keep
public class DerpAdapter extends RecyclerView.Adapter<DerpAdapter.DerpHolder> {

    private List<Item> listData;
    private LayoutInflater inflater;
    private Context context;
    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public DerpAdapter(List<Item> listData, Context c) {
        this.context = c;
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_medicine, parent, false);
        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(final DerpHolder holder, int position) {
        final Item item = listData.get(position);

        holder.medicineName.setText(item.getMedicineName());
        holder.onComingUsageTime.setText(item.getMedicineName());
    }


    private String getTime(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("HH:mm", cal).toString();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setListData(ArrayList<Item> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }


    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView medicineName, onComingUsageTime;
        private View container;


        private DerpHolder(View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.itemMedicineNameText);
            onComingUsageTime = itemView.findViewById(R.id.itemMedicineTimeText);

            container = itemView.findViewById(R.id.itemMedicine);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.itemMedicine) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
        }
    }
}
