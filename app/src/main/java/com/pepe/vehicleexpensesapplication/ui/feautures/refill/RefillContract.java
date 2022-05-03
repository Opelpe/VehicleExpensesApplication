package com.pepe.vehicleexpensesapplication.ui.feautures.refill;

public interface RefillContract {
    interface  View{

        void returnFromRefillActivity();

        void makeDateDialog();

        void setCurrentDateEditText(int day, int month, int year);

        void showToast(String toastMsg);

        void startMyMainActivity();
    }

    interface Presenter{

        void onViewCreated();

        void backFromRefillActivity();

        void refillDateClicked();


        int getDay();

        int getMonth();

        int getYear();

        void saveRefillButtonClicked(String currMileage, String refillDate, String refilledFuel, String priceOfFuel, String refillNotes, boolean fullCapacity);

        void setDateHistoryCount(int i, int i1, int date);
    }
}
