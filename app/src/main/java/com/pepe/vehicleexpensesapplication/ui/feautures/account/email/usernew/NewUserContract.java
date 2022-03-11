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
    }
    interface Presenter{

        void setEmailEditText();

        void saveNewUserButtonClicked(String enteredEmail, String enteredName, String enteredPassword);

        void setTransformationMethod(TransformationMethod visionDraw);
    }
}
