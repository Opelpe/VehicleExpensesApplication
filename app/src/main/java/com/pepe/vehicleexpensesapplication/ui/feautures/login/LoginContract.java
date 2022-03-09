package com.pepe.vehicleexpensesapplication.ui.feautures.login;

public interface LoginContract {

    interface View{

        void startMainActivity();

        void startNewAccountActivity();
    }

    interface Presenter{

        void onLocalLoginButtonClicked();

        void onViewCreated();

        void onAccounButtonClicked();
    }
}
