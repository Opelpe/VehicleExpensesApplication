package com.pepe.vehicleexpensesapplication.ui.feautures.login;

public interface LoginContract {

    interface View{

        void startMyMainActivity();

        void startNewAccountActivity();
    }

    interface Presenter{

        void onLocalLoginButtonClicked();

        void onViewCreated();

        void onAccountButtonClicked();

        void isCheckboxChecked(boolean checked);
    }
}
