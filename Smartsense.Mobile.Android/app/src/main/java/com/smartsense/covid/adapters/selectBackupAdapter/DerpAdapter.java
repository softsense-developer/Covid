package com.smartsense.covid.adapters.selectBackupAdapter;

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
public class DerpAdapter extends RecyclerView.Adapter<DerpAdapter.DerpHolder> {

    private List<BackupItem> listData;
    private LayoutInflater inflater;
    private Context context;
    private ItemClickCallback itemClickCallback;


    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public DerpAdapter(List<BackupItem> listData, Context c) {
        this.context = c;
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public DerpAdapter.DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_select_backup, parent, false);
        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(final DerpHolder holder, int position) {
        final BackupItem item = listData.get(position);
        holder.backupName.setText(item.getBackupName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setListData(ArrayList<BackupItem> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }


    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView backupName;
        private View container;

        private DerpHolder(View itemView) {
            super(itemView);
            backupName = itemView.findViewById(R.id.itemBackupName);
            container = itemView.findViewById(R.id.itemBackupCard);
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
