package com.pepe.vehicleexpensesapplication.data.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefsHelper {

    private static final String SHARED_PREFS_NAME = "SHARED_PREFS_HELPER";

    public static final boolean START_CHECKBOX = false;
    public static final boolean GOOGLE_LOGGED = false;

    private SharedPreferences preferences;

    public SharedPrefsHelper(Context context) {

        preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveCheckboxStatus(boolean startCheckbox) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, startCheckbox);
        editor.commit();
        editor.apply();
    }

    public boolean getCheckboxStatus() {
        Log.d(SHARED_PREFS_NAME, "\n START CHECKBOX STATUS: " + preferences.getBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, START_CHECKBOX));

        return preferences.getBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, START_CHECKBOX);
    }


    public void saveGoogleSignInCompleted(boolean successful) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsPreferences.GOOGLE_LOGGED_STATUS, successful);
        editor.commit();
        editor.apply();
    }


    public boolean getGoogleSignInCompleted() {
        Log.d(SHARED_PREFS_NAME, "\n GOOGLE_LOGGED_STATUS: " + preferences.getBoolean(ConstantsPreferences.GOOGLE_LOGGED_STATUS, GOOGLE_LOGGED));

        return preferences.getBoolean(ConstantsPreferences.GOOGLE_LOGGED_STATUS, GOOGLE_LOGGED);
    }

}