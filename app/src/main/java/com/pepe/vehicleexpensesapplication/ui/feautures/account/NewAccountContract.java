package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public interface NewAccountContract {
    interface View{

        void startEmailAccountActivity();

        void presenterStartActivityForResult(Intent signInIntent, int rcSignIn);

        void startMyMainActivity();

    }
    interface Presenter{

        void onGoogleButtonClicked(GoogleSignInClient mGoogleSignInClient, GoogleSignInOptions gso);

        void onEmailSignButtonClicked();

        void handleSignInResult(Task<GoogleSignInAccount> task);
    }
}
