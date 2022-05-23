package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.content.Context;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.model.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.List;

public class MainFragmentPresenter implements MainFragmentContract.Presenter {

    private static final String MAIN_FR_PRESENTER_TAG = "MAIN_FR_PRESENTER_TAG";

    private MainFragmentContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    private final FirebaseHelper.FirebaseHistoryListener firebaseHistoryListener = items -> {

        if (items.size() > 0) {
            setTotalRefillScores(items);
            setLatestRefillScores(items);
        }
        if (items.size() > 1) {
            setAverageUsageScore(items);
            setTravelingCostScore(items);
        }
    };


    public MainFragmentPresenter(MainFragmentContract.View view, Context mainContext) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(mainContext);
        firebaseHelper = FirebaseHelper.getInstance(mainContext);
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

            float totalAddedMileage = items.get(0).CURRENT_MILEAGE - items.get(items.size() - 1).CURRENT_MILEAGE;
            float totalFuelAmount = 0;
            for (int i = 0; items.size() > i; i++) {
                if (i != items.size() - 1) {
                    totalFuelAmount += items.get(i).FUEL_AMOUNT;
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
                    float diff = items.get(i).CURRENT_MILEAGE - items.get(i + 1).CURRENT_MILEAGE;
                    float fuelUsage = (items.get(i).FUEL_AMOUNT * 100) / diff;
                    float pricePer100 = fuelUsage * (items.get(i).FUEL_PRICE);
                    totalTravelCosts += pricePer100;
                }
            }
            String travelingCostText = String.format("%.1f", totalTravelCosts / items.size());
            view.setTravelingCostText(travelingCostText);

    }

    private void setLatestRefillScores(List<HistoryItemModel> items) {

        if (items.size() > 1){
            float addedMileage = items.get(0).CURRENT_MILEAGE - items.get(1).CURRENT_MILEAGE;
            float fuelUsage = (items.get(0).FUEL_AMOUNT * 100) / addedMileage;
            float fuelCost = items.get(0).FUEL_AMOUNT * items.get(0).FUEL_PRICE;
            float currMileage = items.get(0).CURRENT_MILEAGE;

            String currentMileageText = String.format("%.1f", currMileage);
            String fuelUsageText = String.format("%.1f", fuelUsage);
            String fuelCostText = String.format("%.0f", fuelCost);
            view.setLatestRefillView(currentMileageText, fuelUsageText, fuelCostText);
        }else {
            float fuelCost = items.get(0).FUEL_AMOUNT * items.get(0).FUEL_PRICE;
            float currMileage = items.get(0).CURRENT_MILEAGE;

            String currentMileageText = String.format("%.1f", currMileage);
            String fuelUsageText ="---";
            String fuelCostText = String.format("%.0f", fuelCost);
            view.setLatestRefillView(currentMileageText, fuelUsageText, fuelCostText);
        }
    }

    private void setTotalRefillScores(List<HistoryItemModel> items) {

            float totalAddedMileage = items.get(0).CURRENT_MILEAGE - items.get(items.size() - 1).CURRENT_MILEAGE;
            float totalCosts = 0;
            float totalVolume = 0;

            for (int i = 0; items.size() > i; i++) {

                float fuelCost = items.get(i).FUEL_AMOUNT * items.get(i).FUEL_PRICE;

                totalCosts += fuelCost;
                totalVolume += items.get(i).FUEL_AMOUNT;
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