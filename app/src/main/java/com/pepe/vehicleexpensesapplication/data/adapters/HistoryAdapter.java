package com.pepe.vehicleexpensesapplication.data.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.model.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String HISTORY_ADAPTER_TAG = "HISTORY_ADAPTER_TAG";

    private View.OnClickListener historyListener;

    private HistoryItemListener historyItemListener;


    private List<HistoryItemModel> historyList;

    public interface HistoryItemListener{
        void onItemClicked(long itemID, int position);
    }


    public HistoryAdapter(HistoryItemListener historyListener, List<HistoryItemModel> historyList) {

        this.historyItemListener = historyListener;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View historyView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);


        HistoryViewHolder historyHolder = new HistoryViewHolder(historyView);
        return historyHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryViewHolder historyHolder = (HistoryViewHolder) holder;
        Log.d(HISTORY_ADAPTER_TAG, "parsed items list ON BIND VIEW: " + historyList.size());
        if (historyList.size() != 0) {

            for (int i = 0; i < historyList.size(); i++) {

                    if (position == i) {
                        historyHolder.itemView.setOnClickListener(view -> historyItemListener.onItemClicked(historyList.get(position).ITEM_ID, position));

                        historyHolder.hDateText.setText(historyList.get(i).REFILL_DATE);
                        historyHolder.hMileageText.setText(historyList.get(i).CURR_MILEAGE_TEXT);

                        historyHolder.hLittersText.setText(historyList.get(i).FUEL_AMOUNT_TEXT);
                        historyHolder.hExpensesText.setText(historyList.get(i).FUEL_COST_TEXT);

                        historyHolder.hAddedMileageText.setText(historyList.get(i).ADDED_MILEAGE_TEXT);

                        if (historyList.get(i).FULL_TANK && position != historyList.size() - 1) {
                            historyHolder.hAverageUsageText.setText(historyList.get(i).FUEL_USAGE_TEXT);
                        }

                        if (position == historyList.size() - 1) {

                            historyHolder.itemView.setOnClickListener(view -> historyItemListener.onItemClicked(historyList.get(position).ITEM_ID, position));

                            historyHolder.hDateText.setText(historyList.get(i).REFILL_DATE);

                            historyHolder.hMileageText.setText(historyList.get(i).CURR_MILEAGE_TEXT);
                            historyHolder.hLittersText.setText(historyList.get(i).FUEL_AMOUNT_TEXT);
                            historyHolder.hExpensesText.setText(historyList.get(i).FUEL_COST_TEXT);
                        }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView hDateText;
        TextView hMileageText;
        TextView hAddedMileageText;
        TextView hExpensesText;
        TextView hAverageUsageText;
        TextView hItemIDText;
        TextView hLittersText;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            hDateText = itemView.findViewById(R.id.historyDateText);
            hMileageText = itemView.findViewById(R.id.historyMileageText);
            hAddedMileageText = itemView.findViewById(R.id.historyAddedMileageText);
            hExpensesText = itemView.findViewById(R.id.historyExpenseText);
            hAverageUsageText = itemView.findViewById(R.id.historyAverageUsageText);
            hItemIDText = itemView.findViewById(R.id.historyItemIdText);
            hLittersText = itemView.findViewById(R.id.historyLittersText);
        }
    }

}
