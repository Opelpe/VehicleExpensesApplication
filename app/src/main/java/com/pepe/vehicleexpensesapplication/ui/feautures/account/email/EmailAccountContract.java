package com.pepe.vehicleexpensesapplication.ui.feautures.account.email;

import android.app.AlertDialog;

public interface EmailAccountContract {
    interface View{

        void startExistedUserActivity();

        void startNewUserActivity();

        void showToast(String toastMsg);

        void startExistedGoogleActivity();

        void showLoadingEmailDialog();

        void showDialogEmailExist(String enteredEmail);

        void showNewAccountDialog();

        void cancelLoadingDialog();

        void cancelExistedEmailDialog();

        void cancelNewAccountDialog();
    }

    interface Presenter{

        void onCheckEmailButtonClicked(String enteredEmail);
    }
}
