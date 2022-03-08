package com.pepe.vehicleexpensesapplication.ui.feautures.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.databinding.ActivityMyMainBinding;

public class MyMainActivity extends AppCompatActivity {


    private static final String MAIN_ACTIVITY_TAG = "MAIN_ACTIVITY_TAG";

    private ActivityMyMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(this);
        Log.d(MAIN_ACTIVITY_TAG, "starting " + sharedPrefsHelper.getCheckboxStatus());


        binding = ActivityMyMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

    }
}