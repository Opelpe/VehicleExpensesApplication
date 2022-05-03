package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import android.content.Context;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class HistoryPresenter implements HistoryContract.Presenter {

    private static final String HISTORY_FR_PRESENTER_TAG = "HISTORY_FR_PRESENTER_TAG";

    private HistoryContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    public HistoryPresenter(HistoryContract.View view, Context historyContext) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(historyContext);
    }

    @Override
    public void onViewCreated() {
        try {
            view.setHistoryFragmentToolbar();
        } catch (Exception e) {
            Log.w(HISTORY_FR_PRESENTER_TAG, "on View Created EXCEPTION CAPTURED: " + e);
        }
    }

    @Override
    public void onFloatingRefillButtonClicked() {
        view.startRefillActivity();
    }

    @Override
    public boolean checkIsAnonymous() {
        return sharedPrefsHelper.getIsAnonymous();
    }
}

