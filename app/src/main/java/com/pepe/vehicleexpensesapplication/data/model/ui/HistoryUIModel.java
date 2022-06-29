package com.pepe.vehicleexpensesapplication.data.model.ui;

public class HistoryUIModel {

    public String addedMileageText;
    public String fuelUsageText;
    public String currMileageText;
    public String fuelCostText;
    public String fuelAmountText;
    public String refillDate;

    public HistoryUIModel(String addedMileage, String fuelUsage, String currentMileage, String fuelCost, String fuelAmount,
                          String refillDate, String fuelUsageText) {
        this.addedMileageText = addedMileage;
        this.currMileageText = currentMileage;
        this.fuelCostText = fuelCost;
        this.fuelAmountText = fuelAmount;
        this.refillDate = refillDate;
        this.fuelUsageText = fuelUsageText;
    }

    public HistoryUIModel(String currMileageText) {
        this.currMileageText = currMileageText;
    }

    public HistoryUIModel() {

    }


}
