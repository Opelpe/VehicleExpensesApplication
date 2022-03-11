package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.usernew;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class NewUserPresenter implements NewUserContract.Presenter {

    private static final String NEW_USER_PRESENTER_TAG = " NEW_USER_PRESENTER_TAG";

    private NewUserContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;

    public NewUserPresenter(NewUserContract.View view, Context applicationContext) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(applicationContext);
        firebaseHelper = FirebaseHelper.getInstance();
    }


    @Override
    public void setEmailEditText() {
        String email = sharedPrefsHelper.getEnteredEmail();
        view.setEmailEditText(email);
    }

    @Override
    public void saveNewUserButtonClicked(String enteredEmail, String enteredName, String enteredPassword) {
        Log.d(NEW_USER_PRESENTER_TAG, " " + "\n entered email: " + enteredEmail + "\n entered name: " + enteredName + "\n entered pass: " + enteredPassword + "\n LENGHT:" + enteredPassword.trim().length());

        if (enteredPassword.trim().isEmpty() && enteredEmail.trim().isEmpty() && enteredName.trim().isEmpty()) {
            view.makeToast("FILL ALL EMPTY FIELDS");
        } else {
            if (enteredPassword.trim().isEmpty() && enteredEmail.trim().isEmpty()) {
                view.makeToast("FILL ALL EMPTY FIELDS");
            } else {
                if (enteredPassword.trim().isEmpty() && enteredName.trim().isEmpty()) {
                    view.makeToast("FILL ALL EMPTY FIELDS");
                } else {
                    if (enteredName.trim().isEmpty() && enteredEmail.trim().isEmpty()) {
                        view.makeToast("FILL ALL EMPTY FIELDS");
                    } else {
                        if (enteredPassword.trim().isEmpty()) {
                            view.makeToast("ENTER PASSWORD");
                        } else {
                            if (enteredEmail.trim().isEmpty()) {
                                view.makeToast("ENTER EMAIL");
                            } else {
                                if (enteredName.trim().isEmpty()) {
                                    view.makeToast("ENTER NAME");
                                } else {
                                    if (enteredPassword.trim().length() < 7) {
                                        view.makeToast("PASSWORD MUST CONTAINS 7 CHARACTERS");
                                    }else {
                                        firebaseHelper.fetchSignInMethodsForEmailCallback(enteredEmail).addOnCompleteListener(task -> {
                                            boolean emailExists = task.getResult().getSignInMethods().isEmpty();

                                            view.showLoadingEmailDialog();

                                            if (!emailExists){
                                                view.cancelLoadingDialog();
                                                view.makeToast("EMAIL ALREADY EXISTS");
                                            }else {
                                                firebaseHelper.createWithEmailPassword(enteredEmail, enteredPassword, enteredName);
                                                view.startMyMainActivity();
                                                sharedPrefsHelper.saveSignedUserEmail(enteredEmail);
                                            }
                                        });


                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setTransformationMethod(TransformationMethod transformationMethod) {

        Log.d(NEW_USER_PRESENTER_TAG, "transformationMethod passed: " + transformationMethod + "\n transformationMethod show: " + PasswordTransformationMethod.getInstance());

        if (transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
            view.transformationHide();
        }
        if (transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
            view.transformationShow();
        }
    }
}
