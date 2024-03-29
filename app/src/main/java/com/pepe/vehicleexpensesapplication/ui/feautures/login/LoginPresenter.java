package com.pepe.vehicleexpensesapplication.ui.feautures.login;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class LoginPresenter implements LoginContract.Presenter{

    private static final String LOGIN_PRESENTER_TAG = "LOGIN_PRESENTER_TAG";

    private LoginContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    public LoginPresenter(LoginContract.View view, Context loginContext){
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance(loginContext);
        sharedPrefsHelper = new SharedPrefsHelper(loginContext);
    }

    @Override
    public void onLocalLoginButtonClicked() {

        if (!sharedPrefsHelper.getIsAnonymous()){

            firebaseHelper.loginAnonymously();
            sharedPrefsHelper.saveIsAnonymous(true);
            view.startMyMainActivity();
        }else{
            sharedPrefsHelper.saveIsAnonymous(true);
            sharedPrefsHelper.saveSignWEmailEmail(null);
            sharedPrefsHelper.saveSignWGoogleEmail(null);
            view.startMyMainActivity();
        }
    }

    @Override
    public void onViewCreated() {
        Log.d(LOGIN_PRESENTER_TAG, "\n View has been created");
    }

    @Override
    public void isCheckboxChecked(boolean checked) {
        sharedPrefsHelper.saveCheckboxStatus(checked);
    }

    @Override
    public void onAccountButtonClicked() {
        view.startNewAccountActivity();
    }
}
