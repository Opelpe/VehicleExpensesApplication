package com.pepe.vehicleexpensesapplication.ui.main;

public class MainFragmentPresenter implements MainFragmentContract.Presenter {

    private MainFragmentContract.View view;

    public MainFragmentPresenter(MainFragmentContract.View view) {
        this.view = view;
    }
}
