package com.pepe.vehicleexpensesapplication.ui.feautures.splash;

public interface SplashContract {
    interface View{

        void startMyMainActivity();

        void startLoginActivity();

        void showNoInternetDialog();
    }

    interface Presenter {

        void onViewCreated();
    }
}
