package com.pepe.vehicleexpensesapplication.data.model;

import com.google.firebase.firestore.Exclude;

public class HistoryItemModel {

    public String REFILL_DATE;
    public float FUEL_AMOUNT;
    public float FUEL_PRICE;
    public String REFILL_NOTES;
    public boolean FULL_TANK;
    public float CURRENT_MILEAGE;

    @Exclude
    public long ITEM_ID;

    @Exclude
    public String ADDED_MILEAGE_TEXT;
    @Exclude
    public String FUEL_USAGE_TEXT;
    @Exclude
    public String CURR_MILEAGE_TEXT;
    @Exclude
    public String FUEL_COST_TEXT;
    @Exclude
    public String FUEL_AMOUNT_TEXT;

    public HistoryItemModel(String refillDate, float mileage, float fuelAmount, float fuelPrice, String refillNotes, boolean fullTank, long itemID) {
        this.REFILL_DATE = refillDate;
        this.CURRENT_MILEAGE = mileage;
        this.FUEL_AMOUNT = fuelAmount;
        this.FUEL_PRICE = fuelPrice;
        this.REFILL_NOTES = refillNotes;
        this.FULL_TANK = fullTank;
        this.ITEM_ID = itemID;

    }
    public HistoryItemModel(){

    }

}
