package com.pepe.vehicleexpensesapplication.ui.feautures.activity;

import android.util.Log;

import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.data.model.firebase.NewUserModel;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;

public class MyMainPresenter implements MyMainContract.Presenter {
    private static final String MAIN_PRESENTER_TAG = "MAIN_PRESENTER_TAG";

    private MyMainContract.View view;

    private FirebaseHelper firebaseHelper;

    private SharedPrefsHelper sharedPrefsHelper;

    private NewUserModel newUserModel;

    private FirebaseHelper.FirebaseUserListener firebaseUserListener = new FirebaseHelper.FirebaseUserListener() {
        @Override
        public void usersData(NewUserModel userData) {

            Log.d(MAIN_PRESENTER_TAG, "new user model in MAIN ACTIVITY, ID: " + userData.userID);

                String userPassed = userData.userID;
                String userShared = sharedPrefsHelper.getSignedUserID();
                String provider = userData.provider;

                if (userPassed != null){
                    if (userPassed.equals(userShared)){
                        Log.d(MAIN_PRESENTER_TAG, "new user model in MAIN ACTIVITY," + " MATCH SAHARED = " + userShared + " ID MATCHES: " + userPassed);
                        Log.d(MAIN_PRESENTER_TAG, "new user model in MAIN ACTIVITY, PROVIDER: " + provider);
                    }
                }

        }

        @Override
        public void dataFailure(boolean failure) {

        }
    };

    public MyMainPresenter(MyMainContract.View view, SharedPrefsHelper prefsHelper) {
        this.view = view;
        firebaseHelper = FirebaseHelper.getInstance();
        sharedPrefsHelper = prefsHelper;
    }

    @Override
    public void onViewCreated() {

        firebaseHelper.setFirebaseUsersListener(firebaseUserListener);
        firebaseHelper.checkCurrentUserCallback();

    }

}
