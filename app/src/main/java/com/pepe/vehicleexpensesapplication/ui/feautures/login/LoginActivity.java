package com.pepe.vehicleexpensesapplication.ui.feautures.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.databinding.ActivityLoginBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.NewAccountActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String LOGIN_ACTIVITY_TAG = "LOGIN_ACTIVITY_TAG";

    private LoginContract.Presenter presenter;

    private ActivityLoginBinding binding;

    private Button localLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new LoginPresenter(this, getApplicationContext());

        CheckBox startCheckBox = findViewById(R.id.startCheckBox);
        presenter.isCheckboxChecked(startCheckBox.isChecked());
        startCheckBox.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
            presenter.isCheckboxChecked(startCheckBox.isChecked());
        });

        presenter.onViewCreated();

        Button accountLoginButton = findViewById(R.id.accountLoginButton);
        accountLoginButton.setOnClickListener(view ->  presenter.onAccountButtonClicked());

        localLoginButton = findViewById(R.id.localLoginButton);
        localLoginButton.setOnClickListener(view -> presenter.onLocalLoginButtonClicked());
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
    }

    @Override
    public void startNewAccountActivity() {
        startActivity(new Intent(this, NewAccountActivity.class));
    }
}
