package com.pepe.vehicleexpensesapplication.data.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String HISTORY_ADAPTER_TAG = "HISTORY_ADAPTER_TAG";

    private View.OnClickListener historyListener;

    private FirebaseHelper firebaseHelper;
    private SharedPrefsHelper sharedPrefsHelper;

    public HistoryAdapter(View.OnClickListener historyListener, Context context) {

        this.historyListener = historyListener;
        firebaseHelper = FirebaseHelper.getInstance(context);
        sharedPrefsHelper = new SharedPrefsHelper(context);
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

        if (sharedPrefsHelper.getSHistorySize() != 0) {

            firebaseHelper.getRefillsListCollection()
                    .orderBy("MILEAGE", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<String> refillDatesList = new ArrayList<>();
                            List<String> refillMileageList = new ArrayList<>();
                            List<String> refillPriceList = new ArrayList<>();
                            List<String> refillFuelList = new ArrayList<>();
                            List<String> refillIDList = new ArrayList<>();
                            for (QueryDocumentSnapshot query : task.getResult()) {

                                refillDatesList.add(query.get("DATE").toString());
                                refillMileageList.add(query.get("MILEAGE").toString());
                                refillPriceList.add(query.get("PRICE").toString());
                                refillFuelList.add(query.get("FUEL").toString());
                                refillIDList.add(query.getId());
                            }
                            int refillsListSize = refillIDList.size() - 1;

                            for (int i = 0; i <= historyHolder.getAdapterPosition(); i++) {

                                if (i == 0) {
//                                    Thread.sleep(2500);
                                    if (historyHolder.getAdapterPosition() == i) {
                                        historyHolder.itemView.setOnClickListener(historyListener);

                                        historyHolder.hItemIDText.setText(refillIDList.get(refillsListSize - i));
                                        historyHolder.hDateText.setText(refillDatesList.get(refillsListSize - i));

                                        String rM = String.format(refillMileageList.get(refillsListSize - i));
                                        float fRM = Float.parseFloat(rM);
                                        int iRM = Math.round(fRM);
                                        historyHolder.hMileageText.setText(String.valueOf(iRM));

                                        String rFA = String.format(refillFuelList.get(refillsListSize - i));
                                        float fFA = Float.parseFloat(rFA);
                                        int iRFA = Math.round(fFA);
                                        float fuelAmount = Float.parseFloat(refillFuelList.get(refillsListSize - i));
                                        float litterCost = Float.parseFloat(refillPriceList.get(refillsListSize - i));
                                        float fuelCost = fuelAmount * litterCost;
                                        historyHolder.hLittersText.setText("+" + iRFA);
                                        if (fuelCost > 999) {
                                            historyHolder.hExpensesText.setText(String.format("%.0f", fuelCost));
                                        } else {
                                            historyHolder.hExpensesText.setText(String.format("%.2f", fuelCost));
                                        }
                                        if (sharedPrefsHelper.getSHistorySize() > 1) {
                                            float lastMil = Float.parseFloat(refillMileageList.get(refillsListSize - i));
                                            float currMil = Float.parseFloat(refillMileageList.get(refillsListSize - i - 1));
                                            float addedMil = lastMil - currMil;
                                            if (addedMil > 999) {
                                                historyHolder.hAddedMileageText.setText("+" + String.format("%.0f", addedMil));
                                                if (addedMil > 999999) {
                                                    historyHolder.hAddedMileageText.setText("+  --- ---");
                                                }
                                            } else {
                                                historyHolder.hAddedMileageText.setText("+" + String.format("%.1f", addedMil));
                                            }
                                            float fuelUsage = addedMil / fuelAmount;
                                            if (fuelUsage > 99) {
                                                historyHolder.hAverageUsageText.setText(String.format("%.1f", fuelUsage));
                                                if (fuelUsage > 999) {
                                                    historyHolder.hAverageUsageText.setText(String.format("%.0f", fuelUsage));
                                                    if (fuelUsage > 9999) {
                                                        historyHolder.hAverageUsageText.setText("# , #");
                                                    }
                                                }
                                            } else {
                                                historyHolder.hAverageUsageText.setText(String.format("%.2f", fuelUsage));
                                            }
                                        }
                                    }
                                }
                                if (i > 0) {
                                    if (historyHolder.getAdapterPosition() == i) {

                                        if (i == refillsListSize) {
                                            if (historyHolder.getAdapterPosition() == i) {

                                                historyHolder.itemView.setOnClickListener(historyListener);

                                                historyHolder.hItemIDText.setText(refillIDList.get(refillsListSize - i));
                                                historyHolder.hDateText.setText(refillDatesList.get(refillsListSize - i));

                                                String rM = String.format(refillMileageList.get(refillsListSize - i));
                                                float fRM = Float.parseFloat(rM);
                                                int iRM = Math.round(fRM);
                                                historyHolder.hMileageText.setText(String.valueOf(iRM));

                                                String rFA = String.format(refillFuelList.get(refillsListSize - i));
                                                float fFA = Float.parseFloat(rFA);
                                                int iRFA = Math.round(fFA);
                                                float fuelAmount = Float.parseFloat(refillFuelList.get(refillsListSize - i));
                                                float litterCost = Float.parseFloat(refillPriceList.get(refillsListSize - i));
                                                float fuelCost = fuelAmount * litterCost;
                                                historyHolder.hLittersText.setText("+" + iRFA);
                                                if (fuelCost > 999) {
                                                    historyHolder.hExpensesText.setText(String.format("%.0f", fuelCost));
                                                } else {
                                                    historyHolder.hExpensesText.setText(String.format("%.2f", fuelCost));
                                                }
                                            }
                                        } else {
                                            historyHolder.itemView.setOnClickListener(historyListener);

                                            historyHolder.hItemIDText.setText(refillIDList.get(refillsListSize - i));
                                            historyHolder.hDateText.setText(refillDatesList.get(refillsListSize - i));

                                            String rM = String.format(refillMileageList.get(refillsListSize - i));
                                            Float fRM = Float.parseFloat(rM);
                                            int iRM = Math.round(fRM);
                                            historyHolder.hMileageText.setText(String.valueOf(iRM));

                                            String rFA = String.format(refillFuelList.get(refillsListSize - i));
                                            float fFA = Float.parseFloat(rFA);
                                            int iRFA = Math.round(fFA);
                                            float fuelAmount = Float.parseFloat(refillFuelList.get(refillsListSize - i));
                                            float litterCost = Float.parseFloat(refillPriceList.get(refillsListSize - i));
                                            float fuelCost = fuelAmount * litterCost;
                                            historyHolder.hLittersText.setText("+" + iRFA);
                                            if (fuelCost > 999) {
                                                historyHolder.hExpensesText.setText(String.format("%.0f", fuelCost));
                                            } else {
                                                historyHolder.hExpensesText.setText(String.format("%.2f", fuelCost));
                                            }

                                            float lastMil = Float.parseFloat(refillMileageList.get(refillsListSize - i));
                                            float currMil = Float.parseFloat(refillMileageList.get(refillsListSize - i - 1));
                                            float addedMil = lastMil - currMil;
                                            if (addedMil > 999) {
                                                historyHolder.hAddedMileageText.setText("+" + String.format("%.0f", addedMil));
                                                if (addedMil > 999999) {
                                                    historyHolder.hAddedMileageText.setText("+  --- ---");
                                                }
                                            } else {
                                                historyHolder.hAddedMileageText.setText("+" + String.format("%.1f", addedMil));
                                            }
                                            float fuelUsage = addedMil / fuelAmount;
                                            if (fuelUsage > 99) {
                                                historyHolder.hAverageUsageText.setText(String.format("%.1f", fuelUsage));
                                                if (fuelUsage > 999) {
                                                    historyHolder.hAverageUsageText.setText(String.format("%.0f", fuelUsage));
                                                    if (fuelUsage > 9999) {
                                                        historyHolder.hAverageUsageText.setText("# , #");
                                                    }
                                                }
                                            } else {
                                                historyHolder.hAverageUsageText.setText(String.format("%.2f", fuelUsage));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(e -> Log.w(HISTORY_ADAPTER_TAG, "Get refill QUERY EXCEPTION: " + e));
        } else {
            Log.w(HISTORY_ADAPTER_TAG, "Get refill EXCEPTION: 'HISTORY SIZE < 1'");
        }
    }

    @Override
    public int getItemCount() {
        return sharedPrefsHelper.getSHistorySize();
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
