package com.pepe.vehicleexpensesapplication.data.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    private final List<HistoryItemModel> itemModelList;
    private List<HistoryUIModel> historyUIList;
    private DeleteItemListener deleteItemListener;

    public interface HistoryItemListener{
        void onItemClicked(long itemID);
    }

    public interface  DeleteItemListener{
        void onClick(long itemID);
    }


    public HistoryAdapter(HistoryItemListener historyListener, List<HistoryUIModel> historyList, List<HistoryItemModel> itemModels, DeleteItemListener deleteItemListener) {

        this.historyItemListener = historyListener;
        this.historyUIList = historyList;
        this.itemModelList = itemModels;
        this.deleteItemListener = deleteItemListener;
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
        Log.d(HISTORY_ADAPTER_TAG, "parsed items list ON BIND VIEW: " + historyUIList.size());
        if (historyUIList.size() != 0) {
            historyHolder.deleteItemButton.setOnClickListener(view -> deleteItemListener.onClick(itemModelList.get(position).itemID));
            historyHolder.itemView.setOnClickListener(view -> historyItemListener.onItemClicked(itemModelList.get(position).itemID));
            for (int i = 0; i < historyUIList.size(); i++) {

                    if (position == i) {
                        historyHolder.hDateText.setText(historyUIList.get(i).refillDate);
                        historyHolder.hMileageText.setText(historyUIList.get(i).currMileageText);

                        historyHolder.hLittersText.setText(historyUIList.get(i).fuelAmountText);
                        historyHolder.hExpensesText.setText(historyUIList.get(i).fuelCostText);

                        historyHolder.hAddedMileageText.setText(historyUIList.get(i).addedMileageText);
                        historyHolder.hAverageUsageText.setText(historyUIList.get(i).fuelUsageText);
//                        }

                        if (position == historyUIList.size() - 1) {
                            historyHolder.hDateText.setText(historyUIList.get(i).refillDate);

                            historyHolder.hMileageText.setText(historyUIList.get(i).currMileageText);
                            historyHolder.hLittersText.setText(historyUIList.get(i).fuelAmountText);
                            historyHolder.hExpensesText.setText(historyUIList.get(i).fuelCostText);
                        }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return historyUIList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView hDateText;
        TextView hMileageText;
        TextView hAddedMileageText;
        TextView hExpensesText;
        TextView hAverageUsageText;
        TextView hItemIDText;
        TextView hLittersText;

        ImageButton deleteItemButton;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            hDateText = itemView.findViewById(R.id.historyDateText);
            hMileageText = itemView.findViewById(R.id.historyMileageText);
            hAddedMileageText = itemView.findViewById(R.id.historyAddedMileageText);
            hExpensesText = itemView.findViewById(R.id.historyExpenseText);
            hAverageUsageText = itemView.findViewById(R.id.historyAverageUsageText);
            hItemIDText = itemView.findViewById(R.id.historyItemIdText);
            hLittersText = itemView.findViewById(R.id.historyLittersText);

            deleteItemButton = itemView.findViewById(R.id.deleteHistoryButton);
        }
    }

}
