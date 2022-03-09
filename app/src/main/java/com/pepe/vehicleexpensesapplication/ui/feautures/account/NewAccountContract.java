package com.pepe.vehicleexpensesapplication.ui.feautures.account;

public interface NewAccountContract {
    interface View{

        void startEmailAccountActivity();
    }
    interface Presenter{

        void onGoogleButtonClicked();

        void onEmailSignButtonClicked();
    }
}
