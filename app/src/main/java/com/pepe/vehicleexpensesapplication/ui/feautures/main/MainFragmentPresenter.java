package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.content.Context;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.model.firebase.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.List;

public class MainFragmentPresenter implements MainFragmentContract.Presenter {

    private static final String MAIN_FR_PRESENTER_TAG = "MAIN_FR_PRESENTER_TAG";

    private MainFragmentContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    private final FirebaseHelper.FirebaseHistoryListener firebaseHistoryListener = items -> {
        //  items.stream().map(HistoryItem Mapper::mapToUiModel).collect(Collectors.toList());
        if (items.size() > 0) {
            setTotalRefillScores(items);
            setLatestRefillScores(items);
        }
        if (items.size() > 1) {
            setAverageUsageScore(items);
            setTravelingCostScore(items);
        }

//        for (int i = 0 ; i < items.size(); i++){
//              if (i != items.size() - 1){
//
//                  parsedItems.get(i).currMileageText = setCurrentMileageText(items.get(i).CURRENT_MILEAGE);
//
//                  float diff = items.get(i).CURRENT_MILEAGE - items.get(i + 1).CURRENT_MILEAGE;
//                  parsedItems.get(i).addedMileageText = setAddedMileageText(diff);
//
//                  float fuelCost = items.get(i).FUEL_AMOUNT * items.get(i).FUEL_PRICE;
//                  parsedItems.get(i).fuelCostText = setFuelCostText(fuelCost);
//
//                  parsedItems.get(i).fuelAmountText = setFuelAmountText(items.get(i).FUEL_AMOUNT);
//
//                  float fuelUsage = (items.get(i).FUEL_AMOUNT * 100) / diff;
//                  parsedItems.get(i).fuelUsageText = setFuelUsageText(fuelUsage);
//
//              }else {
//                  parsedItems.get(i).currMileageText = setCurrentMileageText(items.get(i).CURRENT_MILEAGE);
//
//                float fuelCost = items.get(i).FUEL_AMOUNT * items.get(i).FUEL_PRICE;
//                parsedItems.get(i).fuelCostText = setFuelCostText(fuelCost);
//
//                parsedItems.get(i).fuelAmountText = setFuelAmountText(items.get(i).FUEL_AMOUNT);
//              }
//            }

//                    HistoryItemMapper.mapToUiModel(items);//getParsedItems(items);

//            view.setHistoryItems(parsedItems);
    };


    public MainFragmentPresenter(MainFragmentContract.View view, SharedPrefsHelper prefsHelper) {
        this.view = view;
        sharedPrefsHelper = prefsHelper;
        firebaseHelper = FirebaseHelper.getInstance();
    }

    @Override
    public boolean checkIsAnonymous() {
        return sharedPrefsHelper.getIsAnonymous();
    }

    @Override
    public boolean checkGoogleSignIn() {
        return sharedPrefsHelper.getGoogleSignInCompleted();
    }

    @Override
    public void onViewCreated() {
        setMainView();
    }

    private void setMainView() {
        view.setMainFragmentToolbar();

        firebaseHelper.setFirebaseListener(firebaseHistoryListener);
        firebaseHelper.getHistoryItems();
    }

    private void setAverageUsageScore(List<HistoryItemModel> items) {

            float totalAddedMileage = items.get(0).currentMileage - items.get(items.size() - 1).currentMileage;
            float totalFuelAmount = 0;
            for (int i = 0; items.size() > i; i++) {
                if (i != items.size() - 1) {
                    totalFuelAmount += items.get(i).fuelAmount;
                }
            }
            float usage = (totalFuelAmount * 100) / totalAddedMileage;
            if (usage > 9.99) {
                view.setAverageUsageText(String.format("%.1f", usage));
            } else {
                view.setAverageUsageText(String.format("%.2f", usage));
            }
    }

    private void setTravelingCostScore(List<HistoryItemModel> items) {

            float totalTravelCosts = 0;
            for (int i = 0; items.size() > i; i++) {
                if (i != items.size() - 1) {
                    float diff = items.get(i).currentMileage - items.get(i + 1).currentMileage;
                    float fuelUsage = (items.get(i).fuelAmount * 100) / diff;
                    float pricePer100 = fuelUsage * (items.get(i).fuelPrice);
                    totalTravelCosts += pricePer100;
                }
            }
            String travelingCostText = String.format("%.1f", totalTravelCosts / items.size());
            view.setTravelingCostText(travelingCostText);

    }

    private void setLatestRefillScores(List<HistoryItemModel> items) {

        if (items.size() > 1){
            float addedMileage = items.get(0).currentMileage - items.get(1).currentMileage;
            float fuelUsage = (items.get(0).fuelAmount * 100) / addedMileage;
            float fuelCost = items.get(0).fuelAmount * items.get(0).fuelPrice;
            float currMileage = items.get(0).currentMileage;

            String currentMileageText = String.format("%.1f", currMileage);
            String fuelUsageText = String.format("%.1f", fuelUsage);
            String fuelCostText = String.format("%.0f", fuelCost);
            view.setLatestRefillView(currentMileageText, fuelUsageText, fuelCostText);
        }else {
            float fuelCost = items.get(0).fuelAmount * items.get(0).fuelPrice;
            float currMileage = items.get(0).currentMileage;

            String currentMileageText = String.format("%.1f", currMileage);
            String fuelUsageText ="---";
            String fuelCostText = String.format("%.0f", fuelCost);
            view.setLatestRefillView(currentMileageText, fuelUsageText, fuelCostText);
        }
    }

    private void setTotalRefillScores(List<HistoryItemModel> items) {

            float totalAddedMileage = items.get(0).currentMileage - items.get(items.size() - 1).currentMileage;
            float totalCosts = 0;
            float totalVolume = 0;

            for (int i = 0; items.size() > i; i++) {

                float fuelCost = items.get(i).fuelAmount * items.get(i).fuelPrice;

                totalCosts += fuelCost;
                totalVolume += items.get(i).fuelAmount;
            }
            String totalMileageText = "+" + String.format("%.0f", totalAddedMileage);
            String totalMoneyText = String.format("%.0f", totalCosts);
            String totalVolumeText = String.format("%.0f", totalVolume);
            view.setTotalCostsText(totalMileageText, totalMoneyText, totalVolumeText);
    }

    @Override
    public void onRefillButtonClicked() {
        view.startRefillActivity();
    }

}