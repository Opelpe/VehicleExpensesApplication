package com.pepe.vehicleexpensesapplication.ui.feautures.account.email;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class EmailAccountPresenter implements EmailAccountContract.Presenter {

    private static final String EMAIL_PRESENTER_TAG = "EMAIL_PRESENTER_TAG";

    private EmailAccountContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    public EmailAccountPresenter(EmailAccountContract.View view, Context applicationContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
        sharedPrefsHelper = new SharedPrefsHelper(applicationContext);
    }

    @Override
    public void onCheckEmailButtonClicked(String enteredEmail) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        try {
            if (enteredEmail.trim().isEmpty()) {
                view.showToast("ENTER EMAIL");
            } else {
                if (enteredEmail.trim().matches(emailPattern) && enteredEmail.trim().length() > 0) {
                    view.showLoadingEmailDialog();
                    firebaseHelper.fetchSignInMethodsForEmail(enteredEmail)
                            .addOnCompleteListener(task -> {
                                boolean isRegister = task.getResult().getSignInMethods().isEmpty();
                                if (!isRegister) {

                                    view.showDialogEmailExist(enteredEmail);
                                    Log.d(EMAIL_PRESENTER_TAG, "user existed ");
                                    logInExistedUser(enteredEmail.trim());

                                } else {

                                    view.showCreateDialog();
                                    Log.d(EMAIL_PRESENTER_TAG, "user dont't exist, must create new account");
                                    loginNewUser(enteredEmail.trim());
                                }
                            });

                } else {
                    if (!enteredEmail.trim().matches(emailPattern) && enteredEmail.trim().length() > 0) {
                        view.showToast("ENTER CORRECT EMAIL");
                    }
                }
            }
        } catch (Exception e) {
            Log.d(EMAIL_PRESENTER_TAG, "onCheckEmailButtonClicked() EXCEPTION HAS BEEN CAPTURED: \n" + e);
        }
    }

    private void loginNewUser(String enteredEmail) {
        sharedPrefsHelper.saveEnteredEmail(enteredEmail);
        view.startNewUserActivity();
    }

    private void logInExistedUser(String enteredEmail) {
        sharedPrefsHelper.saveEnteredEmail(enteredEmail);

        firebaseHelper.getProviderCallback(enteredEmail).addOnCompleteListener(task -> {
            DocumentSnapshot doc = task.getResult();
            String provider = doc.get("Provider").toString();

            if (provider.equals("Email & Password")) {
                view.startExistedUserActivity();
            } else {
                if (provider.equals("google.com")) {
                    view.startExistedGoogleActivity();
                }
            }
        });
    }
}
