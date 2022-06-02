package com.pepe.vehicleexpensesapplication.data.model.firebase;

import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

public class NewUserModel {

    @PropertyName("DupaDupaDupa")
    public String USER_ID;

    public String USER_EMAIL;
    public String PASSWORD;
    public String NAME;
    public String PROVIDER;
    public boolean IS_ANONYMOUS;

    public NewUserModel(String userID, String userEmail, String password, String name, String provider, boolean isAnonymous) {
        USER_ID = userID;
        USER_EMAIL = userEmail;
        PASSWORD = password;
        NAME = name;
        PROVIDER = provider;
        IS_ANONYMOUS = isAnonymous;
    }

    public NewUserModel(){

    }


}
