package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withgoogle;

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

public class ExistedGooglePresenter implements ExistedGoogleContract.Presenter{

    private static final String EXISTED_GOOGLE_PRESENTER_TAG = "EXISTED_GOOGLE_PRESENTER";

    private ExistedGoogleContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;

    public ExistedGooglePresenter(ExistedGoogleContract.View view, Context context){
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(context);
        firebaseHelper = FirebaseHelper.getInstance();

    }

    @Override
    public void onViewCreated() {
        view.setGoogleEmailInfo(sharedPrefsHelper.getEnteredEmail());
    }

    @Override
    public void onlogInWithGoogleButtonClicked(GoogleSignInClient mGoogleSignInClient, GoogleSignInOptions gso) {
        googleSignInClient = mGoogleSignInClient;
        googleSignInOptions = gso;

        signIn();
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        Log.d(EXISTED_GOOGLE_PRESENTER_TAG, "\n on handleSignInResult START");
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(EXISTED_GOOGLE_PRESENTER_TAG, "\n on handleSignInResult cmpleted TASK: " + account.getId());

            firebaseHelper.loginWithGoogle(account.getIdToken());

            if (account.getEmail().equals(sharedPrefsHelper.getEnteredEmail())){

                sharedPrefsHelper.saveGoogleSignInCompleted(task.isSuccessful());
                sharedPrefsHelper.saveEnteredEmail(account.getEmail());

                if (sharedPrefsHelper.getGoogleSignInCompleted()) {
                    view.startMyMainActivity();
                }
            }else {
                Log.w(EXISTED_GOOGLE_PRESENTER_TAG, "something wrong i nhandle result");
            }
        } catch (ApiException e) {
            Log.w(EXISTED_GOOGLE_PRESENTER_TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        view.presenterStartActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
