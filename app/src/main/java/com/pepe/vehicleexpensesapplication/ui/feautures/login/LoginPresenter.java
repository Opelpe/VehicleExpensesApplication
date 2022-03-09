package com.pepe.vehicleexpensesapplication.ui.feautures.login;

import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;

public class LoginPresenter implements LoginContract.Presenter{

    private static final String LOGIN_PRESENTER_TAG = "LOGIN_PRESENTER_TAG";

    private LoginContract.View view;

    private FirebaseHelper firebaseHelper;

    public LoginPresenter(LoginContract.View view){
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
    }

    @Override
    public void onLocalLoginButtonClicked() {

        firebaseHelper.loginAnonymously();

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
