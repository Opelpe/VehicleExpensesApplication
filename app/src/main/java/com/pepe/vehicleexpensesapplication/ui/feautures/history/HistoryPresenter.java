package com.pepe.vehicleexpensesapplication.ui.feautures.history;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class HistoryPresenter implements HistoryContract.Presenter {

    private static final String HISTORY_FR_PRESENTER_TAG = "HISTORY_FR_PRESENTER_TAG";

    private HistoryContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    public HistoryPresenter(HistoryContract.View view, Context historyContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance(historyContext);
        sharedPrefsHelper = new SharedPrefsHelper(historyContext);
    }

    @Override
    public boolean getSynchronizationStatus() {

        return !sharedPrefsHelper.getIsAnonymous();
    }

    @Override
    public void onViewCreated() {
        try {
            Log.w(HISTORY_FR_PRESENTER_TAG, "handle PROVIDER: START");

            view.setHistoryFragmentToolbar();

        } catch (Exception e) {
            Log.w(HISTORY_FR_PRESENTER_TAG, "handle PROVIDER EXCEPTION CAPTURED:: " + e);
        }
    }

    @Override
    public void onFloatingRefillButtonClicked() {
        view.startRefilActivity();
    }
}
