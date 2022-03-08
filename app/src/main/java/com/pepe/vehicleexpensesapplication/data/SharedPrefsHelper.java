package com.pepe.vehicleexpensesapplication.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefsHelper  {

    private static final String SHARED_PREFS_NAME = "SHARED_PREFS_HELPER";

    public static final boolean START_CHECKBOX = false;

    private SharedPreferences preferences;

    public SharedPrefsHelper(Context context){

        preferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveStartCheckboxStatus(boolean startCheckbox){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, startCheckbox);
        editor.commit();
        editor.apply();
    }

    public boolean getStartCheckboxStatus(){
        Log.d(SHARED_PREFS_NAME, "\n START CHECKBOX STATUS: " + preferences.getBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, START_CHECKBOX));

        return preferences.getBoolean(ConstantsPreferences.START_CHECKBOX_STATUS, START_CHECKBOX);
    }


}

