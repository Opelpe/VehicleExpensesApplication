package com.pepe.vehicleexpensesapplication.ui.feautures.activity;

public class MyMainPresenter implements MyMainContract.Presenter{
    private static final String MAIN_PRESENTER_TAG = "MAIN_PRESENTER_TAG";

    private MyMainContract.View view;


    public MyMainPresenter(MyMainContract.View view){
        this.view = view;
    }

    @Override
    public void onViewCreated() {

    }
}
