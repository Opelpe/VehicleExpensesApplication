package com.pepe.vehicleexpensesapplication.ui.feautures.logout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.pepe.vehicleexpensesapplication.databinding.ActivityLogOutBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.splash.SplashActivity;

public class LogOutActivity extends AppCompatActivity implements LogOutContract.View {

    private final static String LOG_OUT_ACTIVITY_TAG = "LOG_OUT_ACTIVITY_TAG";

    private ActivityLogOutBinding binding;

    private LogOutContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLogOutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new LogOutPresenter(this, getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);

        Button logOutButton = binding.logOutButton;
        logOutButton.setOnClickListener(view -> {
            presenter.onLogOutButtonClicked(googleSignInClient);
        });

        Button wipeDataButton = binding.wipeDataButton;
        wipeDataButton.setOnClickListener(view -> {
            presenter.onWipeDataButtonClicked(googleSignInClient);
        });
    }

    @Override
    public void startSplashActivity() {
        startActivity(new Intent(this, SplashActivity.class));
    }
}
