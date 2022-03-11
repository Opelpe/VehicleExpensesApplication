package com.pepe.vehicleexpensesapplication.ui.feautures.account;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.pepe.vehicleexpensesapplication.MainActivity;
import com.pepe.vehicleexpensesapplication.R;
import com.pepe.vehicleexpensesapplication.data.firebase.FirebaseHelper;
import com.pepe.vehicleexpensesapplication.databinding.ActivityNewAccountBinding;
import com.pepe.vehicleexpensesapplication.ui.feautures.account.email.EmailAccountActivity;
import com.pepe.vehicleexpensesapplication.ui.feautures.activity.MyMainActivity;

public class NewAccountActivity extends AppCompatActivity implements NewAccountContract.View {

    private static final String NACC_ACTIVITY_TAG = "NACC_ACTIVITY_TAG";

    private ActivityNewAccountBinding binding;

    private NewAccountContract.Presenter presenter;

    private static final int RC_SIGN_IN = 100;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewAccountBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        presenter = new NewAccountPresenter(this, getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button signWithGoogleButton = binding.signWithGoogleButton;
        signWithGoogleButton.setOnClickListener(view -> {
            Log.d(NACC_ACTIVITY_TAG, "\n on sign in with google clicked START");
            presenter.onGoogleButtonClicked(mGoogleSignInClient, gso);
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

        Log.d(NACC_ACTIVITY_TAG, " on Activity RESULT START, requested code" + requestCode + " RC_Sign IN : " + RC_SIGN_IN);

        if (requestCode == RC_SIGN_IN) {
            Log.d(NACC_ACTIVITY_TAG, "\n on Activity IF requesteCode" + requestCode);

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
        startActivityForResult(signInIntent, rcSignIn);
    }

    @Override
    public void startMyMainActivity() {
        startActivity(new Intent(this, MyMainActivity.class));
    }

    @Override
    public void showLoadingGoogleDialog(String googleEmail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_check_email);
        AlertDialog dialogg = builder.create();
        dialogg.show();
    }
}