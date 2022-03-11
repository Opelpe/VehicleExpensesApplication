package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withgoogle;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public interface ExistedGoogleContract {
    interface View{

        void setGoogleEmailInfo(String enteredEmail);

        void presenterStartActivityForResult(Intent signInIntent, int rcSignIn);

        void startMyMainActivity();

        void showLoadingGoogleDialog();

        void showToast(String toastMsg);

        void cancelLoadingDialog();
    }

    interface Presenter{

        void onViewCreated();

        void onlogInWithGoogleButtonClicked(GoogleSignInClient mGoogleSignInClient, GoogleSignInOptions gso);

        void handleSignInResult(Task<GoogleSignInAccount> task);
    }
}
