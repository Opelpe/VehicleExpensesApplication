package com.pepe.vehicleexpensesapplication.ui.feautures.splash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pepe.vehicleexpensesapplication.databinding.ActivitySplashBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.login.LoginActivity;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private SplashContract.Presenter presenter;

    private ActivitySplashBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new SplashPresenter(this, getApplicationContext());

        presenter.onViewCreated();
    }

    @Override
    public void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NO INTERNET");
        builder.setMessage("YOU MUST HAVE INTERNET CONNECTION");
        builder.setPositiveButton("REFRESH", (dialogInterface, i) ->
                startActivity(new Intent(getApplicationContext(), SplashActivity.class)));
        AlertDialog noInternetDialog = builder.create();
        noInternetDialog.show();
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
