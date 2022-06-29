package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.model.firebase.NewUserModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class NewAccountPresenter implements NewAccountContract.Presenter {

    private static final String NACC_PRESENTER_TAG = "NACC_PRESENTER_TAG";

    private NewAccountContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private GoogleSignInClient googleSignInClient;

    private FirebaseHelper firebaseHelper;

    private FirebaseHelper.FirebaseUserListener usersListener = new FirebaseHelper.FirebaseUserListener() {
        @Override
        public void usersData(NewUserModel userData) {
            view.cancelLoadingDialog();
            view.showToast("SUCCESSFULLY LOGGED IN \n" + userData.userEmail);
            view.startMyMainActivity();
        }

        @Override
        public void dataFailure(boolean failure) {

        }
    };


    public NewAccountPresenter(NewAccountContract.View view, SharedPrefsHelper prefsHelper) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
        sharedPrefsHelper = prefsHelper;
    }

    @Override
    public void onGoogleButtonClicked(GoogleSignInClient mGoogleSignInClient) {
        googleSignInClient = mGoogleSignInClient;

        signIn();
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        view.presenterStartActivityForResult(signInIntent, ConstantsPreferences.RC_SIGN_IN);
    }

    @Override
    public void onEmailSignButtonClicked() {
        view.startEmailAccountActivity();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        Log.d(NACC_PRESENTER_TAG, "\n on handleSignInResult START");
        try {
            view.showLoadingGoogleDialog();
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(NACC_PRESENTER_TAG, "\n on handleSignInResult completed TASK: " + account.getId() + " \n" + account.getEmail());

            String googleEmail = account.getEmail();

            AuthCredential credential = firebaseHelper.getAuthCredential(account.getIdToken());

            firebaseHelper.setFirebaseUsersListener(usersListener);
            firebaseHelper.loginWithGoogleCallback(credential);

        } catch (Exception e) {
            Log.w(NACC_PRESENTER_TAG, "EXCEPTION CAPTURED signInResult: failed, code = " + e);
            sharedPrefsHelper.saveSignWGoogleEmail(null);
            sharedPrefsHelper.saveSignedUserID(null);
            sharedPrefsHelper.saveGoogleSignInCompleted(false);
        }
    }
}
