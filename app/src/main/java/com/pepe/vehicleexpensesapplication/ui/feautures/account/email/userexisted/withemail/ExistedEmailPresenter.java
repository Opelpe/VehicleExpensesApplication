package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withemail;

import android.content.Context;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class ExistedEmailPresenter implements ExistedEmailContract.Presenter {

    private static final String EX_EMAIL_PRESENTER_TAG = "EX_EMAIL_PRESENTER";

    private ExistedEmailContract.View view;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper firebaseHelper;

    public ExistedEmailPresenter(ExistedEmailContract.View view, Context context) {
        this.view = view;
        sharedPrefsHelper = new SharedPrefsHelper(context);
        firebaseHelper = FirebaseHelper.getInstance(context);
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

                firebaseHelper.loginWithEmailPasswordCallback(enteredEmail, enteredPassword).addOnSuccessListener(authResult -> {

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
