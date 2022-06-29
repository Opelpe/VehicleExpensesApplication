package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withemail;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;

import com.google.firebase.auth.AuthResult;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class ExistedEmailPresenter implements ExistedEmailContract.Presenter {

    private static final String EX_EMAIL_PRESENTER_TAG = "EX_EMAIL_PRESENTER";

    private ExistedEmailContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;

    public ExistedEmailPresenter(ExistedEmailContract.View view, SharedPrefsHelper prefsHelper) {
        this.view = view;
        sharedPrefsHelper = prefsHelper;
        firebaseHelper = FirebaseHelper.getInstance();
    }

    @Override
    public void onViewCreated() {
        view.setExistedEmailText(sharedPrefsHelper.getEnteredEmail());
    }

    @Override
    public void onCheckPasswordEmailButtonClicekd(String enteredPassword) {

        String enteredEmail = sharedPrefsHelper.getEnteredEmail();

        if (enteredPassword.trim().isEmpty()) {
            view.showToast("ENTER PASSWORD", true);
        } else {
                view.showLoadingEmailDialog();

                firebaseHelper.loginWithEmailPasswordTask(enteredEmail, enteredPassword).addOnSuccessListener(authResult -> {

                    AuthResult result = authResult;
                    String currentEmail = result.getUser().getEmail();

                    sharedPrefsHelper.saveIsAnonymous(false);
                    sharedPrefsHelper.saveSignWGoogleEmail(null);
                    sharedPrefsHelper.saveSignWEmailEmail(currentEmail);
                    sharedPrefsHelper.saveSignedUserEmail(currentEmail);
                    sharedPrefsHelper.saveGoogleSignInCompleted(false);

                    view.startMyMainActivity();
                    view.cancelLoadingDialog();
                    view.showToast("SUCCESSFULLY LOG IN", false);

                }).addOnFailureListener(e -> {

                    view.showToast("WRONG PASSWORD OR EMAIL", false);

                    sharedPrefsHelper.saveSignWEmailEmail(null);
                    sharedPrefsHelper.saveSignedUserEmail(null);

                    view.cancelLoadingDialog();

                    Log.d(EX_EMAIL_PRESENTER_TAG, "EXCEPTION CAPTURED: \n" + e);
                });
        }
    }

    @Override
    public void setTransformationMethod(TransformationMethod transformationMethod) {

        if (transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
            view.transformationHide();
        }
        if (transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
            view.transformationShow();
        }
    }

}
