package com.pepe.vehicleexpensesapplication.ui.feautures.account.email;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pepe.vehicleexpensesapplication.databinding.ActivityEmailAccountBinding;

public class EmailAccountActivity extends AppCompatActivity implements EmailAccountContract.View {

    private ActivityEmailAccountBinding binding;

    private EmailAccountContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmailAccountBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new EmailAccountPresenter(this);

    }
}
