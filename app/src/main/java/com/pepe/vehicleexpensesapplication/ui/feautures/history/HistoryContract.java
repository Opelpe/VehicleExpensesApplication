package com.pepe.vehicleexpensesapplication.ui.feautures.history;

public interface HistoryContract {

    interface View{


        void startRefilActivity();

        void setHistoryFragmentToolbar();
    }

    interface  Presenter{
        void onViewCreated();

        void onFloatingRefillButtonClicked();

        boolean getSynchronizationStatus();
    }
}
