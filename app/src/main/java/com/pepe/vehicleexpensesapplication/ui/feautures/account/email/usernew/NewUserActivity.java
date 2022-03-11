package com.pepe.vehicleexpensesapplication.ui.feautures.account.email.usernew;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.appcompat.content.res.AppCompatResources;

import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.databinding.ActivityNewUserBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

import java.time.Duration;

public class NewUserActivity extends AppCompatActivity implements NewUserContract.View {

    private static final String NEW_USER_ACTIVITY_TAG = " NEW_USER_ACTIVITY_TAG";

    private ActivityNewUserBinding binding;

    private NewUserContract.Presenter presenter;

    private EditText newUserEmailEditText;
    private ImageView passwordVisibilityImageView;
    private EditText newUserPasswordEditText;

    private AlertDialog.Builder builder;
    private AlertDialog dialogg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewUserBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new NewUserPresenter(this, getApplicationContext());

        builder = new AlertDialog.Builder(this);

        newUserEmailEditText = binding.newUserEmailEditText;

        presenter.setEmailEditText();

        EditText newUserNameEditText = binding.newUserNameEditText;
        newUserPasswordEditText = binding.newUserPasswordEditText;
        passwordVisibilityImageView = binding.passwordVisibilityImageView;

        TextView justNameText = binding.justNameText;
        TextView justEmailText = binding.justEmailAdressText;
        TextView justPasswordText = binding.justPasswordText;

        passwordVisibilityImageView.setOnClickListener(view -> {
            TransformationMethod transformationMethod = newUserPasswordEditText.getTransformationMethod();
            presenter.setTransformationMethod(transformationMethod);
        });


        newUserNameEditText.setOnFocusChangeListener((view, b) -> {
            if (b){
                justNameText.setTextColor(getResources().getColor(R.color.green));
            }else {
                justNameText.setTextColor(getResources().getColor(R.color.black));
            }
        });

        newUserEmailEditText.setOnFocusChangeListener((view, b) -> {
            if (b){
                justEmailText.setTextColor(getResources().getColor(R.color.green));
            }else {
                justEmailText.setTextColor(getResources().getColor(R.color.black));
            }
        });

        newUserPasswordEditText.setOnFocusChangeListener((view, b) -> {
            if (b){
                justPasswordText.setTextColor(getResources().getColor(R.color.green));
            }else {
                justPasswordText.setTextColor(getResources().getColor(R.color.black));
            }
        });

        Button saveNewUserButton = binding.newUserSaveButton;
        saveNewUserButton.setOnClickListener(view -> {
            Log.d(NEW_USER_ACTIVITY_TAG, "NEW USER SAVE BUTTON CLICKED START");
            try {
                String enteredEmail = newUserEmailEditText.getText().toString();
                String enteredName = newUserNameEditText.getText().toString();
                String enteredPassword = newUserPasswordEditText.getText().toString();
                presenter.saveNewUserButtonClicked(enteredEmail, enteredName, enteredPassword);
            } catch (Exception e) {
                Log.d(NEW_USER_ACTIVITY_TAG, "NEW USER SAVE BUTTON CLICKED EXCEPTION CAPTURED: " + e);
            }
        });

    }

    @Override
    public void setEmailEditText(String email) {
        newUserEmailEditText.setText(email);
    }

    @Override
    public void transformationHide() {
        Log.d(NEW_USER_ACTIVITY_TAG, "TRANSFORMATION PASSWORD SHOW");
        HideReturnsTransformationMethod hide = HideReturnsTransformationMethod.getInstance();
        passwordVisibilityImageView.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        newUserPasswordEditText.setTransformationMethod(hide);
    }

    @Override
    public void transformationShow() {
        Log.d(NEW_USER_ACTIVITY_TAG, "TRANSFORMATION PASSWORD HIDE");
        PasswordTransformationMethod show = PasswordTransformationMethod.getInstance();
        passwordVisibilityImageView.setImageResource(R.drawable.ic_baseline_visibility_24);
        newUserPasswordEditText.setTransformationMethod(show);

    }

    @Override
    public void makeToast(String toastMsg) {
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
    }

    @Override
    public void cancelLoadingDialog() {
        dialogg.cancel();
    }

    @Override
    public void showLoadingEmailDialog() {
        builder.setView(R.layout.dialog_check_email);
        dialogg = builder.create();
        dialogg.show();
    }
}
