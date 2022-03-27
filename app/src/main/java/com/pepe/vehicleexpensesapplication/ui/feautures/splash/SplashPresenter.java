package com.pepe.vehicleexpensesapplication.ui.feautures.splash;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;

import java.net.InetAddress;

public class SplashPresenter implements SplashContract.Presenter {

    private static final String MY_SPLASH_TAG = "MY_SPLASH_TAG";

    private SplashContract.View view;

    private FirebaseHelper firebaseHelper;

    private Handler handler = new Handler();

    SharedPrefsHelper sharedPrefsHelper;

    private Context splashContext;

    public SplashPresenter(SplashContract.View view, Context context) {
        this.view = view;

        firebaseHelper = FirebaseHelper.getInstance(context);
        sharedPrefsHelper = new SharedPrefsHelper(context);
        splashContext = context;

    }

    @Override
    public void onViewCreated() {
        Log.d(MY_SPLASH_TAG, "View created");

        handler.postDelayed(() -> {

//IF FIRST TIME AND NO INTERNET CONNECTION
            if (!sharedPrefsHelper.isNetworkAvailable(splashContext) && !sharedPrefsHelper.isInternetAvailable() && sharedPrefsHelper.getSignedUserID() == null) {
                view.showNoInternetDialog();
            } else {

//        IF USER IS ANONYMOUS:
                if (sharedPrefsHelper.getIsAnonymous() && sharedPrefsHelper.getCheckboxStatus()) {
                    Log.d(MY_SPLASH_TAG, "Logged in as guest: " + sharedPrefsHelper.getSignedUserID() + "\n Checkbox status: "
                            + sharedPrefsHelper.getCheckboxStatus());

                    handleStartMyMainActivity();
                } else {
                    if (sharedPrefsHelper.getIsAnonymous() && !sharedPrefsHelper.getCheckboxStatus()) {
                        Log.d(MY_SPLASH_TAG, "Logged in as guest: " + sharedPrefsHelper.getSignedUserID() + "\n Checkbox status: "
                                + sharedPrefsHelper.getCheckboxStatus());

                        handleStartLoginActivity();
                    }
//          IF USER IS LOGGED VIA GOOGLE:
                    else {
                        if (sharedPrefsHelper.getSignWGoogleEmail() != null && sharedPrefsHelper.getCheckboxStatus()) {
                            Log.d(MY_SPLASH_TAG, "Logged in with GOOGLE: " + sharedPrefsHelper.getSignedUserID()
                                    + "\n Checkbox status: " + sharedPrefsHelper.getCheckboxStatus() + "\n User Email: " + sharedPrefsHelper.getSignWGoogleEmail());

                            handleStartMyMainActivity();
                        } else {
                            if (sharedPrefsHelper.getSignWGoogleEmail() != null && !sharedPrefsHelper.getCheckboxStatus()) {
                                Log.d(MY_SPLASH_TAG, "Logged in with GOOGLE: " + sharedPrefsHelper.getSignedUserID()
                                        + "\n Checkbox status: " + sharedPrefsHelper.getCheckboxStatus() + "\n User Email: " + sharedPrefsHelper.getSignWGoogleEmail());

                                handleStartLoginActivity();
                            }
//                  IF USER IS SIGNED VIA EMAIL:
                            else {
                                if (sharedPrefsHelper.getSignWEEmailEmail() != null && sharedPrefsHelper.getCheckboxStatus()) {
                                    Log.d(MY_SPLASH_TAG, "Logged in with EMAIL: " + sharedPrefsHelper.getSignedUserID() + "\n Checkbox status: "
                                            + sharedPrefsHelper.getCheckboxStatus() + "\n User Email: " + sharedPrefsHelper.getSignWGoogleEmail());

                                    handleStartMyMainActivity();
                                } else {
                                    if (sharedPrefsHelper.getSignWEEmailEmail() != null && !sharedPrefsHelper.getCheckboxStatus()) {
                                        Log.d(MY_SPLASH_TAG, "Logged in with EMAIL: " + sharedPrefsHelper.getSignedUserID() + "\n Checkbox status: "
                                                + sharedPrefsHelper.getCheckboxStatus() + "\n User Email: " + sharedPrefsHelper.getSignWGoogleEmail());

                                        handleStartLoginActivity();
                                    }
//                          IF NOTHING START LOGIN ACTIVITY:
                                    else {
                                        handleStartLoginActivity();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 250);
    }

    private void handleStartMyMainActivity() {

        Log.d(MY_SPLASH_TAG, "Handler Main start");
        handler.postDelayed(() -> view.startMyMainActivity(), 250);
    }

    private void handleStartLoginActivity() {

        Log.d(MY_SPLASH_TAG, "Handler Login start");
        handler.postDelayed(() -> view.startLoginActivity(), 250);
    }
}
