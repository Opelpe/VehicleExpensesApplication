package com.pepe.vehicleexpensesapplication.ui.feautures.account.email;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class EmailAccountPresenter implements EmailAccountContract.Presenter {

    private static final String EMAIL_PRESENTER_TAG = "EMAIL_PRESENTER_TAG";

    private EmailAccountContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    public EmailAccountPresenter(EmailAccountContract.View view, Context applicationContext) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance(applicationContext);
        sharedPrefsHelper = new SharedPrefsHelper(applicationContext);
    }

    @Override
    public void onCheckEmailButtonClicked(String enteredEmail) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        try {
            if (enteredEmail.trim().isEmpty()) {
                view.showToast("ENTER EMAIL");
            } else {
                view.showLoadingEmailDialog();
                if (enteredEmail.trim().matches(emailPattern) && enteredEmail.trim().length() > 0) {
                    firebaseHelper.fetchSignInMethodsForEmailCallback(enteredEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    boolean isRegister = task.getResult().getSignInMethods().isEmpty();
                                    if (!isRegister) {

                                        view.showDialogEmailExist(enteredEmail);
                                        Log.d(EMAIL_PRESENTER_TAG, "user existed ");
                                        logInExistedUser(enteredEmail.trim());

                                    } else {
                                        view.showNewAccountDialog();
                                        Log.d(EMAIL_PRESENTER_TAG, "user dont't exist, must create new account");
                                        loginNewUser(enteredEmail.trim());
                                    }
                                }
                            })
                            .addOnFailureListener(e -> {
                                view.cancelLoadingDialog();
                                view.showToast("ENTER CORRECT EMAIL");
                            });
                } else {

                    if (!enteredEmail.trim().matches(emailPattern) && enteredEmail.trim().length() > 0) {
                        view.cancelLoadingDialog();
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
        view.cancelLoadingDialog();
        view.cancelNewAccountDialog();
        view.cancelExistedEmailDialog();
        view.startNewUserActivity();
    }

    private void logInExistedUser(String enteredEmail) {
        sharedPrefsHelper.saveEnteredEmail(enteredEmail);

        firebaseHelper.getProviderCallback(enteredEmail).addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.w(EMAIL_PRESENTER_TAG, "Provider listen failed.", error);
            } else {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("Provider") != null) {

                        String providerName = doc.get("Provider").toString();

                        if (providerName.equals("Email & Password")) {
                            view.cancelLoadingDialog();
                            view.cancelExistedEmailDialog();
                            view.cancelNewAccountDialog();
                            view.startExistedUserActivity();
                        } else {
                            if (providerName.equals("google.com")) {
                                view.cancelLoadingDialog();
                                view.cancelExistedEmailDialog();
                                view.cancelNewAccountDialog();
                                view.startExistedGoogleActivity();
                            }
                        }
                    }
                }
            }
        });
    }
}
