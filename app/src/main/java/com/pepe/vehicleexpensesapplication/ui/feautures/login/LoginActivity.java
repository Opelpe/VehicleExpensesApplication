package com.pepe.vehicleexpensesapplication.ui.feautures.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.SharedPrefsHelper;
import com.pepe.vehicleexpensesapplication.databinding.ActivityLoginBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.NewAccountActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static final String LOGIN_ACTIVITY_TAG = "LOGIN_ACTIVITY_TAG";

    private LoginContract.Presenter presenter;

    private ActivityLoginBinding binding;

    private AlertDialog.Builder loadingDialogBuilder;
    private AlertDialog loadingDialog;

    private Button localLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());

        presenter = new LoginPresenter(this, sharedPrefsHelper);

        loadingDialogBuilder = new AlertDialog.Builder(this);

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
    public void showLoadingDialog() {
        loadingDialogBuilder.setView(R.layout.dialog_create_user);
        loadingDialog = loadingDialogBuilder.create();
        loadingDialog.show();
    }

    @Override
    public void cancelLoadingDialog() {
        if (loadingDialog != null){
            if (loadingDialog.isShowing()) {
                loadingDialog.cancel();
            }}
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
