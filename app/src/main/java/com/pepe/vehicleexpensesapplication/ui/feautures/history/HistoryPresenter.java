package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import android.content.Context;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.model.mapper.HistoryItemMapper;
import com.pepe.vehicleexpensesapplication.data.model.ui.HistoryUIModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HistoryPresenter implements HistoryContract.Presenter {

    private static final String HISTORY_FR_PRESENTER_TAG = "HISTORY_FR_PRESENTER_TAG";

    private HistoryContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;

    private final FirebaseHelper.FirebaseHistoryListener firebaseHistoryListener = new FirebaseHelper.FirebaseHistoryListener() {
        @Override
        public void onHistoryItemsLoaded(List<HistoryItemModel> items) {

            List<HistoryUIModel> parsedItems = items.stream().map(HistoryItemMapper::mapToUiModel).collect(Collectors.toList());


            for (int i = 0 ; i < parsedItems.size(); i++){
              if (i < parsedItems.size() - 1){


              }else {
//                  parsedItems.get(i).addedMileageText
              }
            }

                    //HistoryItemMapper.mapToUiModel(items)//getParsedItems(items);

            view.setHistoryItems(parsedItems);
        }
    };

    public HistoryPresenter(HistoryContract.View view, Context historyContext) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(historyContext);
        firebaseHelper = FirebaseHelper.getInstance(historyContext);
    }

    @Override
    public void onViewCreated() {
        try {
            view.setHistoryFragmentToolbar();
            firebaseHelper.setFirebaseListener(firebaseHistoryListener);
            firebaseHelper.getHistoryItems();

        } catch (Exception e) {
            Log.w(HISTORY_FR_PRESENTER_TAG, "On View Created EXCEPTION CAPTURED: " + e);
        }
    }

//    private List<HistoryItemModel> getParsedItems(List<HistoryItemModel> items) {
//        List<HistoryItemModel> parsedItems = new ArrayList<>(items);
//
//        for (int i = 0; items.size() > i; i++) {
//
//            if (i != items.size() - 1) {
//                parsedItems.get(i).CURR_MILEAGE_TEXT = setCurrentMileageText(parsedItems.get(i).CURRENT_MILEAGE);
//
//                float diff = parsedItems.get(i).CURRENT_MILEAGE - parsedItems.get(i + 1).CURRENT_MILEAGE;
//                parsedItems.get(i).ADDED_MILEAGE_TEXT = setAddedMileageText(diff);
//
//                float fuelCost = parsedItems.get(i).FUEL_AMOUNT * parsedItems.get(i).FUEL_PRICE;
//                parsedItems.get(i).FUEL_COST_TEXT = setFuelCostText(fuelCost);
//
//                parsedItems.get(i).FUEL_AMOUNT_TEXT = setFuelAmountText(parsedItems.get(i).FUEL_AMOUNT);
//
//                float fuelUsage = (parsedItems.get(i).FUEL_AMOUNT * 100) / diff;
//                parsedItems.get(i).FUEL_USAGE_TEXT = setFuelUsageText(fuelUsage);
//
//                Log.w(HISTORY_FR_PRESENTER_TAG, "get parsedItems, ALL ITEMS: " + parsedItems);
//            } else {
//
//                parsedItems.get(i).CURR_MILEAGE_TEXT = setCurrentMileageText(parsedItems.get(i).CURRENT_MILEAGE);
//
//                float fuelCost = parsedItems.get(i).FUEL_AMOUNT * parsedItems.get(i).FUEL_PRICE;
//                parsedItems.get(i).FUEL_COST_TEXT = setFuelCostText(fuelCost);
//
//                parsedItems.get(i).FUEL_AMOUNT_TEXT = setFuelAmountText(parsedItems.get(i).FUEL_AMOUNT);
//
//                Log.w(HISTORY_FR_PRESENTER_TAG, "get parsedItems, LAST ITEM: " + parsedItems);
//            }
//        }
//        Log.w(HISTORY_FR_PRESENTER_TAG, "get parsedItems, before RETURN: " + parsedItems);
//        return parsedItems;
//    }

    private String setFuelUsageText(float fuelUsage) {
        if (fuelUsage > 99) {
            return String.format("%.1f", fuelUsage);
        } else {
            if (fuelUsage > 999) {
                return String.format("%.0f", fuelUsage);
            } else {
                if (fuelUsage > 9999) {
                    return "# , #";
                } else {
                    return String.format("%.2f", fuelUsage);
                }
            }
        }
    }

    private String setAddedMileageText(float diff) {
        if (diff > 999) {
            return "+" + String.format("%.0f", diff);
        } else {
            if (diff > 99999) {
                return ("+  --- ---");
            }
            return "+" + String.format("%.1f", diff);
        }
    }

    private String setFuelAmountText(float fuelAmount) {
        if (fuelAmount > 9999) {
            return "+9999";
        } else {
            return "+" + String.format("%.0f", fuelAmount);
        }
    }

    private String setFuelCostText(float fuelCost) {
        if (fuelCost > 999) {
            return String.format("%.0f", fuelCost);
        } else {
            if (fuelCost > 9999) {
                return "+9999";
            }
            return String.format("%.2f", fuelCost);
        }
    }

    private String setCurrentMileageText(float parsedItems) {
        if (parsedItems > 999999) {
            return "+999999";
        } else {
            return String.format("%.1f", parsedItems);
        }
    }

    @Override
    public void onFloatingRefillButtonClicked() {
        view.startRefillActivity();
    }

    @Override
    public boolean checkIsAnonymous() {
        return sharedPrefsHelper.getIsAnonymous();
    }
}

