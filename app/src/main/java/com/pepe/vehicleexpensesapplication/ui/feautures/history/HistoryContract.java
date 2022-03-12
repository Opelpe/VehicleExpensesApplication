package com.pepe.vehicleexpensesapplication.ui.feautures.history;

public interface HistoryContract {

    interface View{

        void setSynchornizationImageViewOff();

        void setSynchornizationImageViewOn();
    }

    interface  Presenter{
        void onViewCreated();
    }
}
