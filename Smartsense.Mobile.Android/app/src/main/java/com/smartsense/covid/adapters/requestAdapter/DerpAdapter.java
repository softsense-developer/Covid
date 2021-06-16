package com.smartsense.covid.adapters.requestAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.smartsense.covid.R;

import java.util.ArrayList;
import java.util.List;

@Keep
public class DerpAdapter extends RecyclerView.Adapter<DerpAdapter.DerpHolder> {

    private List<Promotion> listData;
    private LayoutInflater inflater;
    private Context context;
    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onAcceptClick(int p);
        void onRefuseClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public DerpAdapter(List<Promotion> listData, Context c) {
        this.context = c;
        inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_request, parent, false);
        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(final DerpHolder holder, int position) {
        Promotion promotion = listData.get(position);

        holder.itemRequestNameText.setText((promotion.getName()+" "+ promotion.getSurname()+ " "+context.getString(R.string.doctor_request_text)));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setListData(ArrayList<Promotion> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }


    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemRequestNameText;
        private MaterialButton itemRequestRefuse, itemRequestAccept;
        private View container;

        private DerpHolder(View itemView) {
            super(itemView);
            itemRequestNameText = itemView.findViewById(R.id.itemRequestNameText);
            itemRequestRefuse = itemView.findViewById(R.id.itemRequestRefuse);
            itemRequestAccept = itemView.findViewById(R.id.itemRequestAccept);
            container = itemView.findViewById(R.id.itemRequest);
            itemRequestRefuse.setOnClickListener(this);
            itemRequestAccept.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.itemRequestRefuse) {
                itemClickCallback.onRefuseClick(getAdapterPosition());
            }else if (view.getId() == R.id.itemRequestAccept) {
                itemClickCallback.onAcceptClick(getAdapterPosition());
            }
        }
    }
}
