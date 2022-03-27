package com.pepe.vehicleexpensesapplication.ui.feautures.main;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public interface MainFragmentContract {
    interface View{

        void setMainFragmentToolbar();


        void startMyMainActivity();


        void startRefillActivity();
    }

    interface Presenter{

        void onViewCreated();

        void onRefillButtonClicked();

        boolean getSynchronizationStatus();
    }
}
