package com.pepe.vehicleexpensesapplication.ui.feautures.activity;

import android.content.Context;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.model.HistoryItemModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

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



    }

}
