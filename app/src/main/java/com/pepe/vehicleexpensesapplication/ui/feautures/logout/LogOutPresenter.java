package com.pepe.vehicleexpensesapplication.ui.feautures.logout;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class LogOutPresenter implements LogOutContract.Presenter {

    private final static String LOG_OUT_PRESENTER_TAG = "LOG_OUT_PRESENTER_TAG";

    private LogOutContract.View view;

    private FirebaseHelper firebaseHelper;
    private SharedPrefsHelper sharedPrefsHelper;

    public LogOutPresenter(LogOutContract.View view, Context context) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance(context);
        sharedPrefsHelper = new SharedPrefsHelper(context);
    }

    @Override
    public void onLogOutButtonClicked(GoogleSignInClient googleSignInClient) {
        firebaseHelper.logOutCurrentUser();
        googleSignInClient.signOut().addOnCompleteListener(task -> {

            sharedPrefsHelper.saveSignWGoogleEmail(null);
            sharedPrefsHelper.saveSignWEmailEmail(null);
            sharedPrefsHelper.saveSignedUserID(null);
            sharedPrefsHelper.saveGoogleSignInCompleted(false);
            sharedPrefsHelper.saveSignedUserEmail(null);
            checkLogOutStatus();
        });
    }

    private void checkLogOutStatus() {
        if (sharedPrefsHelper.getSignWGoogleEmail() == null && sharedPrefsHelper.getSignWEEmailEmail() == null && sharedPrefsHelper.getSignedUserID() == null) {
            view.startSplashActivity();
        }
    }

    @Override
    public void onWipeDataButtonClicked(GoogleSignInClient googleSignInClient) {

        String userId = sharedPrefsHelper.getSignedUserID();

        firebaseHelper.deleteUserDataCallback(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseHelper.deleteFirebaseUserCallback().addOnCompleteListener(task1 ->
                        googleSignInClient.signOut().addOnCompleteListener(task2 -> {

                            sharedPrefsHelper.saveSignWGoogleEmail(null);
                            sharedPrefsHelper.saveSignWEmailEmail(null);
                            sharedPrefsHelper.saveSignedUserID(null);
                            sharedPrefsHelper.saveGoogleSignInCompleted(false);
                            sharedPrefsHelper.saveSignedUserEmail(null);

                            checkLogOutStatus();
                        }));
            }
        });

    }
}
