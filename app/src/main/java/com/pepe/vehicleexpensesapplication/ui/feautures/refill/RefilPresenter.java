package com.pepe.vehicleexpensesapplication.ui.feautures.refill;

import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class RefilPresenter implements RefillContract.Presenter {

    private static final String REFIL_PRESENTER_TAG = "REFIL_PRESENTER_TAG";

    private RefillContract.View view;
    private SharedPrefsHelper sharedPrefsHelper;
    private FirebaseHelper firebaseHelper;

    private Calendar calendar;

    private String dateHistoryCount;

    public RefilPresenter(RefillContract.View view, Context refilContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance(refilContext);
        sharedPrefsHelper = new SharedPrefsHelper(refilContext);
        calendar = Calendar.getInstance();
    }

    @Override
    public void onViewCreated() {

        int day = getDay();
        int month = getMonth();
        int year = getYear();

        Log.d(REFIL_PRESENTER_TAG, "on View Created, GET DATE: " + getDay() + " / " + getMonth() + " / " + getYear());

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
    public void saveRefillButtonClicked(String currMileage, String refillDate, String refilledFuel, String priceOfFuel, String refillNotes, boolean fullCapacity) {

        if (!currMileage.trim().isEmpty() && !refillDate.trim().isEmpty() && !refilledFuel.trim().isEmpty() && !priceOfFuel.trim().isEmpty()) {
            float fCurrMileage = Float.parseFloat(currMileage);
            float fRefillFuel = Float.parseFloat(refilledFuel);
            float fFuelPrice = Float.parseFloat(priceOfFuel);
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

            if (currMileage.contains("-") || refilledFuel.contains("-") || priceOfFuel.contains("-")) {
                view.showToast("MILEAGE, AMOUNT OF FUEL AND PRICE \nCANNOT BE NEGATIVE");
            } else {

                firebaseHelper.saveNewRefillTask(fCurrMileage, fRefillFuel, fFuelPrice, refillDate, fullCapacity, calendar.getTimeInMillis(), truDate, refillNotes)
                        .addOnCompleteListener(task -> {
                            view.showToast("SUCCESSFULLY SAVED");
                            view.startMyMainActivity();
                        }).addOnFailureListener(e -> {
                            view.showToast("SAVING FAILED!");
                            view.returnFromRefillActivity();
                        });
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
            if (priceOfFuel.isEmpty()) {
                view.showToast("WHAT WAS PRICE OF FUEL?");
            }
        }
    }

    @Override
    public void setDateHistoryCount(int i, int i1, int i2) {
        dateHistoryCount = String.valueOf(i) + String.valueOf(i1 + 1) + String.valueOf(i2);
    }
}
