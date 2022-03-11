package com.pepe.vehicleexpensesapplication.ui.feautures.account.email;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.databinding.ActivityEmailAccountBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withemail.ExistedEmailActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withgoogle.ExistedGoogleActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.email.usernew.NewUserActivity;

public class EmailAccountActivity extends AppCompatActivity implements EmailAccountContract.View {

    private static final String EMAIL_ACTIVITY_TAG = "EMAIL_ACTIVITY_TAG";

    private ActivityEmailAccountBinding binding;

    private EmailAccountContract.Presenter presenter;

    private AlertDialog.Builder loadingEmailBuilder;
    private AlertDialog.Builder existsEmailBuilder;
    private AlertDialog.Builder newAccountBuilder;

    private AlertDialog loadingEmailDialog;
    private AlertDialog existsEmailDialog;
    private AlertDialog newAccountDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmailAccountBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new EmailAccountPresenter(this, getApplicationContext());

        loadingEmailBuilder = new AlertDialog.Builder(this);
        existsEmailBuilder = new AlertDialog.Builder(this);
        newAccountBuilder = new AlertDialog.Builder(this);

        EditText emailEditText = binding.emailEditText;

        TextView emailAdresText = binding.emailAdresText;

        Button checkEmailButton = binding.checkEmailButton;

        emailEditText.setOnFocusChangeListener((view, b) -> {
            if (b) {
                emailAdresText.setTextColor(getResources().getColor(R.color.green));
            } else {
                emailAdresText.setTextColor(getResources().getColor(R.color.black));
            }
        });

        checkEmailButton.setOnClickListener(view -> {

            Log.d(EMAIL_ACTIVITY_TAG, "Check Email Button CLICKED");
            try {
                String enteredEmail = emailEditText.getText().toString();

                Log.d(EMAIL_ACTIVITY_TAG, "Check Email Button CLICKED, entered email: \n" + enteredEmail);

                presenter.onCheckEmailButtonClicked(enteredEmail);
            } catch (Exception e) {
                Log.d(EMAIL_ACTIVITY_TAG, "Check Email Button Exception Captured: " + e);
            }
        });
    }

    @Override
    public void startExistedUserActivity() {
        startActivity(new Intent(this, ExistedEmailActivity.class));
    }

    @Override
    public void startNewUserActivity() {
        startActivity(new Intent(this, NewUserActivity.class));
    }

    @Override
    public void showToast(String toastMsg) {
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startExistedGoogleActivity() {
        startActivity(new Intent(this, ExistedGoogleActivity.class));
    }

    @Override
    public void showLoadingEmailDialog() {
        loadingEmailBuilder.setView(R.layout.dialog_check_email);
        loadingEmailDialog = loadingEmailBuilder.create();
        loadingEmailDialog.show();
    }

    @Override
    public void showDialogEmailExist(String enteredEmail) {
        existsEmailBuilder.setView(R.layout.dialog_check_email)
                .setMessage(enteredEmail + " have been already used!")
                .setTitle("EMAIL ALREADY EXIST");
        existsEmailDialog = existsEmailBuilder.create();
        existsEmailDialog.show();
    }

    @Override
    public void showNewAccountDialog() {
        newAccountBuilder.setView(R.layout.dialog_check_email)
                .setMessage("CREATE NEW ACCOUNT")
                .setTitle("EMAIL DOESN'T EXIST");
        newAccountDialog = newAccountBuilder.create();
        newAccountDialog.show();
    }

    @Override
    public void cancelLoadingDialog() {
        if (loadingEmailDialog.isShowing()) {
            loadingEmailDialog.cancel();
        }

    }

    @Override
    public void cancelExistedEmailDialog() {
        if (existsEmailDialog.isShowing()) {
            existsEmailDialog.cancel();
        }

    }

    @Override
    public void cancelNewAccountDialog() {
        if (newAccountDialog.isShowing()) {
            newAccountDialog.cancel();
        }

    }
}
