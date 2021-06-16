package com.smartsense.covid.adapters.companionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.recyclerview.widget.RecyclerView;

import com.smartsense.covid.R;

import java.util.ArrayList;
import java.util.List;

@Keep
public class DerpAdapter extends RecyclerView.Adapter<DerpAdapter.DerpHolder> {

    private List<Companion> listData;
    private LayoutInflater inflater;
    private Context context;
    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onDeleteClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public DerpAdapter(List<Companion> listData, Context c) {
        this.context = c;
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_companion, parent, false);
        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(final DerpHolder holder, int position) {
        Companion companion = listData.get(position);

        holder.itemCompanionNameText.setText((companion.getName() + " " + companion.getSurname()));
        holder.itemCompanionEmailText.setText(companion.getEmail());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setListData(ArrayList<Companion> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }


    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemCompanionNameText, itemCompanionEmailText;
        private ImageView deleteCompanionBt;
        private View container;

        private DerpHolder(View itemView) {
            super(itemView);
            itemCompanionNameText = itemView.findViewById(R.id.itemCompanionNameText);
            itemCompanionEmailText = itemView.findViewById(R.id.itemCompanionEmailText);
            deleteCompanionBt = itemView.findViewById(R.id.deleteCompanionBt);
            container = itemView.findViewById(R.id.itemRequest);
            deleteCompanionBt.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.deleteCompanionBt) {
                itemClickCallback.onDeleteClick(getAdapterPosition());
            }
        }
    }
}
