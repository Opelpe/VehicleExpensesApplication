package com.pepe.vehicleexpensesapplication.ui.feautures.logout;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface LogOutContract {
    interface View{

        void startSplashActivity();
    }
    interface Presenter{

        void onLogOutButtonClicked(GoogleSignInClient googleSignInClient);

        void onWipeDataButtonClicked(GoogleSignInClient googleSignInClient);
    }
}
