package com.pepe.vehicleexpensesapplication.ui.feautures.splash;

public interface SplashContract {
    interface View{

        void startMainActivity();

        void startLoginActivity();
    }

    interface Presenter {

        void onViewCreated();
    }
}
