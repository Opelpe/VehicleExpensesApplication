package com.pepe.vehicleexpensesapplication.ui.feautures.login;

import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class LoginPresenter implements LoginContract.Presenter{

    private static final String LOGIN_PRESENTER_TAG = "LOGIN_PRESENTER_TAG";

    private LoginContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper.FirebaseAnonymousListener firebaseAnonymousListener =
            new FirebaseHelper.FirebaseAnonymousListener() {

                @Override
                public void userData(String userID, String userEmail, String password, String name, String provider, boolean isAnonymous) {
                    sharedPrefsHelper.saveIsAnonymous(isAnonymous);
                    sharedPrefsHelper.saveSignedUserID(userID);
                    view.cancelLoadingDialog();
                    view.startMyMainActivity();

                }
            };

    public LoginPresenter(LoginContract.View view, SharedPrefsHelper prefsHelper){
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
        sharedPrefsHelper = prefsHelper;
    }

    @Override
    public void onLocalLoginButtonClicked() {

        firebaseHelper.setFirebaseAnonymousListener(firebaseAnonymousListener);
        firebaseHelper.signInAnonymously();
        view.showLoadingDialog();


//        if (!sharedPrefsHelper.getIsAnonymous()){
//
//            firebaseHelper.loginAnonymously();
//            sharedPrefsHelper.saveIsAnonymous(true);
//            view.startMyMainActivity();
//        }else{
//            sharedPrefsHelper.saveIsAnonymous(true);
//            sharedPrefsHelper.saveSignWEmailEmail(null);
//            sharedPrefsHelper.saveSignWGoogleEmail(null);
//            view.startMyMainActivity();
//        }
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
