package com.pepe.vehicleexpensesapplication.data.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedPrefsHelper {

    private static final String SHARED_PREFS_NAME = "SHARED_PREFS_HELPER";

    public static final boolean START_CHECKBOX = false;
    public static final boolean GOOGLE_LOGGED = false;
    public static final String EMAIL_ENTERED = null;
    public static final String EMAIL_SIGNED = null;
    public static final String UID_SIGNED = null;
    public static final String W_EMAIL_SIGNED = null;
    public static final String W_GOOGLE_SIGNED = null;
    public static final boolean IS_ANONYMOUS = false;
    private static final int HISTORY_SIZE_COUNT = 0;
    private static final List<String> ID_OF_HISTORY_ITEM = new ArrayList<>();

    private SharedPreferences preferences;

    public SharedPrefsHelper(Context context) {

        preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void saveCheckboxStatus(boolean startCheckbox) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, startCheckbox);
        editor.apply();
    }

    public boolean getCheckboxStatus() {
        Log.d(SHARED_PREFS_NAME, "\n START CHECKBOX STATUS: " + preferences.getBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, START_CHECKBOX));

        return preferences.getBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, START_CHECKBOX);
    }

    public void saveEnteredEmail(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ConstantsPreferences.ENETERED_EMAIL, email);
        editor.apply();
    }

    public String getEnteredEmail() {
        Log.d(SHARED_PREFS_NAME, "\n ENTERED EMAIL: " + preferences.getString(ConstantsPreferences.ENETERED_EMAIL, EMAIL_ENTERED));
        return preferences.getString(ConstantsPreferences.ENETERED_EMAIL, EMAIL_ENTERED);
    }

    public void saveSignedUserEmail(String savedEmail){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ConstantsPreferences.SIGNED_USER_EMAIL, savedEmail);
        editor.apply();
    }

    public String getSignedUserEmail(){
        Log.d(SHARED_PREFS_NAME, "\n SIGNED EMAIL: " + preferences.getString(ConstantsPreferences.SIGNED_USER_EMAIL, EMAIL_SIGNED));
        return preferences.getString(ConstantsPreferences.SIGNED_USER_EMAIL, EMAIL_SIGNED);
    }

    public void saveIsAnonymous(boolean isAnonymous){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsPreferences.IS_USER_ANONYMOUS, isAnonymous);
        editor.apply();
    }

    public boolean getIsAnonymous(){
        Log.d(SHARED_PREFS_NAME, "\n IS ANONYMOUS: " + preferences.getBoolean(ConstantsPreferences.IS_USER_ANONYMOUS, IS_ANONYMOUS));
        return preferences.getBoolean(ConstantsPreferences.IS_USER_ANONYMOUS, IS_ANONYMOUS);
    }

    public void saveSignedUserID(String uID){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ConstantsPreferences.SIGNED_USER_ID, uID);
        editor.apply();
    }

    public String getSignedUserID(){
        Log.d(SHARED_PREFS_NAME, "\n SIGNED_USER_ID: " + preferences.getString(ConstantsPreferences.SIGNED_USER_ID, UID_SIGNED));
        return preferences.getString(ConstantsPreferences.SIGNED_USER_ID, UID_SIGNED);
    }

    public void saveSignWEmailEmail(String signWithEmail){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ConstantsPreferences.W_EMAIL_SIGNED_EMAIL, signWithEmail);
        editor.apply();
    }

    public String getSignWEEmailEmail(){
        Log.d(SHARED_PREFS_NAME, "\n SIGNED_USER_ID: " + preferences.getString(ConstantsPreferences.W_EMAIL_SIGNED_EMAIL, W_EMAIL_SIGNED));
        return preferences.getString(ConstantsPreferences.W_EMAIL_SIGNED_EMAIL, W_EMAIL_SIGNED);
    }

    public void saveSignWGoogleEmail(String signWithGoogle){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ConstantsPreferences.W_GOOGLE_SIGNED_EMAIL, signWithGoogle);
        editor.apply();
    }

    public String getSignWGoogleEmail(){
        Log.d(SHARED_PREFS_NAME, "\n SIGNED_USER_ID: " + preferences.getString(ConstantsPreferences.W_GOOGLE_SIGNED_EMAIL, W_GOOGLE_SIGNED));
        return preferences.getString(ConstantsPreferences.W_GOOGLE_SIGNED_EMAIL, W_GOOGLE_SIGNED);
    }

    public void saveGoogleSignInCompleted(boolean successful) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsPreferences.GOOGLE_LOGGED_STATUS, successful);
        editor.apply();
    }


    public boolean getGoogleSignInCompleted() {
        Log.d(SHARED_PREFS_NAME, "\n GOOGLE_LOGGED_STATUS: " + preferences.getBoolean(ConstantsPreferences.GOOGLE_LOGGED_STATUS, GOOGLE_LOGGED));

        return preferences.getBoolean(ConstantsPreferences.GOOGLE_LOGGED_STATUS, GOOGLE_LOGGED);
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public void saveHistorySize(int size) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ConstantsPreferences.HISTORY_SIZE, size);
        Log.d(SHARED_PREFS_NAME, "\n history size saving: " + size);
        editor.apply();
    }

    public int getSHistorySize(){
        Log.d(SHARED_PREFS_NAME, "\n HISTORY SIZE: " + preferences.getInt(ConstantsPreferences.HISTORY_SIZE, HISTORY_SIZE_COUNT));
        return preferences.getInt(ConstantsPreferences.HISTORY_SIZE, HISTORY_SIZE_COUNT);
    }

}