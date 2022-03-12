package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public interface MainFragmentContract {
    interface View{

        void setMainFragmentToolbar();

        void presenterStartActivityForResult(Intent signInIntent, int rcSignIn);

        void showLoadingGoogleDialog(String googleEmail);

        void startMyMainActivity();

        void setSynchornizationImageViewOn();

        void setSynchornizationImageViewOff();
    }

    interface Presenter{

        void onViewCreated();

        void actionChangeGooleAccountClicked(GoogleSignInOptions gso, GoogleSignInClient gsc);

        void handleSignInResult(Task<GoogleSignInAccount> task);
    }
}
