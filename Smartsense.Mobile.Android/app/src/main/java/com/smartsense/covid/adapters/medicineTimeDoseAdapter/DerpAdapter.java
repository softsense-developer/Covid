package com.smartsense.covid.adapters.medicineTimeDoseAdapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Keep;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

        void onTimeClick(int p);
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
        View view = inflater.inflate(R.layout.item_medicine_time_dose, parent, false);
        return new DerpHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(final DerpHolder holder, int position) {
        final Item item = listData.get(position);

        holder.medicineTimeTI.getEditText().setText(getTime(item.getTimeStamp()));
        if (getItemCount() > 1) {
            holder.medicineTimeDoseClose.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            holder.medicineTimeDoseClose.setColorFilter(ContextCompat.getColor(context, R.color.colorGrey), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.medicineDoseTI.getEditText().setText(item.getDose());

    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            listData.get(position).setDose(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
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

        private ImageView medicineTimeDoseClose;
        private TextInputLayout medicineTimeTI, medicineDoseTI;
        private TextInputEditText medicineTimeTIET;
        private View container;
        public MyCustomEditTextListener myCustomEditTextListener;


        private DerpHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            medicineTimeDoseClose = itemView.findViewById(R.id.medicineTimeDoseClose);
            medicineTimeTI = itemView.findViewById(R.id.medicineTimeTI);
            medicineTimeTIET = itemView.findViewById(R.id.medicineTimeTIET);
            medicineDoseTI = itemView.findViewById(R.id.medicineDoseTI);
            container = itemView.findViewById(R.id.itemMedicineTimeDose);
            medicineTimeDoseClose.setOnClickListener(this);
            medicineTimeTIET.setOnClickListener(this);
            this.myCustomEditTextListener = myCustomEditTextListener;
            medicineDoseTI.getEditText().addTextChangedListener(myCustomEditTextListener);

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.medicineTimeDoseClose) {
                itemClickCallback.onItemClick(getAdapterPosition());
            } else if (view.getId() == R.id.medicineTimeTIET) {
                itemClickCallback.onTimeClick(getAdapterPosition());
            }
        }
    }
}
