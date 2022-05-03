package com.pepe.vehicleexpensesapplication.ui.feautures.activity;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class MyMainPresenter implements MyMainContract.Presenter {
    private static final String MAIN_PRESENTER_TAG = "MAIN_PRESENTER_TAG";

    private MyMainContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    public MyMainPresenter(MyMainContract.View view, Context context) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance(context);
        sharedPrefsHelper = new SharedPrefsHelper(context);
    }

    @Override
    public void onViewCreated() {

        try {
            setRefillHistorySize();

        } catch (Exception e) {
            Log.w(MAIN_PRESENTER_TAG, "refill query EXCEPTION (try/catch): " + e);
            sharedPrefsHelper.saveHistorySize(0);
        }

    }

    private void setRefillHistorySize() {
        firebaseHelper.getUid();
        firebaseHelper.getRefillsListCollection().addSnapshotListener((value, error) -> {
            value.getDocuments().size();
            Log.w(MAIN_PRESENTER_TAG, "refill query EVENT LISTENER: " +    value.getDocuments().size());
            sharedPrefsHelper.saveHistorySize(value.getDocuments().size());
        });

    }
}
