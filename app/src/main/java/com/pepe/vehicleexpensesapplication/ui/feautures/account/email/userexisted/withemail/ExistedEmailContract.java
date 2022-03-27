package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withemail;

import android.text.method.TransformationMethod;

public interface ExistedEmailContract {
    interface View{

        void setExistedEmailText(String email);

        void transformationShow();

        void transformationHide();

        void startMyMainActivity();

        void showToast(String toastMsg, boolean lenghtLong);

        void showLoadingEmailDialog();


        void cancelLoadingDialog();
    }

    interface  Presenter{

        void onViewCreated();

        void onCheckPasswordEmailButtonClicekd(String password);

        void setTransformationMethod(TransformationMethod transformationMethod);
    }
}
