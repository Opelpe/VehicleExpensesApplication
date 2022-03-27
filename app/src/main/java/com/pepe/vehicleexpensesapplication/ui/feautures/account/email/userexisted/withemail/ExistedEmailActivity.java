package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.userexisted.withemail;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.databinding.ActivityExistedEmailBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

public class ExistedEmailActivity extends AppCompatActivity implements ExistedEmailContract.View{

    private static final String EX_EMAIL_ACTIVITY_TAG = "EX_EMAIL_ACTIVITY";

    private ActivityExistedEmailBinding binding;

    private ExistedEmailContract.Presenter presenter;

    private TextView aboutEmailInfoTextView;
    private ImageView passwordVisibilityImageView;
    private EditText passwordEditText;


    private AlertDialog.Builder builder;
    private AlertDialog dialogg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityExistedEmailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new ExistedEmailPresenter(this, getApplicationContext());

        builder = new AlertDialog.Builder(this);

        aboutEmailInfoTextView = binding.aboutEmailInfoTextView;

        passwordEditText = binding.existedPasswordEditText;

        passwordVisibilityImageView = binding.passwordVisibilityImageView;

        presenter.onViewCreated();

        passwordVisibilityImageView.setOnClickListener(view -> {
            TransformationMethod transformationMethod = passwordEditText.getTransformationMethod();
            presenter.setTransformationMethod(transformationMethod);
        });

        TextView passwordTextView = binding.passwordJustText;

        passwordEditText.setOnFocusChangeListener((view, b) -> {
            if (b){
                passwordTextView.setTextColor(getResources().getColor(R.color.green));
            }else {
                passwordTextView.setTextColor(getResources().getColor(R.color.black));
            }
        });

        Button checkPasswordEmailButton = binding.logInExistedUserButton;
        checkPasswordEmailButton.setOnClickListener(view -> {
            try {
                String password = passwordEditText.getText().toString();
                presenter.onCheckPasswordEmailButtonClicekd(password);
            }catch (Exception e){
                Log.d(EX_EMAIL_ACTIVITY_TAG, "checkPasswordEmailButton CLICKED: \n EXEPCTION CAPTURED: " +e);
            }
        });
    }

    @Override
    public void setExistedEmailText(String email) {
        aboutEmailInfoTextView.setText("Adress " + email + " has been already used. Enter password to this account.");
    }

    @Override
    public void transformationShow() {
        Log.d(EX_EMAIL_ACTIVITY_TAG, "TRANSFORMATION PASSWORD SHOW");
        PasswordTransformationMethod show = PasswordTransformationMethod.getInstance();
        passwordVisibilityImageView.setImageResource(R.drawable.ic_baseline_visibility_24);
        passwordEditText.setTransformationMethod(show);
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    @Override
    public void transformationHide() {
        Log.d(EX_EMAIL_ACTIVITY_TAG, "TRANSFORMATION PASSWORD HIDE");
        HideReturnsTransformationMethod hide = HideReturnsTransformationMethod.getInstance();
        passwordVisibilityImageView.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        passwordEditText.setTransformationMethod(hide);
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
    }

    @Override
    public void showToast(String toastMsg, boolean lenghtLong) {
        if (lenghtLong){
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoadingEmailDialog() {
        builder.setView(R.layout.dialog_check_email);
        dialogg = builder.create();
        dialogg.show();
    }

    @Override
    public void cancelLoadingDialog() {
        dialogg.cancel();
    }

}
