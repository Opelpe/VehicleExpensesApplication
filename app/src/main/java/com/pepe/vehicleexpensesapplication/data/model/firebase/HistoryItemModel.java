package com.pepe.vehicleexpensesapplication.data.model.firebase;

import com.google.firebase.firestore.Exclude;

public class HistoryItemModel {

    public int defaultDate;
    public float fuelAmount;
    public float fuelPrice;
    public float currentMileage;
    public String refillNotes;
    public boolean fullTank;
    public long itemID;

    public HistoryItemModel(int defaultDate, float mileage, float fuelAmount, float fuelPrice, String refillNotes, boolean fullTank, long itemID) {
        this.defaultDate = defaultDate;
        this.currentMileage = mileage;
        this.fuelAmount = fuelAmount;
        this.fuelPrice = fuelPrice;
        this.refillNotes = refillNotes;
        this.fullTank = fullTank;
        this.itemID = itemID;

    }
    public HistoryItemModel(){

    }

}
