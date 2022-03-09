package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;

public class NewAccountPresenter implements NewAccountContract.Presenter{

    private static final String NACC_PRESENTER_TAG = "NACC_PRESENTER_TAG";

    private NewAccountContract.View view;
//
//    private static final int RC_SIGN_IN = 100;
//    private GoogleSignInClient googleSignInClient;
//    private GoogleSignInOptions googleSignInOptions;

    private FirebaseHelper firebaseHelper;

    public NewAccountPresenter (NewAccountContract.View view){
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
    }

    @Override
    public void onGoogleButtonClicked() {

    }

    @Override
    public void onEmailSignButtonClicked() {
        view.startEmailAccountActivity();
    }
}
