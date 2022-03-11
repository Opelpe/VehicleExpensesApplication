package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withemail;

import android.text.method.TransformationMethod;

public interface ExistedEmailContract {
    interface View{

        void setExistedEmailText(String email);

        void transformationShow();

        void transformationHide();

        void startMyMainActivity();

        void showToast(String toastMsg);

        void showLoadingEmailDialog();

        void makeToast(String toastMsg);

        void cancelLoadingDialog();
    }

    interface  Presenter{

        void onViewCreated();

        void onCheckPasswordEmailButtonClicekd(String password);

        void setTransformationMethod(TransformationMethod transformationMethod);
    }
}
