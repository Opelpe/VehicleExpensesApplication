package com.pepe.vehicleexpensesapplication.ui.feautures.account.email;

public class EmailAccountPresenter implements EmailAccountContract.Presenter{

    private EmailAccountContract.View view;

    public EmailAccountPresenter(EmailAccountContract.View view){
        this.view = view;

    }
}
