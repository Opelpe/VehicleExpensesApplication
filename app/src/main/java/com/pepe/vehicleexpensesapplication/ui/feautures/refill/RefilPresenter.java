package com.pepe.vehicleexpensesapplication.ui.feautures.refill;

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

    private final FirebaseHelper.FirebaseSuccessListener firebaseSuccessListener = success -> {
        if (success) {
            view.showToast("SUCCESSFULLY SAVED");
            view.startMyMainActivity();
        } else {
            view.showToast("SAVING FAILED!");
            view.returnFromRefillActivity();
        }
    };

    public RefilPresenter(RefillContract.View view, SharedPrefsHelper sharedPrefsHelper) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
        this.sharedPrefsHelper = sharedPrefsHelper;
        calendar = Calendar.getInstance();
    }

    @Override
    public void onViewCreated() {

        sharedPrefsHelper.saveSelectedDate(0);
        view.setDateEditText(setCurrentDate());
        firebaseHelper.setFirebaseSuccessListener(firebaseSuccessListener);
    }

    private String setCurrentDate() {
        int day = getDay();
        int month = getMonth();
        int year = getYear();

        Log.d(REFILL_PRESENTER_TAG, "on View Created, GET DATE: " + getDay() + " / " + getMonth() + " / " + getYear());
        StringBuilder stringBuilder = new StringBuilder();
        if (getDay() != 0 && getMonth() != 0 && getYear() != 0) {

            if (day < 10 && month < 10) {
                stringBuilder.append(0).append(day).append("/").append(0).append(month).append("/").append(year);
                return stringBuilder.toString();

            } else {
                if (month < 10) {
                    stringBuilder.append(day).append("/").append(0).append(month).append("/").append(year);
                    return stringBuilder.toString();
                }
                    if (day < 10) {
                        stringBuilder.append(0).append(day).append("/").append(month).append("/").append(year);
                        return stringBuilder.toString();
                    }
            }
        }
            stringBuilder.append(0).append(0).append("/").append(0).append(0).append("/").append(year);
            return stringBuilder.toString();

    }

    private int getDefaultDate(){
        int day = getDay();
        int month = getMonth();
        int year = getYear();

        Log.d(REFILL_PRESENTER_TAG, "on View Created, GET DATE: " + getDay() + " / " + getMonth() + " / " + getYear());
        StringBuilder stringBuilder = new StringBuilder();
        if (getDay() != 0 && getMonth() != 0 && getYear() != 0) {

            if (day < 10 && month < 10) {
                stringBuilder.append(0).append(day).append(0).append(month).append(year);
                return Integer.parseInt(stringBuilder.toString());

            } else {
                if (month < 10) {
                    stringBuilder.append(day).append(0).append(month).append(year);
                    return Integer.parseInt(stringBuilder.toString());
                }
                if (day < 10) {
                    stringBuilder.append(0).append(day).append(month).append(year);
                    return Integer.parseInt(stringBuilder.toString());
                }
            }
        }
        stringBuilder.append(0).append(0).append(0).append(0).append(0).append(0).append(0).append(0);
        return Integer.parseInt(stringBuilder.toString());
    }

    @Override
    public void setSelectedDate(int day, int month, int year) {

        StringBuilder formattedDateBuilder = new StringBuilder();
        StringBuilder defaultDateBuilder = new StringBuilder();
        if (day < 10 && month < 10) {
            formattedDateBuilder.append(0).append(day).append("/").append(0).append(month).append("/").append(year);
            defaultDateBuilder.append(0).append(day).append(0).append(month).append(year);
            view.setDateEditText(formattedDateBuilder.toString());
            sharedPrefsHelper.saveSelectedDate(Integer.parseInt(defaultDateBuilder.toString()));

        } else {
            if (month < 10) {
                formattedDateBuilder.append(day).append("/").append(0).append(month).append("/").append(year);
                defaultDateBuilder.append(day).append(0).append(month).append(year);
                view.setDateEditText(formattedDateBuilder.toString());
                sharedPrefsHelper.saveSelectedDate(Integer.parseInt(defaultDateBuilder.toString()));
            }
            if (day < 10) {
                formattedDateBuilder.append(0).append(day).append("/").append(month).append("/").append(year);
                defaultDateBuilder.append(0).append(day).append(month).append(year);
                view.setDateEditText(formattedDateBuilder.toString());
                sharedPrefsHelper.saveSelectedDate(Integer.parseInt(defaultDateBuilder.toString()));
            }
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


        Log.d(REFILL_PRESENTER_TAG, "refilll date in  long: " + refillDate.replaceAll("/", ""));

        calendar.getTimeInMillis();

        if (!currMileage.trim().isEmpty() && !refilledFuel.trim().isEmpty() && !pricePerLiter.trim().isEmpty()) {
            float fCurrMileage = Float.parseFloat(currMileage);
            float fFuelAmount = Float.parseFloat(refilledFuel);
            float fFuelPrice = Float.parseFloat(pricePerLiter);

            if (currMileage.contains("-") || refilledFuel.contains("-") || pricePerLiter.contains("-")) {
                view.showToast("MILEAGE, AMOUNT OF FUEL AND PRICE \nCANNOT BE NEGATIVE");
            } else {

                if (sharedPrefsHelper.getSelectedDate() > 0) {
                    firebaseHelper.saveNewRefill(fCurrMileage, fFuelAmount, fFuelPrice, sharedPrefsHelper.getSelectedDate(), fullCapacity, calendar.getTimeInMillis(), refillNotes);
                } else {
                    firebaseHelper.saveNewRefill(fCurrMileage, fFuelAmount, fFuelPrice, getDefaultDate(), fullCapacity, calendar.getTimeInMillis(), refillNotes);
                }
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

}
