package com.pepe.vehicleexpensesapplication.ui.feautures.history;

public class HistoryPresenter implements HistoryContract.Presenter {

    private HistoryContract.View view;

    public HistoryPresenter(HistoryContract.View view) {
        this.view = view;
    }

}
