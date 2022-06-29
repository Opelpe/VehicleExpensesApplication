package com.pepe.vehicleexpensesapplication.data.model.firebase;

import com.google.firebase.firestore.PropertyName;

public class NewUserModel {

    @PropertyName("USER_ID")
    public String userID;

    public String userEmail;
    public String password;
    public String name;
    public String provider;
    public boolean isAnonymous;

    public NewUserModel(String userID, String userEmail, String password, String name, String provider, boolean isAnonymous) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.password = password;
        this.name = name;
        this.provider = provider;
        this.isAnonymous = isAnonymous;
    }

    public NewUserModel(){

    }


}
