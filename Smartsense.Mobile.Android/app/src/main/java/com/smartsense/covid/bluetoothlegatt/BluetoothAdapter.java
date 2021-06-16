package com.smartsense.covid.bluetoothlegatt;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsense.covid.R;

import java.util.ArrayList;
import java.util.List;

@Keep
public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.DerpHolder> {

    private List<BluetoothDevice> listData;
    private LayoutInflater inflater;
    private Context context;
    private ItemClickCallback itemClickCallback;


    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public BluetoothAdapter(List<BluetoothDevice> listData, Context c) {
        this.context = c;
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public BluetoothAdapter.DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bluetooth_scan, parent, false);
        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(final DerpHolder holder, int position) {
        final BluetoothDevice item = listData.get(position);
        holder.itemBluetoothName.setText(item.getName());
        holder.itemBluetoothAddress.setText(item.getAddress());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setListData(ArrayList<BluetoothDevice> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }

    public BluetoothDevice getDevice(int position) {
        return listData.get(position);
    }

    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemBluetoothName, itemBluetoothAddress;
        private View container;

        private DerpHolder(View itemView) {
            super(itemView);
            itemBluetoothName = itemView.findViewById(R.id.itemBluetoothName);
            itemBluetoothAddress = itemView.findViewById(R.id.itemBluetoothAddress);
            container = itemView.findViewById(R.id.itemBluetoothCard);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.itemBluetoothCard) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
        }
    }

}
