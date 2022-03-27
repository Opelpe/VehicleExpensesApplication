package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.content.Context;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class MainFragmentPresenter implements MainFragmentContract.Presenter {

    private static final String MAIN_FR_PRESENTER_TAG = "MAIN_FR_PRESENTER_TAG";

    private MainFragmentContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    public MainFragmentPresenter(MainFragmentContract.View view, Context mainContext) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(mainContext);
        firebaseHelper = FirebaseHelper.getInstance(mainContext);
    }

    @Override
    public boolean getSynchronizationStatus() {
        if (sharedPrefsHelper.getIsAnonymous()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onViewCreated() {
        try {
            view.setMainFragmentToolbar();

        } catch (Exception e) {
            Log.w(MAIN_FR_PRESENTER_TAG, "handle PROVIDER EXCEPTION CAPTURED:: " + e);
        }
    }

    @Override
    public void onRefillButtonClicked() {
        view.startRefillActivity();
    }

}
