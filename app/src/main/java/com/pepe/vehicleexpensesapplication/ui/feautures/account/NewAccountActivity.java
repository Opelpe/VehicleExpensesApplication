package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.pepe.vehicleexpensesapplication.databinding.ActivityNewAccountBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.email.EmailAccountActivity;

public class NewAccountActivity extends AppCompatActivity implements NewAccountContract.View {

    private static final String NACC_ACTIVITY_TAG = "NACC_ACTIVITY_TAG";

    private ActivityNewAccountBinding binding;

    private NewAccountContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewAccountBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new NewAccountPresenter(this);

        Button signWithGoogleButton = binding.signWithGoogleButton;
        signWithGoogleButton.setOnClickListener(view -> {
            Log.d(NACC_ACTIVITY_TAG, "\n on sign in with google clicked START");
            presenter.onGoogleButtonClicked();
        });

        Button signInWithEmailButton = binding.signInWithEmailButton;
        signInWithEmailButton.setOnClickListener(view -> {
            Log.d(NACC_ACTIVITY_TAG, "\n on sign in with email clicked START");
            presenter.onEmailSignButtonClicked();
        });





    }

    @Override
    public void startEmailAccountActivity() {
        startActivity(new Intent(this, EmailAccountActivity.class));
    }
}