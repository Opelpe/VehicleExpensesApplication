package com.pepe.vehicleexpensesapplication.ui.feautures.splash;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewCreated();

    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
