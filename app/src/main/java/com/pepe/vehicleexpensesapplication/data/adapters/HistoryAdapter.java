package com.pepe.vehicleexpensesapplication.data.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String HISTORY_ADAPTER_TAG = "HISTORY_ADAPTER_TAG";

    private View.OnClickListener historyListener;

    private HistoryItemListener historyItemListener;


    private List<HistoryUIModel> historyList;

    public interface HistoryItemListener{
        void onItemClicked(long itemID, int position);
    }


    public HistoryAdapter(HistoryItemListener historyListener, List<HistoryUIModel> historyList) {

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
//                        historyHolder.itemView.setOnClickListener(view -> historyItemListener.onItemClicked(historyList.get(position).ITEM_ID, position));

//                        historyHolder.hDateText.setText(historyList.get(i).);
                        historyHolder.hMileageText.setText(historyList.get(i).currMileageText);

                        historyHolder.hLittersText.setText(historyList.get(i).fuelAmountText);
                        historyHolder.hExpensesText.setText(historyList.get(i).fuelCostText);

                        historyHolder.hAddedMileageText.setText(historyList.get(i).addedMileageText);

//                        if (historyList.get(i).FULL_TANK && position != historyList.size() - 1) {
//                            historyHolder.hAverageUsageText.setText(historyList.get(i).FUEL_USAGE_TEXT);
//                        }

                        if (position == historyList.size() - 1) {

//                            historyHolder.itemView.setOnClickListener(view -> historyItemListener.onItemClicked(historyList.get(position).ITEM_ID, position));

//                            historyHolder.hDateText.setText(historyList.get(i).REFILL_DATE);

                            historyHolder.hMileageText.setText(historyList.get(i).currMileageText);
                            historyHolder.hLittersText.setText(historyList.get(i).fuelAmountText);
                            historyHolder.hExpensesText.setText(historyList.get(i).fuelCostText);
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
