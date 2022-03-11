package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class NewAccountPresenter implements NewAccountContract.Presenter {

    private static final String NACC_PRESENTER_TAG = "NACC_PRESENTER_TAG";

    private NewAccountContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;

    private FirebaseHelper firebaseHelper;

    public NewAccountPresenter(NewAccountContract.View view, Context applicationContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
        sharedPrefsHelper = new SharedPrefsHelper(applicationContext);
    }

    @Override
    public void onGoogleButtonClicked(GoogleSignInClient mGoogleSignInClient, GoogleSignInOptions gso) {

        googleSignInClient = mGoogleSignInClient;
        googleSignInOptions = gso;

        signIn();
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        view.presenterStartActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onEmailSignButtonClicked() {
        view.startEmailAccountActivity();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        Log.d(NACC_PRESENTER_TAG, "\n on handleSignInResult START");
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(NACC_PRESENTER_TAG, "\n on handleSignInResult cmpleted TASK: " + account.getId());

            firebaseHelper.loginWithGoogle(account.getIdToken());
            sharedPrefsHelper.saveGoogleSignInCompleted(task.isSuccessful());
            sharedPrefsHelper.saveEnteredEmail(account.getEmail());

            if (sharedPrefsHelper.getGoogleSignInCompleted()) {
                view.startMyMainActivity();
            }
        } catch (ApiException e) {
            Log.w(NACC_PRESENTER_TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
