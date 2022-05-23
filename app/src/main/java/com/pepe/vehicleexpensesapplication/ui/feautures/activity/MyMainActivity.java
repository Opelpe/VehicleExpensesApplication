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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.databinding.ActivityMyMainBinding;

public class MyMainActivity extends AppCompatActivity implements MyMainContract.View {


    private static final String MAIN_ACTIVITY_TAG = "MAIN_ACTIVITY_TAG";

    private ActivityMyMainBinding binding;

    private MyMainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyMainBinding.inflate(getLayoutInflater());

        presenter = new MyMainPresenter(this, getApplicationContext());

        setContentView(binding.getRoot());

        presenter.onViewCreated();

        BottomNavigationView navView = binding.navView;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupWithNavController(navView, navController);

    }

}

