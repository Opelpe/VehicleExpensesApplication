package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.usernew;

import android.text.method.TransformationMethod;

public interface NewUserContract {
    interface View{

        void setEmailEditText(String email);

        void transformationHide();

        void transformationShow();

        void makeToast(String toastMsg);

        void startMyMainActivity();

        void cancelLoadingDialog();

        void showLoadingEmailDialog();

        void showExistedAccountDialog(String enteredEmail);

        void startExistedEmailActivity();

        void startExistedGoogleActivity();
    }
    interface Presenter{

        void onViewCreated();

        void saveNewUserButtonClicked(String enteredEmail, String enteredName, String enteredPassword);

        void setTransformationMethod(TransformationMethod visionDraw);

        void checkUserProvider(String enteredEmail);
    }
}
