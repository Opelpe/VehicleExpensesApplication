package com.pepe.vehicleexpensesapplication.data.model.ui;

public class HistoryUIModel {

    public String addedMileageText;
    public String fuelUsageText;

    public HistoryUIModel(String currMileageText) {
        this.currMileageText = currMileageText;
    }

    public String currMileageText;
    public String fuelCostText;
    public String fuelAmountText;
    public String averageUsageText;

    public HistoryUIModel(String addedMileage, String fuelUsage, String currentMileage, String fuelCost, String fuelAmount, String averageUsage) {
        this.addedMileageText = addedMileage;
        this.fuelUsageText = fuelUsage;
        this.currMileageText = currentMileage;
        this.fuelCostText = fuelCost;
        this.fuelAmountText = fuelAmount;
        this.averageUsageText = averageUsage;
    }

    public HistoryUIModel(){

    }




}
