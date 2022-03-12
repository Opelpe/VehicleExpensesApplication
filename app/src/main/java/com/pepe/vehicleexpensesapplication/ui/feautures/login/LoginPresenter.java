package com.pepe.vehicleexpensesapplication.ui.feautures.login;

import android.content.Context;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class LoginPresenter implements LoginContract.Presenter {

    private static final String LOGIN_PRESENTER_TAG = "LOGIN_PRESENTER_TAG";

    private LoginContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    public LoginPresenter(LoginContract.View view, Context loginContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
        sharedPrefsHelper = new SharedPrefsHelper(loginContext);
    }

    @Override
    public void onLocalLoginButtonClicked() {

        firebaseHelper.loginAnonymously();
        try {
            firebaseHelper.getCurrentUser().getUid();

            sharedPrefsHelper.saveSignedUserEmail(firebaseHelper.getCurrentUser().getUid());
        } catch (Exception e) {
            Log.d(LOGIN_PRESENTER_TAG, "\n saveSignedUserEmail EXCEPTION CAPTURED: " + e);
        }

        view.startMainActivity();
    }

    @Override
    public void onViewCreated() {
        Log.d(LOGIN_PRESENTER_TAG, "\n View has been created");
    }

    @Override
    public void onAccounButtonClicked() {
        view.startNewAccountActivity();
    }
}
