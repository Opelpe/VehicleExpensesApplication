package com.pepe.vehicleexpensesapplication.ui.feautures.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.databinding.ActivityMyMainBinding;

public class MyMainActivity extends AppCompatActivity {


    private static final String MAIN_ACTIVITY_TAG = "MAIN_ACTIVITY_TAG";

    private ActivityMyMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(this);
        Log.d(MAIN_ACTIVITY_TAG, "starting \n chekbox status:" + sharedPrefsHelper.getCheckboxStatus() + "\n signed in email: " + sharedPrefsHelper.getSignedUserEmail());

        binding = ActivityMyMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.myMainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.ic_baseline_local_gas_station_24);
//        getSupportActionBar().setIcon(R.drawable.ic_baseline_local_gas_station_24);
        getSupportActionBar().setTitle("VEA");

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_main, R.id.navigation_history, R.id.navigation_settings).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

}

