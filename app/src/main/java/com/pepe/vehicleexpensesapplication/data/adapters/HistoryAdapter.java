package com.pepe.vehicleexpensesapplication.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepe.vehicleexpensesapplication.R;


public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private View.OnClickListener historyListener;

    public HistoryAdapter(View.OnClickListener historyListener) {

        this.historyListener = historyListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View historyView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        HistoryViewHolder historyHolder = new HistoryViewHolder(historyView);
        historyHolder.itemView.setOnClickListener(view -> historyListener.onClick(historyView));


        return historyHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryViewHolder historyHolder = (HistoryViewHolder) holder;

//        historyHolder.historyDate.setText("02.12.2025");
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView historyDate;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            historyDate = itemView.findViewById(R.id.historyDateText);
        }
    }

}
