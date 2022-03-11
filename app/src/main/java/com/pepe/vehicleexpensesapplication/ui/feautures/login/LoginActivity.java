package com.pepe.vehicleexpensesapplication.ui.feautures.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.databinding.ActivityLoginBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.NewAccountActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String LOGIN_ACTIVITY_TAG = "LOGIN_ACTIVITY_TAG";

    private LoginContract.Presenter presenter;

    private ActivityLoginBinding binding;

    private Button localLoginButton;
    private Button accountLoginButton;

    private SharedPrefsHelper sharedPrefsHelper;

    private FirebaseHelper fireBaseHelper;

    private boolean startCheckboxChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        fireBaseHelper = FirebaseHelper.getInstance();

        presenter = new LoginPresenter(this, getApplicationContext());

        sharedPrefsHelper = new SharedPrefsHelper(this);

        CheckBox startCheckBox = findViewById(R.id.startCheckBox);

        startCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            startCheckboxChecked = startCheckBox.isChecked();
            sharedPrefsHelper.saveCheckboxStatus(startCheckboxChecked);
        });

        presenter.onViewCreated();

        Button accountLoginButton = findViewById(R.id.accountLoginButton);
        accountLoginButton.setOnClickListener(view -> {
            Log.d(LOGIN_ACTIVITY_TAG, "Sign in with Google Button clicked");
                    presenter.onAccounButtonClicked();
                });

        localLoginButton = findViewById(R.id.localLoginButton);
        localLoginButton.setOnClickListener(view -> presenter.onLocalLoginButtonClicked());
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
    }

    @Override
    public void startNewAccountActivity() {
        startActivity(new Intent(this, NewAccountActivity.class));
    }
}
