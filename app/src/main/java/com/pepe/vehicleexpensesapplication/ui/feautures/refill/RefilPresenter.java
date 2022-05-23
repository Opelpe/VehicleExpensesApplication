package com.pepe.vehicleexpensesapplication.ui.feautures.refill;

import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class RefilPresenter implements RefillContract.Presenter {

    private static final String REFILL_PRESENTER_TAG = "REFILL_PRESENTER_TAG";

    private RefillContract.View view;
    private SharedPrefsHelper sharedPrefsHelper;
    private FirebaseHelper firebaseHelper;

    private Calendar calendar;

    private String dateHistoryCount;

    private final FirebaseHelper.FirebaseSuccessListener firebaseSuccessListener = success -> {
        if (success){
            view.showToast("SUCCESSFULLY SAVED");
            view.startMyMainActivity();
        }else {
            view.showToast("SAVING FAILED!");
            view.returnFromRefillActivity();
        }
    };

    public RefilPresenter(RefillContract.View view, Context refillContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance(refillContext);
        sharedPrefsHelper = new SharedPrefsHelper(refillContext);
        calendar = Calendar.getInstance();
    }

    @Override
    public void onViewCreated() {

        setCurrentDate();
        firebaseHelper.setFirebaseSuccessListener(firebaseSuccessListener);
    }

    private void setCurrentDate() {
        int day = getDay();
        int month = getMonth();
        int year = getYear();

        Log.d(REFILL_PRESENTER_TAG, "on View Created, GET DATE: " + getDay() + " / " + getMonth() + " / " + getYear());

        if (getDay() != 0 && getMonth() != 0 && getYear() != 0) {
            view.setCurrentDateEditText(day, month, year);
        }
    }

    @Override
    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    @Override
    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    @Override
    public void refillDateClicked() {
        view.makeDateDialog();
    }

    @Override
    public void backFromRefillActivity() {
        view.returnFromRefillActivity();
    }

    @Override
    public void saveRefillButtonClicked(String currMileage, String refillDate, String refilledFuel, String pricePerLiter, String refillNotes, boolean fullCapacity) {

        if (!currMileage.trim().isEmpty() && !refillDate.trim().isEmpty() && !refilledFuel.trim().isEmpty() && !pricePerLiter.trim().isEmpty()) {
            float fCurrMileage = Float.parseFloat(currMileage);
            float fRefillFuel = Float.parseFloat(refilledFuel);
            float fFuelPrice = Float.parseFloat(pricePerLiter);
            String truDate;
            if (dateHistoryCount == null) {
                truDate = getYear() + String.valueOf(getMonth()) + getDay();
            } else {
                if (dateHistoryCount.isEmpty()) {
                    truDate = getYear() + String.valueOf(getMonth()) + getDay();
                } else {
                    truDate = dateHistoryCount;
                }
            }

            if (currMileage.contains("-") || refilledFuel.contains("-") || pricePerLiter.contains("-")) {
                view.showToast("MILEAGE, AMOUNT OF FUEL AND PRICE \nCANNOT BE NEGATIVE");
            } else {

                firebaseHelper.saveNewRefill(fCurrMileage, fRefillFuel, fFuelPrice, refillDate, fullCapacity, calendar.getTimeInMillis(), truDate, refillNotes);
            }
        } else {
            if (currMileage.isEmpty()) {
                view.showToast("HOW BIG IS \\n CURRENT MILEAGE OF YOUR CAR?");
            }
            if (refillDate.isEmpty()) {
                view.showToast("ENTER CORRECT DATE");
            }
            if (refilledFuel.isEmpty()) {
                view.showToast("HOW MANY LITTERS HAVE YOU REFILL?");
            }
            if (pricePerLiter.isEmpty()) {
                view.showToast("WHAT WAS PRICE OF FUEL?");
            }
        }
    }

    @Override
    public void setDateHistoryCount(int i, int i1, int i2) {
        dateHistoryCount = String.valueOf(i) + String.valueOf(i1 + 1) + String.valueOf(i2);
    }
}
