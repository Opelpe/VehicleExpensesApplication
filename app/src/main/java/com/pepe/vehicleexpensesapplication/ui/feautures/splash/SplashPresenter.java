package com.pepe.vehicleexpensesapplication.ui.feautures.splash;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;

public class SplashPresenter implements SplashContract.Presenter {

    private static final String MY_SPLASH_TAG = "MY_SPLASH_TAG";

    private SplashContract.View view;

    private FirebaseHelper firebaseHelper;

    private Handler handler = new Handler();

    SharedPrefsHelper sharedPrefsHelper;

    public SplashPresenter(SplashContract.View view, Context context) {
        this.view = view;

        firebaseHelper = FirebaseHelper.getInstance();
        sharedPrefsHelper = new SharedPrefsHelper(context);

    }

    @Override
    public void onViewCreated() {
        Log.d(MY_SPLASH_TAG, "View created");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(MY_SPLASH_TAG, "Handler start");

                if (firebaseHelper.getCurrentUser() != null && sharedPrefsHelper.getCheckboxStatus() && firebaseHelper.getCurrentUser().isAnonymous()) {
                    Log.d(MY_SPLASH_TAG, "Logged in as guest: " + firebaseHelper.getCurrentUser().getUid() + "\n Checkbox status: " + sharedPrefsHelper.getCheckboxStatus());
                    view.startMainActivity();

                } else {
                    if (firebaseHelper.getCurrentUser() != null && sharedPrefsHelper.getCheckboxStatus() && !firebaseHelper.getCurrentUser().isAnonymous()) {
                        Log.d(MY_SPLASH_TAG, "Logged in as: " + firebaseHelper.getCurrentUser().getEmail() + "\n Checkbox status: " + sharedPrefsHelper.getCheckboxStatus());
                        view.startMainActivity();
                    } else {
                        if (firebaseHelper.getCurrentUser() != null && !sharedPrefsHelper.getCheckboxStatus() && firebaseHelper.getCurrentUser().isAnonymous()) {
                            Log.d(MY_SPLASH_TAG, "Logged in as guest: " + firebaseHelper.getCurrentUser().getUid() + "\n Checkbox status: " + sharedPrefsHelper.getCheckboxStatus());
                            view.startLoginActivity();
                        } else {
                            if (firebaseHelper.getCurrentUser() != null && !sharedPrefsHelper.getCheckboxStatus() && !firebaseHelper.getCurrentUser().isAnonymous()) {
                                Log.d(MY_SPLASH_TAG, "Logged in as: " + firebaseHelper.getCurrentUser().getEmail() + "\n Checkbox status: " + sharedPrefsHelper.getCheckboxStatus());
                                view.startLoginActivity();
                            } else {
                                if (firebaseHelper.getCurrentUser() == null) {
                                    Log.d(MY_SPLASH_TAG, "Must log in, user: " + firebaseHelper.getCurrentUser() + "\n Checkbox status: " + sharedPrefsHelper.getCheckboxStatus());
                                    view.startLoginActivity();
                                }

                            }
                        }
                    }
                }
            }
        }, 500);
    }
}
