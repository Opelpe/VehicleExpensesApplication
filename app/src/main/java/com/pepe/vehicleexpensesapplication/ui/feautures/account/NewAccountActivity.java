package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.sharedprefs.ConstantsPreferences;
import com.pepe.vehicleexpensesapplication.databinding.ActivityNewAccountBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.email.EmailAccountActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

public class NewAccountActivity extends AppCompatActivity implements NewAccountContract.View {

    private static final String NACC_ACTIVITY_TAG = "NACC_ACTIVITY_TAG";

    private ActivityNewAccountBinding binding;

    private NewAccountContract.Presenter presenter;

    private GoogleSignInClient googleSignInClient;

    private AlertDialog.Builder checkingEmailBuilder;
    private AlertDialog checkingEmailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewAccountBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new NewAccountPresenter(this, getApplicationContext());

        checkingEmailBuilder = new AlertDialog.Builder(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        Button signWithGoogleButton = binding.signWithGoogleButton;
        signWithGoogleButton.setOnClickListener(view -> {
            Log.d(NACC_ACTIVITY_TAG, "\n on sign in with google clicked START");
            presenter.onGoogleButtonClicked(googleSignInClient);
        });

        Button signInWithEmailButton = binding.signInWithEmailButton;
        signInWithEmailButton.setOnClickListener(view -> {
            Log.d(NACC_ACTIVITY_TAG, "\n on sign in with email clicked START");
            presenter.onEmailSignButtonClicked();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        googleSignInClient.signOut();

        Log.d(NACC_ACTIVITY_TAG, " on Activity RESULT START, requested code: " + requestCode + " RC_Sign IN : " + ConstantsPreferences.RC_SIGN_IN + "   mresult code: " + resultCode);

        if (requestCode == ConstantsPreferences.RC_SIGN_IN) {
            Log.d(NACC_ACTIVITY_TAG, "\n on Activity IF requesteCode: " + requestCode);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            presenter.handleSignInResult(task);

        }
    }

    @Override
    public void startEmailAccountActivity() {
        startActivity(new Intent(this, EmailAccountActivity.class));
    }

    @Override
    public void presenterStartActivityForResult(Intent signInIntent, int rcSignIn) {
        Log.d(NACC_ACTIVITY_TAG, "\n presenterStartActivityForResult signInIntent: " + rcSignIn);

        startActivityForResult(signInIntent, rcSignIn);
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
        if (checkingEmailDialog != null) {
            if (checkingEmailDialog.isShowing()) {
                checkingEmailDialog.cancel();
            }
        }
    }

    @Override
    public void showLoadingGoogleDialog() {

        checkingEmailBuilder.setView(R.layout.dialog_check_email);
        checkingEmailDialog = checkingEmailBuilder.create();
        checkingEmailDialog.show();
    }

    @Override
    public void cancelLoadingDialog() {
        if (checkingEmailDialog != null) {
            if (checkingEmailDialog.isShowing()) {
                checkingEmailDialog.cancel();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cancelLoadingDialog();
    }

    @Override
    public void showToast(String toastMsg) {
        Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show();
    }
}